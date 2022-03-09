package dev.ethereum.opcodes.adt

sealed trait BlockOpcode[+T <: Opcode] {
  val id: Int
  val stack: StackOpcode[T]

  override def toString: String = s"{${id}}: ${stack.opcodes.mkString("[", ",", "]")}"
}

case class BasicBlockOpcode[+T <: Opcode](id: Int, stack: StackOpcode[T]) extends BlockOpcode[T]

case class StackOpcode[+T <: Opcode](opcodes: List[T]) {
  def get(n: Int): T = opcodes(n)

  def head: T = get(0)

  lazy val dim: Int = opcodes.length

  override def toString: String = opcodes.mkString("[", ",", "]")
}