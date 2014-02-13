import scalapipe._
import scalapipe.dsl._

object Mean extends Kernels {

	val pixelData = input(UNSIGNED32)
	val mean = output(FLOAT32)

	val outputCount = config(UNSIGNED32, 'outputCount, 1000)

	val count = local(UNSIGED32, 0)
	val state = local(UNSIGED32, 0)
	val tempMean = local(FLOAT32, 0)
	val temp = local(FLOAT32, 0)

	switch(state) {

		when(0) {
			count = 0
			tempMean = 0
			state = 1
		}

		when(1) {
			if(count < outPutCount) {
				temp = tempMean * count
				count += 1
				temp += cast(pixelData, FLOAT32)
				tempMean = temp / count
			} else {
				mean = tempMean
				state = 0
			}
		}
	}
}
