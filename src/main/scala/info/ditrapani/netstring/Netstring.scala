package info.ditrapani.netstring

import scala.deriving.Mirror
import scala.compiletime.{erasedValue, summonInline}

trait Netstring[A]:
  extension (a: A) def toNetstring: String

object Netstring:
  inline given derived[T](using m: Mirror.Of[T]): Netstring[T] =
    val elemInstances: List[Netstring[_]] = summonAll[m.MirroredElemTypes]
    inline m match
      case s: Mirror.SumOf[T] => eqSum(s, elemInstances)
      case p: Mirror.ProductOf[T] => eqProduct(p, elemInstances)

  private inline def summonAll[T <: Tuple]: List[Netstring[_]] =
    inline erasedValue[T] match
      case _: EmptyTuple => Nil
      case _: (t *: ts) => summonInline[Netstring[t]] :: summonAll[ts]

  private def eqSum[T](s: Mirror.SumOf[T], elems: List[Netstring[_]]): Netstring[T] =
    new Netstring[T]:
      extension (a: T)
        def toNetstring: String =
          val ord = s.ordinal(a)
          val tag = s"ord$ord"
          s"${tag.toNetstring}${netstringify(a, elems(ord))}".toNetstring

  private def eqProduct[T](p: Mirror.ProductOf[T], elems: List[Netstring[_]]): Netstring[T] =
    new Netstring[T]:
      extension (a: T)
        def toNetstring: String =
          val str = iterator(a)
            .zip(elems)
            .map { case (v, netstring) => netstringify(v, netstring) }
            .mkString
          str.toNetstring

  private def iterator[T](p: T) = p.asInstanceOf[Product].productIterator

  private def netstringify(a: Any, netstring: Netstring[_]): String =
    given Netstring[Any] = netstring.asInstanceOf[Netstring[Any]]
    a.toNetstring

given Netstring[String] with
  extension (a: String) def toNetstring: String = s"${a.length()}:$a,"

given Netstring[Int] with
  extension (a: Int)
    def toNetstring: String =
      a.toString.toNetstring

given Netstring[Boolean] with
  extension (a: Boolean)
    def toNetstring: String =
      a.toString.toNetstring
