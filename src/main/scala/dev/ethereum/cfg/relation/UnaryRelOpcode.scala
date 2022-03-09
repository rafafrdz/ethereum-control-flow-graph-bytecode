package dev.ethereum.cfg.relation

import dev.ethereum.opcodes.adt.{BasicBlockOpcode, Opcode}

sealed trait UnaryRelOpcode[+T <: Opcode] extends RelOpcode[T] {
  val next: BasicBlockOpcode[T]
}

case class PushJumpRelOpcode[+T <: Opcode](from: BasicBlockOpcode[T], next: BasicBlockOpcode[T]) extends UnaryRelOpcode[T]

case class BasicRelOpcode[+T <: Opcode](from: BasicBlockOpcode[T], next: BasicBlockOpcode[T]) extends UnaryRelOpcode[T]

case class SymbolicRelOpcode[+T <: Opcode](from: BasicBlockOpcode[T], next: BasicBlockOpcode[T]) extends UnaryRelOpcode[T]
