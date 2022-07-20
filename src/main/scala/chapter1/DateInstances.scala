package chapter1

import java.util.Date

object DateInstances {

  import cats._

//  implicit val dateShow: Show[Date] ={
//    new Show[Date] {
//      override def show(t: Date): String = s"${t.getTime}ms since the epoch"
//    }
//  }

  implicit val dateShow: Show[Date] =
    Show.show(date => s"${date.getTime}ms since the epoch")

}
