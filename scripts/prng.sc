import $ivy.`org.typelevel::cats-core:2.8.0`

import cats.data.State

type Carry = State[Boolean, Int] 
type Prng = State[(Int, Int, Int, Int), Int]

def hasCarry(x: Int): Boolean = ((x >> 8) & 0x01) == 0x01

def rol(x0: Int): Carry = State(carry => {
  val c = if (carry) 1 else 0
  val x = (x0 << 1) + c
  (hasCarry(x), x & 0xFF)
})

def adc(x: Int, y: Int): Carry = State(carry => {
  val c = if (carry) 1 else 0
  val z = x + y + c
  (hasCarry(z), z & 0xFF)
})

def prng: Prng = State(state0 => {
  val (d50, d70, d80, d90) = state0

  val p = d70 & 0x03
  val init = p == 0x00 || p == 0x03

  val state =
    (for {
      d9 <- rol(d90)
      d7 <- rol(d70)
      q  <- adc(d7, 0x22)
      r  <- adc(q, d9)
      d8 <- adc(r ^ 0x5A, d80)
      d5 = d8 ^ d50
    } yield {
      (d5, d7, d8, d9)
    }).runA(init).value

  (state, state._1)
})

def runPrng(d5: Int, d7: Int, d8: Int, d9: Int): Int = prng.runA((d5, d7, d8, d9)).value

def hex(x: Int): String = {
  val s = x.toHexString.toUpperCase
  if (s.length == 1) {
    s.padTo(2, '0').reverse
  } else {
    s
  }
}

@main
def main() = {
  val r = runPrng(0x78, 0xA0, 0x66, 0x47)
  println(hex(r))
  assert(0x76 == r)
}
