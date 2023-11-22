package com.example.bookcrowded.common

import com.example.bookcrowded.ui.common.RepoResult
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class BaseRealTimeRepository<T : Any>(
    private val collectionName: String,
    private val documentClass: Class<T>
) {
    private val db = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = db.reference.child(collectionName)

    suspend fun getAll(): List<T> {
        return try {
            val dataSnapshot = databaseReference.get().await()
            dataSnapshot.children.mapNotNull { it.getValue(documentClass) }
        } catch (e: Exception) {
            // 예외 처리
            emptyList()
        }
    }

    fun addMessageListener(callback: (List<T>) -> Unit) {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = snapshot.children.mapNotNull { it.getValue(documentClass) }
                callback(messages)
            }

            override fun onCancelled(error: DatabaseError) {
                // 에러 처리
            }
        })
    }


    suspend fun add(item: T): RepoResult<Boolean> = withContext(Dispatchers.IO)  {
        return@withContext try {
            databaseReference.push().setValue(item).await()
            RepoResult.Success(true)
        } catch (e: Exception) {
            // 예외 처리
            RepoResult.Success(false)
        }
    }

    suspend fun update(itemId: String, updatedItem: T): Boolean {
        return try {
            databaseReference.child(itemId).setValue(updatedItem).await()
            true
        } catch (e: Exception) {
            // 예외 처리
            false
        }
    }

    suspend fun delete(itemId: String): Boolean {
        return try {
            databaseReference.child(itemId).removeValue().await()
            true
        } catch (e: Exception) {
            // 예외 처리
            false
        }
    }
}
