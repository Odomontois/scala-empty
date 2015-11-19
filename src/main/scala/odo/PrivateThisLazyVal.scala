/**
  * Author: Oleg Nizhnik
  * Date  : 18.11.2015
  * Time  : 16:12
  */
package odo

class PrivateThisLazyVal {
  def double(x: Int) = x * 2
  private [this] val x = double(100)
  private [this] lazy val y = double(100)

  def sum = x + y
}
