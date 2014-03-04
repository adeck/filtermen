package veritas.kernels

import scalapipe._
import scalapipe.dsl._

// Relies on the following config parameters:
//    outputCount   --    The number of frames across which the standard
//                        deviation should be taken.

// This code employs the magic number 1600, rather than
// explicitly calculating image size using width * height.
// Again, this is done because the StdDev module is only
// expected to be employed in the Veritas application, where
// images are expected to be 40x40 pixels, and we expect to
// use one of these modules per image.

class StdDev(_name:String) extends Kernel(_name:String)
{

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
