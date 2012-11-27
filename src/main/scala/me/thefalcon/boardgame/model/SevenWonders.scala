package me.thefalcon.boardgame.model

object SevenWonders {

  case class SevenWondersGame(
    finalScore: Set[SWPlayerState]
  ) extends BoardGame[SWPlayerState] 

  case class SevenWondersScore(
    militaryPoints: Int,
    cashPoints: Int,
    wonderPoints: Int,
    civicsPoints: Int,
    tradePoints: Int,
    sciencePoints: Int,
    guildPoints: Int
  ) extends Score {
    override def points: Int = {
      scoreCategories sum
    }

    private[this] def scoreCategories: List[Int] = List(
      militaryPoints, cashPoints, wonderPoints, 
      civicsPoints, tradePoints, sciencePoints, guildPoints
    )
  }

  case class SWPlayerState(
    player: Player,
    score: SevenWondersScore,
    wonderUsed: Option[Wonder]
  ) extends PlayerState

  sealed abstract trait Wonder
  case object Gizah extends Wonder
  case object Rhodos extends Wonder
  case object Babylon extends Wonder
  case object Halikarnassos extends Wonder
  case object Olympia extends Wonder
  case object Alexandria extends Wonder
  case object Ephesos extends Wonder
  
  object Wonder {
    def fromString: String => Option[Wonder] = s => s.toLowerCase match {
          case "gizah" =>         Some(Gizah)
          case "rhodos" =>        Some(Rhodos)
          case "babylon" =>       Some(Babylon)
          case "halikarnassos" => Some(Halikarnassos)
          case "olympia" =>       Some(Olympia)
          case "alexandria" =>    Some(Alexandria)
          case "ephesos" =>       Some(Ephesos)
          case _ => None
    }
  }
  
  case class SciencePoints(
    numTablet: Int,
    numCompass: Int,
    numGear: Int
  ) {
    def points: Int = {
      val scienceComponents = List(numTablet, numCompass, numGear) 
      val numSets = scienceComponents min
      val verticalPoints = scienceComponents map { n: Int => n*n } sum 
      
      7 * numSets + verticalPoints
    }
  }
  
  import spray.json._
  object SWJsonProtocol extends DefaultJsonProtocol {
    implicit object WonderJsonFormat extends RootJsonFormat[Option[Wonder]] {
      def write(wOpt: Option[Wonder]) = wOpt match {
        case Some(w) => JsString(w toString)
        case None    => JsString("")
      }
    
      def read(value: JsValue) = value match {
        case JsString(str) => Wonder.fromString(str)
        case _ => None
      }
    }

    implicit val scoreFormat = jsonFormat7(SevenWondersScore)
    implicit val playerFormat = jsonFormat1(Player)
    implicit val playerStateFormat = jsonFormat3(SWPlayerState)
    implicit val gameFormat = jsonFormat1(SevenWondersGame)
  }
}
