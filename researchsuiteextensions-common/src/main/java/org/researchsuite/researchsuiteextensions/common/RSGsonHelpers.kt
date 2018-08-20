package org.researchsuite.researchsuiteextensions.common

import com.google.gson.*
import org.json.JSONArray
import java.lang.reflect.Type

public interface JsonConvertible<T>:  JsonSerializer<T>, JsonDeserializer<T> {

}

public val JsonElement.asJsonObjectOrNull: JsonObject?
    get() = if (this.isJsonObject) this.asJsonObject else null

public val JsonElement.asJsonArrayOrNull: JsonArray?
    get() = if (this.isJsonArray) this.asJsonArray else null

public val JsonElement.asJsonPrimitiveOrNull: JsonPrimitive?
    get() = if (this.isJsonPrimitive) this.asJsonPrimitive else null

public val JsonPrimitive.asStringOrNull: String?
    get() = if (this.isString) this.asString else null

public val JsonPrimitive.asBooleanOrNull: Boolean?
    get() = if (this.isBoolean) this.asBoolean else null

public val JsonPrimitive.asNumberOrNull: Number?
    get() = if (this.isNumber) this.asNumber else null

public fun JsonObject.getStringOrNull(memberName: String): String? {
    return if (this.has(memberName)) this.get(memberName).asJsonPrimitiveOrNull?.asStringOrNull else null
}

public fun JsonObject.getBooleanOrNull(memberName: String): Boolean? {
    return if (this.has(memberName)) this.get(memberName).asJsonPrimitiveOrNull?.asBooleanOrNull else null
}

public fun JsonObject.getNumberOrNull(memberName: String): Number? {
    return if (this.has(memberName)) this.get(memberName).asJsonPrimitiveOrNull?.asNumberOrNull else null
}

public fun JsonObject.getJsonObjectOrNull(memberName: String): JsonObject? {
    return if (this.has(memberName)) this.get(memberName).asJsonObjectOrNull else null
}

public fun JsonObject.getJsonArrayOrNull(memberName: String): JsonArray? {
    return if (this.has(memberName)) this.get(memberName).asJsonArrayOrNull else null
}

@Throws(JsonParseException::class)
public fun <T> JsonObject.getObjectOrNull(memberName: String, context: JsonDeserializationContext, typeOfT: Type): T? {
    val jsonObject = this.getJsonObjectOrNull(memberName)
    return jsonObject?.let {
        try {
            context.deserialize<T>(it, typeOfT)
        } catch (e: JsonParseException) {
            null
        }
    }
}

public fun JsonObject.getPlainObjectOrNull(memberName: String): Any? {
    return if (this.has(memberName)) this.get(memberName).asPlainObject else null
}

public val JsonElement.asPlainObject: Any?
    get() = {

        when {

            this.isJsonNull -> {
                null
            }

            this.isJsonPrimitive -> {

                val primitive = this.asJsonPrimitive

                when {

                    primitive.isString -> { primitive.asString }
                    primitive.isBoolean -> { primitive.asBoolean }
                    primitive.isNumber -> {  primitive.asNumber }
                    else -> { null }

                }

            }

            this.isJsonObject -> {
                val obj = this.asJsonObject
                val keyset = obj.keySet()

//                val map: Map<String, Any> = keyset.map { key ->
//                    val element = obj.get(key)
//                    element.asPlainObject?.let {
//                        Pair(key, it)
//                    }
//                }.filterNotNull().toMap()

                val map= keyset.map { key ->
                    val element = obj.get(key)
                    element.asPlainObject?.let {
                        Pair(key, it)
                    }
                }.filterNotNull().toMap()

                map
            }

            this.isJsonArray -> {
                val array = this.asJsonArray.map {
                    it.asPlainObject
                }.filterNotNull()

                array
            }

            else -> {
                null
            }

        }

    }()