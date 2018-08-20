package org.researchsuite.researchsuiteextensions.common

import android.util.AtomicFile
import java.io.*
import java.security.GeneralSecurityException

open class RSKeyValueStore(val filePath: String, val objectConverter: RSObjectConverter<Map<String, Any>> = RSJavaObjectConverter()) {

    var currentMap: Map<String, Any>? = null

    @Synchronized
    private fun loadMap(): Map<String, Any>? {
        val file = File(this.filePath)

        val bytes: ByteArray? = {
            try {
                val atomicFile = AtomicFile(file)
                atomicFile.readFully()
            } catch (e: FileNotFoundException) {
                null
            }
        }()

        if (bytes == null) {
            return  null
        }

        return this.objectConverter.fromBytes(bytes)
    }

    @Synchronized
    private fun getMap(): Map<String, Any> {

        return currentMap ?: {
            //check if file exists
            //if so, load file, return it
            loadMap().let {
                this.currentMap = it
                it
            } ?: {
                //if not, create file and save empty map to disk
                //return empty map
                val map = mapOf<String, Any>()
                this.saveMap(map)
                map
            }()

        }()


    }

    @Synchronized
    private fun saveMap(map: Map<String, Any>) {

        //convert map to bytes
        val bytes = this.objectConverter.toBytes(map)

        val file = File(this.filePath)
        file.mkdirs()

        val atomicFile = AtomicFile(file)
        val fos = atomicFile.startWrite()

        fos.write(bytes)
        atomicFile.finishWrite(fos)
        fos.close()

        this.currentMap = map
    }

    //get
    fun get(key: String): Any? {
        return this.getMap().get(key)
    }

    //set
    fun set(key: String, value: Any?) {

        if (value == null) {
            this.saveMap(this.getMap().minus(key))
        }
        else {
            this.saveMap(this.getMap().plus(Pair(key, value)))
        }
    }

    //has
    fun has(key: String): Boolean {
        return this.getMap().containsKey(key)
    }

    //remove
    fun remove(key: String) {
        this.saveMap(this.getMap().minus(key))
    }

    //clear
    fun clear() {
        this.saveMap(mapOf())
    }

}