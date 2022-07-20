package chapter1.printables

import cats.Show

object PrintableInstances {

  implicit val stringPrintable: Printable[String] =
    new Printable[String] {
      override def format(value: String): String = value
    }

  implicit val intPrintable: Printable[Int] =
    new Printable[Int] {
      override def format(value: Int): String = value.toString
    }

  implicit val catPrintable: Printable[Cat] =
    new Printable[Cat] {
      override def format(value: Cat): String =
        s"${value.name} is ${value.age} year-old  ${value.color} cat"
    }

  import cats.instances.int._
  import cats.instances.string._
  import cats.syntax.show._

  implicit val catShow: Show[Cat] = Show.show[Cat] { cat =>
    val name  = cat.name.show
    val age  = cat.age.show
    val color  = cat.color.show
    s"${name} is ${age} year-old  ${color} cat"
  }

}
