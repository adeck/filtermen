import polarization.kernels.PolarizationBorderExt

import scalapipe.kernels._
import scalapipe.dsl._

object PolarizationBorderExtTest extends App {

	val imgHeight = 500
	val imgWidth = 500

	val Random = new Kernel("Random") {
			val y0 = output(UNSIGNED32)

			val iterations = config(UNSIGNED32, 'iterations, imgHeight * imgWidth * 10)
			val i = local(UNSIGNED32, 0)
			y0 = stdio.rand() % 100
			if (iterations > 0)
				iterations -= 1
      		else
				stop
		}

		val UUT = new PolarizationBorderExt("PolBorderExt",imgWidth,imgHeight)

		val Print = new Kernel("Print") {
			val x0 = input(UNSIGNED32) 

			val width = config(UNSIGNED32, 'width, imgWidth + 2)
			val height = config(UNSIGNED32, 'height, imgHeight + 2)

			val temp = local(UNSIGNED8, 0)
			val count = local(UNSIGNED32, 1)
			val hCount = local(UNSIGNED32, 0)
			val counter = local(UNSIGNED32, 0)

			temp = x0
			//stdio.printf("%3.d", temp)
			if (count % width == 0) {
				//stdio.printf("\n\n")
				hCount += 1
				if (hCount == height) {
					stdio.printf("Frame:\t%d\n\n",counter)
					counter += 1
					hCount = 0
				}
			} else {
				//stdio.printf(" ")
			}	
			count += 1

		}

		val app = new Application {
			param('queueDepth, 10000)
			val iterations = imgHeight * imgWidth * 10
			val random = Random('iterations -> iterations)
			val border = UUT(random, 'width -> imgWidth, 'height -> imgHeight)
			Print(border)

			map(ANY_KERNEL -> UUT, CPU2FPGA())
			map(UUT -> ANY_KERNEL, FPGA2CPU())
		}

		app.emit("PolarizationBorderExtTest500")

}