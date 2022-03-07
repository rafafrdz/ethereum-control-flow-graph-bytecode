# Ethereum bytecode analyzer with Control-Flow Graph method
<img src="https://img.shields.io/badge/Build-Processing-gree?logo=github"/> <img src="https://img.shields.io/badge/-Scala-%23f61938?logo=scala" /> 

Motivated by the immutable nature of Ethereum smart contracts and of their transactions, quite many approaches have been proposed to detect defects and security problems before smart contracts become persistent in the blockchain and they are granted control on substantial financial value.

Because smart contracts source code might not be available, static analysis approaches mostly face the challenge of analyzing compiled Ethereum bytecode, that is available directly from the official blockchain. However, due to the intrinsic complexity of Ethereum bytecode (especially in jump resolution), static analysis encounters significant obstacles that reduce the accuracy of exiting automated tools.

This project aims to offer a static analysis algorithm based on the symbolic execution of the Ethereum operand stack that allows us to resolve jumps in Ethereum bytecode and to construct an accurate control-flow graph (CFG) of the compiled smart contracts.

## Usage

You can see an example of how go from a bytecode string to a control-flow graph in **src/main/scala/dev/ethereum/opcode/Analyzed.scala**

### Parse from a bytecode string to OpCode

```scala
val runtimeByteCode: String = "PUSH1 0x80 PUSH1 0x40 MSTORE PUSH1 0x4 CALLDATASIZE LT PUSH1 0x3F JUMPI PUSH1 0x0 CALLDATALOAD PUSH29 0x100000000000000000000000000000000000000000000000000000000 SWAP1 DIV PUSH4 0xFFFFFFFF AND DUP1 PUSH4 0x72A20C78 EQ PUSH1 0x44 JUMPI JUMPDEST PUSH1 0x0 DUP1 REVERT JUMPDEST CALLVALUE DUP1 ISZERO PUSH1 0x4F JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH1 0x56 PUSH1 0x58 JUMP JUMPDEST STOP JUMPDEST PUSH1 0x0 DUP1 SWAP1 POP JUMPDEST PUSH1 0x0 DUP1 SLOAD SWAP1 POP DUP2 LT ISZERO PUSH1 0x95 JUMPI DUP1 DUP2 EXP PUSH1 0x0 DUP3 DUP2 SLOAD DUP2 LT ISZERO ISZERO PUSH1 0x7B JUMPI INVALID JUMPDEST SWAP1 PUSH1 0x0 MSTORE PUSH1 0x20 PUSH1 0x0 KECCAK256 ADD DUP2 SWAP1 SSTORE POP DUP1 DUP1 PUSH1 0x1 ADD SWAP2 POP POP PUSH1 0x5E JUMP JUMPDEST POP JUMP STOP LOG1 PUSH6 0x627A7A723058 KECCAK256 0xca 0xaa 0xad XOR EXP 0xf5 PUSH14 0xB73AA6BA3E26CA16BBDC0070FBCA MLOAD 0xce 0xc6 0xcb PUSH31 0xE1F0C63871E400290000000000000000000000000000000000000000000000"
def main(args: Array[String]): Unit = {
	parse(runtimeByteCode)
}
```

### From OpCode to Simple Blocks

In order to identify all simple blocks in a ethereum bytecode stack, we have to call **blocks** method.

```scala
val runtimeByteCode: String = "PUSH1 0x80 PUSH1 0x40 MSTORE PUSH1 0x4 CALLDATASIZE LT PUSH1 0x3F JUMPI PUSH1 0x0 CALLDATALOAD PUSH29 0x100000000000000000000000000000000000000000000000000000000 SWAP1 DIV PUSH4 0xFFFFFFFF AND DUP1 PUSH4 0x72A20C78 EQ PUSH1 0x44 JUMPI JUMPDEST PUSH1 0x0 DUP1 REVERT JUMPDEST CALLVALUE DUP1 ISZERO PUSH1 0x4F JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH1 0x56 PUSH1 0x58 JUMP JUMPDEST STOP JUMPDEST PUSH1 0x0 DUP1 SWAP1 POP JUMPDEST PUSH1 0x0 DUP1 SLOAD SWAP1 POP DUP2 LT ISZERO PUSH1 0x95 JUMPI DUP1 DUP2 EXP PUSH1 0x0 DUP3 DUP2 SLOAD DUP2 LT ISZERO ISZERO PUSH1 0x7B JUMPI INVALID JUMPDEST SWAP1 PUSH1 0x0 MSTORE PUSH1 0x20 PUSH1 0x0 KECCAK256 ADD DUP2 SWAP1 SSTORE POP DUP1 DUP1 PUSH1 0x1 ADD SWAP2 POP POP PUSH1 0x5E JUMP JUMPDEST POP JUMP STOP LOG1 PUSH6 0x627A7A723058 KECCAK256 0xca 0xaa 0xad XOR EXP 0xf5 PUSH14 0xB73AA6BA3E26CA16BBDC0070FBCA MLOAD 0xce 0xc6 0xcb PUSH31 0xE1F0C63871E400290000000000000000000000000000000000000000000000"

def main(args: Array[String]): Unit = {
  val opcodeParsed: List[RawOpCode] = parse(runtimeByteCode)
  blocks(opcodeParsed)
}
```

