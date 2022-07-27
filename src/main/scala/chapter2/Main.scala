package chapter2

import cats.kernel.Monoid

trait App {
  def run(): Unit
  def main(args: Array[String]): Unit = {
    run()
  }
}

object Main extends App {
  override def run(): Unit = {

    import cats.Monoid
    import cats.instances.string._

    val x = Monoid[String].combine("stringy", " string")
    println(x)
    Monoid[String].empty
  }

}
object Main2 extends App {
  override def run(): Unit = {

    import cats.syntax.semigroup._
    import cats.instances.string._

    val x = "stingy" |+| " " |+| "string"
    println(x)

    import cats.instances.int._
    import cats.instances.option._

//    def add(items: List[Option[Int]]): Option[Int] = {
//      items.foldLeft(Monoid[Int].empty)(_|+|_)
//      items.foldLeft(Monoid[Option[Int]].empty)(_|+|_)
//    }

    def add[A](items: List[A])(implicit monoid: Monoid[A])= {
      items.foldLeft(monoid.empty)(_|+|_)
    }

    // this thingy with [A: Monoid] is called context bound syntax
//    def add[A: Monoid](items: List[A]): A = {
//      items.foldLeft(Monoid[A].empty)(_|+|_)
//    }

    add(List(1,2,3))
    add(List(Some(1),Some(2),None))


    case class Order(totalCost: Double, quantity: Double)

    implicit val monoid: Monoid[Order] = new Monoid[Order] {
      override def empty: Order = Order(0.0, 0.0)
      override def combine(x: Order, y: Order): Order = Order(x.totalCost + y.totalCost, x.quantity + y.quantity)
    }
  }

}
