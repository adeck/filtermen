package polarization.kernels
import scalapipe._
import scalapipe.dsl._

class GenHSV(_name:String) extends Kernel(_name:String)
{
	//parms(0) = intensity
	//parms(1) = degree 
	//parms(2) = angle
	val parms = input(Vector(FLOAT32, 3))
	val dataOut = output(Vector(FLOAT32, 3))
	val temp = local(Vector(FLOAT32,3))

	if(parms(0) > 255){
		temp(0) = 255
	}
	else {
		temp(0) = parms(0)
	}
	if(parms(1) > 255){
		temp(1) = 255
	}
	else {
		temp(1) = parms(1)
	}
	if(parms(2) > 179){
		temp(2) = 179
	}
	else {
		temp(2) = parms(2)
	}
	dataOut = temp
}
