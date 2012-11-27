package me.thefalcon.boardgame.model

import spray.json.{JsValue, JsNull}


trait InputComponent[T] {
  def input: Input[T]
  
  trait Input[T] {
    def next: T
  }
}

trait CsvInputComponent extends InputComponent[Csv] {

  class CsvInput(filename: String) extends Input[Csv] {
    def next = Csv fromFile filename
  }
}

trait JsonInputComponent extends InputComponent[JsValue] {
  
  trait JsonInput extends Input[JsValue] {
    def next: JsValue
  }
}

trait NullJsonInputComponent extends JsonInputComponent {
  import spray.json.JsNull
  
  def input: JsonInput

  class NullJsonInput extends JsonInput {
    def next = JsNull
  }
}

case class Csv(
  heading: List[String],
  records: List[Map[String,String]]
)
object Csv {
  def fromFile(filename: String): Csv = {
    import au.com.bytecode.opencsv.CSVReader
    import scala.collection.JavaConversions._

    val reader = new CSVReader(new java.io.FileReader(filename))
    val header = reader.readNext toList
    val lines: List[List[String]] = reader.readAll().toList map { _ toList }
    val rows: List[Map[String, String]] = lines.map{ Map.empty ++ header.zip(_) }
    
    Csv(header, rows)
  } 

}
