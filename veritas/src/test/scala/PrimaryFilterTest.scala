import veritas.kernels.PrimaryFilter

import scalapipe.kernels._
import scalapipe.dsl._

object PrimaryTest extends App {

	val Random = new Kernel("Random") {
		val iter = local(UNSIGNED16, 42 * 42)	
		val y0 = output (UNSIGNED16)
		val y1 = output (UNSIGNED16)
		val y2 = output (UNSIGNED16)

    val fd = local(stdio.FILEPTR, 0)
		val temp = local(UNSIGNED16,0)
		val i = local(UNSIGNED16, 0)
    if (fd == 0)
    {
      fd = stdio.fopen("../primary_filter_test_pixels.txt", "r")
      if (fd == 0) 
      {
        stdio.printf("ERROR: could not open %s\n", "primary_filter_test_pixels.txt")
        stdio.exit(-1)
      }
      i = 0
    } 
    else 
    {
      stdio.fscanf(fd, "%hu", addr(temp))
      y0 = temp
      y1 = 50
      y2 = 150
    }

		if (i < iter) {
			i+=1
		}
		
		else {
			stop
		}
	}
	
	val Print = new Kernel("Print") {
		val x0 = input(UNSIGNED16)
    val tmp = local(UNSIGNED16)
    //*
    val fd = local(stdio.FILEPTR, 0)
    if (fd == 0)
    {
      fd = stdio.fopen("../primary_filter_test_output.txt", "w")
      if (fd == 0) 
      {
        stdio.printf("ERROR: could not open %s\n", "primary_filter_test_output.txt")
        stdio.exit(-1)
      }
    } 
    //*/
    tmp = x0
    stdio.fprintf(fd, "%hu\n", tmp)
    stdio.printf("%hu\n", tmp)
	}	

	val UUT = new PrimaryFilter("Primary")
	
	val app = new Application {
		val random = Random()
		val encoded = UUT(random(0), random(1), random(2),
                      'outputCount -> 30, 'width -> 40, 'height -> 40)
		Print(encoded)
	}

	app.emit("PrimaryTest")
}

