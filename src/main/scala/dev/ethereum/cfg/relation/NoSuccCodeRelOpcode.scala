package dev.ethereum.cfg.relation

import dev.ethereum.opcodes.adt.{BasicBlockOpcode, Opcode}

trait NoSuccCodeRelOpcode[+T <: Opcode] extends RelOpcode[T]

case class LeafCodeRelOpcode[+T <: Opcode](from: BasicBlockOpcode[T]) extends NoSuccCodeRelOpcode[T]

case class EndCodeRelOpcode[+T <: Opcode](from: BasicBlockOpcode[T]) extends NoSuccCodeRelOpcode[T]

/** Orphan Jumps */
case class NonResolvedSymbolicRelOpcode[+T <: Opcode](from: BasicBlockOpcode[T]) extends NoSuccCodeRelOpcode[T]

case class ResolvedSymbolicRelOpcode[+T <: Opcode](from: BasicBlockOpcode[T]) extends RelOpcode[T]
