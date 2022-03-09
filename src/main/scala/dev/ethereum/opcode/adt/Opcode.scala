package dev.ethereum.opcode.adt

sealed trait Opcode

sealed trait ByteOpcode extends Opcode

object ByteOpcode {
  val PUSH1: String = "PUSH1"

  val JUMP: String = "JUMP"
  val JUMPDEST: String = "JUMPDEST"
  val JUMPI: String = "JUMPI"
  val STOP: String = "STOP"
  val REVERT: String = "REVERT"
  val RETURN: String = "RETURN"
  val INVALID: String = "INVALID"
  val SELFDESTRUCT: String = "SELFDESTRUCT"
  val NOSUCC: List[String] = List(STOP, REVERT, RETURN, INVALID, SELFDESTRUCT)
  val ENDCODE: List[String] = List(JUMP, JUMPI) ++ NOSUCC
}


case class StackOpcode[+T <: WrapperRawCode](bopcode: List[T]) {
  def get(n: Int): T = bopcode(n)

  def head: T = get(0)

  lazy val dim: Int = bopcode.length

  override def toString: String = bopcode.mkString("[", ",", "]")
}

trait WrapperRawCode extends ByteOpcode {
  self =>
  val code: RawOpcode

  def same(other: String): Boolean = code.op.equalsIgnoreCase(other)

  def same(other: RawOpcode): Boolean = code == other

  def like[T <: WrapperRawCode]: T = self.asInstanceOf[T]
}

case class Rest(code: RawOpcode) extends WrapperRawCode {
  override def toString: String = code.toString
}


sealed trait RawOpcode extends Opcode {
  val op: String
}

case class SingleOp(op: String) extends RawOpcode {
  override def toString: String = op
}

case class WithValueOp(op: String, value: String) extends RawOpcode {
  override def toString: String = s"$op $value"
}