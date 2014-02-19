import scalapipe.dsl._

//needs to be for individual pixels, not a mean for all of the pixels

object Mean extends Kernel {

	val pixelData = input(UNSIGNED32)
	val mean = output(FLOAT32)

	val outputCount = config(UNSIGNED32, 'outputCount, 1000)

	val count = local(UNSIGNED32, 0)
	val state = local(UNSIGNED32, 0)
	val means = local(Vector(FLOAT32, 1600))
	val temp = local(FLOAT32, 0)

	val i = local(UNSIGNED32, 0)

	switch(state) {

		when(0) {
			count = 1
			i = 0
			while(i < 1600) {
				means(i) = 0
				i += 1
			}
			state = 1
		}

		when(1) {
			if(count <= outPutCount) {
				i = 0
				while(i < 1600) {
					temp = means(i) * count
					temp += cast(pixelData, FLOAT32)
					means(i) = temp / count
					i += 1
				}
				count += 1
			} else {
				i = 0
				while(i < 1600) {
					mean = means(i)
					i += 1
				}
				state = 0
			}
		}
	}
}
