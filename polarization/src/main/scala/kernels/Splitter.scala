package polarization.kernels
import scalapipe._
import scalapipe.dsl._

class Splitter(_name:String,width:Int,height:Int,n:Int) extends Kernel(_name:String)
{
	val typ = UNSIGNED32

	val dataIn = input(typ)
	val stream0 = output(typ)
	val stream1 = output(typ)
	val stream2 = output(typ)
	val stream3 = output(typ)
	val stream4 = output(typ)
	val stream5 = output(typ)
	val stream6 = output(typ)
	val stream7 = output(typ)
	val stream8 = output(typ)
	val stream9 = output(typ)
	val stream10 = output(typ)
	val stream11 = output(typ)
	val stream12 = output(typ)
	val stream13 = output(typ)
	val stream14 = output(typ)
	val stream15 = output(typ)
	val stream16 = output(typ)
	val stream17 = output(typ)
	val stream18 = output(typ)
	val stream19 = output(typ)
	val stream20 = output(typ)

	//val width = config(UNSIGNED32, 'width, 10)
	//val height = config(UNSIGNED32, 'height, 10)

	val x = local(UNSIGNED32, 0)
	val i = local(UNSIGNED32, 0)

	val split = local(UNSIGNED32, 0)
	val splitCount = local(UNSIGNED32, 0)
	val streamCount = local(UNSIGNED32, 0)
	val nextStream = local(UNSIGNED32, 0)

	val temp1 = local(UNSIGNED32, 0)
	val state = local(UNSIGNED32, -1)

	split = height / n
	splitCount = (width + 2) * (split - 2)

