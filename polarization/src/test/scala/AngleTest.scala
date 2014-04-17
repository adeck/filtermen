import polarization.kernels.AoP

import scalapipe.kernels._
import scalapipe.dsl._

object AngleTest extends App {

	val NumSequence = new Kernel("NumSequence") {
		val iter = local(UNSIGNED16, 1600)
		val out = output(Vector(FLOAT32, 4))
		val tempVec = local(Vector(FLOAT32, 4))
		val i = local(UNSIGNED16, 1)
		tempVec(0) = i
		tempVec(1) = i*i
		tempVec(2) = i+2
		tempVec(3) = 0
		out = tempVec
		if(i<iter) {
			i+=1
		}
		else {
			stop
		}	
	}

	val Print = new Kernel("Print") {
		val x0 = input(FLOAT32)
		stdio.printf(" %f \n", x0)

	}

	val UUT = new AoP("AoP")

	val app = new Application {
		val num = NumSequence()
		val encoded = UUT(num)
		Print(encoded)
	//	map(ANY_KERNEL -> UUT, CPU2FPGA())
	//	map(UUT -> ANY_KERNEL, FPGA2CPU())

	}
}
