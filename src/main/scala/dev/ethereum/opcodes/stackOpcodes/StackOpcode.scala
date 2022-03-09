package dev.ethereum.opcodes.stackOpcodes

import dev.ethereum.opcodes.{Opcode, OpcodeId}

trait StackOpcode extends Opcode {
  protected val id: OpcodeId = opcodeId
}
