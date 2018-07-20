package org.researchsuite.researchsuiteextensions.encryption

import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.aead.AeadFactory
import java.security.GeneralSecurityException

class RSAEADEncryptor(keysetHandle: KeysetHandle): RSEncryptor {
    val aead = AeadFactory.getPrimitive(keysetHandle)

    @Throws(GeneralSecurityException::class)
    override fun encrypt(plaintext: ByteArray, associatedData: ByteArray?): ByteArray {
        val ciphertext = aead.encrypt(plaintext, associatedData)
        return ciphertext
    }

    @Throws(GeneralSecurityException::class)
    override fun decrypt(ciphertext: ByteArray, associatedData: ByteArray?): ByteArray {
        val decrypted = aead.decrypt(ciphertext, associatedData)
        return decrypted
    }

}