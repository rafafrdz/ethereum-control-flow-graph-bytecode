package dev.ethereum.opcode.tools

import dev.ethereum.opcode.adt._

import scala.annotation.tailrec

object ParseOpcode {

  def from[T <: WrapperRawCode](opcode: String): List[BasicBlockOpcode[T]] = blocks(parse(opcode))

  def parse(opc: String): List[RawOpcode] = {
    val opcList = opc.split(" ").toList

    @tailrec
    def aux(acc: List[RawOpcode], rest: List[String]): List[RawOpcode] = {
      rest match {
        case ::(head, next) if head.head.isDigit => aux(WithValueOp(acc.head.op, head) :: acc.tail, next)
        case ::(head, next) => aux(SingleOp(head) :: acc, next)
        case Nil => acc
      }
    }

    aux(Nil, opcList).reverse
  }

  def blocks[T <: WrapperRawCode](stack: List[RawOpcode]): List[BasicBlockOpcode[T]] = {
    @tailrec
    def aux(id: Int, acc: List[BasicBlockOpcode[T]], res: List[RawOpcode]): List[BasicBlockOpcode[T]] = {
      val (blckRaw, nextblck): (List[RawOpcode], List[RawOpcode]) = res.span(b => !ByteOpcode.ENDCODE.contains(b.op))
      nextblck match {
        case ::(head, next) =>
          val blck: List[T] = (blckRaw :+ head).map(b => Rest(b).like[T])
          aux(id + 1, BasicBlockOpcode(id, StackOpcode(blck)) :: acc, next)
        case Nil => BasicBlockOpcode(id, StackOpcode(blckRaw.map(b => Rest(b).like[T]))) :: acc
      }
    }

    aux(0, Nil, stack).reverse
  }


}
