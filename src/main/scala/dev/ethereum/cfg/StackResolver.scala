package dev.ethereum.cfg

import dev.ethereum.cfg.relation._
import dev.ethereum.opcode.adt._

import scala.annotation.tailrec

object StackResolver {

  def resolve[T <: WrapperRawCode](bcodes: List[BasicBlockOpcode[T]]): List[RelOpcode[T]] =
    from(Nil, bcodes).reverse

  @tailrec
  private def from[T <: WrapperRawCode](acc: List[RelOpcode[T]], rest: List[BasicBlockOpcode[T]]): List[RelOpcode[T]] = {
    rest match {
      case ::(b@BasicBlockOpcode(id, stack), next) if RelOpcode.isPushJump(stack) => from(PushJumpRelOpcode(b, to(next)) :: acc, next)
      case ::(b@BasicBlockOpcode(id, stack), next) if RelOpcode.isPushJumpI(stack) =>
        val (falseBr, trueBr, _): (BasicBlockOpcode[T], BasicBlockOpcode[T], List[BasicBlockOpcode[T]]) = branch(next)
        from(PushJumpIRelOpcode(b, falseBr, trueBr) :: acc, next)

      case ::(b@BasicBlockOpcode(id, stack), next) if RelOpcode.isOtherJump(stack) => from(NonResolvedSymbolicRelOpcode(b) :: acc, next)
      case ::(b@BasicBlockOpcode(id, stack), next) if RelOpcode.noSucc(stack) => from(LeafCodeRelOpcode(b) :: acc, next)
      case ::(b@BasicBlockOpcode(id, stack), Nil) => EndCodeRelOpcode(b) :: acc
      case ::(b@BasicBlockOpcode(id, stack), next) => from(BasicRelOpcode(b, next.head) :: acc, next)
      case Nil => acc
    }
  }

  @tailrec
  private def to[T <: WrapperRawCode](rest: List[BasicBlockOpcode[T]]): BasicBlockOpcode[T] =
    rest match {
      case ::(b@BasicBlockOpcode(_, stack), next) if RelOpcode.noSucc(stack) => to(next)
      case ::(b@BasicBlockOpcode(_, stack), next) => b
    }

  private def branch[T <: WrapperRawCode](rest: List[BasicBlockOpcode[T]]): (BasicBlockOpcode[T], BasicBlockOpcode[T], List[BasicBlockOpcode[T]]) = {
    val (f, frest): (BasicBlockOpcode[T], List[BasicBlockOpcode[T]]) = falseTo(rest)
    val (t, trest): (BasicBlockOpcode[T], List[BasicBlockOpcode[T]]) = trueTo(frest, Nil)
    (f, t, trest)
  }

  private def falseTo[T <: WrapperRawCode](rest: List[BasicBlockOpcode[T]]): (BasicBlockOpcode[T], List[BasicBlockOpcode[T]]) = (rest.head, rest.tail)

  @tailrec
  private def trueTo[T <: WrapperRawCode](rest: List[BasicBlockOpcode[T]], diff: List[BasicBlockOpcode[T]]): (BasicBlockOpcode[T], List[BasicBlockOpcode[T]]) =
    rest match {
      case ::(b@BasicBlockOpcode(_, stack), next) if RelOpcode.startByJumpDest(stack) => (b, diff ++ next)
      case ::(b@BasicBlockOpcode(_, _), next) => trueTo(next, diff :+ b)
    }


}
