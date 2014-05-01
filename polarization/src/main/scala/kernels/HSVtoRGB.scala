package polarization.kernels
import scalapipe._
import scalapipe.dsl._

//input from: Combine
//output to: HSVtoRGB
class HSVtoRGB(_name:String) extends Kernel(_name:String)
{
  val typ = FLOAT32
	val hsv_in = input(Vector(typ, 3))
	val rgb_out = output(Vector(typ, 3))
	val hsv = local(Vector(typ, 3))
	val rgb = local(Vector(typ, 3))
  // Temporary input vars.
	val H = local(typ)
  val S = local(typ)
  val V = local(typ)
  // Temporary output vars
	val R = local(typ)
  val G = local(typ)
  val B = local(typ)
  // Normal locals!
  val i = local(UNSIGNED16)
  val f = local(typ)
  val p = local(typ)
  val q = local(typ)
  val t = local(typ)

  // Init
  hsv = hsv_in
  H = hsv(0)
  S = hsv(1)
  V = hsv(2)
  H /= 60
  i = cast(H, UNSIGNED16)
  f = H - i
  p = V * (1 - S)
  q = V * (1 - S * f)
  t = V * (1 - S * (1 - f))
  if ( S == 0 )
  {
    R = 0
    G = 0
    B = 0
  }
  else
  {
    if (i == 0)
    {
      R = V
      G = t
      B = p
    }
    else if (i == 1)
    {
      R = q
      G = V
      B = p
    }
    else if (i == 2)
    {
      R = p
      G = V
      B = t
    }
    else if (i == 3)
    {
      R = p
      G = q
      B = V
    }
    else if (i == 4)
    {
      R = t
      G = p
      B = V
    }
    else
    {
      R = V
      G = p
      B = q
    }
  }
  // assign output
  rgb(0) = R
  rgb(1) = G
  rgb(2) = B
  rgb_out = rgb
}
