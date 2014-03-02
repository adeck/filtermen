package veritas.kernels

import scalapipe._
import scalapipe.dsl.Type // I made this parameterizable.

// Relies on the following config parameters:
//    width
//    height
//    outputCount   --    The number of frames across which the standard
//                        thresholds should be taken.

class PrimaryFilter(_name:String) extends Kernel(_name:String)
{
  // to make this thing type parameterizable.
  val typ = UNSIGNED16
  // ------== Data sigs ==------
  val pixelIn = input(typ)
  val pixelOut = output(typ)

  // ------== Control sigs ==------
  val lowThreshIn = input(typ)
  val highThreshIn = input(typ)

  // ------== Parameters ==------
  // The number of frames between reloading threshold values
  val refreshRate = config(UNSIGNED32, 'outputCount, 1000)
  // Adding 2 because border extension.
  val width = 2 + config(UNSIGNED32, 'width, 40)
  val height = 2 + config(UNSIGNED32, 'width, 40)
  
  // ------== Locals ==------
  // The pixel currently being read in, not the pixel under scrutiny
  val curPixel = local(typ, 0)
  // The pixel under scrutiny; i.e. the pixel in the middle
  //  of the 3x3 block being processed. 
  val mainPixel = local(typ, 0)
  // Coordinates of pixel being read in
  val x = local(UNISGNED32, 0)
  val y = local(UNSIGNED32, 0)
  // Index of current frame; used to determine when new threshold
  //    values should be read in.
  val frameCnt = local(UNSIGNED32, 0)

  // TODO -- figure out which line will work in scalapipe and burninate
  //          the line that fails.
  //val vectorSize = config(UNSIGNED16, 'vectorSize, width * 2 + 2)
  val vectorSize = width * 2 + 2;
  // Circular buffer of the last vectorSize pixels read in.
  val pixelBuf = local(Vector(typ, vectorSize))
  // Pointer into the circular buffer.
  val bufPtr = local(UNSIGNED8, 0)

  // the location in the circular buffer of the pixel in the middle
  //  of the 3x3 block being processed. 
  mainPixLoc = local(UNSIGNED32, 0)
  // The buffers of low and high thresholding values foreach pixel
  val lowThreshBuf = local(Vector(typ, width * height))
  val highThreshBuf = local(Vector(typ, width * height))

  // ------== Main Loop body ==------

  curPixel = pixelIn
  if (frameCnt == 0)
  {
    lowThreshBuf (y * height + x) = lowThreshIn
    highThreshBuf (y * height + x) = highThreshIn
  }
  // Big picture algo description:
  //  The pixel currently being read in is the pixel to the lower
  //  right of the output pixel. So, within the circular buffer,
  //  that means two of the lower row of pixels are beside the three
  //  pixels in the top row, and the middle row of pixels are as far
  //  as possible from the other relevant pixels in the circular buffer.
  //    This means the output pixel is actually rowsize + 1 pixels behind
  //  the current pixel, and also rowsize + 1 pixels ahead of the oldest
  //  pixel in the buffer. bufPtr points to the oldest pixel in the buffer.
  //    Thus, we have that the neighboring pixels are at:
  //        bufPtr, bufPtr + 1, bufPtr + 2, // top row
  //        bufPtr + width, // middle row left
  //        bufPtr + width + 1, // output pixel location
  //        bufPtr + width + 2, // middle row right
  //        bufPtr + bufSize - 2  // bottom row left
  //        bufPtr + bufSize - 1, // bottom row middle
  //        curPixel              // bottom row right

  // If this ends up being a bottleneck or using a multiplier,
  //  it only really modularly increments, so that can be changed.
  mainPixLoc = (y - 1) * width + (x - 1)
  if (y > 1 && x > 1)
  {
    mainPixel = pixelBuf ((bufPtr + width + 1) % vectorSize)
    if (curPixel < lowThreshBuf (mainPixLoc))
      pixelOut = 0
    else if 
      ( 
        // The numbers here allow me to easily check that I'm covering the
        //  entire 3x3 box:
        //    1 2 3
        //    4 5 6
        //    7 8 9
        mainPixel > highThreshBuf (mainPixLoc) || // 5
        curPixel > highThreshBuf (mainPixLoc + width + 1) || // 9
        pixelBuf (bufPtr) > highThreshBuf (mainPixLoc - width - 1) || // 1
        pixelBuf ((bufPtr + 1) % vectorSize) > highThreshBuf (mainPixLoc - width) || // 2
        pixelBuf ((bufPtr + 2) % vectorSize) > highThreshBuf (mainPixLoc - width + 1) || // 3
        pixelBuf ((bufPtr + width) % vectorSize) > highThreshBuf (mainPixLoc - 1) || // 4
              // mainPixel, 5
        pixelBuf ((bufPtr + width + 2) % vectorSize) > highThreshBuf (mainPixLoc + 1) || // 6
        pixelBuf ((bufPtr + vectorSize - 1) % vectorSize) > highThreshBuf (mainPixLoc + width - 1) || // 7
        pixelBuf ((bufPtr + vectorSize - 2) % vectorSize) > highThreshBuf (mainPixLoc + width) || // 8
        // curPixel, 9
      )
    {
      pixelOut = mainPixel
    }
  }

  // ------== Loop update step ==------

  // updates frame index
  frameCnt = (frameCnt + 1) % refreshRate
  // updates pixel buffer ptr & pixel buffer
  pixelBuf (bufPtr) = curPixel
  bufPtr = (bufPtr + 1) % vectorSize
  // updates x and y pointers
  x = (x + 1) % width
  if (x == 0)
  {
    y = (y + 1) % height
  }
}

