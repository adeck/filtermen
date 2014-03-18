import veritas.kernels.RunLengthEncode

import scalapipe.kernels._
import scalapipe.dsl._

object RLE_test2 extends App {

	val NumSequence = new Kernel("NumSequence") {
		//val iter = input(UNSIGNED32)
		val iter = local(UNSIGNED32, 50)	
		val y0 = output (UNSIGNED32)

		val i = local(UNSIGNED32, 0)

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
		val x0 = input(UNSIGNED32)
		stdio.printf(" %u \n", x0)
	}	

	val UUT = new RunLengthEncode("RLE")
	
	val app = new Application {
		val num = NumSequence()
		val encoded = UUT(num)
		Print(encoded)
	}

	app.emit("RLE_test2")
}

