package kernels
import scalapipe._
import scalapipe.dsl._

object BorderExt extends Kernels {
	/*Questions that need answering:
		Can we run this block in while loop instead of using implicit looping as much as we do?
			How much faster would a while loop be, if possible?
	*/

	val dataIn = input(UNSIGNED32)
	val dataOut = output(UNSIGNED32)
	val width = config(UNSIGNED32, 'width, 40)
	val height = config(UNSIGNED32, 'height, 40)

	val x = local(UNSIGNED32, 0)
	val y = local(UNSIGNED32, 0)
	val i = local(UNSIGNED32, 0)
	val rowQueue = local(Vector(UNSIGNED32, width))
	val tempPixel = local(UNSIGNED32, 0)

	val state = local(UNSIGNED8, 0)

	tempPixel = dataIn
	dataOut = tempPixel

	//if x is at the beginning of end of a row, output that pixel twice
	if((x == 0) || (x == (width - 1))) {
		dataOut = tempPixel
	}

	//if the row is the top or bottom of the image, copy that row into a temporary queue
	if((y == 0) || (y == height - 1)) {
		rowQueue(x) = tempPixel
		//after that queue has been filled, output it
		if(x == (width - 1)) {
			while(i < width) {
				dataOut = rowQueue(i)
				if((i == 0) || (i == (width - 1))) {
					dataOut = rowQueue(i)
				}
				i += 1
			}
		}
	}

	//if x is at the end of the row, reset x and move down 1 row for y
	if(x == (width - 1)) {
		x = 0
		if(y == (height - 1)) {
			y = 0
		} else {
			y += 1
		}
	} else {
		x += 1
	}
}