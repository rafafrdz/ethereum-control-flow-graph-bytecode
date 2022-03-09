package dev.ethereum.cfg

import dev.ethereum.cfg.relation.SymbolicRelOpcode
import dev.ethereum.opcode.adt.{BasicBlockOpcode, WrapperRawCode}

object SymbolicStackResolver {

  def resolve[T <: WrapperRawCode](from: BasicBlockOpcode[T], rest: List[BasicBlockOpcode[T]]): BasicBlockOpcode[T] = ??? // TODO

  def resolve[T <: WrapperRawCode](stack: SymbolicRelOpcode[T]): BasicBlockOpcode[T] = ??? // TODO

}
