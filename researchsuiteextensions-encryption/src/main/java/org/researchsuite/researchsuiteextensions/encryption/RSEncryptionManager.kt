package org.researchsuite.researchsuiteextensions.encryption

import android.content.Context
import android.os.Build
import com.google.crypto.tink.KeysetManager
import com.google.crypto.tink.aead.AeadConfig

import com.google.crypto.tink.aead.AeadKeyTemplates
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.google.crypto.tink.integration.android.AndroidKeystoreKmsClient
import com.google.crypto.tink.proto.KeyTemplate
import com.google.crypto.tink.subtle.Random

class RSEncryptionManager(
        val masterKeyID: String,
        val context: Context,
        val preferencesID: String
) {

    companion object {
        fun generateKeyMaterial(size: Int): ByteArray {
            return Random.randBytes(size)
        }
    }

    val masterKeyUri = "android-keystore://$masterKeyID"

    init{
        AeadConfig.register()
    }

    fun deleteKeysets() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.deleteSharedPreferences(this.preferencesID)
        }
        else {
            val preferences = context.getSharedPreferences(this.preferencesID, Context.MODE_PRIVATE)
            val preferencesMap = preferences.all
            with(preferences.edit()) {
                preferencesMap.keys.forEach {
                    remove(it)
                }
                commit()
            }

            assert(preferences.all.count() == 0)

        }

    }

    fun generateNewKeysetManager(keysetID: String, keyTemplate: KeyTemplate): AndroidKeysetManager {
        val manager = AndroidKeysetManager.Builder()
                .withSharedPref(this.context, keysetID, this.preferencesID)
                .withKeyTemplate(keyTemplate)
                .withMasterKeyUri(this.masterKeyUri)
                .build()

        return manager
    }

    fun getAEADEncryptor(keysetID: String): RSAEADEncryptor {
        val manager = this.generateNewKeysetManager(keysetID, AeadKeyTemplates.AES256_GCM)
        return RSAEADEncryptor(manager.keysetHandle)
    }

}