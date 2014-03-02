import scalapipe.kernels._
import scalapipe.dsl._

object StdDevTest extends App {

	val pixels = 1600
	val nums = 1000

		val RandomReader = new Kernel("RandomReader") {
			//val y0 = output(FLOAT32)
			val y1 = output(UNSIGNED32)

			val iterations = config(UNSIGNED32, 'iterations, pixels * 10 * nums)
			val i = local(UNSIGNED32, 0)
			val fd = local(stdio.FILEPTR, 0)
			//val fileName = local(STRING, "RandomNumbers.txt")
			val temp = local(SIGNED32)
			
			if(fd == 0) {
				fd = stdio.fopen("../RandomNumbers.txt", "r")
				if(fd == 0) {
					stdio.printf("ERROR: could not open %s\n", "RandomNumbers.txt")
					stdio.exit(-1)
				}
				i = 0
				/*while(i < pixels) {
					y0 = 60
					i += 1
				}*/
			} else {
				stdio.fscanf(fd, "%d", addr(temp))
				//stdio.printf("%d\n", temp)
				y1 = temp
			}
			if(iterations > 0) {
				iterations -= 1
				/*if(iterations % nums == 0) {
					i = 0
					while(i < pixels) {
						y0 = 60
						i += 1
					}
				}*/
			} else {
				stop
			}
		}

		val StdDev = new Kernel("StdDev") {
			//val mean = input(FLOAT32)
			val pixelData = input(UNSIGNED32)
			val stdDev = output(FLOAT32)

			val outputCount = config(UNSIGNED32, 'outputCount, nums)

			val count = local(UNSIGNED32, 0)
			val means = local(Vector(FLOAT32, pixels))
			val stdDevs = local(Vector(FLOAT32, pixels))
			val temp = local(FLOAT32, 0)

			val state = local(UNSIGNED8, 0)

			val i = local(UNSIGNED32, 0)

			switch(state) {

				when(0) {
					count = 1
					i = 0
					while(i < pixels) {
						means(i) = 60
						stdDevs(i) = 0
						i += 1
					}
					state = 1
				}

				when(1) {
					if(count <= outputCount) {
						i = 0
						while(i < pixels) {
							temp = cast(pixelData, FLOAT32)
							stdDevs(i) += ((temp - means(i)) * (temp - means(i)))
							i += 1
						}
						count += 1
					} else {
						i = 0
						while(i < pixels) {
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

		

		val Print = new Kernel("Print") {
			val x0 = input(FLOAT32) 

			val count = local(UNSIGNED32, 0)
			val temp = local(FLOAT32, 0)

			temp = x0
			stdio.printf("Pixel %u:\t%.2f\n", count % pixels, temp)
			if(count % pixels == (pixels - 1)) {
				stdio.printf("\n")
			}
			count += 1
		}

		val app = new Application {
			param('queueDepth, 65536)
			//val iterations = 1600
			//val width = 5
			//val height = 5
			//val newWidth = 7
			//val newHeight = 7
			val random = RandomReader()
			val stdDev = StdDev(random)
			Print(stdDev)
		}

		app.emit("StdDevTest")
}