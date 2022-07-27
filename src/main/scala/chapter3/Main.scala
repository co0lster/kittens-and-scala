package chapter3

import cats.Functor


trait App {
  def run(): Unit

  def main(args: Array[String]): Unit = {
    run()
  }
}

object Main extends App {
  override def run(): Unit = {
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.Future
    import scala.concurrent.Await
    import scala.concurrent.duration.DurationInt

    val future: Future[String] =
      Future(123)
        .map(n => n + n)
        .map(n => n * 2)
        .map(n => s"$n")

    Await.result(future, 1.second)
  }
}

object Main2 extends App {
  override def run(): Unit = {

    // Functor is a class that encapsulates sequencing computation
    // Higher Kinds and Type Constructors

    // In scala we declare Type Constructors using _


    //    def MyMethod[F[_]] = {
    //      val functor = Functor.apply[F]
    //    }
    //  }

    // This is analogous to specifying function parameter types.
    // When we declare a parameter we also give its type
    // However we use them using only the name

    def x(x: Int): Int = {
      x * x
    }

    import cats.instances.list._

    val list1 = List(1, 2, 3)
    val list2 = Functor[List].map(list1)(_ * 2)

    import cats.instances.option._

    val option1 = Option(1)
    val option2 = Functor[Option].map(option1)(_.toString)

    // LIFT TIME

    val func = (x: Int) => x * 3
    val liftedFunc = Functor[Option].lift(func)

    val res = liftedFunc(Option(1))

    println(res)

    // as replaces value inside Functor with given value
    Functor[List].as(list1, "as")

  }
}

object Main3 extends App {
  override def run(): Unit = {
    import cats.instances.function._
    import cats.syntax.functor._

    val func1 = (a: Int) => a + 1
    val func2 = (a: Int) => a * 2
    val func3 = (a: Int) => s"${a}!"
    val func4 = func1.map(func2).map(func3)

    println(func4(123))
  }
}


object Main4 extends App {
  override def run(): Unit = {

    import cats.instances.function._
    import cats.syntax.functor._

    def doMath[F[_]](start: F[Int])(implicit functor: Functor[F]): F[Int] =
      start.map(n => n + 1 * 2)

    import cats.instances.option._
    import cats.instances.list._

    doMath(Option(9))
    doMath(List(1, 2, 3))

  }
}

object Main5 extends App {
  override def run(): Unit = {

    implicit val optionFunctor: Functor[Option] =
      new Functor[Option] {
        override def map[A, B](value: Option[A])(func: A => B): Option[B] =
          value.map(func)
      }

  }
}

object Main6 extends App {
  override def run(): Unit = {

    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.{ExecutionContext, Future}

    //    implicit def futureFunctor
    //    (implicit ec: ExecutionContext): Functor[Future] =
    //      new Functor[Future] {
    //        override def map[A, B](value: Future[A])(func: A => B): Future[B] =
    //          value.map(func)
    //      }
    //
    //    Functor[Future]
  }
}

sealed trait Tree[+A]

final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

final case class Leaf[A](value: A) extends Tree[A]

object Tree {
  def branch[A](left: Tree[A], right: Tree[A]): Tree[A] =
    Branch(left, right)

  def leaf[A](value: A): Tree[A] =
    Leaf(value)
}

object Main7 extends App {

  override def run(): Unit = {

    import TreeFunctor._
    import cats.syntax.functor._
    val res = Tree.branch(Tree.leaf(10), Tree.leaf(20)).map(_ * 2)
    val res2 = Tree.branch(Tree.leaf(10), Tree.branch(Tree.leaf(10), Tree.leaf(20))).map(_ * 2)
    println(res2)
  }
}


object Main8 extends App {
  override def run(): Unit = {
    import cats.Monad

    import cats.instances.option._
    Monad[Option].flatMap(Option(1))(a => Option(a * 2))

    import cats.instances.list._
    Monad[List].flatMap(List(1, 2, 3))(a => List(a * 2))


  }
}

object Main9 extends App {
  override def run(): Unit = {
    import cats.Monad
    import cats.syntax.functor._
    import cats.syntax.flatMap._

    def sumSquare[F[_] : Monad](a: F[Int], b: F[Int]): F[Int] =
      for {
        x <- a
        y <- b
      } yield x * x + y * y

    import cats.Id

    println(sumSquare(3: Id[Int], 5: Id[Int]))

  }
}


object Main10 extends App {
  override def run(): Unit = {
    import cats.Id
    def pure[A](value: A): Id[A] = value

    def map[A, B](value: Id[A])(func: A => B): Id[B] = func(value)

    def flatMap[A, B](value: Id[A])(func: A => Id[B]): Id[B] = func(value)
  }
}


object Main11 extends App {
  override def run(): Unit = {

    object wrapper {
      sealed trait LoginError extends Product with Serializable

      final case class UserNotFound(username: String) extends LoginError

      final case class PasswordIncorrect(username: String) extends LoginError

      case object UnexpectedError extends LoginError
    };
    import wrapper._

    case class User(username: String, password: String)
    type LoginResult = Either[LoginError, User]


    def handleError(error: LoginError): Unit ={
      error match {
        case wrapper.UserNotFound(username) => println(s"$username not found")
        case wrapper.PasswordIncorrect(username) => println(s"$username password incorrect")
        case wrapper.UnexpectedError => println("Unexpected error")
      }
    }

    import cats.implicits.catsSyntaxEitherId
    val result1: LoginResult = User("dave", "123").asRight
    val result2: LoginResult = UserNotFound("dave").asLeft

  }
}
