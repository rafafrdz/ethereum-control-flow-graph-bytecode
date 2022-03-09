package dev.ethereum.opcodes.adt.raws

sealed trait RawOpcode {
  val op: String

  override def toString: String = op
}

case class SimpleOpcode(op: String) extends RawOpcode

case class WithParameterOpcode(op: String, parameter: String) extends RawOpcode {
  override def toString: String = s"$op $parameter"
}
