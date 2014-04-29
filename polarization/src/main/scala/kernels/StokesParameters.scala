package polarization.kernels
import scalapipe._
import scalapipe.dsl._

class StokesParameters(_name:String) extends Kernel(_name:String)
{
	val typ = UNSIGNED32

	val dataIn = input(Vector(FLOAT32, 4))
	val dataOut = output(Vector(FLOAT32, 3))

	val intensity = local(Vector(FLOAT32, 4))
	val stokes = local(Vector(FLOAT32, 3))

	intensity = dataIn

	stokes(0) = intensity(0) + intensity(1)
	stokes(1) = intensity(0) - intensity(1)
	stokes(2) = intensity(2) - intensity(3)

	dataOut = stokes	
}