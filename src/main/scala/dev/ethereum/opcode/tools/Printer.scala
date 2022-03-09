package dev.ethereum.opcode.tools

trait Printer[T]

object Printer {
  def tabular(n: Int): String = (0 until n).map(_ => "\t").mkString("")
}
