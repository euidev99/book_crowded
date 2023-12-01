package com.example.bookcrowded.ui.common

import android.util.Log
import com.example.bookcrowded.ui.dto.SellItem
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


/**
 * 베이스로 혹시나 레포지토리가 다른 내용을 쓸 수도 있으니,
 * 서비스의 분기 / api, db 를 분기하고자 BaseRepository 로 작업
 * 한 콜렉션을 가져오는 레포지토리
 */

class BaseRepository<T: Any>(
    private val collectionName: String,
    private val documentClass: Class<T>) {

    private val db = FirebaseFirestore.getInstance()
    private val collectionReference: CollectionReference = db.collection(collectionName)

    suspend fun addDocument(documentData: T) : RepoResult<Boolean> = withContext(Dispatchers.IO) {
        try {
            //응답 처리 보류..
            collectionReference.add(documentData).await()
            RepoResult.Success(true)
        } catch (e: Exception) {
            RepoResult.Error(e)
        }
    }

    suspend fun getAllDocuments(): RepoResult<List<T>> = withContext(Dispatchers.IO) {
        try {
            Log.d("Repository", "Fetching all documents from: $collectionName")
            val querySnapshot = db.collection(collectionName).get().await()
            val dataList = querySnapshot.toObjects(documentClass)
            RepoResult.Success(dataList)
        } catch (e: Exception) {
            RepoResult.Error(e)
        }
    }

    suspend fun getAllDocumentIds(): RepoResult<List<String>> = withContext(Dispatchers.IO) {
        try {
            val querySnapshot = db.collection(collectionName).get().await()
            val documentIds = querySnapshot.documents.map { it.id }
            RepoResult.Success(documentIds)
        } catch (e: Exception) {
            RepoResult.Error(e)
        }
    }

    suspend fun getDocumentById(documentId: String): RepoResult<T> = withContext(Dispatchers.IO) {
        try {
            val documentSnapshot = collectionReference.document(documentId).get().await()
            if (documentSnapshot.exists()) {
                val documentData = documentSnapshot.toObject(documentClass)
                if (documentData != null) {
                    RepoResult.Success(documentData)
                } else {
                    RepoResult.Error(Exception("Document not found"))
                }
            } else {
                RepoResult.Error(Exception("Document not found"))
            }
        } catch (e: Exception) {
            RepoResult.Error(e)
        }
    }

    suspend fun updateDocument(documentId: String, updates: Map<String, Any>): RepoResult<Unit> = withContext(Dispatchers.IO) {
        try {
            collectionReference.document(documentId).update(updates).await()
            RepoResult.Success(Unit)
        } catch (e: Exception) {
            RepoResult.Error(Exception("Error updating document: $documentId, ${e.message}", e))
        }
    }

    suspend fun deleteDocument(documentId: String): RepoResult<Unit> = withContext(Dispatchers.IO) {
        try {
            collectionReference.document(documentId).delete().await()
            RepoResult.Success(Unit)
        } catch (e: Exception) {
            RepoResult.Error(Exception("Error deleting document: $documentId, ${e.message}", e))
        }
    }

    suspend fun getDocumentsByField(fieldName: String, value: Any): RepoResult<List<T>> = withContext(Dispatchers.IO) {
        try {
            Log.d("Repository", "Fetching documents by field: $fieldName = $value")
            val querySnapshot = collectionReference.whereEqualTo(fieldName, value).get().await()
            val dataList = querySnapshot.toObjects(documentClass)
            RepoResult.Success(dataList)
        } catch (e: Exception) {
            RepoResult.Error(Exception("Error fetching documents by field: $fieldName, ${e.message}", e))
        }
    }

    suspend fun getDocumentIdsByField(fieldName: String, value: Any): RepoResult<List<String>> = withContext(Dispatchers.IO) {
        try {
            val querySnapshot = collectionReference.whereEqualTo(fieldName, value).get().await()
            val documentIds = querySnapshot.documents.map { it.id }
            RepoResult.Success(documentIds)
        } catch (e: Exception) {
            RepoResult.Error(Exception("Error fetching documents by field: $fieldName, ${e.message}", e))
        }
    }

    suspend fun updateField(documentId: String, field: String, newValue: Any): RepoResult<Unit> = withContext(Dispatchers.IO) {
        try {
            collectionReference.document(documentId).update(field, newValue).await()
            RepoResult.Success(Unit)
        } catch (e: Exception) {
            RepoResult.Error(Exception("Error updating field: $field in document: $documentId, ${e.message}", e))
        }
    }

    suspend fun updateFields(documentId: String, updates: Map<String, Any>): RepoResult<Boolean> = withContext(Dispatchers.IO) {
        try {
            collectionReference.document(documentId).update(updates).await()
            RepoResult.Success(true)
        } catch (e: Exception) {
            RepoResult.Error(Exception("Error updating fields in document: $documentId, ${e.message}", e))
        }
    }

    // 쿼리를 생성하는 메서드
    fun getQuery(): Query {
        return collectionReference
    }


    // 쿼리를 사용하여 문서 가져오는 메서드
    suspend fun getDocumentsWithQuery(query: Query): RepoResult<List<T>> = withContext(Dispatchers.IO) {
        try {
            val querySnapshot = query.get().await()
            val dataList = querySnapshot.toObjects(documentClass)
            RepoResult.Success(dataList)
        } catch (e: Exception) {
            RepoResult.Error(Exception("Error fetching documents with query: ${e.message}", e))
        }
    }

}