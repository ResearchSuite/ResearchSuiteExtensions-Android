package org.researchsuite.researchsuiteextensions.common

import java.io.*

interface RSObjectConverter<T: Any> {
    fun toBytes(src: T): ByteArray
    fun fromBytes(bytes: ByteArray): T?
}

//class RSGsonObjectConverter<T: Any>(val gson: Gson = RSGsonObjectConverter.gson): RSObjectConverter<T> {
//
//    companion object {
//        val gson = {
//            GsonBuilder()
//                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ")
//                    .create()
//        }()
//    }
//
//    override fun toBytes(src: T): ByteArray {
//        val jsonString = gson.toJson(src)
//        return jsonString.toByteArray()
//    }
//
//    private inline fun <reified T:Any> fromJsonString(jsonString: String): T? {
//        return gson.fromJson<T>(jsonString, T::class.java)
//    }
//    override fun fromBytes(bytes: ByteArray): T? {
//        val jsonString = String(bytes)
//        return this.fromJsonString(jsonString)
//    }
//
//}