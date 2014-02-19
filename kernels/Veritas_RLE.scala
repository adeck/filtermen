import scalapipe.dsl._

object RunLengthEncode extends Kernel{
	
	val pixelData = input(UNSIGNED32)
	val encoded_data = output(UNSIGNED32)
	
	// Don't run encode across frames
	val pixelCount = config(UNSIGNED16, 'pixelCount, 1600)

	val count = local(UNSIGNED16, 0)
	val runlength = local(UNSIGNED16, 0)
	val prevZero = local(UNSIGNED8, 0) //is there a bool type? 
	val pixel = local(UNSIGNED32,0) // want to set this local variable to pixel input 
	pixel = pixelData

	if(pixel ==0){
		if(prevZero = 0){
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
			encoded_data = runlength
			prevZero = 0
			runlength = 0
			encoded_data = pixel	
		}	
	}
	count+=1;
	if(count == pixelNum){
		count =0
		if(prevZero ==1){
			encoded_data = runlength	
			runlength = 0	
		}	
		prevZero = 0	
	}
}
