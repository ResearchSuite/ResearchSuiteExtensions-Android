package org.researchsuite.researchsuiteextensions.encryption

import java.security.GeneralSecurityException

interface RSEncryptor {

    @Throws(GeneralSecurityException::class)
    public fun encrypt(plaintext: ByteArray, associatedData: ByteArray?): ByteArray

    @Throws(GeneralSecurityException::class)
    public fun decrypt(ciphertext: ByteArray, associatedData: ByteArray?): ByteArray

}