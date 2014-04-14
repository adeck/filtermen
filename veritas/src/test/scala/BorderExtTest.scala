import veritas.kernels.BorderExt

import scalapipe.kernels._
import scalapipe.dsl._

object BorderExtTest extends App {

		val Random = new Kernel("Random") {
			val y0 = output(UNSIGNED16)

			val iterations = config(UNSIGNED32, 'iterations, 1600)
			y0 = stdio.rand() % 100

			if (iterations > 0)
				iterations -= 1
      else
				stop
		}

		val UUT = new BorderExt("BorderExt")

		val Print = new Kernel("Print") {
			val x0 = input(UNSIGNED16) 

			val width = config(UNSIGNED32, 'width, 42)
			val height = config(UNSIGNED32, 'height, 42)

			val temp = local(UNSIGNED8, 0)
			val count = local(UNSIGNED32, 1)
			val hCount = local(UNSIGNED32, 0)

			temp = x0
			stdio.printf("%u", temp)
			if (count % width == 0) {
				stdio.printf("\n\n")
				hCount += 1
				if (hCount == height) {
					stdio.printf("\n\n")
					hCount = 0
				}
			} else {
				stdio.printf("\t")
			}
			count += 1
		//	stdio.exit(0)
		}

		val app = new Application {
			val iterations = 1600
			val width = 5
			val height = 5
			val newWidth = 7
			val newHeight = 7
			val random = Random('iterations -> iterations)
			val border = UUT(random)
			Print(border)
			map(ANY_KERNEL -> UUT, CPU2FPGA())
			map(UUT -> ANY_KERNEL, FPGA2CPU())
		}

		app.emit("BorderTest")
}
