import polarization.kernels.StokesParameters

import scalapipe.kernels._
import scalapipe.dsl._

object StokesParametersTest extends App {

	val imgHeight = 100
	val imgWidth = 100


	val Random = new Kernel("Random") {
			val y0 = output(Vector(FLOAT32, 4))

			val iterations = config(UNSIGNED32, 'iterations, imgHeight * imgWidth * 20)
			val i = local(UNSIGNED32, 0)
			val temp = local(Vector(FLOAT32, 4))
			temp(0) = cast(stdio.rand() % 10000, FLOAT32)/100.0
			temp(1) = cast(stdio.rand() % 10000, FLOAT32)/100.0
			temp(2) = cast(stdio.rand() % 10000, FLOAT32)/100.0
			temp(3) = cast(stdio.rand() % 10000, FLOAT32)/100.0

			y0 = temp
			if (iterations > 0)
				iterations -= 1
      		else
				stop
		}

		val UUT = new StokesParameters("StokesParameters")

		val Print = new Kernel("Print") {
			val x0 = input(Vector(FLOAT32, 3)) 

			val width = config(UNSIGNED32, 'width, imgWidth)
			val height = config(UNSIGNED32, 'height, imgHeight)

			val temp = local(Vector(FLOAT32, 3))
			val count = local(UNSIGNED32, 1)
			val hCount = local(UNSIGNED32, 0)
			val counter = local(UNSIGNED32, 0)

			temp = x0
			//stdio.printf("Stokes 0:\t%.2f\nStokes 1:\t %.3f\nStokes 2:\t%.2f\n\n", temp(0), temp(1), temp(2))
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
			val iterations = imgHeight * imgWidth * 20
			val random = Random('iterations -> iterations)
			val border = UUT(random, 'width -> imgWidth, 'height -> imgHeight)
			Print(border)

			map(ANY_KERNEL -> UUT, CPU2FPGA())
			map(UUT -> ANY_KERNEL, FPGA2CPU())
		}

		app.emit("StokesParametersTest100")

}