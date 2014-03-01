import scalapipe.kernels._
import scalapipe.dsl._

object BorderTest extends App {

		val Random = new Kernel("Random") {
			val y0 = output(UNSIGNED32)

			val iterations = config(UNSIGNED32, 'iterations, 1600)
			y0 = stdio.rand() % 100

			if(iterations > 0) {
				iterations -= 1
			}

			if(iterations == 0) {
				stop
			}
		}

		val BorderExt = new Kernel("BorderExt") {
			val dataIn = input(UNSIGNED32)
			val dataOut = output(UNSIGNED32)
			val width = config(UNSIGNED32, 'width, 40)
			val height = config(UNSIGNED32, 'height, 40)

			val x = local(UNSIGNED32, 0)
			val y = local(UNSIGNED32, 0)
			val i = local(UNSIGNED32, 0)
			val rowQueue = local(Vector(UNSIGNED32, 40))
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
				//stdio.printf("y:\t%u\n", y)
				rowQueue(x) = tempPixel
				//after that queue has been filled, output it
				if(x == (width - 1)) {
					i = 0
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

		val Print = new Kernel("Print") {
			val x0 = input(UNSIGNED32) 

			val width = config(UNSIGNED32, 'width, 42)
			val height = config(UNSIGNED32, 'height, 42)

			val temp = local(UNSIGNED8, 0)
			val count = local(UNSIGNED32, 1)
			val hCount = local(UNSIGNED32, 0)

			temp = x0
			stdio.printf("%u", temp)
			if(count % width == 0) {
				stdio.printf("\n\n")
				hCount += 1
				if(hCount == height) {
					stdio.printf("\n\n")
					hCount = 0
				}
			} else {
				stdio.printf("\t")
			}
			count += 1

		}

		val app = new Application {

			val iterations = 1600
			val width = 5
			val height = 5
			val newWidth = 7
			val newHeight = 7
			val random = Random('iterations -> iterations)
			val border = BorderExt(random)
			Print(border)
		}

		app.emit("BorderTest")
}