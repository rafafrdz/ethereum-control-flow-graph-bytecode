package dev.ethereum.cfg.relation

import dev.ethereum.opcodes.adt.raws.WrapperRawCode
import dev.ethereum.opcodes.adt.{BasicBlockOpcode, Opcode, StackOpcode}

trait RelOpcode[+T <: Opcode] {
  val from: BasicBlockOpcode[T]
}

object RelOpcode {
  private def isPreceded[T <: Opcode](stack: StackOpcode[T]): Boolean = stack.dim >= 2

  private def precededBy[T <: Opcode](stack: StackOpcode[T], opcode: String): Boolean = stack.get(stack.dim - 2).same(opcode)

  private def endBy[T <: Opcode](stack: StackOpcode[T], opcode: String): Boolean = stack.get(stack.dim - 1).same(opcode)

  private def startBy[T <: Opcode](stack: StackOpcode[T], opcode: String): Boolean = stack.head.same(opcode)

  private def isPUSHand[T <: Opcode](stack: StackOpcode[T], opCode: String): Boolean = precededBy(stack, WrapperRawCode.PUSH1) && endBy(stack, opCode)

  private def isNotPUSHand[T <: Opcode](stack: StackOpcode[T], opCode: String): Boolean = !precededBy(stack, WrapperRawCode.PUSH1) && endBy(stack, opCode)

  def isPushJump[T <: Opcode](stack: StackOpcode[T]): Boolean =
    isPreceded[T](stack) && isPUSHand(stack, WrapperRawCode.JUMP)

  def isPushJumpI[T <: Opcode](stack: StackOpcode[T]): Boolean =
    isPreceded[T](stack) && isPUSHand(stack, WrapperRawCode.JUMPI)

  def iSymbolicJump[T <: Opcode](stack: StackOpcode[T]): Boolean =
    isPreceded[T](stack) && isNotPUSHand(stack, WrapperRawCode.JUMP)

  def noSucc[T <: Opcode](stack: StackOpcode[T]): Boolean =
    WrapperRawCode.NOSUCC.map(endBy(stack, _)).reduce(_ || _)

  def startByJumpDest[T <: Opcode](stack: StackOpcode[T]): Boolean =
    startBy(stack, WrapperRawCode.JUMPDEST)

}

