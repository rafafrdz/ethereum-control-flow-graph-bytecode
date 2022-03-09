package dev.ethereum.opcodes.stackOpcodes

import dev.ethereum.opcodes.{Opcode, OpcodeId}

case class PushOpcode(upx: Int, parameter: BigInt) extends Opcode {
  override protected val opcodeId: OpcodeId = OpcodeId.PUSH

  /**
   * Get the number of elements that the opcode consumes. They have to be already in the stack.
   *
   * @return consumed stack elements.
   */
  override def stackConsumed: Int = 0

  override def lenght: BigInt = super.lenght + parameter

  override def toString: String = super.toString + parameter + " " + bytes.substring(2)

  /**
   * Two pushOpcode are considered the same opcode if they have the same parameter length, ignoring the argument
   *
   * @param other opcode to test
   * @return if they are both push of the same length (e.g. PUSH1 != PUSH20)
   */

  override def isSame(other: Opcode): Boolean =
    super.isSame(other) && other.isInstanceOf[PushOpcode] && other.asInstanceOf[PushOpcode].parameter == parameter
}

object PushOpcode {

  /**
   * Basic constructor for all Push opcodes
   *
   * @param _offset    the offset in the bytecode, expressed in bytes
   * @param _upx       the number of the PUSH. It must be between 1 and 32
   * @param _parameter the number pushed to the stack.
   */
  def create(_offset: Long, _upx: Int, _parameter: BigInt): PushOpcode = {
    require(_parameter > 0 && _parameter <= 32, "Push parameter length must be between 1 and 32 bytes")
    new PushOpcode(_upx, _parameter) {
      override val offset: Long = _offset
    }
  }

  def create(_offset: Long, _upx: Int): PushOpcode = {
    new PushOpcode(_upx, BigInt(0)) {
      override val offset: Long = _offset
    }
  }
}
