import scalapipe.kernels._
import scalapipe.dsl._

object RLE_test extends App {

	val Random = new Kernel("Random") {
		//val iter = input(UNSIGNED32)
		val iter = local(UNSIGNED32, 50)	
		val y0 = output (UNSIGNED32)

		val temp = local(UNSIGNED32,0)
		val i = local(UNSIGNED32, 0)

		temp = stdio.rand() %100
		if(temp < 40) {
			y0= 0
		}
		else{
			y0 = temp
		}

		if(i < iter) {
			i+=1
		}
		
		else {
			stop
		}
	}
	
	val Print = new Kernel("Print") {
		val x0 = input(UNSIGNED32)
		stdio.printf(" %u \n", x0)
	}	

	val RLE = new Kernel("RLE") {
	
		val pixelData = input(UNSIGNED32)
		val encoded_data = output(UNSIGNED32)

		// Don't run encode across frames
		//val pixelCount = config(UNSIGNED16, 'pixelCount, 50)
		val pixelCount = local(UNSIGNED16, 50)
		val count = local(UNSIGNED16, 0)
		val runLength = local(UNSIGNED16, 0)
		val prevZero = local(UNSIGNED8, 0) //is there a bool type? 
		val pixel = local(UNSIGNED32,0) // want to set this local variable to pixel input 
		pixel = pixelData

		if(pixel ==0){
			if(prevZero == 0){
				prevZero = 1
				runLength+=1
				encoded_data =0

			}
			else{
				runLength+=1
			}
		}
		else {
			if(prevZero == 0){
				encoded_data = pixel	
			}
			else{
				encoded_data = runLength
				prevZero = 0
				runLength = 0
				encoded_data = pixel	
			}	
		}
		count+=1;
		if(count == pixelCount){
			count =0
			if(prevZero ==1){
				encoded_data = runLength	
				runLength = 0	
			}	
			prevZero = 0	
		}
	
	}
	
	val app = new Application {
		val random = Random()
		val encoded = RLE(random)
		Print(encoded)
	}

	app.emit("RLE_test")
}

