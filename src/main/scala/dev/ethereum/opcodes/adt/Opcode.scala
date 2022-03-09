package dev.ethereum.opcodes.adt

import dev.ethereum.opcodes.OpcodeId

trait Opcode {
  self =>

  protected val opcodeId: OpcodeId

  /**
   * Default getter for the offset, the position in the code
   *
   * @return offset of the opcode
   */
  val offset: Long = 0

  /**
   * Get the bytecode length of the opcode.
   *
   * Default value is 1, but the PUSH statement overrides this method.
   *
   * @return length in byte of the opcode.
   */
  def lenght: BigInt = 1

  /**
   * Get the number of elements that the opcode consumes. They have to be already in the stack.
   *
   * @return consumed stack elements.
   */
  def stackConsumed: BigInt

  /**
   * Get the number of elements that the opcode generates on the stack.
   *
   * Default value is 1, but some opcodes override this method
   *
   * @return generated stack elements.
   */
  def stackGenerated: BigInt = 1

  /**
   * Default getter for Name
   *
   * @return name of the opcode
   */
  def name: String = opcodeId.name

  /**
   * Assembles the opcode giving the bytes representation
   *
   * @return bytes representation
   */
  def bytes: String = opcodeId.bytes

  /**
   * String representation of the opcode, with offset and name
   *
   * e.g. "0: PUSH1 60"
   *
   * @return "offset: opcode"
   */
  override def toString: String = offset + ": " + name

  /**
   * Checks whether the other opcode represents the same one, even if with a different offset
   *
   * @param other opcode to test
   * @return if they represents the same opcode
   */
  def isSame(other: Opcode): Boolean = opcodeId == other.opcodeId

  /**
   * Checks whether the other opcode represents the same one, even if with a different offset
   *
   * @param other opcode to test
   * @return if they represents the same opcode
   */
  def same(other: Opcode): Boolean = isSame(other)

  /**
   * Checks whether the other opcode represents the same one, even if with a different offset
   *
   * @param other opcode to test
   * @return if they represents the same opcode
   */
  def same(other: String): Boolean = toString == other

  /**
   * Two opcodes are equals iff they have the same opcodeID and offset
   *
   * @param obj other object to test
   * @return if they are equals
   */
  override def equals(obj: Any): Boolean = {
    lazy val same: Boolean = self == obj
    lazy val clazz: Boolean = Option(obj).isEmpty || self.getClass != obj.getClass
    lazy val objop: Opcode = obj.asInstanceOf[Opcode]
    lazy val res: Boolean = offset == objop.offset && opcodeId == objop.opcodeId
    if (same) same else if (clazz) !clazz else res
  }

  /**
   * Default hashcode. It's calculated as a xor between opcodeID hashcode and the offset
   *
   * @return hashcode
   */
  override def hashCode(): Int = opcodeId.hashCode() ^ offset.toInt

}