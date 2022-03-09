package dev.ethereum.opcodes.adt.raws

import dev.ethereum.opcodes.OpcodeId
import dev.ethereum.opcodes.adt.Opcode


object WrapperRawCode {
  val PUSH1: String = OpcodeId.JUMP.name + "1"
  val JUMP: String = OpcodeId.JUMP.name
  val JUMPDEST: String = OpcodeId.JUMPDEST.name
  val JUMPI: String = OpcodeId.JUMPI.name
  val STOP: String = OpcodeId.STOP.name
  val REVERT: String = OpcodeId.REVERT.name
  val RETURN: String = OpcodeId.RETURN.name
  val INVALID: String = OpcodeId.INVALID.name
  val SELFDESTRUCT: String = OpcodeId.SELFDESTRUCT.name
  val NOSUCC: List[String] = List(STOP, REVERT, RETURN, INVALID, SELFDESTRUCT)
  val ENDCODE: List[String] = List(JUMP, JUMPI) ++ NOSUCC

}

trait WrapperRawCode extends Opcode {
  self =>
  val code: RawOpcode

  override def same(other: String): Boolean = code.op.equalsIgnoreCase(other)

  def same(other: RawOpcode): Boolean = code == other

  def like[T <: Opcode]: T = self.asInstanceOf[T]

  override def toString: String = code.toString

  override protected val opcodeId: OpcodeId = OpcodeId.RAW

  /**
   * Get the number of elements that the opcode consumes. They have to be already in the stack.
   *
   * @return consumed stack elements.
   */
  override def stackConsumed: BigInt = 0

}

/** Todo. all bytecode */
case class Rest(code: RawOpcode) extends WrapperRawCode