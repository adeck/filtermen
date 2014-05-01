package polarization.kernels
import scalapipe._
import scalapipe.dsl._

class InterpolationInt(_name:String,width:Int,height:Int) extends Kernel(_name:String)
{
	val typ = UNSIGNED32

	val dataIn = input(typ)
	val dataOut = output(Vector(UNSIGNED32, 4))

	//val width = config(UNSIGNED32, 'width, 10)
	//val height = config(UNSIGNED32, 'height, 10)

	val x = local(UNSIGNED32, 0)
	val y = local(UNSIGNED32, 0)
	val i = local(UNSIGNED32, 0)

	val temp1 = local(UNSIGNED32, 0)
	val temp2 = local(UNSIGNED32, 0)

	val tempVector = local(Vector(UNSIGNED32, 4))

	val rowQueue1 = local(Vector(typ, width + 2))
	val rowQueue2 = local(Vector(typ, width + 2))
	val rowQueue3 = local(Vector(typ, width + 2))

	val state = local(UNSIGNED8, 0)

	switch(state) {

		when(0) {
			i = 0
			while(i < width + 2) {
				rowQueue1(i) = dataIn
				i += 1
			}

			i = 0
			while(i < width + 2) {
				rowQueue2(i) = dataIn
				i += 1
			}

			i = 0
			while(i < width + 2) {
				rowQueue3(i) = dataIn
				i += 1
			}

			x = 1
			y = 1
			state = 1
		}

		when(1) {
			tempVector(0) = rowQueue2(x)
			tempVector(1) = (rowQueue2(x - 1) + rowQueue2(x + 1)) >> 1
			tempVector(2) = (rowQueue1(x) + rowQueue3(x)) >> 1
			tempVector(3) = (rowQueue1(x - 1) + rowQueue1(x + 1) + rowQueue3(x - 1) + rowQueue3(x + 1)) >> 2
			dataOut = tempVector
			if(x < width) {
				x += 1
				state = 2
			} else {
				if(y < height) {
					rowQueue1 = rowQueue2
					rowQueue2 = rowQueue3
					i = 0
					while(i < width + 2) {
						rowQueue3(i) = dataIn
						i += 1
					}
					y += 1
					x = 1
					state = 3
				} else {
					state = 0
				}
			}
		}

		when(2) {
			tempVector(0) = (rowQueue2(x - 1) + rowQueue2(x + 1)) >> 1
			tempVector(1) = rowQueue2(x)
			tempVector(2) = (rowQueue1(x - 1) + rowQueue1(x + 1) + rowQueue3(x - 1) + rowQueue3(x + 1)) >> 2
			tempVector(3) = (rowQueue1(x) + rowQueue3(x)) >> 1
			dataOut = tempVector
			if(x < width) {
				x += 1
				state = 1
			} else {
				if(y < height) {
					rowQueue1 = rowQueue2
					rowQueue2 = rowQueue3
					i = 0
					while(i < width + 2) {
						rowQueue3(i) = dataIn
						i += 1
					}

					y += 1
					x = 1
					state = 3
				} else {
					state = 0
				}
			}
		}	

		when(3) {
			tempVector(0) = (rowQueue1(x) + rowQueue3(x)) >> 1
			tempVector(1) = (rowQueue1(x - 1) + rowQueue1(x + 1) + rowQueue3(x - 1) + rowQueue3(x + 1)) >> 2
			tempVector(2) = rowQueue2(x)
			tempVector(3) = (rowQueue2(x - 1) + rowQueue2(x + 1)) >> 1
			dataOut = tempVector
			if(x < width) {
				x += 1
				state = 4
			} else {
				if(y < height) {
					rowQueue1 = rowQueue2
					rowQueue2 = rowQueue3
					i = 0
					while(i < width + 2) {
						rowQueue3(i) = dataIn
						i += 1
					}

					y += 1
					x = 1
					state = 1
				} else {
					state = 0
				}
			}
		}

		when(4) {
			tempVector(0) = (rowQueue1(x - 1) + rowQueue1(x + 1) + rowQueue3(x - 1) + rowQueue3(x + 1)) >> 2
			tempVector(1) = (rowQueue1(x) + rowQueue3(x)) >> 1
			tempVector(2) = (rowQueue2(x - 1) + rowQueue2(x + 1)) >> 1
			tempVector(3) = rowQueue2(x)
			dataOut = tempVector
			if(x < width) {
				x += 1
				state = 3
			} else {
				if(y < height) {
					rowQueue1 = rowQueue2
					rowQueue2 = rowQueue3
					i = 0
					while(i < width + 2) {
						rowQueue3(i) = dataIn
						i += 1
					}

					y += 1
					x = 1
					state = 1
				} else {
					state = 0
				}
			}
		}

	}
}