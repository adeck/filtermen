import scalapipe.dsl._

//needs to be for individual pixels, not a mean for all of the pixels

object Mean extends Kernel {

	val pixelData = input(UNSIGNED32)
	val mean = output(FLOAT32)

	val outputCount = config(UNSIGNED32, 'outputCount, 1000)

	val count = local(UNSIGNED32, 0)
	val state = local(UNSIGNED32, 0)
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
