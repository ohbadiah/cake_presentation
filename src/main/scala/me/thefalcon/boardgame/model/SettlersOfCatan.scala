package me.thefalcon.boardgame.model

object SettlersOfCatan {
  
  case class SettlersGame(
    finalScore: Set[SettlersPlayerState]
  ) extends BoardGame[SettlersPlayerState] 

  case class SettlersScore(
    numSettlements: Int,
    numCities: Int,
    longestRoad: Boolean,
    largestArmy: Boolean,
    victoryCards: Int
  ) extends Score {
    override def points: Int = numSettlements + (2 * numCities) + (
        2 * List(longestRoad, largestArmy).count{ identity }
      ) + victoryCards
  }

  case class SettlersPlayerState(
    player: Player,
    score: SettlersScore
  ) extends PlayerState
} 
