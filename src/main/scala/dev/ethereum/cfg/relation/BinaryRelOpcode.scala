package dev.ethereum.cfg.relation

import dev.ethereum.opcode.adt.{BasicBlockOpcode, WrapperRawCode}

trait BinaryRelOpcode[+T <: WrapperRawCode] extends RelOpcode[T] {
  val falseBranch: BasicBlockOpcode[T]
  val trueBranch: BasicBlockOpcode[T]
}

case class PushJumpIRelOpcode[+T <: WrapperRawCode](from: BasicBlockOpcode[T], falseBranch: BasicBlockOpcode[T], trueBranch: BasicBlockOpcode[T]) extends BinaryRelOpcode[T]