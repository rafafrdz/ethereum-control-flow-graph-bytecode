package dev.ethereum.cfg.relation

import dev.ethereum.opcode.adt.{BasicBlockOpcode, WrapperRawCode}

trait NoSuccCodeRelOpcode[+T <: WrapperRawCode] extends RelOpcode[T]

case class LeafCodeRelOpcode[+T <: WrapperRawCode](from: BasicBlockOpcode[T]) extends NoSuccCodeRelOpcode[T]

case class EndCodeRelOpcode[+T <: WrapperRawCode](from: BasicBlockOpcode[T]) extends NoSuccCodeRelOpcode[T]

/** Orphan Jumps */
case class NonResolvedSymbolicRelOpcode[+T <: WrapperRawCode](from: BasicBlockOpcode[T]) extends NoSuccCodeRelOpcode[T]

case class ResolvedSymbolicRelOpcode[+T <: WrapperRawCode](from: BasicBlockOpcode[T]) extends RelOpcode[T]
