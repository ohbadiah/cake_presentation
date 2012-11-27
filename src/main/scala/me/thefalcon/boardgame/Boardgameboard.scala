package me.thefalcon.boardgame

import model._
import SevenWondersReaders._

object Boardgameboard extends App with CsvInputComponent with HasSWCsvReader {
  lazy val reader = new SWCsvReader
  lazy val input = new CsvInput("scores.csv")

  val games = reader.read map { _.winner }
  games foreach println
}
