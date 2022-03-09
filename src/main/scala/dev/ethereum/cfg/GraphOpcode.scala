package dev.ethereum.cfg

import dev.ethereum.cfg.relation._
import dev.ethereum.opcodes.adt._

import scala.annotation.tailrec

case class GraphOpcode[T <: Opcode](rels: Map[Int, List[Int]], blocks: Set[BasicBlockOpcode[T]]) {
  private lazy val orderMap = rels.toList.sortBy(_._1)
  private lazy val orderRefsMap = refs.toList.sortBy(_._1)

  override def toString: String = orderMap.map {
    case (i, xs) if xs.isEmpty => s"$i --> END"
    case (i, xs) => s"$i --> ${xs.mkString("[", ",", "]")}"
  }.mkString("\n")

  lazy val refs: Map[Int, Set[BasicBlockOpcode[T]]] = rels.map { case (i, value) => i -> blocks.filter(b => value.contains(b.id)) }

  def prettyPrint: String = orderRefsMap.map {
    case (i, xs) if xs.isEmpty => s"$i --> END"
    case (i, xs) => s"$i --> ${xs.toList.sortBy(_.id).map(d => s"{${d.id}}: " + d.stack.opcodes.mkString(", ")).mkString("[\n", "\n", "\n]")}"
  }.mkString("\n")

  def show(blocks: Boolean = true): Unit = if (blocks) println(prettyPrint) else println(toString)

  def showBlocks(): Unit = blocks.toList.sortBy(_.id) foreach println
}

object GraphOpcode {
  def cfg[T <: Opcode](rels: List[RelOpcode[T]]): GraphOpcode[T] = map(rels)

  def map[T <: Opcode](rels: List[RelOpcode[T]]): GraphOpcode[T] = {
    @tailrec
    def aux(acc: List[(Int, Option[Int])], rest: List[RelOpcode[T]]): List[(Int, Option[Int])] = {
      rest match {
        case ::(head, next) => head match {
          case rel: UnaryRelOpcode[T] => aux((rel.from.id -> Option(rel.next.id)) :: acc, next)
          case rel: BinaryRelOpcode[T] => aux((rel.from.id -> Option(rel.trueBranch.id)) :: (rel.from.id -> Option(rel.falseBranch.id)) :: acc, next)
          case rel: NoSuccCodeRelOpcode[T] => aux((rel.from.id -> None) :: acc, next)
        }
        case Nil => acc.reverse
      }
    }

    val mapList: List[(Int, Option[Int])] = aux(Nil, rels)
    val mapRels: Map[Int, List[Int]] =
      mapList.map { case (i, _) => (i, mapList.filter { case (j, _) => i == j }.flatMap { case (_, maybeInt) => maybeInt }) }.toMap
    val blocks: Set[BasicBlockOpcode[T]] = rels.map(_.from).toSet
    GraphOpcode[T](mapRels, blocks)
  }
}