	switch(state) {
		when(0) {
				if(i < (splitCount + width + 2)) {
					temp1 = dataIn
					stream0 = temp1
					i += 1
				} else {
					if(x == 0) {
						nextStream = (streamCount + 1) % n
					} else if(x < (width + 2)) {
						temp1 = dataIn
						stream0 = temp1
						stream1 = temp1
						x += 1
					} else {
						x = 0
						i = 0
						state = 1
					}

				}

		}
		when(1) {
			if(i < splitCount) {
					temp1 = dataIn
					stream1= temp1
					i += 1
				} else {
					if(x == 0) {
						nextStream = (streamCount + 1) % n
					} else if(x < (width + 2)) {
						temp1 = dataIn
						stream1 = temp1
						stream2 = temp1
						x += 1
					} else {
						x = 0
						i = 0
						state = 2
					}

				}

		}
		when(2) {
				if(i < splitCount) {
					temp1 = dataIn
					stream2 = temp1
					i += 1
				} else {
					if(x == 0) {
						nextStream = (streamCount + 1) % n
					} else if(x < (width + 2)) {
						temp1 = dataIn
						stream2 = temp1
						stream3 = temp1
						x += 1
					} else {
						x = 0
						i = 0
						state = 3
					}

				}

		}
		when(3) {
			if(i < splitCount) {
					temp1 = dataIn
					stream3 = temp1
					i += 1
				} else {
					if(x == 0) {
						nextStream = (streamCount + 1) % n
					} else if(x < (width + 2)) {
						temp1 = dataIn
						stream3 = temp1
						stream4 = temp1
						x += 1
					} else {
						x = 0
						i = 0
						state = 4
					}

				}

		}
		when(4) {
				if(i < splitCount) {
					temp1 = dataIn
					stream4 = temp1
					i += 1
				} else {
					if(x == 0) {
						nextStream = (streamCount + 1) % n
					} else if(x < (width + 2)) {
						temp1 = dataIn
						stream4 = temp1
						stream5 = temp1
						x += 1
					} else {
						x = 0
						i = 0
						state = 5
					}

				}

		}
		when(5) {
			if(i < splitCount) {
					temp1 = dataIn
					stream5 = temp1
					i += 1
				} else {
					if(x == 0) {
						nextStream = (streamCount + 1) % n
					} else if(x < (width + 2)) {
						temp1 = dataIn
						stream5 = temp1
						stream6 = temp1
						x += 1
					} else {
						x = 0
						i = 0
						state = 6
					}

				}

		}
		when(6) {
				if(i < splitCount) {
					temp1 = dataIn
					stream6 = temp1
					i += 1
				} else {
					if(x == 0) {
						nextStream = (streamCount + 1) % n
					} else if(x < (width + 2)) {
						temp1 = dataIn
						stream6 = temp1
						stream7 = temp1
						x += 1
					} else {
						x = 0
						i = 0
						state = 7
					}

				}

		}
		when(7) {
			if(i < splitCount) {
					temp1 = dataIn
					stream7 = temp1
					i += 1
				} else {
					if(x == 0) {
						nextStream = (streamCount + 1) % n
					} else if(x < (width + 2)) {
						temp1 = dataIn
						stream7 = temp1
						stream8 = temp1
						x += 1
					} else {
						x = 0
						i = 0
						state = 8
					}

				}

		}
		when(8) {
				if(i < splitCount) {
					temp1 = dataIn
					stream8 = temp1
					i += 1
				} else {
					if(x == 0) {
						nextStream = (streamCount + 1) % n
					} else if(x < (width + 2)) {
						temp1 = dataIn
						stream8 = temp1
						stream9 = temp1
						x += 1
					} else {
						x = 0
						i = 0
						state = 9
					}

				}

		}
		when(9) {
			if(i < splitCount) {
					temp1 = dataIn
					stream9 = temp1
					i += 1
				} else {
					if(x == 0) {
						nextStream = (streamCount + 1) % n
					} else if(x < (width + 2)) {
						temp1 = dataIn
						stream9 = temp1
						stream10 = temp1
						x += 1
					} else {
						x = 0
						i = 0
						state = 10
					}

				}

		}
		when(10) {
				if(i < splitCount) {
					temp1 = dataIn
					stream10 = temp1
					i += 1
				} else {
					if(x == 0) {
						nextStream = (streamCount + 1) % n
					} else if(x < (width + 2)) {
						temp1 = dataIn
						stream10 = temp1
						stream11 = temp1
						x += 1
					} else {
						x = 0
						i = 0
						state = 11
					}

				}

		}
		when(11) {
			if(i < splitCount) {
					temp1 = dataIn
					stream11 = temp1
					i += 1
				} else {
					if(x == 0) {
						nextStream = (streamCount + 1) % n
					} else if(x < (width + 2)) {
						temp1 = dataIn
						stream11 = temp1
						stream12 = temp1
						x += 1
					} else {
						x = 0
						i = 0
						state = 12
					}

				}

		}
		when(12) {
				if(i < splitCount) {
					temp1 = dataIn
					stream12 = temp1
					i += 1
				} else {
					if(x == 0) {
						nextStream = (streamCount + 1) % n
					} else if(x < (width + 2)) {
						temp1 = dataIn
						stream12 = temp1
						stream13 = temp1
						x += 1
					} else {
						x = 0
						i = 0
						state = 13
					}

				}

		}
		when(13) {
			if(i < splitCount) {
					temp1 = dataIn
					stream13 = temp1
					i += 1
				} else {
					if(x == 0) {
						nextStream = (streamCount + 1) % n
					} else if(x < (width + 2)) {
						temp1 = dataIn
						stream13 = temp1
						stream14 = temp1
						x += 1
					} else {
						x = 0
						i = 0
						state = 14
					}

				}

		}
		when(14) {
				if(i < splitCount) {
					temp1 = dataIn
					stream14 = temp1
					i += 1
				} else {
					if(x == 0) {
						nextStream = (streamCount + 1) % n
					} else if(x < (width + 2)) {
						temp1 = dataIn
						stream14 = temp1
						stream15 = temp1
						x += 1
					} else {
						x = 0
						i = 0
						state = 15
					}

				}

		}
		when(15) {
			if(i < splitCount) {
					temp1 = dataIn
					stream15 = temp1
					i += 1
				} else {
					if(x == 0) {
						nextStream = (streamCount + 1) % n
					} else if(x < (width + 2)) {
						temp1 = dataIn
						stream15 = temp1
						stream16 = temp1
						x += 1
					} else {
						x = 0
						i = 0
						state = 16
					}

				}

		}
		when(16) {
				if(i < splitCount) {
					temp1 = dataIn
					stream16 = temp1
					i += 1
				} else {
					if(x == 0) {
						nextStream = (streamCount + 1) % n
					} else if(x < (width + 2)) {
						temp1 = dataIn
						stream16 = temp1
						stream17 = temp1
						x += 1
					} else {
						x = 0
						i = 0
						state = 17
					}

				}

		}
		when(17) {
			if(i < splitCount) {
					temp1 = dataIn
					stream17 = temp1
					i += 1
				} else {
					if(x == 0) {
						nextStream = (streamCount + 1) % n
					} else if(x < (width + 2)) {
						temp1 = dataIn
						stream17 = temp1
						stream18 = temp1
						x += 1
					} else {
						x = 0
						i = 0
						state = 18
					}

				}

		}
		when(18) {
				if(i < splitCount) {
					temp1 = dataIn
					stream18 = temp1
					i += 1
				} else {
					if(x == 0) {
						nextStream = (streamCount + 1) % n
					} else if(x < (width + 2)) {
						temp1 = dataIn
						stream18 = temp1
						stream19 = temp1
						x += 1
					} else {
						x = 0
						i = 0
						state = 19
					}

				}

		}
		when(19) {
			if(i < splitCount) {
					temp1 = dataIn
					stream19 = temp1
					i += 1
				} else {
					if(x == 0) {
						nextStream = (streamCount + 1) % n
					} else if(x < (width + 2)) {
						temp1 = dataIn
						stream19 = temp1
						stream20 = temp1
						x += 1
					} else {
						x = 0
						i = 0
						state = 20
					}

				}

		}
		when(20) {
			if(i < splitCount) {
					temp1 = dataIn
					stream20 = temp1
					i += 1
				} else {
					if(x == 0) {
						nextStream = (streamCount + 1) % n
					} else if(x < (width + 2)) {
						temp1 = dataIn
						//stream19 = temp1
						stream20 = temp1
						x += 1
					} else {
						x = 0
						i = 0
						state = 0
					}

				}

		}
	}



	
} 