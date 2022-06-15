package info.ditrapani.netstring

import scala.deriving.Mirror
import scala.compiletime.{erasedValue, summonInline}

trait NetString[A]:
    extension (a: A) def toNetstring: String

object NetString:
    inline given derived[T](using m: Mirror.Of[T]): NetString[T] =
        val elemInstances: List[NetString[_]] = summonAll[m.MirroredElemTypes]
        inline m match
            case s: Mirror.SumOf[T]     => eqSum(s, elemInstances)
            case p: Mirror.ProductOf[T] => eqProduct(p, elemInstances)

    inline def summonAll[T <: Tuple]: List[NetString[_]] =
        inline erasedValue[T] match
            case _: EmptyTuple => Nil
            case _: (t *: ts) => summonInline[NetString[t]] :: summonAll[ts]


    def eqSum[T](s: Mirror.SumOf[T], elems: List[NetString[_]]): NetString[T] =
        new NetString[T]:
            extension (a: T) def toNetstring: String =
                val ord = s.ordinal(a)
                val tag = s"ord$ord"
                s"${tag.toNetstring}${netstringify(a, elems(ord))}".toNetstring

    def eqProduct[T](p: Mirror.ProductOf[T], elems: List[NetString[_]]): NetString[T] =
        new NetString[T]:
            extension (a: T) def toNetstring: String =
                val str = iterator(a)
                    .zip(elems)
                    .map { case (v, netstring) => netstringify(v, netstring) }
                    .mkString
                str.toNetstring

    def iterator[T](p: T) = p.asInstanceOf[Product].productIterator

    def netstringify(a: Any, netstring: NetString[_]): String =
        given NetString[Any] = netstring.asInstanceOf[NetString[Any]]
        a.toNetstring

given NetString[String] with
    extension (a: String) def toNetstring: String = s"${a.length()}:$a,"

given NetString[Int] with
    extension (a: Int) def toNetstring: String =
        a.toString.toNetstring

given NetString[Boolean] with
    extension (a: Boolean) def toNetstring: String =
        a.toString.toNetstring