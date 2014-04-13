package veritas.kernels

import scalapipe.dsl._
import scalapipe.kernels._

// Relies on the following config parameters:
//    width
//    height
//    outputCount   --    The number of frames across which the standard
//                        thresholds should be taken.

// TODO -- Currently, it uses a mix of magic numbers and width and height 
//          parameters due to type constraints and a lack of casting.
//         These need to be removed, because as is they mean that
//          width and height are both _required_ to be 40. Also, as is
//          the code isn't DRY. But it compiles, anyway.

class PrimaryFilter(_name:String) extends Kernel(_name:String)
{
  // to make this thing type parameterizable.
  val typ = UNSIGNED16
  // ------== Data sigs ==------
  val pixelIn = input(typ, 'pixelIn)
  val pixelOut = output(typ, 'pixelOut)

  // ------== Control sigs ==------
  val lowThreshIn = input(typ, 'lowThreshIn)
  val highThreshIn = input(typ, 'highThreshIn)

  // ------== Parameters ==------
  // The number of frames between reloading threshold values
  val refreshRate = config(UNSIGNED32, 'outputCount, 1000)
  // Adding 2 because border extension.
  val width = 2 + config(UNSIGNED32, 'width, 40)
  val height = 2 + config(UNSIGNED32, 'height, 40)
  
  // ------== Locals ==------
  // Coordinates of pixel being read in
  val x = local(UNSIGNED32, 0)
  val y = local(UNSIGNED32, 0)
  // Index of current frame; used to determine when new threshold
  //    values should be read in.
  val frameCnt = local(UNSIGNED32, 0)

  // TODO FIXME -- 42 == row width, however, there is no point in having
  //          a 'width' parameter if I put this magic number in.
  val vectorSize = 42 * 2 + 2
  // TODO FIXME -- This _should_ be width * height. Unfortunately, it's
  //          not being nice about that.
  val imgSize = 42 * 42
  // Circular buffer of the last vectorSize pixels read in.
  val pixelBuf = local(Vector(typ, vectorSize))
  // Pointer into the circular buffer.
  val bufPtr = local(UNSIGNED8, 0)

  // the location in the circular buffer of the pixel in the middle
  //  of the 3x3 block being processed. 
  val mainPixLoc = local(UNSIGNED32)
  // The buffers of low and high thresholding values foreach pixel
  val lowThreshBuf = local(Vector(typ, imgSize))
  val highThreshBuf = local(Vector(typ, imgSize))

  // top row
  val upperLeft = local(typ)
  val upperLeftHi = local(typ)
  val upperMid = local(typ)
  val upperMidHi = local(typ)
  val upperRight = local(typ)
  val upperRightHi = local(typ)
  // middle row
  val midLeft = local(typ)
  val midLeftHi = local(typ)
  val midMid = local(typ)
  val midMidLo = local(typ)
  val midMidHi = local(typ)
  val midRight = local(typ)
  val midRightHi = local(typ)
  // bottom row
  val lowerLeft = local(typ)
  val lowerLeftHi = local(typ)
  val lowerMid = local(typ)
  val lowerMidHi = local(typ)
  val lowerRight = local(typ)
  val lowerRightHi = local(typ)

  // ------== Main Loop body ==------

  lowerRight = pixelIn
  if (frameCnt == 0)
  {
    lowThreshBuf (y * height + x) = lowThreshIn
    highThreshBuf (y * height + x) = highThreshIn
  }

  if (y > 1 && x > 1)
  {
    // If this ends up being a bottleneck or using a multiplier,
    //  it only really modularly increments, so that can be changed.
    mainPixLoc = (y - 1) * width + (x - 1)
    // the pixel to filter
    if ((bufPtr + width + 1) >= vectorSize)
      midMid = pixelBuf ((bufPtr + width + 1) - vectorSize)
    else
      midMid = pixelBuf (bufPtr + width + 1)
    midMidLo = lowThreshBuf (mainPixLoc)
    midMidHi = highThreshBuf (mainPixLoc)
    // top row
    upperLeft = pixelBuf (bufPtr)
    upperLeftHi = highThreshBuf (mainPixLoc - width - 1)
    if ((bufPtr + 1) == vectorSize)
      upperMid = pixelBuf (0)
    else
      upperMid = pixelBuf (bufPtr + 1)
    upperMidHi = highThreshBuf (mainPixLoc - width)
    if ((bufPtr + 2) >= vectorSize)
      upperRight = pixelBuf ((bufPtr + 2) - vectorSize)
    else
      upperRight = pixelBuf (bufPtr + 2)
    upperRightHi = highThreshBuf (mainPixLoc - width + 1)
    // middle row
    if ((bufPtr + width) >= vectorSize)
      midLeft = pixelBuf ((bufPtr + width) - vectorSize)
    else
      midLeft = pixelBuf (bufPtr + width)
    midLeftHi = highThreshBuf (mainPixLoc - 1)
    if ((bufPtr + width + 2) >= vectorSize)
      midRight = pixelBuf ((bufPtr + width + 2) - vectorSize)
    else
      midRight = pixelBuf (bufPtr + width + 2)
    midRightHi = highThreshBuf (mainPixLoc + 1)
    // bottom row
    if (bufPtr == 0 || bufPtr == 1)
      lowerLeft = pixelBuf (bufPtr + vectorSize - 2)
    else
      lowerLeft = pixelBuf (bufPtr - 2)
    lowerLeftHi = highThreshBuf (mainPixLoc + width - 1)
    if (bufPtr == 0)
      lowerMid = pixelBuf (vectorSize - 1)
    else
      lowerMid = pixelBuf (bufPtr - 1)
    lowerMidHi = highThreshBuf (mainPixLoc + width)
    //lowerRight
    lowerRightHi = highThreshBuf (mainPixLoc + width + 1)
    if (midMid < midMidLo)
    {
      pixelOut = 0
    }
    // Checks lowerRight first because that doesn't require a
    // memory access.
    else if (
             midMidHi < midMid ||
             lowerRightHi < lowerRight ||
             upperLeftHi < upperLeft ||
             upperMidHi < upperMid ||
             upperRightHi < upperRight ||
             midLeftHi < midLeft ||
             midRightHi < midRight ||
             lowerLeftHi < lowerLeft ||
             lowerMidHi < lowerMid
            )
    {
      pixelOut = midMid
    }
    else
    {
      pixelOut = 0
    }
  }

  // ------== Loop update step ==------

  // updates pixel buffer ptr & pixel buffer
  pixelBuf (bufPtr) = lowerRight
  if ((bufPtr + 1) == vectorSize)
    bufPtr = 0
  else
    bufPtr = bufPtr + 1
  // updates x and y pointers
  if ((x + 1) == width)
    x = 0
  else
    x = (x + 1)
  if (x == 0)
  {
    if ((y + 1) == height)
    {
      y = 0
      // updates frame index
      if ((frameCnt + 1) == refreshRate)
        frameCnt = 0
      else
        frameCnt += 1
    }
    else 
      y = (y + 1)
  }
}


