package polarization.kernels
import scalapipe._
import scalapipe.dsl._

class PolarizationBorderExt(_name:String,width:Int,height:Int) extends Kernel(_name:String)
{
	val typ = UNSIGNED32

	val dataIn = input(typ)
	val dataOut = output(typ)

	//val width = config(UNSIGNED32, 'width, 10)
	//val height = config(UNSIGNED32, 'height, 10)

	val x = local(UNSIGNED32, 0)
	val y = local(UNSIGNED32, 0)
	val i = local(UNSIGNED32, 0)

	val temp1 = local(UNSIGNED32, 0)
	val temp2 = local(UNSIGNED32, 0)

	val rowQueue1 = local(Vector(typ, width + 2))
	val rowQueue2 = local(Vector(typ, width + 2))

	val state = local(UNSIGNED8, 0)

	if(y <> 0 && y <> 1 && y <> (height - 1) && y <> (height - 2)) {
		if(x == 0) {
			temp1 = dataIn
			x += 1
		} else if(x == 1) {
			temp2 = dataIn
			dataOut = temp2
			dataOut = temp1
			dataOut = temp2
			x += 1
		} else if(x == width - 2) {
			temp1 = dataIn
			x += 1
		} else if(x == width - 1) {
			temp2 = dataIn
			dataOut = temp1
			dataOut = temp2
			dataOut = temp1
			x = 0
			y += 1		
		} else {
			temp1 = dataIn
			dataOut = temp1
			x += 1
		}
	} else {
		if(y == 0) {
			rowQueue1(x) = dataIn
			if(x == width - 1) {
				x = 0
				y += 1
			} else {
				x += 1
			}
		} else if(y == 1) {
			rowQueue2(x) = dataIn
			if(x == width - 1) {
				i = 0
				temp1 = rowQueue2(1)
				temp2 = rowQueue2(width - 2)
				while(i <= width - 1) {
					if(i == 0) {
						dataOut = temp1
					}
					dataOut = rowQueue2(i)
					if(i == (width - 1)) {
						dataOut = temp2
					}
					i += 1
				}
				i = 0
				temp1 = rowQueue1(1)
				temp2 = rowQueue1(width - 2)
				while(i <= width - 1) {
					if(i == 0){
						dataOut = temp1
					}
					dataOut = rowQueue1(i)
					if(i == (width - 1)) {
						dataOut = temp2
					}
					i += 1
				}
				i = 0
				temp1 = rowQueue2(1)
				temp2 = rowQueue2(width - 2)
				while(i <= width - 1) {
					if(i == 0) {
						dataOut = temp1
					}
					dataOut = rowQueue2(i)
					if(i == (width - 1)) {
						dataOut = temp2
					}
					i += 1
				}
				x = 0
				y += 1
			} else {
				x += 1
			}
		} else if(y == (height - 2)){
			rowQueue1(x) = dataIn
			if(x == width - 1) {
				x = 0
				y += 1
			} else {
				x += 1
			}
		} else if(y == (height - 1)) {
			rowQueue2(x) = dataIn
			if(x == width - 1) {
				i = 0
				temp1 = rowQueue1(1)
				temp2 = rowQueue1(width - 2)
				while(i <= width - 1) {
					if(i == 0) {
						dataOut = temp1
					}
					dataOut = rowQueue1(i)
					if(i == width - 1) {
						dataOut = temp2
					}
					i += 1
				}
				i = 0
				temp1 = rowQueue2(1)
				temp2 = rowQueue2(width - 2)
				while(i <= width - 1) {
					if(i == 0) {
						dataOut = temp1
					}
					dataOut = rowQueue2(i)
					if(i == width - 1) {
						dataOut = temp2
					}
					i += 1
				}
				i = 0
				temp1 = rowQueue1(1)
				temp2 = rowQueue1(width - 2)
				while(i <= width - 1) {
					if(i == 0) {
						dataOut = temp1
					}
					dataOut = rowQueue1(i)
					if(i == width - 1) {
						dataOut = temp2
					}
					i += 1
				}
				x = 0
				y = 0
			} else {
				x += 1
			}
		}
		
	}

	

	/*if(y == 0) {
		if(x < width) {
			rowQueue1(x) = dataIn
			x += 1
		} else {
			x = 0
			y += 1
		}
	} else if((y == 1) || (y == height - 2)) {
		if(x < width) {
			rowQueue2(x) = dataIn
			x += 1
		} else {
			if(y == 1) {
				i = 0
				dataOut = rowQueue2(1)
				while(i < width) {
					dataOut = rowQueue2(i)
					i += 1
				}
				dataOut = rowQueue2(width - 2)
			}
			y += 1
		}
	} else if(y == height) {
		i = 0
		dataOut = rowQueue2(1)
		while(i < width) {
			dataOut = rowQueue2(i)
			i += 1
		}
		dataOut = rowQueue2(width - 2)
		y = 0
		x = 0
	} else if((x == 0) || (x == width - 2)) {
		temp1 = dataIn
		x += 1
	} else if(x == 1) {
		temp2 = dataIn
		dataOut = temp2
		dataOut = temp1
		dataOut = temp2
		x += 1
	} else if(x == width - 1) {
		temp2 = dataIn
		dataOut = temp1
		dataOut = temp2
		dataOut = temp1
		x = 0
		y += 1
	} else {
		temp1 = dataIn
		dataOut = temp1
		x += 1
	}*/
} 