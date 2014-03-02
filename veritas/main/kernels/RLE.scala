import scalapipe.dsl._

// Relies on the following config parameters:
//    pixelCount    --    The number of pixels per frame
//    outputCount   --    The number of frames across which the standard
//                        thresholds should be taken.

object RunLengthEncode extends Kernel {
	//assumption: pixel data is unsigned16
	val pixelData = input(UNSIGNED16)
	val encoded_data = output(UNSIGNED16)
	
	// Don't run encode across frames
	val pixelCount = config(UNSIGNED16, 'pixelCount, 1600)

	val count = local(UNSIGNED16, 0)
	val runlength = local(UNSIGNED16, 0)
	val prevZero = local(BOOL, 0)
	val pixel = local(UNSIGNED32, 0) // want to set this local variable 
                                  // to pixel input 
	pixel = pixelData

	if (pixel == 0) {
		if (prevZero == 0) {
			prevZero = 1
			runLength += 1
			encoded_data = 0
		}
		else {
			runLength += 1
		}
	}
	else {
		if (prevZero == 0) {
			encoded_data = pixel	
		}
		else {
			encoded_data = runlength
			prevZero = 0
			runlength = 0
			encoded_data = pixel	
		}	
	}
	count += 1;
	if (count == pixelNum) {
		count = 0
		if (prevZero == 1) {
			encoded_data = runlength	
			runlength = 0	
		}	
		prevZero = 0	
	}
}
