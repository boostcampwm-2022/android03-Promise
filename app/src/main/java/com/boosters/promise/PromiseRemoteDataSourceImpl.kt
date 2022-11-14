package com.boosters.promise

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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

    override suspend fun addPromise(promise: Promise) {
        promiseRef.child(promise.id).setValue(promise)
    }

    override suspend fun removePromise(promise: Promise) {
        promiseRef.child(promise.id).removeValue()
    }

    companion object {
        private const val DATABASE_PROMISE_REF_PATH = "promise"
    }

}