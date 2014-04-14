import veritas.kernels.StdDev

import scalapipe.kernels._
import scalapipe.dsl._

object StdDevTest extends App {

	val pixels = 1600
	val nums = 1000

		val RandomReader = new Kernel("RandomReader") {
			val y0 = output(FLOAT32)
			val y1 = output(UNSIGNED16)

			val iterations = config(UNSIGNED32, 'iterations, pixels * 10 * nums)
			val i = local(UNSIGNED32, 0)
			val fd = local(stdio.FILEPTR, 0)
			//val fileName = local(STRING, "RandomNumbers.txt")
			val temp = local(SIGNED32)
			
			if(fd == 0) {
				fd = stdio.fopen("./RandomNumbers.txt", "r")
				if(fd == 0) {
					stdio.printf("ERROR: could not open %s\n", "RandomNumbers.txt")
					stdio.exit(-1)
				}
				i = 0
				/*while(i < pixels) {
					y0 = 60
					i += 1
				}*/
			} else {
				stdio.fscanf(fd, "%d", addr(temp))
				//stdio.printf("%d\n", temp)
				y1 = temp
			}
			if(iterations > 0) {
				iterations -= 1
				if(iterations % nums == 0) {
					i = 0
					while(i < pixels) {
						y0 = 60
						i += 1
					}
				}
			} else {
				stop
			}
		}

		val UUT = new StdDev("StdDev")

		val Print = new Kernel("Print") {
			val x0 = input(FLOAT32) 

			val count = local(UNSIGNED32, 0)
			val temp = local(FLOAT32, 0)

			temp = x0
			stdio.printf("Pixel %u:\t%.2f\n", count % pixels, temp)
			if(count % pixels == (pixels - 1)) {
				stdio.printf("\n")
			}
			count += 1
			//if(count == pixels * nums * 10) {
			//	stdio.exit(0)
			//}
			//stdio.exit(0)
		}

		val app = new Application {
			param('queueDepth, 65536)
			//val iterations = 1600
			//val width = 5
			//val height = 5
			//val newWidth = 7
			//val newHeight = 7
			val random = RandomReader()
			val stdDev = UUT(random(0), random(1))
			Print(stdDev)
			map(ANY_KERNEL -> UUT, CPU2FPGA())
			map(UUT -> ANY_KERNEL, FPGA2CPU())
		}

		app.emit("StdDevTest")
}
