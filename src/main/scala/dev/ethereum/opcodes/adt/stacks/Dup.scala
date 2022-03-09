package dev.ethereum.opcodes.adt.stacks

import dev.ethereum.opcodes.OpcodeId
import dev.ethereum.opcodes.OpcodeId.hex
import dev.ethereum.opcodes.adt.Opcode

case class Dup(value: BigInt) extends Opcode {
  override protected val opcodeId: OpcodeId = OpcodeId.DUP

  /**
   * Get the number of elements that the opcode consumes. They have to be already in the stack.
   *
   * @return consumed stack elements.
   */
  override def stackConsumed: BigInt = value

  override def lenght: BigInt = super.lenght + value

  override def toString: String = super.toString + value

  override def bytes: String = hex(opcodeId.opcode + value - 1)


  /**
   * Two pushOpcode are considered the same opcode if they have the same parameter length, ignoring the argument
   *
   * @param other opcode to test
   * @return if they are both push of the same length (e.g. PUSH1 != PUSH20)
   */

  override def isSame(other: Opcode): Boolean =
    super.isSame(other) && other.isInstanceOf[Dup] && other.asInstanceOf[Dup].value == value
}

object Dup {

  /**
   * Basic constructor for all Push opcodes
   *
   * @param _offset the offset in the bytecode, expressed in bytes
   * @param _value  tthe number of the stack value to be duplicated. It must be between 1 and 16
   */
  def create(_offset: Long, _value: BigInt): Dup = {
    require(_value > 0 && _value <= 16, "DUP number must be between 1 and 16")
    new Dup(_value) {
      override val offset: Long = _offset
    }
  }
}

