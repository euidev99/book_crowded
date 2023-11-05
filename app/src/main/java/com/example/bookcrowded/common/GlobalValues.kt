package com.example.bookcrowded.common

import androidx.collection.SparseArrayCompat
import androidx.collection.set

object GlobalValues {

    private val values = SparseArrayCompat<MutableMap<String, Any?>?>()

    fun clear() {
        values.clear()
    }

    fun put(code: Int, key: String, value: Any?) {
        val map = values[code] ?: let {
            val temp = mutableMapOf<String, Any?>()
            values[code] = temp
            temp
        }
        map[key] = value
    }

    fun get(key: Int, name: String): Any? {
        return values.get(key)?.get(name)
    }

    fun remove(key: Int) {
        values.remove(key)
    }

    fun pop(key: Int): MutableMap<String, Any?>? {
        val value = values[key]
        remove(key)
        return value
    }

    fun containsKey(key: Int): Boolean {
        return values.containsKey(key)
    }

    operator fun get(key: Int): MutableMap<String, Any?>? {
        return values[key]
    }

    operator fun set(key: Int, value: MutableMap<String, Any?>?) {
        values.put(key, value)
    }
}