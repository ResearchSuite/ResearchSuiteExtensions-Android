package org.researchsuite.researchsuiteextensions.encryption

import java.security.GeneralSecurityException

class RSClearEncryptor: RSEncryptor {

    //It might be nice to add HMAC to this...
    //does this add anything over just using encryption?
    @Throws(GeneralSecurityException::class)
    override fun decrypt(ciphertext: ByteArray, associatedData: ByteArray?): ByteArray {
        return ciphertext
    }

    @Throws(GeneralSecurityException::class)
    override fun encrypt(plaintext: ByteArray, associatedData: ByteArray?): ByteArray {
        return plaintext
    }

}