import polarization.kernels.HSVtoRGB

import scalapipe.kernels._
import scalapipe.dsl._

object HSVtoRGBTest extends App {

  val typ = FLOAT32
	val Random = new Kernel("Random") {
		val iter = local(UNSIGNED16, 100 * 20 * 20)	
    val i = local(UNSIGNED16, 0)
		val y0 = output (Vector(typ, 3))
    val y_tmp = local (Vector(typ, 3))
    
    y_tmp(0) = stdio.rand()
    y_tmp(1) = stdio.rand()
    y_tmp(2) = stdio.rand()
		if (i < iter) 
    {
			i+=1
		}
		else 
    {
			stop
		}
    y0 = y_tmp
	}

	val UUT = new HSVtoRGB("HSVtoRGB")
	
	val app = new Application {
		val random = Random()
	  UUT(random)
    map(ANY_KERNEL -> UUT, CPU2FPGA())
    map(UUT -> ANY_KERNEL, FPGA2CPU())
	}

	app.emit("HSVtoRGBTest")
}

