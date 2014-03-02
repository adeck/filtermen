import scalapipe.kernels._
import scalapipe.dsl._

// UUT
import veritas.kernels.PrimaryFilter

// extends App == scala boilerplate that may actually not be
//                helpful, anymore. But... tradition! *shrug*
object PrimaryFilterTest extends App
{
  val uut = new PrimaryFilter("PrimaryFilter")
  val thresh = new Kernel("_threshold")
  {
    // TODO -- extract threshold values
  }
  val input = new Kernel("_input")
  {
    // TODO -- extract input pixels
  }
  val output = new Kernel("_output")
  {
    // TODO -- write output pixels
  }
  val app = new Application
  {
    // Hook up the kernels.
  }
  app.emit("PrimaryFilter")
}
