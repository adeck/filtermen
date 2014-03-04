package veritas.kernels

import scalapipe._
import scalapipe.dsl._

// Relies on the following config parameters:
//    width
//    height

// TODO -- Currently uses the magic number '40' in place of width
// in at least one place, because otherwise it won't compile.
// Clearly, this is an issue that needs fixing.

class BorderExt(_name:String) extends Kernel(_name:String)
{
	 /*
    * Questions that need answering:
	  *   1.  Can we run this block in while loop instead of using implicit 
    *       looping as much as we do?
	  *   2.  How much faster would a while loop be, if possible?
	  */
  val typ = UNSIGNED16

	val dataIn = input(typ)
	val dataOut = output(typ)
	val width = config(UNSIGNED32, 'width, 40)
	val height = config(UNSIGNED32, 'height, 40)

	val x = local(UNSIGNED32, 0)
	val y = local(UNSIGNED32, 0)
	val i = local(UNSIGNED32, 0)
  // TODO FIXME -- change that '40' to something dependent on width
  //              unfortunately, just using width doesn't do the
  //              trick.
	val rowQueue = local(Vector(typ, 40))
	val tempPixel = local(typ, 0)

	val state = local(UNSIGNED8, 0)

	tempPixel = dataIn
	dataOut = tempPixel

	//if x is at the beginning of end of a row, output that pixel twice
	if ((x == 0) || (x == (width - 1))) {
		dataOut = tempPixel
	}

	//if the row is the top or bottom of the image, copy that row into a temporary queue
	if ((y == 0) || (y == height - 1)) {
		rowQueue(x) = tempPixel
		//after that queue has been filled, output it
		if (x == (width - 1)) {
			while (i < width) {
				dataOut = rowQueue(i)
				if ((i == 0) || (i == (width - 1))) {
					dataOut = rowQueue(i)
				}
				i += 1
			}
		}
	}

	// if x is at the end of the row, reset x and move down 1 row for y
	if (x == (width - 1)) {
		x = 0
		if (y == (height - 1)) {
			y = 0
		} else {
			y += 1
		}
	} else {
		x += 1
	}
}
