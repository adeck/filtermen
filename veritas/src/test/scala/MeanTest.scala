import veritas.kernels.Mean

import scalapipe.kernels._
import scalapipe.dsl._

object MeanTest extends App {

	val pixels = 10

		val Random = new Kernel("Random") {
			val y0 = output(UNSIGNED16)

			val iterations = config(UNSIGNED32, 'iterations, pixels * 20000)
			y0 = stdio.rand() % 100

			if(iterations > 0) {
				iterations -= 1
			}

			if(iterations == 0) {
				stop
			}
		}

		val UUT = new Mean("Mean")

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
		//	stdio.exit(0)
		}

		val app = new Application {

			val iterations = 1600
			val width = 5
			val height = 5
			val newWidth = 7
			val newHeight = 7
			val random = Random()
			val mean = UUT(random)
			Print(mean)
			map(ANY_KERNEL -> UUT, CPU2FPGA())
			map(UUT -> ANY_KERNEL, FPGA2CPU())
		}

		app.emit("MeanTest")
}
