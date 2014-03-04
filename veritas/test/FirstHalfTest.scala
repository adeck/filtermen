import scalapipe.kernels._
import scalapipe.dsl._

object FirstHalfTest extends App {

	val pixels = 1600
	val nums = 1000

		val RandomReader = new Kernel("RandomReader") {
			val y0 = output(UNSIGNED32)
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
				y0 = temp
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

		val Mean = new Kernel("Mean") {
			val pixelData = input(UNSIGNED32)
			val mean0 = output(FLOAT32)
			val mean1 = output(FLOAT32)
			val mean2 = output(FLOAT32)

			val outputCount = config(UNSIGNED32, 'outputCount, 1000)

			val count = local(UNSIGNED32, 0)
			val state = local(UNSIGNED32, 0)
			val means = local(Vector(FLOAT32, pixels))
			val temp = local(FLOAT32, 0)
			val tempPixel = local(UNSIGNED32, 0)

			val i = local(UNSIGNED32, 0)

			switch(state) {

				when(0) {
					count = 0
					i = 0
					while(i < pixels) {
						means(i) = 0
						i += 1
					}
					state = 1
				}

				when(1) {
					if(count <= outputCount) {
						i = 0
						while(i < pixels) {
							tempPixel = cast(pixelData, FLOAT32)
							if(i == 0) {
								//stdio.printf("Pixel 0:\t%.3f\tIncoming Pixel:\t%.3f\n", means(i), tempPixel)
							}
							temp = means(i) * count
							temp += tempPixel
							means(i) = temp / (count + 1)
							i += 1
						}
						count += 1
					} else {
						i = 0
						while(i < pixels) {
							mean0 = means(i)
							mean1 = means(i)
							mean2 = means(i)
							i += 1
						}
						state = 0
					}
				}
			}
		}

		val StdDev = new Kernel("StdDev") {
			val mean = input(FLOAT32)
			val pixelData = input(UNSIGNED32)
			val stdDev0 = output(FLOAT32)
			val stdDev1 = output(FLOAT32)

			val outputCount = config(UNSIGNED32, 'outputCount, nums)

			val count = local(UNSIGNED32, 0)
			val means = local(Vector(FLOAT32, pixels))
			val stdDevs0 = local(Vector(FLOAT32, pixels))
			val stdDevs1 = local(Vector(FLOAT32, pixels))
			val temp = local(FLOAT32, 0)

			val state = local(UNSIGNED8, 0)

			val i = local(UNSIGNED32, 0)

			switch(state) {

				when(0) {
					count = 1
					i = 0
					while(i < pixels) {
						stdDevs0(i) = 0
						stdDevs1(i) = 0
						i += 1
					}
					state = 1
				}

				when(1) {
					if(count <= outputCount) {
						i = 0
						while(i < pixels) {
							temp = cast(pixelData, FLOAT32)
							stdDevs0(i) += temp * temp
							stdDevs1(i) += temp
							i += 1
						}
						count += 1
					} else {
						i = 0
						while(i < pixels) {
							means(i) = mean
							i += 1
						}

						i = 0
						while(i < pixels) {
							stdDevs0(i) -= (2 * means(i) * stdDevs1(i))
							stdDevs0(i) += outputCount * means(i) * means(i)
							stdDevs0(i) = stdDevs0(i) / (outputCount - 1)
							stdDevs0(i) = sqrt(stdDevs0(i))
							stdDev0 = stdDevs0(i)
							stdDev1 = stdDevs0(i)
							i += 1
						}
						state = 0
					}
				}

			}
		}

		val Threshold = new Kernel("Threshold") {
			val mean = input(FLOAT32)
			val stdDev = input(FLOAT32)

			val loThreshold = output(UNSIGNED32)
			val hiThreshold = output(UNSIGNED32)

			val tempMean = local(FLOAT32)
			val tempStdDev = local(FLOAT32)

			tempMean = mean
			tempStdDev = stdDev

			loThreshold = cast((tempMean + (1.5 * tempStdDev)), UNSIGNED32)
			hiThreshold = cast((tempMean + (2 * tempStdDev)), UNSIGNED32)
		}

		val Print = new Kernel("Print") {
			val mean = input(FLOAT32)
			val stdDev = input(FLOAT32) 
			val loThreshold = input(UNSIGNED32)
			val hiThreshold = input(UNSIGNED32)

			val count = local(UNSIGNED32, 0)
			val temp0 = local(FLOAT32, 0)
			val temp1 = local(FLOAT32, 0)
			val temp2 = local(UNSIGNED32, 0)
			val temp3 = local(UNSIGNED32, 0)

			temp0 = mean
			temp1 = stdDev
			temp2 = loThreshold
			temp3 = hiThreshold
			stdio.printf("Pixel %u:\tmean:\t%.2f\tstdDev:\t%.2f\tloThreshold:\t%u\thiThreshold:\t%u\n", count % pixels, temp0,temp1,temp2,temp3)
			if(count % pixels == (pixels - 1)) {
				stdio.printf("\n")
			}
			count += 1
		}

		val app = new Application {
			param('queueDepth, 100000)
			//val iterations = 1600
			//val width = 5
			//val height = 5
			//val newWidth = 7
			//val newHeight = 7
			val random = RandomReader()
			val mean = Mean(random(0))
			val stdDev = StdDev(mean(0), random(1))
			val threshold = Threshold(mean(1), stdDev(0))
			Print(mean(2), stdDev(1), threshold(0), threshold(1))
		}

		app.emit("FirstHalfTest")
}