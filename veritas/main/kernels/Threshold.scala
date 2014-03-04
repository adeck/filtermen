import scalapipe._
import scalapipe.dsl._

// Does not rely on config parameters.

object Threshold extends Kernels {

	val mean = input(FLOAT32)
	val stdDev = input(FLOAT32)

	val loThreshold = output(UNSIGNED8)
	val hiThreshold = output(UNSIGNED8)

	val tempMean = local(FLOAT32)
	val tempStdDev = local(FLOAT32)

	tempMean = mean
	tempStdDev = stdDev

	loThreshold = cast(UNSIGNED8, (tempMean + (1.5 * tempStdDev)))
	hiThreshold = cast(UNSIGNED8, (tempMean + (2 * tempStdDev)))
}
