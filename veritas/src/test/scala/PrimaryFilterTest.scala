import veritas.kernels.PrimaryFilter

import scalapipe.kernels._
import scalapipe.dsl._


// extends App == scala boilerplate that may actually not be
//                helpful, anymore. But... tradition! *shrug*
object PrimaryFilterTest extends App
{
  val imgSize = 40 * 40
  val UUT = new PrimaryFilter("PrimaryFilter")
  val Read = new Kernel("_input")
  {
    val iterations = config(UNSIGNED32, 'iterations, imgSize)
    val out = output(UNSIGNED16)
    val lowThresh = output(UNSIGNED16)
    lowThresh = 50
    val highThresh = output(UNSIGNED16)
    highThresh = 150
    val fd = local(stdio.FILEPTR, 0)
    val tmp = local(UNSIGNED16)
    if (fd == 0)
    {
      val fname = "../primary_filter_test_pixels.txt"
      fd = stdio.fopen(fname, "r")
      if (fd == 0)
      {
        stdio.printf("ERROR: Unable to open \"%s\".\n", fname)
        stdio.exit(-1)
      }
    }
    else
    {
      stdio.fscanf(fd, "%d", addr(tmp))
      out = tmp
    }
    if (iterations > 0)
      iterations -= 1
    else
    {
      stdio.printf("Completed run")
      stop
    }
  }
  val Print = new Kernel("_output")
  {
    val in = input(UNSIGNED16)
    val tmp = local(UNSIGNED16)
    val fd = local(stdio.FILEPTR, 0)
    if (fd == 0)
    {
      val fname = "../primary_filter_test_output.txt"
      fd = stdio.fopen(fname, "w")
      if (fd == 0)
      {
        stdio.printf("ERROR: Unable to open \"%s\".\n", fname)
        stdio.exit(-1)
      }
    }
    else
    {
      tmp = in
      stdio.fprintf(fd, "%d\n", addr(tmp))
    }
  }
  val app = new Application
  {
    val iterations = 40
    val in = Read('iterations -> iterations)
    val out = UUT(in(0), in(1), in(2), 
                  'outputCount -> 30, 'width -> 40, 'height -> 40)
    Print(out(0))
  }
  app.emit("PrimaryFilterTest")
}
