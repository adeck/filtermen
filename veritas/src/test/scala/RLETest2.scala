import veritas.kernels.RunLengthEncode

import scalapipe.kernels._
import scalapipe.dsl._

object RLE_test2 extends App {

	val NumSequence = new Kernel("NumSequence") {
		val iter = local(UNSIGNED16, 1600)	
		val y0 = output (UNSIGNED16)

		val i = local(UNSIGNED16, 1)

		if(i < 20 || i>30) {
			y0= 0
		}
		else{
			y0 = i
		}

		if(i < iter) {
			i+=1
		}
		
		else {
			stop
		}
	}
	
	val Print = new Kernel("Print") {
		val x0 = input(UNSIGNED16)
		stdio.printf(" %u \n", x0)
	//	stdio.exit(0)
	}	

	val UUT = new RunLengthEncode("RLE")
	
	val app = new Application {
		val num = NumSequence()
		val encoded = UUT(num)
		Print(encoded)
		map(ANY_KERNEL -> UUT, CPU2FPGA())
		map(UUT -> ANY_KERNEL, FPGA2CPU())
	}

	app.emit("RLE_test2")
}

