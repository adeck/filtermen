package polarization.kernels
import scalapipe._
import scalapipe.dsl._

//inputs from: Stokes Parameters
//outputs to: Combine
class DoLP(_name:String) extends Kernel(_name:String)
{
	val parms = input(Vector(FLOAT32, 4))
	val dataOut = output(FLOAT32)
	val s1 = local(FLOAT32, 0)
	val s2 = local(FLOAT32, 0)
	s1 = parms(1)
	s2 = parms(2)

	dataOut = sqrt(s1*s1 + s2*s2)/parms(0)
}

