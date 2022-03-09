package dev.ethereum.cfg.relation

import dev.ethereum.opcode.adt.{BasicBlockOpcode, WrapperRawCode}

sealed trait UnaryRelOpcode[+T <: WrapperRawCode] extends RelOpcode[T] {
  val next: BasicBlockOpcode[T]
}

case class PushJumpRelOpcode[+T <: WrapperRawCode](from: BasicBlockOpcode[T], next: BasicBlockOpcode[T]) extends UnaryRelOpcode[T]

case class BasicRelOpcode[+T <: WrapperRawCode](from: BasicBlockOpcode[T], next: BasicBlockOpcode[T]) extends UnaryRelOpcode[T]

case class SymbolicRelOpcode[+T <: WrapperRawCode](from: BasicBlockOpcode[T], next: BasicBlockOpcode[T]) extends UnaryRelOpcode[T]
