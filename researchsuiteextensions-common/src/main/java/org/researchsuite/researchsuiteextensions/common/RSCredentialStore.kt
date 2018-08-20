package org.researchsuite.researchsuiteextensions.common

import android.content.Context

interface RSCredentialStore {

    fun get(context: Context, key: String): ByteArray?
    fun set(context: Context, key: String, value: ByteArray?)
    fun remove(context: Context, key: String)
    fun has(context: Context, key: String): Boolean

}
