package org.researchsuite.researchsuiteextensions.encryption

import org.researchsuite.researchsuiteextensions.common.RSJavaObjectConverter

public class RSEncryptedJavaObjectConverter<T: Any>(val encryptor: RSEncryptor): RSJavaObjectConverter<T>() {

    override fun toBytes(src: T): ByteArray {
        val clearBytes = super.toBytes(src)
        return encryptor.encrypt(clearBytes, null)
    }

    override fun fromBytes(bytes: ByteArray): T? {
        val clearBytes = encryptor.decrypt(bytes, null)
        return super.fromBytes(clearBytes)
    }

}