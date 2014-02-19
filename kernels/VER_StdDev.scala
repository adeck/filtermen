import scalapipe._
import scalapipe.dsl._

object StdDev extends Kernels {

	val mean = input(FLOAT32)
	val pixelData = input(UNSIGNED32)
	val stdDev = output(FLOAT32)

	val outputCount = config(UNSIGNED32, 'outputCount, 1000)

	val count = local(UNSIGNED32, 0)
	val means = local(Vector(FLOAT32, 1600))
	val stdDevs = local(Vector(FLOAT32, 1600))
	val temp = local(FLOAT32, 0)

	val state = local(UNSIGNED8, 0)

	val i = local(UNSIGNED32, 0)

	switch(state) {

		when(0) {
			count = 1
			i = 0
			while(i < 1600) {
				means(i) = mean
				stdDevs(i) = 0
				i += 1
			}
			state = 1
		}

		when(1) {
			if(count <= outputCount) {
				i = 0
				while(i < 1600) {
					temp = cast(pixelData, FLOAT32)
					stdDevs(i) += ((temp - means(i)) * (temp - means(i)))
					i += 1
				}
				count += 1
			} else {
				i = 0
				while(i < 1600) {
					stdDevs(i) = stdDevs(i) / (outputCount - 1)
					stdDevs(i) = sqrt(stdDevs(i))
					stdDev = stdDevs(i)
					i += 1
				}
				state = 0
			}
		}

	}
}