package dev.ethereum.opcode.adt

sealed trait BlockOpcode[+T <: WrapperRawCode] {
  val id: Int
  val stack: StackOpcode[T]

  override def toString: String = s"{${id}}: ${stack.bopcode.mkString("[", ",", "]")}"
}

case class BasicBlockOpcode[+T <: WrapperRawCode](id: Int, stack: StackOpcode[T]) extends BlockOpcode[T]