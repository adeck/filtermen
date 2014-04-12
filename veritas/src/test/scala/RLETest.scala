import veritas.kernels.RunLengthEncode

import scalapipe.kernels._
import scalapipe.dsl._

object RLE_test extends App {

	val Random = new Kernel("Random") {
		//val iter = input(UNSIGNED32)
		val iter = local(UNSIGNED16, 1600)	
		val y0 = output (UNSIGNED16)

		val temp = local(UNSIGNED16,0)
		val i = local(UNSIGNED16, 0)

		temp = stdio.rand() %100
		if(temp < 40) {
			y0= 0
		}
		else{
			y0 = temp
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
	}	

	val UUT = new RunLengthEncode("RLE")
	
	val app = new Application {
		val random = Random()
		val encoded = UUT(random)
		Print(encoded)
	}

	app.emit("RLE_test")
}

