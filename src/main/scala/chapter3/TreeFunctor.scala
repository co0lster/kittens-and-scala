package chapter3

import cats.Functor

object TreeFunctor {
  implicit val treeFunctor: Functor[Tree] = {
    new Functor[Tree] {
      override def map[A, B](value: Tree[A])(func: A => B): Tree[B] =
        value match {
          case Branch(left, right) =>
            Branch(map(left)(func), map(right)(func))
          case Leaf(fa) =>
            Leaf(func(fa))
        }

    }
  }
}
