package me.thefalcon.boardgame.model


trait IBoardGame[T <: PlayerState] {
  def finalScore: Set[T]
  def winner: Player
}
trait BoardGame[T <: PlayerState] extends IBoardGame[T] {
  val finalScore: Set[T]
  def winner: Player = finalScore.maxBy{ _.score.points }.player
}

trait HasScore {
  def score: Score
}
trait Score {
  def points: Int
}


trait HasPlayer {
  def player: Player

}
case class Player(handle: String) 

trait PlayerState extends HasPlayer with HasScore

