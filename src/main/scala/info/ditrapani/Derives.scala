package info.ditrapani

import info.ditrapani.netstring.{Netstring, given}

@main def DeriveNetstringTypeclasses() =
  fun("Hello, world!")
  fun(999)
  fun(Adult("Joe", 75))
  fun(Child("Sally", false))
  val visitor: Visitor = Visitor.Child("Steve", true)
  fun(visitor)

def fun[T: Netstring](t: T): Unit =
  println(t.toNetstring)

case class Adult(name: String, cash: Int) derives Netstring
case class Child(name: String, sick: Boolean) derives Netstring

enum Visitor derives Netstring:
  case Adult(name: String, cash: Int)
  case Child(name: String, sick: Boolean)
