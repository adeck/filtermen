import polarization.kernels.Splitter

import scalapipe.kernels._
import scalapipe.dsl._

object SplitterTest extends App {

	val imgHeight = 10
	val imgWidth = 10
	val n = 2

	val Random = new Kernel("Random") {
			val y0 = output(UNSIGNED32)

			val temp = local(UNSIGNED32, 0)

			val iterations = config(UNSIGNED32, 'iterations, (imgHeight + 2) * (imgWidth + 2) * 100)
			val i = local(UNSIGNED32, 0)

			temp = stdio.rand() % 100
			y0 = temp

			if (iterations > 0)
				iterations -= 1
      		else
				stop
		}

		val UUT = new Splitter("Splitter",imgWidth,imgHeight,n)

		val Print = new Kernel("Print") {
			val x0 = input(UNSIGNED32) 
			val x1 = input(UNSIGNED32)
			val x2 = input(UNSIGNED32)
			val x3 = input(UNSIGNED32) 
			val x4 = input(UNSIGNED32)
			val x5 = input(UNSIGNED32)
			val x6 = input(UNSIGNED32) 
			val x7 = input(UNSIGNED32)
			val x8 = input(UNSIGNED32)
			val x9 = input(UNSIGNED32) 
			val x10 = input(UNSIGNED32)
			val x11 = input(UNSIGNED32)
			val x12 = input(UNSIGNED32) 
			val x13 = input(UNSIGNED32)
			val x14 = input(UNSIGNED32)
			val x15 = input(UNSIGNED32) 
			val x16 = input(UNSIGNED32)
			val x17 = input(UNSIGNED32)
			val x18 = input(UNSIGNED32) 
			val x19 = input(UNSIGNED32)
			val x20 = input(UNSIGNED32)

			val temp = local(UNSIGNED32, 0)

			temp = x0
			temp = x1
			temp = x2
			temp = x3
			temp = x4
			temp = x5
			temp = x6
			temp = x7
			temp = x8
			temp = x9
			temp = x10
			temp = x11
			temp = x12
			temp = x13
			temp = x14
			temp = x15
			temp = x16
			temp = x17
			temp = x18
			temp = x19
			temp = x20

		}

		val app = new Application {
			param('queueDepth, 10000)
			val iterations = (imgHeight + 2) * (imgWidth + 2) * 10
			val border = UUT(Random('iterations -> iterations))
			Print(border(0),border(1),border(2),border(3),border(4),border(5),border(6),border(7),border(8),border(9),border(10),border(11),
				border(12),border(13),border(14),border(15),border(16),border(17),border(18),border(19),border(20))

			map(ANY_KERNEL -> UUT, CPU2FPGA())
			map(UUT -> ANY_KERNEL, FPGA2CPU())
		}

		app.emit("SplitterTest")

}