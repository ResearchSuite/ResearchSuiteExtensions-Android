package org.researchsuite.researchsuiteextensions.common

import java.io.*

open class RSJavaObjectConverter<T: Any>: RSObjectConverter<T> {

    override fun toBytes(src: T): ByteArray {
        val bos = ByteArrayOutputStream()

        try {
            val oos = ObjectOutputStream(bos)
            oos.writeObject(src)
            oos.close()
        } catch (ioe: IOException) {
//            Log.e(TAG, "Cannot write file " + file.getPath());
        }

        val bytes = bos.toByteArray()
        bos.close()
        return bytes
    }

    override fun fromBytes(bytes: ByteArray): T? {
        val bis = ByteArrayInputStream(bytes)

        try {
            val ois = ObjectInputStream(bis)
            val map = ois.readObject() as T
            ois.close()
            return map

        } catch (ioe: IOException) {
//            Log.e(TAG, "Cannot read file " + file.getPath());
            return null
        }
    }

}