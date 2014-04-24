package polarization.kernels
import scalapipe._
import scalapipe.dsl._

//Gets input from: Intensity, DoLP, AoP
//Sends output to: GenHSV

class Combine(_name:String) extends Kernel(_name:String)
{
	val intensity = input(FLOAT32)
	val degree = input(FLOAT32)
	val angle = input(FLOAT32)
	val dataOut = output(Vector(FLOAT32, 3)
	val temp = local(Vector(FLOAT32,3))
	temp(0)=intensity
	temp(1) = degree
	temp(2) = angle
	dataOut = temp
}
