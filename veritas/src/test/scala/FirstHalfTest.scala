import veritas.kernels.{Mean, StdDev, Threshold}

import scalapipe.kernels._
import scalapipe.dsl._

object FirstHalfTest extends App {

	val pixels = 1600
	val nums = 1000

		val RandomReader = new Kernel("RandomReader") {
			val y0 = output(UNSIGNED32)
			val y1 = output(UNSIGNED32)

			val iterations = config(UNSIGNED32, 'iterations, pixels * 10 * nums)
			val i = local(UNSIGNED32, 0)
			val fd = local(stdio.FILEPTR, 0)
			//val fileName = local(STRING, "RandomNumbers.txt")
			val temp = local(SIGNED32)
			
			if(fd == 0) {
				fd = stdio.fopen("../RandomNumbers.txt", "r")
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
				y0 = temp
				y1 = temp
			}
			if(iterations > 0) {
				iterations -= 1
				/*if(iterations % nums == 0) {
					i = 0
					while(i < pixels) {
						y0 = 60
						i += 1
					}
				}*/
			} else {
				stop
			}
		}

		val Mean0 = new Mean("Mean")
		val StdDev0 = new StdDev("StdDev")
		val Threshold0 = new Threshold("Threshold")

		val Print = new Kernel("Print") {
			val mean = input(FLOAT32)
			val stdDev = input(FLOAT32) 
			val loThreshold = input(UNSIGNED32)
			val hiThreshold = input(UNSIGNED32)

			val count = local(UNSIGNED32, 0)
			val temp0 = local(FLOAT32, 0)
			val temp1 = local(FLOAT32, 0)
			val temp2 = local(UNSIGNED32, 0)
			val temp3 = local(UNSIGNED32, 0)

			temp0 = mean
			temp1 = stdDev
			temp2 = loThreshold
			temp3 = hiThreshold
			stdio.printf("Pixel %u:\tmean:%.2f\tstdDev:\t%.2f\tloThreshold:\t%u\thiThreshold:\t%u\n", count % pixels, temp0,temp1,temp2,temp3)
			if(count % pixels == (pixels - 1)) {
				stdio.printf("\n")
			}
			count += 1
			stdio.exit(0)
		}

		val app = new Application {
			param('queueDepth, 20000)
			//val iterations = 1600
			//val width = 5
			//val height = 5
			//val newWidth = 7
			//val newHeight = 7
			val random = RandomReader()
			val mean = Mean0(random(0))
			val stdDev = StdDev0(mean(0), random(1))
			val threshold = Threshold0(mean(1), stdDev(0))
			Print(mean(2), stdDev(1), threshold(0), threshold(1))
			map(ANY_KERNEL -> Mean0, CPU2FPGA())
      // There is no FPGA2FPGA symbol for some reason. *shrug*
			//map(Mean0 -> ANY_KERNEL, FPGA2FPGA())
			//map(StdDev0 -> ANY_KERNEL, FPGA2FPGA())
			map(Threshold0 -> ANY_KERNEL, FPGA2CPU())
		}

		app.emit("FirstHalfTest")
}
