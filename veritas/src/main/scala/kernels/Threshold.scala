package veritas.kernels

import scalapipe._
import scalapipe.dsl._

// Does not rely on config parameters.

class Threshold(_name:String) extends Kernel(_name:String)
{
  val typ = UNSIGNED16
	val mean = input(FLOAT32)
	val stdDev = input(FLOAT32)

	val loThreshold = output(typ)
	val hiThreshold = output(typ)

	val tempMean = local(FLOAT32)
	val tempStdDev = local(FLOAT32)

	tempMean = mean
	tempStdDev = stdDev

  // This is how type conversion is done.
  //    UNSIGNED16( <thing_to_convert> )
	loThreshold = typ(tempMean + (1.5 * tempStdDev))
	hiThreshold = typ(tempMean + (2 * tempStdDev))
}
