package org.researchsuite.researchsuiteextensions.common


@Throws(RSCommonErrors.ConversionError::class)
inline fun <reified K, reified V> Map<*, *>.toMap(): Map<K,V> {

    val convertedMap: Map<K,V> = this.map { entry ->
        if (entry.key is K) {
            (entry.key as K)?.let { key ->
                (entry.value as? V)?.let { value ->
                    Pair(key, value)
                }
            }
        }
        else null
    }.filterNotNull().toMap()

    if (convertedMap.count() != this.count()) {
        throw RSCommonErrors.ConversionError()
    }

    return convertedMap
}

inline fun <reified K, reified V> Map<*, *>.toMapOrNull(): Map<K,V>? {
    try {
        return this.toMap<K, V>()
    }
    catch (e: RSCommonErrors.ConversionError) {
        return null
    }
}


public fun <E> List<E>.indexOfOrNull(element: E): Int? {
    val index = this.indexOf(element)
    return if (index > -1) index else null
}

public fun Any.valueForKeyPath(keyPath: String): Any? {
    return (this as? Map<*, *>)?.let {

        try {
            it.toMap<String, Any>().valueForKeyPath(keyPath)
        }
        catch (e: RSCommonErrors.ConversionError) {
            return null
        }

    }
}

public fun <V: Any> Map<String,V>.valueForKeyPath(keyPath: String): Any? {
    val keyList = keyPath.split(".")

    return keyList.firstOrNull()?.let {
        this.get(it)
    }?.let {
        val tail = keyList.drop(1)
        if (tail.count() > 0) { it.valueForKeyPath(tail.joinToString(".")) }
        else { it }
    }
}


public fun Any.valueForPath(path: List<Any>): Any? {
    val list = this as? List<*>
    val map = this as? Map<*, *>

    if (list != null) {
        return list.valueForPath(path)
    }
    else if (map != null) {
        return map.valueForPath(path)
    }
    else { return null }
}

public fun Map<*, *>.valueForPath(path: List<Any>): Any? {

    val head = path.firstOrNull()
    val tail = path.drop(1)

    when (head) {

        is String -> {
            return (this as? Map<*,*>)?.valueForKeyPath(head)?.let {
                if (tail.count() > 0) it.valueForPath(tail)
                else it
            }
        }

        else -> {
            return null
        }

    }

}

public fun List<*>.valueForPath(path: List<Any>): Any? {

    val head = path.firstOrNull()
    val tail = path.drop(1)

    when (head) {

        is Int -> {
            return this.getOrNull(head)?.let {
                if (tail.count() > 0) it.valueForPath(tail)
                else it
            }
        }

        else -> {
            return null
        }

    }

}