### From Block to Control-Flow Graph

In order to get an accurate control-flow graph from the assembly code, we have to call **relation** method. Here we have some options to show the graph:

```scala
val runtimeByteCode: String = "PUSH1 0x80 PUSH1 0x40 MSTORE PUSH1 0x4 CALLDATASIZE LT PUSH1 0x3F JUMPI PUSH1 0x0 CALLDATALOAD PUSH29 0x100000000000000000000000000000000000000000000000000000000 SWAP1 DIV PUSH4 0xFFFFFFFF AND DUP1 PUSH4 0x72A20C78 EQ PUSH1 0x44 JUMPI JUMPDEST PUSH1 0x0 DUP1 REVERT JUMPDEST CALLVALUE DUP1 ISZERO PUSH1 0x4F JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH1 0x56 PUSH1 0x58 JUMP JUMPDEST STOP JUMPDEST PUSH1 0x0 DUP1 SWAP1 POP JUMPDEST PUSH1 0x0 DUP1 SLOAD SWAP1 POP DUP2 LT ISZERO PUSH1 0x95 JUMPI DUP1 DUP2 EXP PUSH1 0x0 DUP3 DUP2 SLOAD DUP2 LT ISZERO ISZERO PUSH1 0x7B JUMPI INVALID JUMPDEST SWAP1 PUSH1 0x0 MSTORE PUSH1 0x20 PUSH1 0x0 KECCAK256 ADD DUP2 SWAP1 SSTORE POP DUP1 DUP1 PUSH1 0x1 ADD SWAP2 POP POP PUSH1 0x5E JUMP JUMPDEST POP JUMP STOP LOG1 PUSH6 0x627A7A723058 KECCAK256 0xca 0xaa 0xad XOR EXP 0xf5 PUSH14 0xB73AA6BA3E26CA16BBDC0070FBCA MLOAD 0xce 0xc6 0xcb PUSH31 0xE1F0C63871E400290000000000000000000000000000000000000000000000"

def main(args: Array[String]): Unit = {
  val opcodeParsed: List[RawOpCode] = parse(runtimeByteCode)
  val blockList: List[BasicBlock] = blocks(opcodeParsed)
  val rels: List[OpCodeRel] = relation(blockList)
  val graph: Graph = Graph.map(rels)

  graph.showBlocks()
  graph.show(false)
}
```

- **showBlocks** method. To show all blocks that performs in the graph. An example:

```
{0}: [PUSH1 0x80,PUSH1 0x40,MSTORE,PUSH1 0x4,CALLDATASIZE,LT,PUSH1 0x3F,JUMPI]
{1}: [PUSH1 0x0,CALLDATALOAD,PUSH29 0x100000000000000000000000000000000000000000000000000000000,SWAP1,DIV,PUSH4 0xFFFFFFFF,AND,DUP1,PUSH4 0x72A20C78,EQ,PUSH1 0x44,JUMPI]
{2}: [JUMPDEST,PUSH1 0x0,DUP1,REVERT]
{3}: [JUMPDEST,CALLVALUE,DUP1,ISZERO,PUSH1 0x4F,JUMPI]
{4}: [PUSH1 0x0,DUP1,REVERT]
{5}: [JUMPDEST,POP,PUSH1 0x56,PUSH1 0x58,JUMP]
{6}: [JUMPDEST,STOP]
{7}: [JUMPDEST,PUSH1 0x0,DUP1,SWAP1,POP,JUMPDEST,PUSH1 0x0,DUP1,SLOAD,SWAP1,POP,DUP2,LT,ISZERO,PUSH1 0x95,JUMPI]
{8}: [DUP1,DUP2,EXP,PUSH1 0x0,DUP3,DUP2,SLOAD,DUP2,LT,ISZERO,ISZERO,PUSH1 0x7B,JUMPI]
{9}: [INVALID]
{10}: [JUMPDEST,SWAP1,PUSH1 0x0,MSTORE,PUSH1 0x20,PUSH1 0x0,KECCAK256,ADD,DUP2,SWAP1,SSTORE,POP,DUP1,DUP1,PUSH1 0x1,ADD,SWAP2,POP,POP,PUSH1 0x5E,JUMP]
{11}: [JUMPDEST,POP,JUMP]
{12}: [STOP]
{13}: [LOG1,PUSH6 0x627A7A723058,KECCAK256 0xad,XOR,EXP 0xf5,PUSH14 0xB73AA6BA3E26CA16BBDC0070FBCA,MLOAD 0xcb,PUSH31 0xE1F0C63871E400290000000000000000000000000000000000000000000000]
```

- **show** method. To show the whole graph. You can pass a Boolean parameter if [true] you would like to see the graph and the blocks with its bytecodes or [false] if just see the map relationship between blocks. An example, with the parameter false:

```
0 --> [1,2]
1 --> [2,3]
2 --> END
3 --> [4,5]
4 --> END
5 --> [7]
6 --> END
7 --> [8,10]
8 --> [9,10]
9 --> END
10 --> [11]
11 --> SYMBOLIC (not resolved)
12 --> END
13 --> END
```

### Future Features

- [ ]  Resolve symbolic bytecode feature (that has not yet developed)