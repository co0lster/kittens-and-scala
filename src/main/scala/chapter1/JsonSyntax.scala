package chapter1

object JsonSyntax {
  // extension methods to extends existing types
  // it's so called interface syntax
  implicit class JsonWriterOps[A](value: A) {
    def toJson(implicit w: JsonWriter[A]): Json =
      w.write(value)
  }
}
