package polarization.kernels
import scalapipe._
import scalapipe.dsl._

class DoLP(_name:String) extends Kernel(_name:String)
{
	val parms = input(Vector(FLOAT32, 4))
	val s0 = local(FLOAT32, parms(0))
	val s1 = local(FLOAT32, parms(1))
	val s2 = local(FLOAT32, parms(2))
	

	val dataOut = output(FLOAT32)

	dataOut = sqrt(s1*s1 + s2*s2)/s0
}

