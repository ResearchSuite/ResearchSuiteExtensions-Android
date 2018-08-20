package org.researchsuite.researchsuiteextensions.app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.researchsuite.researchsuiteextensions.encryption.RSEncryptionManager

class MainActivity : AppCompatActivity()/**/ {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val encryptionManager = RSEncryptionManager(
                "TEAMWork.encryptionManager.masterKey",
                this,
                "TEAMWork.encryptionManager.prefsFile"
        )

        val encryptor1 = encryptionManager.getAEADEncryptor("test_keyset")

        val text = "Plain text"

        val cipher1 = encryptor1.encrypt(text.toByteArray(), null)

        val clear1 = String(encryptor1.decrypt(cipher1, null))

        assert(text == clear1)

        encryptionManager.deleteKeysets()

        val encryptor2 = encryptionManager.getAEADEncryptor("test_keyset")
        val cipher2 = encryptor2.encrypt(text.toByteArray(), null)

        val clear2 = String(encryptor2.decrypt(cipher2, null))

        assert(text == clear2)



    }
}
