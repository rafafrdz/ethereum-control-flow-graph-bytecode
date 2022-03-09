package dev.ethereum.opcodes.adt.stacks

import dev.ethereum.opcodes.OpcodeId
import dev.ethereum.opcodes.adt.Opcode

case class Push(value: BigInt, parameter: BigInt) extends Opcode {
  override protected val opcodeId: OpcodeId = OpcodeId.PUSH

  /**
   * Get the number of elements that the opcode consumes. They have to be already in the stack.
   *
   * @return consumed stack elements.
   */
  override def stackConsumed: BigInt = 0

  override def lenght: BigInt = super.lenght + parameter

  override def toString: String = super.toString + parameter + " " + bytes.substring(2)

  /**
   * Two pushOpcode are considered the same opcode if they have the same parameter length, ignoring the argument
   *
   * @param other opcode to test
   * @return if they are both push of the same length (e.g. PUSH1 != PUSH20)
   */

  override def isSame(other: Opcode): Boolean =
    super.isSame(other) && other.isInstanceOf[Push] && other.asInstanceOf[Push].parameter == parameter
}

object Push {

  /**
   * Basic constructor for all Push opcodes
   *
   * @param _offset    the offset in the bytecode, expressed in bytes
   * @param _value     the number of the PUSH. It must be between 1 and 32. Eg: PUSH10
   * @param _parameter the number pushed to the stack. (HEX: 0xYY)
   */
  def create(_offset: Long, _value: Int, _parameter: BigInt): Push = {
    require(_value > 0 && _value <= 32, "Push parameter length must be between 1 and 32 bytes")
    new Push(_value, _parameter) {
      override val offset: Long = _offset
    }
  }

  def create(_offset: Long, _upx: Int): Push = create(_offset, _upx, 0)
}