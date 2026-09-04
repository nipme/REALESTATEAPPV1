package com.example.data.remote

import android.content.Context
import android.util.Log
import com.example.data.local.PropertyEntity
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Service to manage cloud persistence and real-time synchronization with Cloud Firestore.
 * Handles properties and user favorites with graceful offline fallback.
 */
class FirestoreService(private val context: Context) {

    private val tag = "FirestoreService"

    private val firestore: FirebaseFirestore? by lazy {
        try {
            if (FirebaseApp.getApps(context).isNotEmpty()) {
                FirebaseFirestore.getInstance()
            } else {
                FirebaseApp.initializeApp(context)?.let {
                    FirebaseFirestore.getInstance()
                }
            }
        } catch (e: Exception) {
            Log.w(tag, "Cloud Firestore initialization note: ${e.message}")
            null
        }
    }

    val isConfigured: Boolean
        get() = firestore != null

    /**
     * Upload or update a property in Cloud Firestore.
     */
    suspend fun uploadProperty(property: PropertyEntity): Boolean {
        val db = firestore ?: return false
        return try {
            val data = hashMapOf(
                "id" to property.id,
                "title" to property.title,
                "description" to property.description,
                "price" to property.price,
                "currency" to property.currency,
                "type" to property.type,
                "purpose" to property.purpose,
                "city" to property.city,
                "neighborhood" to property.neighborhood,
                "area" to property.area,
                "bedrooms" to property.bedrooms,
                "bathrooms" to property.bathrooms,
                "parkingSpaces" to property.parkingSpaces,
                "imageResName" to property.imageResName,
                "isFavorite" to property.isFavorite,
                "agentName" to property.agentName,
                "agentPhone" to property.agentPhone,
                "agentLicense" to property.agentLicense,
                "amenities" to property.amenities,
                "publisherType" to property.publisherType,
                "publisherBadge" to property.publisherBadge,
                "typeSpecificDetails" to property.typeSpecificDetails,
                "createdAt" to property.createdAt,
                "syncedAt" to System.currentTimeMillis()
            )
            db.collection(COLLECTION_PROPERTIES)
                .document(property.id.toString())
                .set(data, SetOptions.merge())
                .await()
            Log.d(tag, "Property ${property.id} successfully synced to Cloud Firestore")
            true
        } catch (e: Exception) {
            Log.e(tag, "Failed to upload property ${property.id} to Firestore: ${e.message}")
            false
        }
    }

    /**
     * Update favorite state in Cloud Firestore.
     */
    suspend fun updateFavoriteStatus(propertyId: Long, isFavorite: Boolean): Boolean {
        val db = firestore ?: return false
        return try {
            val favoriteData = hashMapOf(
                "propertyId" to propertyId,
                "isFavorite" to isFavorite,
                "updatedAt" to System.currentTimeMillis()
            )
            db.collection(COLLECTION_FAVORITES)
                .document(propertyId.toString())
                .set(favoriteData, SetOptions.merge())
                .await()

            db.collection(COLLECTION_PROPERTIES)
                .document(propertyId.toString())
                .set(mapOf("isFavorite" to isFavorite), SetOptions.merge())
                .await()

            Log.d(tag, "Favorite status for $propertyId updated in Cloud Firestore to $isFavorite")
            true
        } catch (e: Exception) {
            Log.e(tag, "Failed to update favorite in Firestore: ${e.message}")
            false
        }
    }

    /**
     * Delete a property from Cloud Firestore.
     */
    suspend fun deleteProperty(propertyId: Long): Boolean {
        val db = firestore ?: return false
        return try {
            db.collection(COLLECTION_PROPERTIES)
                .document(propertyId.toString())
                .delete()
                .await()
            db.collection(COLLECTION_FAVORITES)
                .document(propertyId.toString())
                .delete()
                .await()
            true
        } catch (e: Exception) {
            Log.e(tag, "Failed to delete property from Firestore: ${e.message}")
            false
        }
    }

    /**
     * Fetch all properties currently in Firestore.
     */
    suspend fun fetchAllProperties(): List<PropertyEntity> {
        val db = firestore ?: return emptyList()
        return try {
            val snapshot = db.collection(COLLECTION_PROPERTIES).get().await()
            snapshot.documents.mapNotNull { doc ->
                mapDocumentToProperty(doc.data ?: return@mapNotNull null)
            }
        } catch (e: Exception) {
            Log.e(tag, "Failed to fetch properties from Firestore: ${e.message}")
            emptyList()
        }
    }

    /**
     * Real-time flow of properties from Cloud Firestore.
     */
    fun observeCloudProperties(): Flow<List<PropertyEntity>> = callbackFlow {
        val db = firestore
        if (db == null) {
            close()
            return@callbackFlow
        }

        val listener = db.collection(COLLECTION_PROPERTIES)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(tag, "Firestore snapshot listener error: ${error.message}")
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { doc ->
                        doc.data?.let { mapDocumentToProperty(it) }
                    }
                    trySend(list)
                }
            }

        awaitClose {
            listener.remove()
        }
    }

    private fun mapDocumentToProperty(data: Map<String, Any>): PropertyEntity? {
        return try {
            PropertyEntity(
                id = (data["id"] as? Number)?.toLong() ?: 0L,
                title = data["title"] as? String ?: "",
                description = data["description"] as? String ?: "",
                price = (data["price"] as? Number)?.toLong() ?: 0L,
                currency = data["currency"] as? String ?: "ر.س",
                type = data["type"] as? String ?: "شقة",
                purpose = data["purpose"] as? String ?: "للبيع",
                city = data["city"] as? String ?: "الرياض",
                neighborhood = data["neighborhood"] as? String ?: "",
                area = (data["area"] as? Number)?.toInt() ?: 100,
                bedrooms = (data["bedrooms"] as? Number)?.toInt() ?: 2,
                bathrooms = (data["bathrooms"] as? Number)?.toInt() ?: 2,
                parkingSpaces = (data["parkingSpaces"] as? Number)?.toInt() ?: 1,
                imageResName = data["imageResName"] as? String ?: "apartment_luxury",
                isFavorite = data["isFavorite"] as? Boolean ?: false,
                agentName = data["agentName"] as? String ?: "وسيط معتمد",
                agentPhone = data["agentPhone"] as? String ?: "+966500000000",
                agentLicense = data["agentLicense"] as? String ?: "رخصة فال: 1200000000",
                amenities = data["amenities"] as? String ?: "تكييف مركزي,أمن وحراسة",
                publisherType = data["publisherType"] as? String ?: "broker",
                publisherBadge = data["publisherBadge"] as? String ?: "وسيط معتمد فال",
                typeSpecificDetails = data["typeSpecificDetails"] as? String ?: "",
                createdAt = (data["createdAt"] as? Number)?.toLong() ?: System.currentTimeMillis()
            )
        } catch (e: Exception) {
            Log.e(tag, "Error mapping document to PropertyEntity: ${e.message}")
            null
        }
    }

    companion object {
        const val COLLECTION_PROPERTIES = "properties"
        const val COLLECTION_FAVORITES = "favorites"
    }
}
