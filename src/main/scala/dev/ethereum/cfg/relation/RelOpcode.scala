package dev.ethereum.cfg.relation

import dev.ethereum.opcode.adt.{BasicBlockOpcode, ByteOpcode, StackOpcode, WrapperRawCode}

trait RelOpcode[+T <: WrapperRawCode] {
  val from: BasicBlockOpcode[T]
}

object RelOpcode {
  private def isPreceded[T <: WrapperRawCode](stack: StackOpcode[T]): Boolean = stack.dim >= 2

  private def precededBy[T <: WrapperRawCode](stack: StackOpcode[T], opcode: String): Boolean = stack.get(stack.dim - 2).same(opcode)

  private def endBy[T <: WrapperRawCode](stack: StackOpcode[T], opcode: String): Boolean = stack.get(stack.dim - 1).same(opcode)

  private def startBy[T <: WrapperRawCode](stack: StackOpcode[T], opcode: String): Boolean = stack.head.same(opcode)

  private def isPUSHand[T <: WrapperRawCode](stack: StackOpcode[T], opCode: String): Boolean = precededBy(stack, ByteOpcode.PUSH1) && endBy(stack, opCode)

  private def isNotPUSHand[T <: WrapperRawCode](stack: StackOpcode[T], opCode: String): Boolean = !precededBy(stack, ByteOpcode.PUSH1) && endBy(stack, opCode)

  def isPushJump[T <: WrapperRawCode](stack: StackOpcode[T]): Boolean =
    isPreceded[T](stack) && isPUSHand(stack, ByteOpcode.JUMP)

  def isPushJumpI[T <: WrapperRawCode](stack: StackOpcode[T]): Boolean =
    isPreceded[T](stack) && isPUSHand(stack, ByteOpcode.JUMPI)

  def isOtherJump[T <: WrapperRawCode](stack: StackOpcode[T]): Boolean =
    isPreceded[T](stack) && isNotPUSHand(stack, ByteOpcode.JUMP)

  def noSucc[T <: WrapperRawCode](stack: StackOpcode[T]): Boolean =
    ByteOpcode.NOSUCC.map(endBy(stack, _)).reduce(_ || _)

  def startByJumpDest[T <: WrapperRawCode](stack: StackOpcode[T]): Boolean =
    startBy(stack, ByteOpcode.JUMPDEST)

}

