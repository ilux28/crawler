package ru.train.exception

case class CustomException() extends Throwable("Exception") {
  override def equals(that: Any): Boolean = super.equals()
}
