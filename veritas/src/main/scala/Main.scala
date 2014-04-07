package veritas
import kernels.{  
                  PrimaryFilter, RunLengthEncode, Threshold,
                  Mean, StdDev, BorderExt
               }

import scalapipe.kernels._
import scalapipe.dsl._

// extends App == scala boilerplate that may actually not be
//                helpful, anymore. But... tradition! *shrug*
object Main extends App
{
  // External units.
  val BorderExtInput = new BorderExt("BorderExtInput")
  val PrimaryUnit = new PrimaryFilter("PrimaryFilter")
  val RLEUnit = new RunLengthEncode("RunLengthEncode")
  val ThresholdUnit = new Threshold("Threshold")
  val BorderExtLoThreshold = new BorderExt("BorderExtLoThreshold")
  val BorderExtHiThreshold = new BorderExt("BorderExtHiThreshold")
  val MeanUnit = new Mean("Mean")
  val StdDevUnit = new StdDev("StdDev")
  // Constants
  val imgSize = 42 * 42
  // Internal units (subject to change)
  val Read = new Kernel("_input")
  {
    val fname = "../main_input.txt"
    val iterations = config(UNSIGNED32, 'iterations, imgSize)
    val out = output(UNSIGNED16)
    val fd = local(stdio.FILEPTR, 0)
    val tmp = local(UNSIGNED16)
    if (fd == 0)
    {
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
    val tmp = local(UNSIGNED16)
    val fd = local(stdio.FILEPTR, 0)
    if (fd == 0)
    {
      val fname = "../main_output.txt"
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
    val outputCount = 1000
    val in = Read('iterations -> iterations)
    val be_in = BorderExtInput(in)
    val mean = MeanUnit(in)
    val std_dev = StdDevUnit(mean, in)
    val threshold = ThresholdUnit(mean, std_dev)
    // be --> border extended
    val be_lo_threshold = BorderExtLoThreshold(threshold(0))
    val be_hi_threshold = BorderExtHiThreshold(threshold(1))
    val primary = PrimaryUnit(be_in, be_lo_threshold, be_hi_threshold,
                      'outputCount -> 1000, 'width -> 40, 'height -> 40)
    val runlength_encoded = RLEUnit(primary)
    Print(runlength_encoded(0))
  }
  app.emit("Veritas")
}
