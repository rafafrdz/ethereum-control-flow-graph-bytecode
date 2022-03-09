package dev.ethereum.opcodes.adt

import scala.annotation.tailrec

trait OpCode

object OpCode {
  def tabular(n: Int): String = (0 until n).map(_ => "\t").mkString("")
}

sealed trait ByteOpCode extends OpCode

object ByteOpCode {
  val PUSH1: String = "PUSH1"

  val JUMP: String = "JUMP"
  val JUMPDEST: String = "JUMPDEST"
  val JUMPI: String = "JUMPI"
  val STOP: String = "STOP"
  val REVERT: String = "REVERT"
  val RETURN: String = "RETURN"
  val INVALID: String = "INVALID"
  val SELFDESTRUCT: String = "SELFDESTRUCT"
  val NOSUCC: List[String] = List(STOP, REVERT, RETURN, INVALID, SELFDESTRUCT)
  val ENDCODE: List[String] = List(JUMP, JUMPI) ++ NOSUCC
}

case class BasicBlock(id: Int, stack: Stack) extends ByteOpCode {
  override def toString: String = s"{${id}}: ${stack.bopcode.mkString("[", ",", "]")}"
}

case class Stack(bopcode: List[WrapperRawCode]) extends ByteOpCode {
  def get(n: Int): WrapperRawCode = bopcode(n)

  def head: WrapperRawCode = get(0)

  lazy val dim: Int = bopcode.length

  override def toString: String = bopcode.mkString("[", ",", "]")
}

trait WrapperRawCode extends ByteOpCode {
  val code: RawOpCode

  def same(other: String): Boolean = code.op.equalsIgnoreCase(other)

  def same(other: RawOpCode): Boolean = code == other
}

case class Rest(code: RawOpCode) extends WrapperRawCode {
  override def toString: String = code.toString
}


sealed trait RawOpCode extends OpCode {
  val op: String
}

case class SingleOp(op: String) extends RawOpCode {
  override def toString: String = op
}

case class WithValueOp(op: String, value: String) extends RawOpCode {
  override def toString: String = s"$op $value"
}


sealed trait OpCodeRel {
  val from: BasicBlock
}

object OpCodeRel {
  private def isPreceded(stack: Stack): Boolean = stack.dim >= 2

  private def precededBy(stack: Stack, opcode: String): Boolean = stack.get(stack.dim - 2).same(opcode)

  private def endBy(stack: Stack, opcode: String): Boolean = stack.get(stack.dim - 1).same(opcode)

  private def startBy(stack: Stack, opcode: String): Boolean = stack.head.same(opcode)

  private def isPUSHand(stack: Stack, opCode: String): Boolean = precededBy(stack, ByteOpCode.PUSH1) && endBy(stack, opCode)

  private def isNotPUSHand(stack: Stack, opCode: String): Boolean = !precededBy(stack, ByteOpCode.PUSH1) && endBy(stack, opCode)

  def isPushJump(stack: Stack): Boolean =
    isPreceded(stack) && isPUSHand(stack, ByteOpCode.JUMP)

  def isPushJumpI(stack: Stack): Boolean =
    isPreceded(stack) && isPUSHand(stack, ByteOpCode.JUMPI)

  def isOtherJump(stack: Stack): Boolean =
    isPreceded(stack) && isNotPUSHand(stack, ByteOpCode.JUMP)

  def noSucc(stack: Stack): Boolean =
    ByteOpCode.NOSUCC.map(endBy(stack, _)).reduce(_ || _)

  def startByJumpDest(stack: Stack): Boolean =
    startBy(stack, ByteOpCode.JUMPDEST)


}

trait UnaryCodeRel extends OpCodeRel {
  val next: BasicBlock
}

trait BinaryCodeRel extends OpCodeRel {
  val falseBranch: BasicBlock
  val trueBranch: BasicBlock
}

trait NoSuccCodeRel extends OpCodeRel {

}

case class PushJumpRel(from: BasicBlock, next: BasicBlock) extends UnaryCodeRel

case class PushJumpIRel(from: BasicBlock, falseBranch: BasicBlock, trueBranch: BasicBlock) extends BinaryCodeRel

case class BasicRel(from: BasicBlock, next: BasicBlock) extends UnaryCodeRel

case class LeafCodeRel(from: BasicBlock) extends NoSuccCodeRel

case class EndCodeRel(from: BasicBlock) extends NoSuccCodeRel

/** Orphan Jumps */
case class NonResolvedSymbolicRel(from: BasicBlock) extends NoSuccCodeRel

case class SymbolicRel(from: BasicBlock, next: BasicBlock) extends UnaryCodeRel

object SymbolicRel {
  def resolve(from: BasicBlock, rest: List[BasicBlock]): BasicBlock = ??? // TODO
}

case class Graph(rels: Map[Int, List[Int]], blocks: Set[BasicBlock]) {
  private lazy val orderMap = rels.toList.sortBy(_._1)
  private lazy val orderRefsMap = refs.toList.sortBy(_._1)

  override def toString: String = orderMap.map {
    case (i, xs) if xs.isEmpty => s"$i --> END"
    case (i, xs) => s"$i --> ${xs.mkString("[", ",", "]")}"
  }.mkString("\n")

  lazy val refs: Map[Int, Set[BasicBlock]] = rels.map { case (i, value) => i -> blocks.filter(b => value.contains(b.id)) }

  def prettyPrint: String = orderRefsMap.map {
    case (i, xs) if xs.isEmpty => s"$i --> END"
    case (i, xs) => s"$i --> ${xs.toList.sortBy(_.id).map(d => s"{${d.id}}: " + d.stack.bopcode.mkString(", ")).mkString("[\n", "\n", "\n]")}"
  }.mkString("\n")

  def show(blocks: Boolean = true): Unit = if (blocks) println(prettyPrint) else println(toString)

  def showBlocks(): Unit = blocks.toList.sortBy(_.id) foreach println
}

object Graph {
  def map(rels: List[OpCodeRel]): Graph = {
    @tailrec
    def aux(acc: List[(Int, Option[Int])], rest: List[OpCodeRel]): List[(Int, Option[Int])] = {
      rest match {
        case ::(head, next) => head match {
          case rel: UnaryCodeRel => aux((rel.from.id -> Option(rel.next.id)) :: acc, next)
          case rel: BinaryCodeRel => aux((rel.from.id -> Option(rel.trueBranch.id)) :: (rel.from.id -> Option(rel.falseBranch.id)) :: acc, next)
          case rel: NoSuccCodeRel => aux((rel.from.id -> None) :: acc, next)
        }
        case Nil => acc.reverse
      }
    }

    val mapList: List[(Int, Option[Int])] = aux(Nil, rels)
    val mapRels: Map[Int, List[Int]] =
      mapList.map { case (i, _) => (i, mapList.filter { case (j, _) => i == j }.flatMap { case (_, maybeInt) => maybeInt }) }.toMap
    val blocks: Set[BasicBlock] = rels.map(_.from).toSet
    Graph(mapRels, blocks)
  }
}