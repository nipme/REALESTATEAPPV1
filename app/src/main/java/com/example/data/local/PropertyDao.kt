package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {
    @Query("SELECT * FROM properties ORDER BY id DESC")
    fun getAllProperties(): Flow<List<PropertyEntity>>

    @Query("SELECT * FROM properties WHERE isFavorite = 1 ORDER BY id DESC")
    fun getFavoriteProperties(): Flow<List<PropertyEntity>>

    @Query("SELECT * FROM properties WHERE id = :id")
    fun getPropertyById(id: Long): Flow<PropertyEntity?>

    @Query("SELECT COUNT(*) FROM properties")
    suspend fun getPropertyCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProperty(property: PropertyEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(properties: List<PropertyEntity>)

    @Update
    suspend fun updateProperty(property: PropertyEntity)

    @Query("UPDATE properties SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Long, isFavorite: Boolean)

    @Query("DELETE FROM properties WHERE id = :id")
    suspend fun deletePropertyById(id: Long)
}
