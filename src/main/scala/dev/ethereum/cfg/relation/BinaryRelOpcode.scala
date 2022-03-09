package dev.ethereum.cfg.relation

import dev.ethereum.opcodes.adt.{BasicBlockOpcode, Opcode}

trait BinaryRelOpcode[+T <: Opcode] extends RelOpcode[T] {
  val falseBranch: BasicBlockOpcode[T]
  val trueBranch: BasicBlockOpcode[T]
}

case class PushJumpIRelOpcode[+T <: Opcode](from: BasicBlockOpcode[T], falseBranch: BasicBlockOpcode[T], trueBranch: BasicBlockOpcode[T])
  extends BinaryRelOpcode[T]