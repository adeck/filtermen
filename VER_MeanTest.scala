import scalapipe.kernels._
import scalapipe.dsl._

object MeanTest extends App {

	val pixels = 10

		val Random = new Kernel("Random") {
			val y0 = output(UNSIGNED32)

			val iterations = config(UNSIGNED32, 'iterations, pixels * 20000)
			y0 = stdio.rand() % 100

			if(iterations > 0) {
				iterations -= 1
			}

			if(iterations == 0) {
				stop
			}
		}

		val Mean = new Kernel("Mean") {
			val pixelData = input(UNSIGNED32)
			val mean = output(FLOAT32)

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
							mean = means(i)
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

			val iterations = 1600
			val width = 5
			val height = 5
			val newWidth = 7
			val newHeight = 7
			val random = Random()
			val mean = Mean(random)
			Print(mean)
		}

		app.emit("MeanTest")
}