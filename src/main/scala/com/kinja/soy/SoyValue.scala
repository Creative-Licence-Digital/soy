package com.kinja.soy

import com.google.template.soy.data.{ SoyMapData, SoyListData }
import scala.collection.JavaConversions._

/**
 * Base trait of types which can be used in Google Cloure Templatates.
 */
sealed trait SoyValue extends Any {

  /**
   * Converts the SoyValue to a (Java) type which can be used directly in Google Closure Templates.
   */
  def build: Any
}

case object SoyNull extends SoyValue {
  @inline def build = null
}

case class SoyString(value: String) extends AnyVal with SoyValue {
  @inline def build = value
}

case class SoyBoolean(value: Boolean) extends AnyVal with SoyValue {
  @inline def build = value
}

case class SoyInt(value: Int) extends AnyVal with SoyValue {
  @inline def build = value
}

case class SoyFloat(value: Float) extends AnyVal with SoyValue {
  @inline def build = value
}

case class SoyDouble(value: Double) extends AnyVal with SoyValue {
  @inline def build = value
}

case class SoyList(value: Seq[SoyValue]) extends AnyVal with SoyValue {
  def build = new SoyListData(asJavaIterable(value.map(_.build)))
}

case class SoyMap(value: Map[String, SoyValue]) extends AnyVal with SoyValue {

  def build = new SoyMapData(mapAsJavaMap(value.map { case (k, v) => k -> v.build }))

  /**
   * All distinct keys of the map.
   */
  def keys: Set[String] = value.map(_._1).toSet

  /**
   * All distinct values of the map.
   */
  def values: Set[SoyValue] = value.map(_._2).toSet

  /**
   * Merge this map with an other one. Values from other override value of the current map.
   */
  def ++(other: SoyMap): SoyMap = {
    SoyMap(value ++ other.value)
  }

  /**
   * Removes a key from the map.
   */
  def -(key: String): SoyMap = SoyMap(value.filterNot(_._1 == key))

  /**
   * Appends a key-value pair to the map.
   */
  def +(field: (String, SoyValue)): SoyMap = SoyMap(value + field)

}
