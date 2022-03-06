package dev.ethereum.opcode

import dev.ethereum.opcode.adt._

import scala.annotation.tailrec

object OpCodeParser {

  def parse(opc: String): List[RawOpCode] = {
    val opcList = opc.split(" ").toList

    @tailrec
    def aux(acc: List[RawOpCode], rest: List[String]): List[RawOpCode] = {
      rest match {
        case ::(head, next) if head.head.isDigit => aux(WithValueOp(acc.head.op, head) :: acc.tail, next)
        case ::(head, next) => aux(SingleOp(head) :: acc, next)
        case Nil => acc
      }
    }

    aux(Nil, opcList).reverse
  }

  def blocks(stack: List[RawOpCode]): List[BasicBlock] = {
    @tailrec
    def aux(id: Int, acc: List[BasicBlock], res: List[RawOpCode]): List[BasicBlock] = {
      val (blckRaw, nextblck): (List[RawOpCode], List[RawOpCode]) = res.span(b => !ByteOpCode.ENDCODE.contains(b.op))
      nextblck match {
        case ::(head, next) =>
          val blck: List[Rest] = (blckRaw :+ head).map(Rest)
          aux(id + 1, BasicBlock(id, Stack(blck)) :: acc, next)
        case Nil => BasicBlock(id, Stack(blckRaw.map(Rest))) :: acc
      }
    }

    aux(0, Nil, stack).reverse
  }

  def relation(bcodes: List[BasicBlock]): List[OpCodeRel] = {
    @tailrec
    def from(acc: List[OpCodeRel], rest: List[BasicBlock]): List[OpCodeRel] = {
      rest match {
        case ::(b@BasicBlock(id, stack), next) if OpCodeRel.isPushJump(stack) => from(PushJumpRel(b, to(next)) :: acc, next)
        case ::(b@BasicBlock(id, stack), next) if OpCodeRel.isPushJumpI(stack) => {
          val (falseBr, trueBr, _): (BasicBlock, BasicBlock, List[BasicBlock]) = branch(next)
          from(PushJumpIRel(b, falseBr, trueBr) :: acc, next)
        }
        case ::(b@BasicBlock(id, stack), next) if OpCodeRel.isOtherJump(stack) => from(NonResolvedSymbolicRel(b) :: acc, next)
        case ::(b@BasicBlock(id, stack), next) if OpCodeRel.noSucc(stack) => from(LeafCodeRel(b) :: acc, next)
        case ::(b@BasicBlock(id, stack), Nil) => EndCodeRel(b) :: acc
        case ::(b@BasicBlock(id, stack), next) => from(BasicRel(b, next.head) :: acc, next)
        case Nil => acc
      }
    }

    @tailrec
    def to(rest: List[BasicBlock]): BasicBlock =
      rest match {
        case ::(b@BasicBlock(_, stack), next) if OpCodeRel.noSucc(stack) => to(next)
        case ::(b@BasicBlock(_, stack), next) => b
      }

    def branch(rest: List[BasicBlock]): (BasicBlock, BasicBlock, List[BasicBlock]) = {
      val (f, frest): (BasicBlock, List[BasicBlock]) = falseTo(rest)
      val (t, trest): (BasicBlock, List[BasicBlock]) = trueTo(frest, Nil)
      (f, t, trest)
    }


    def falseTo(rest: List[BasicBlock]): (BasicBlock, List[BasicBlock]) = (rest.head, rest.tail)

    @tailrec
    def trueTo(rest: List[BasicBlock], diff: List[BasicBlock]): (BasicBlock, List[BasicBlock]) =
      rest match {
        case ::(b@BasicBlock(_, stack), next) if OpCodeRel.startByJumpDest(stack) => (b, diff ++ next)
        case ::(b@BasicBlock(_, _), next) => trueTo(next, diff :+ b)
      }

    from(Nil, bcodes).reverse
  }



}
