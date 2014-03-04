package veritas.kernels

import scalapipe.dsl._

// Relies on the following config parameters:
//    outputCount   --    The number of frames across which the mean
//                        should be taken.

// Gives the mean for each individual pixel across an
// outputCount number of 'frames', not a mean for all of the pixels
// in one frame.
//
// It is worth noting that, as currently written, this hard-codes
// the magic number '1600' for frame size, rather than using
// config parameters. This is done because the mean module is expected
// to only be used to process 40x40 pixel images.

class Mean(_name:String) extends Kernel(_name:String)
{

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
			while (i < 1600) {
				means(i) = 0
				i += 1
			}
			state = 1
		}

		when(1) {
			if (count <= outputCount) {
				i = 0
				while (i < 1600) {
					temp = means(i) * count
					temp += cast(pixelData, FLOAT32)
					means(i) = temp / count
					i += 1
				}
				count += 1
			} else {
				i = 0
				while (i < 1600) {
					mean = means(i)
					i += 1
				}
				state = 0
			}
		}
	}
}
