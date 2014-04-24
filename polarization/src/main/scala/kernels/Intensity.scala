package polarization.kernels
import scalapipe._
import scalapipe.dsl._

//Gets its input from: Interpolation
class Intensity(_name:String) extends Kernel(_name:String)
{
	val parms = input(Vector(FLOAT32, 4))
	val dataOut = output(FLOAT32)
	dataOut = (parms(0) + parms(2)) / 2
}
