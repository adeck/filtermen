import scalapipe._
import scalapipe.dsl._

object StdDev extends Kernels {

	val mean = input(FLOAT32)
	val pixelData = input(UNSIGNED32)
	val stdDev = output(FLOAT32)

	val outputCount = config(UNSIGNED32, 'outputCount, 1000)

	val count = local(UNSIGNED32, 0)
	val tempMean = local(FLOAT32, 0)
	val tempStdDev = local(FLOAT32, 0)
	val temp = local(FLOAT32, 0)

	val state = local(UNSIGNED8, 0)

	switch(state) {

		when(0) {
			count = 0
			tempMean = mean
			tempStdDev = 0
			state = 1
		}

		when(1) {
			if(count < outputCount) {
				temp = (FLOAT32) pixelData
				tempStdDev += ((temp - tempMean) * (temp - tempMean))
				count += 1
			} else {
				tempStdDev = tempStdDev / (outputCount - 1)
				tempStdDev = sqrt(tempStdDev) //FIXME: how do we want to do this?
				stdDev = tempStdDev
				state = 0
			}
		}

	}
}