import scalapipe.kernels._
import scalapipe.dsl._

// UUT
import veritas.kernels.PrimaryFilter

// extends App == scala boilerplate that may actually not be
//                helpful, anymore. But... tradition! *shrug*
object PrimaryFilterTest extends App
{
  val imgSize = 1600
  val UUT = new PrimaryFilter("PrimaryFilter")
  val Read = new Kernel("_input")
  {
    val iterations = config(UNSIGNED32, 'iterations, imgSize)
    val out = output(UNSIGNED16)
    val fd = local(stdio.FILEPTR, 0)
    val tmp = local(UNSIGNED16)
    if (fd == 0)
    {
      // TODO -- extract input pixels
      val fname = "primary_filter_test_pixels.txt"
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
      stop
  }
  val Print = new Kernel("_output")
  {
    val in = input(UNSIGNED16)
    stdio.printf("%d", in)
  }
  val app = new Application
  {
    val iterations = 40
    val lowThresh = 50
    val highThresh = 150
    val in = Read('iterations -> iterations)
    val out = UUT('width -> 40, 'height -> 40, 'outputCount -> 30,
                  'pixelIn -> in(0), 
                  'lowThresh -> lowThresh, 'highThresh -> highThresh)
    Print(out('pixelOut))
  }
  app.emit("PrimaryFilter")
}
