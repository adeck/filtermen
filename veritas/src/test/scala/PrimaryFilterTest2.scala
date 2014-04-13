import veritas.kernels.PrimaryFilter

import scalapipe.kernels._
import scalapipe.dsl._

object PrimaryTest2 extends App {

	val Random = new Kernel("Random") {
		val iter = local(UNSIGNED16, 42 * 42)	
		val y0 = output (UNSIGNED16)
		val y1 = output (UNSIGNED16)
		val y2 = output (UNSIGNED16)

		val i = local(UNSIGNED16, 0)
    y0 = stdio.rand()
    y1 = 50
    y2 = 150

		if (i < iter) {
			i+=1
		}
		
		else {
			stop
		}
	}
	
	val Print = new Kernel("Print") {
		val x0 = input(UNSIGNED16)
    stdio.printf("%hu\n", x0)
	}	

	val UUT = new PrimaryFilter("Primary")
	
	val app = new Application {
		val random = Random()
		val encoded = UUT(random(0), random(1), random(2),
                      'outputCount -> 30, 'width -> 40, 'height -> 40)
		Print(encoded)
	}

	app.emit("PrimaryTest2")
}

