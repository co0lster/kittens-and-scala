package chapter1

import cats.{Eq, Show}
import chapter1.printables.Cat

import java.util.Date


trait App {
  def run(): Unit

  def main(args: Array[String]): Unit = {
    run()
  }
}

object Main extends App {
  override def run(): Unit = {
    /*
    Do an import of any type class instances
    compiler will find relevant types for our
    method without specified implicit parameter
     */
    import chapter1.JsonWriterInstances._
    Json.toJson(Person("Eddie", "lol@omg.lol"))
    // compiler is changing above to this
    // Json.toJson(Person("Eddie", "lol@omg.lol"))(personWriter)
  }

}

object Main2 extends App {
  override def run(): Unit = {
    import chapter1.JsonSyntax._
    import chapter1.JsonWriterInstances._
    Person("Max", "max@omg.lol").toJson
    // compiler is changing above to this
    // Person("Max", "max@omg.lol").toJson(personWriter)
  }
}

object Main3 extends App {
  override def run(): Unit = {
    import chapter1.JsonWriterInstances._
    println(implicitly[JsonWriter[String]])
  }
}

object Main4 extends App {
  override def run(): Unit = {
    import chapter1.JsonWriterInstances._
    Json.toJson(Option("String"))
  }
}

object Main5 extends App {
  override def run(): Unit = {
    import chapter1.printables.Printable
    import chapter1.printables.PrintableInstances._
    val cat1 = Cat("Cymes", 11, "W packi")
    val cat2 = Cat("Hiki", 11, "Biszkopcik")
    Printable.print(cat1)
    Printable.print(cat2)
  }
}

object Main6 extends App {
  override def run(): Unit = {

    import cats.implicits._
    implicit val catShow: Show[Cat] = Show.show[Cat] { cat =>
      val name = cat.name.show
      val age = cat.age.show
      val color = cat.color.show
      s"${name} is ${age} year-old  ${color} cat"
    }

    println(Cat("Cymes", 11, "W packi").show)
  }
}

object Main7 extends App {
  override def run(): Unit = {
    import cats.instances.int._
    import cats.syntax.eq._
    123 === 123
    println(123 === 123)
    println(123 =!= 123)
  }
}

object Main8 extends App {
  override def run(): Unit = {

    import cats.instances.long._
    import cats.syntax.eq._
    implicit val dateEq: Eq[Date] =
      Eq.instance[Date]  { (date1, date2) =>
       date1.getTime === date2.getTime
      }

   val x = new Date()
    Thread.sleep(1000)
   val y = new Date()

    println(x === y)

  }
}

object Main9 extends App {
  override def run(): Unit = {

    import cats.instances.int._
    import cats.instances.string._
    import cats.syntax.eq._
    implicit val dateEq: Eq[Cat] =
      Eq.instance[Cat]  { (cat1, cat2) =>
        (cat1.color === cat2.color) &&
        (cat1.name === cat2.name) &&
        (cat1.age === cat2.age)
      }

    val cat1 = Cat("Garfield", 9, "orange and black")
    val cat2 = Cat("Bubu", 9, "orange and black")

    println(cat1 === cat2)

    import cats.instances.option._
    val optionCat1 = Option(cat1)
    val optionCat2 = Option.empty[Cat]

    optionCat1 === optionCat2
  }
}
/*
Covariance
trait List[+A]
covariance is used for outputs
data that we can later get out of container type like List
*/

/*
Contravariance
trait List[-A]
type F[B] is a subtype of F[A]
when A is a subtype of B
useful for modeling types used for inputs

trait JsonWriter[-A] {
  def write (value: A): Json
}
*/

object Main10 extends App {
  override def run(): Unit = {
    trait Shape
    case class Circle() extends Shape

    val circle: Circle = ???
    val shape: Shape = ???

    val shapeWriter: JsonWriter[Shape] = ???
    val circleWriter: JsonWriter[Circle] = ???

    def format[A](value: A, writer: JsonWriter[A]): Json = writer.write(value)
    // JsonWriter[Shape] is a subtype of JsonWriter[Circle] because
    // Circle is a subtype of Shape

    format(circle, circleWriter)
    // You can use shapeWriter to save a circle:
    format(circle, shapeWriter)
    // type mismatch:
    // format(shape, circleWriter)
    // You cannot use circleWriter to write Shape that could be a triangle or anything man!
  }
}

/*
Invariance
trait List[A]
type F[B] is a subtype of F[A]
when A is a subtype of B
useful for modeling types used for inputs

trait JsonWriter[-A] {
  def write (value: A): Json
}
*/
