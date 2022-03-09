package dev.ethereum.opcodes

import dev.ethereum.opcodes.OpcodeId.{INVALID, toHex}

trait OpcodeId {
  val opcode: Int
  val name: String
  def bytes: String = toHex(opcode)

  override def toString: String = name

}

object OpcodeId {
  private def toHex(hex: Int): String = f"0x${hex}%02X"

  private def instance(opcde: Int, nme: String): OpcodeId = new OpcodeId {
    val opcode: Int = opcde
    val name: String = nme
  }

  val STOP: OpcodeId = instance(0x00, "STOP")
  val ADD: OpcodeId = instance(0x01, "ADD")
  val MUL: OpcodeId = instance(0x02, "MUL")
  val SUB: OpcodeId = instance(0x03, "SUB")
  val DIV: OpcodeId = instance(0x04, "DIV")
  val SDIV: OpcodeId = instance(0x05, "SDIV")
  val MOD: OpcodeId = instance(0x06, "MOD")
  val SMOD: OpcodeId = instance(0x07, "SMOD")
  val ADDMOD: OpcodeId = instance(0x08, "ADDMOD")
  val MULMOD: OpcodeId = instance(0x09, "MULMOD")
  val EXP: OpcodeId = instance(0x0A, "EXP")
  val SIGNEXTEND: OpcodeId = instance(0x0B, "SIGNEXTEND")
  val LT: OpcodeId = instance(0x10, "LT")
  val GT: OpcodeId = instance(0x11, "GT")
  val SLT: OpcodeId = instance(0x12, "SLT")
  val SGT: OpcodeId = instance(0x13, "SGT")
  val EQ: OpcodeId = instance(0x14, "EQ")
  val ISZERO: OpcodeId = instance(0x15, "ISZERO")
  val AND: OpcodeId = instance(0x16, "AND")
  val OR: OpcodeId = instance(0x17, "OR")
  val XOR: OpcodeId = instance(0x18, "XOR")
  val NOT: OpcodeId = instance(0x19, "NOT")
  val BYTE: OpcodeId = instance(0x1A, "BYTE")
  val SHL: OpcodeId = instance(0x1B, "SHL")
  val SHR: OpcodeId = instance(0x1C, "SHR")
  val SAR: OpcodeId = instance(0x1D, "SAR")
  val SHA3: OpcodeId = instance(0x20, "SHA3")
  val ADDRESS: OpcodeId = instance(0x30, "ADDRESS")
  val BALANCE: OpcodeId = instance(0x31, "BALANCE")
  val ORIGIN: OpcodeId = instance(0x32, "ORIGIN")
  val CALLER: OpcodeId = instance(0x33, "CALLER")
  val CALLVALUE: OpcodeId = instance(0x34, "CALLVALUE")
  val CALLDATALOAD: OpcodeId = instance(0x35, "CALLDATALOAD")
  val CALLDATASIZE: OpcodeId = instance(0x36, "CALLDATASIZE")
  val CALLDATACOPY: OpcodeId = instance(0x37, "CALLDATACOPY")
  val CODESIZE: OpcodeId = instance(0x38, "CODESIZE")
  val CODECOPY: OpcodeId = instance(0x39, "CODECOPY")
  val GASPRICE: OpcodeId = instance(0x3A, "GASPRICE")
  val EXTCODESIZE: OpcodeId = instance(0x3B, "EXTCODESIZE")
  val EXTCODECOPY: OpcodeId = instance(0x3C, "EXTCODECOPY")
  val RETURNDATASIZE: OpcodeId = instance(0x3D, "RETURNDATASIZE")
  val RETURNDATACOPY: OpcodeId = instance(0x3E, "RETURNDATACOPY")
  val EXTCODEHASH: OpcodeId = instance(0x3F, "EXTCODEHASH")
  val BLOCKHASH: OpcodeId = instance(0x40, "BLOCKHASH")
  val COINBASE: OpcodeId = instance(0x41, "COINBASE")
  val TIMESTAMP: OpcodeId = instance(0x42, "TIMESTAMP")
  val NUMBER: OpcodeId = instance(0x43, "NUMBER")
  val DIFFICULTY: OpcodeId = instance(0x44, "DIFFICULTY")
  val GASLIMIT: OpcodeId = instance(0x45, "GASLIMIT")
  val CHAINID: OpcodeId = instance(0x46, "CHAINID")
  val SELFBALANCE: OpcodeId = instance(0x47, "SELFBALANCE")
  val POP: OpcodeId = instance(0x50, "POP")
  val MLOAD: OpcodeId = instance(0x51, "MLOAD")
  val MSTORE: OpcodeId = instance(0x52, "MSTORE")
  val MSTORE8: OpcodeId = instance(0x53, "MSTORE8")
  val SLOAD: OpcodeId = instance(0x54, "SLOAD")
  val SSTORE: OpcodeId = instance(0x55, "SSTORE")
  val JUMP: OpcodeId = instance(0x56, "JUMP")
  val JUMPI: OpcodeId = instance(0x57, "JUMPI")
  val PC: OpcodeId = instance(0x58, "PC")
  val MSIZE: OpcodeId = instance(0x59, "MSIZE")
  val GAS: OpcodeId = instance(0x5A, "GAS")
  val JUMPDEST: OpcodeId = instance(0x5B, "JUMPDEST")
  val PUSH: OpcodeId = instance(0x60, "PUSH")
  val DUP: OpcodeId = instance(0x80, "DUP")
  val SWAP: OpcodeId = instance(0x90, "SWAP")
  val LOG: OpcodeId = instance(0xA0, "LOG")
  val CREATE: OpcodeId = instance(0xF0, "CREATE")
  val CALL: OpcodeId = instance(0xF1, "CALL")
  val CALLCODE: OpcodeId = instance(0xF2, "CALLCODE")
  val RETURN: OpcodeId = instance(0xF3, "RETURN")
  val DELEGATECALL: OpcodeId = instance(0xF4, "DELEGATECALL")
  val STATICCALL: OpcodeId = instance(0xFA, "STATICCALL")
  val REVERT: OpcodeId = instance(0xFD, "REVERT")
  val INVALID: OpcodeId = instance(0xFE, "INVALID")
  val SELFDESTRUCT: OpcodeId = instance(0xFF, "SELFDESTRUCT")
}

object algo extends App {


  println(INVALID.opcode.toHexString)
  println(f"0x${INVALID.opcode}%02X")
}