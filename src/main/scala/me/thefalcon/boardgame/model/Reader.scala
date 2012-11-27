package me.thefalcon.boardgame.model

trait BoardGameReaderComponent[T <: PlayerState] {
  def reader: BoardGameReader[T]

  trait BoardGameReader[T <: PlayerState] {
    def read: List[BoardGame[T]]
  }
}

object SevenWondersReaders {
  import spray.json._
  import SevenWonders._

  trait HasSWJsonReader extends BoardGameReaderComponent[SWPlayerState] {
    this: JsonInputComponent => 
    import SWJsonProtocol._

    class SWJsonReader extends BoardGameReader[SWPlayerState] {
      def read: List[SevenWondersGame] = 
        input.next.convertTo[List[SevenWondersGame]]
    }
  }

  trait HasSWCsvReader extends BoardGameReaderComponent[SWPlayerState] {
    this: CsvInputComponent =>
    
    class SWCsvReader extends BoardGameReader[SWPlayerState] {
      private[this] def relevantColumns = List( 
          "wonderused", "military", "gold", "wonder", "civic", 
          "trade", "guild", "science"
        )
      def read: List[SevenWondersGame] = {
        val csv = input.next
        val grouped = csv.records.groupBy{ _.get("game") }
        val playerStates = grouped map { _._2 map toSWPlayerState }
        val boardGames = playerStates map { Set.empty ++ _ } map { SevenWondersGame(_) }
        boardGames toList
      }
     
      private[this] def toSWPlayerState: Map[String,String] => SWPlayerState = 
        row => 
          SWPlayerState(
            Player(row("handle")),
            toSWScore(row),
            Wonder.fromString(row("wonderused"))  
          ) 

      private[this] def toSWScore: Map[String, String] => SevenWondersScore =
        row =>
          SevenWondersScore(
            row("military").toInt,
            row("cash").toInt,
            row("wonder").toInt,
            row("civics").toInt,
            row("trade").toInt,
            row("science").toInt,
            row("guild").toInt
          )
    }

  }

/*  object ComponentRegistry extends NullJsonInputComponent with
    HasSWJsonReader {
    val input = new NullJsonInput
    val reader = new SWJsonReader
  } */
}
