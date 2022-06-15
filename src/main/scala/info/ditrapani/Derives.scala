package info.ditrapani

import info.ditrapani.netstring.{Netstring, given}

@main def DeriveNetstringTypeclasses() =
  println("Hello, world!".toNetstring)
  println(999.toNetstring)
  println(Adult("Joe", 75).toNetstring)
  println(Child("Sally", false).toNetstring)

case class Adult(name: String, cash: Int) derives Netstring
case class Child(name: String, sick: Boolean) derives Netstring
