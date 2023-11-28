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

    suspend fun getAllWithSubId(firstId: String): List<T> {
        return try {
            val userReference = databaseReference.child(firstId)
            val dataSnapshot = userReference.get().await()
            dataSnapshot.children.mapNotNull { it.getValue(documentClass) }
        } catch (e: Exception) {
            // 예외 처리
            emptyList()
        }
    }

    suspend fun getSubIds(firstId: String): List<String> {
        return try {
            val userIdReference = databaseReference.child(firstId)
            val dataSnapshot = userIdReference.get().await()

            val subIds = mutableListOf<String>()

            dataSnapshot.children.forEach { randomIdSnapshot ->
                subIds.add(randomIdSnapshot.key ?: "")
            }

            subIds
        } catch (e: Exception) {
            // 예외 처리
            emptyList()
        }
    }


    suspend fun getAllWithTwoDepthSubId(firstId: String, secondId: String): List<T> {
        return try {
            val subIdReference = databaseReference.child(firstId).child(secondId)
            val dataSnapshot = subIdReference.get().await()
            dataSnapshot.children.mapNotNull { it.getValue(documentClass) }
        } catch (e: Exception) {
            // 예외 처리
            emptyList()
        }
    }

    suspend fun getAllStartWithId(id: String): List<T> {
        return try {
            val dataSnapshot = databaseReference.get().await()
            dataSnapshot.children
                .filter { it.key?.startsWith(id) == true }
                .mapNotNull { it.getValue(documentClass) }
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

    suspend fun addWithId(item: T, itemId: String): RepoResult<Boolean> = withContext(Dispatchers.IO)  {
        return@withContext try {
//            databaseReference.child(subId).child(itemId).setValue(item).await()

            val userReference = databaseReference.child(itemId)
            userReference.setValue(item).await()

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
