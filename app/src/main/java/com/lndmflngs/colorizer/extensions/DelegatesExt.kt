package com.lndmflngs.colorizer.extensions

import kotlin.reflect.KProperty

object DelegatesExt {
  fun <T> notNullSingleValue() = NotNullSingleValueVar<T>()
}

class NotNullSingleValueVar<T> {
  private var value: T? = null

  operator fun getValue(
    thisRef: Any?,
    property: KProperty<*>
  ): T =
    value ?: throw IllegalStateException("${property.name} not initialized")

  operator fun setValue(
    thisRef: Any?,
    property: KProperty<*>,
    value: T
  ) {
    this.value = if (this.value == null) value
    else throw IllegalStateException("${property.name} already initialized")
  }
}

open class SingletonHolder<out T, in A>(creator: (A) -> T) {
  private var creator: ((A) -> T)? = creator
  @Volatile private var instance: T? = null

  fun getInstance(arg: A): T {
    val i = instance
    if (i != null) {
      return i
    }

    return synchronized(this) {
      val i2 = instance
      if (i2 != null) {
        i2
      } else {
        val created = creator!!(arg)
        instance = created
        creator = null
        created
      }
    }
  }
}