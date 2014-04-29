import polarization.kernels.Interpolation

import scalapipe.kernels._
import scalapipe.dsl._

object InterpolationTest extends App {

	val imgHeight = 250
	val imgWidth = 250

	val Random = new Kernel("Random") {
			val y0 = output(UNSIGNED32)
			val y1 = output(UNSIGNED32)

			val temp = local(UNSIGNED32, 0)

			val iterations = config(UNSIGNED32, 'iterations, (imgHeight + 2) * (imgWidth + 2) * 100)
			val i = local(UNSIGNED32, 0)

			temp = stdio.rand() % 100
			y0 = temp
			y1 = temp
			if (iterations > 0)
				iterations -= 1
      		else
				stop
		}

		val UUT = new Interpolation("PolBorderExt",imgWidth,imgHeight)

		val Print = new Kernel("Print") {
			val x0 = input(UNSIGNED32) 
			val x1 = input(Vector(FLOAT32,4))

			val width = config(UNSIGNED32, 'width, imgWidth + 2)
			val height = config(UNSIGNED32, 'height, imgHeight + 2)

			val temp = local(UNSIGNED32, 0)
			val tempF = local(Vector(FLOAT32, 4))
			val count = local(UNSIGNED32, 1)
			val hCount = local(UNSIGNED32, 0)
			val counter = local(UNSIGNED32, 0)

			temp = x0
			tempF = x1
			//stdio.printf("%f\t%f\t%f\t%f\n", tempF(0), tempF(1), tempF(2), tempF(3))
			if (count % width == 0) {
				//stdio.printf("\n\n")
				hCount += 1
				if (hCount == height) {
					stdio.printf("Frame:\t%d\n\n", counter)
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
			val iterations = (imgHeight + 2) * (imgWidth + 2) * 10
			val random = Random('iterations -> iterations)
			val border = UUT(random(0), 'width -> imgWidth, 'height -> imgHeight)
			Print(random(1),border)

			map(ANY_KERNEL -> UUT, CPU2FPGA())
			map(UUT -> ANY_KERNEL, FPGA2CPU())
		}

		app.emit("InterpolationTest250")

}