package polarization.kernels
import scalapipe._
import scalapipe.dsl._

class DoLP(_name:String) extends Kernel(_name:String)
{
	val s0 = input(FLOAT32)
	val s1 = input(FLOAT32)
	val s2 = input(FLOAT32)

	val dataOut = output(FLOAT32)

	dataOut = sqrt(s1*s1 + s2*s2)/s0
}

