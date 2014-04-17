package polarization.kernels
import scalapipe._
import scalapipe.dsl._

class AoP(_name:String) extends Kernel(_name:String)
{
	//s0 = parms(0), s1 = parms(1), s2 = parms(2)
	val parms = input(Vector(FLOAT32, 4))
	
	val dataOut = output(FLOAT32)

	val t1 = local(FLOAT32, 0)
	val t2 = local(FLOAT32, 0)
	val t3 = local(FLOAT32, 0)

	t1 = parms(2) / parms(1)
	t2 = t1*t1*t1
	t3 = t2*t1*t1/5
	t2 = t2/3
	dataOut = (t1 - t2 + t3)/2
}	

