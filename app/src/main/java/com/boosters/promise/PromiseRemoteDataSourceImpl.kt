package com.boosters.promise

import android.util.Log
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PromiseRemoteDataSourceImpl @Inject constructor(
    private val database: FirebaseDatabase
) : PromiseRemoteDataSource {

    private val promiseRef = database.getReference(DATABASE_PROMISE_REF_PATH)

    init {
        promiseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = (dataSnapshot.value ?: return) as HashMap<String, Promise>
                value.keys.forEach { key ->
                    Log.d("MainActivity", "${value[key]}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override suspend fun addPromise(promise: Promise): Promise? {
        var key: String? = promise.id
        var result = promise
        if (key == "") {
            key = promiseRef.push().key ?: ""
            result = promise.copy(id = key)
        }
        return withContext(Dispatchers.IO) {
            key?.let {
                try {
                    val task = promiseRef.child(it).setValue(result)
                    task.await()
                    if (!task.isSuccessful) key = null
                } catch (e: DatabaseException) {
                    return@withContext null
                }
            }
            result
        }
    }

    override suspend fun removePromise(promise: Promise): Boolean {
        return withContext(Dispatchers.IO) {
            val isSuccessful = try {
                val task = promise.id.let { promiseRef.child(it).removeValue() }
                task.await()
                Log.d("TAG","${task.result}")
                task.isSuccessful
            } catch (e: DatabaseException) {
                false
            }
            isSuccessful
        }
    }

    companion object {
        private const val DATABASE_PROMISE_REF_PATH = "promise"
    }

}