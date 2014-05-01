import polarization.kernels.HSVtoRGB

import scalapipe.kernels._
import scalapipe.dsl._

object HSVtoRGBTest extends App {

  val typ = FLOAT32
	val Random = new Kernel("Random") {
		val iter = local(UNSIGNED32, 100 * 20 * 20)	
    val i = local(UNSIGNED32, 0)
		val y0 = output (Vector(typ, 3))
    val y_tmp = local (Vector(typ, 3))
    
    y_tmp(0) = cast(stdio.rand(), typ)
    y_tmp(1) = cast(stdio.rand(), typ)
    y_tmp(2) = cast(stdio.rand(), typ)
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
  val Dummy = new Kernel("Dummy")
  {
    val x = input(Vector(typ, 3))
    val tmp = local(Vector(typ, 3))
    tmp = x
  }
	
	val app = new Application {
		val random = Random()
	  val rgb = UUT(random)
    Dummy(rgb)
    map(ANY_KERNEL -> UUT, CPU2FPGA())
    map(UUT -> ANY_KERNEL, FPGA2CPU())
	}

	app.emit("HSVtoRGBTest")
}

