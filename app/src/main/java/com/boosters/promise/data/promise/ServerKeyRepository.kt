package com.boosters.promise.data.promise

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ServerKeyRepository @Inject constructor(
    private val database: FirebaseFirestore
) {

    private val serverKeyRef = database.collection(DATABASE_SERVER_KEY_REF_PATH)

    suspend fun getServerKey(): String {
        val task = serverKeyRef.document(DATABASE_SERVER_KEY_DOCUMENT_PATH).get()
        task.await()
        return task.result.data?.values?.joinToString() ?: ""
    }

    companion object {
        private const val DATABASE_SERVER_KEY_REF_PATH = "server_key"
        private const val DATABASE_SERVER_KEY_DOCUMENT_PATH = "key"
    }

}