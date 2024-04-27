package com.example.myappnews.Data.Local.Dictionary.Helper

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myappnews.Data.Local.Dictionary.Entity.DictionaryFolder
import com.example.myappnews.Data.Local.Dictionary.Entity.DictionaryItem


@Dao
interface DictionaryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDictionaryItem(dictionaryItem: DictionaryItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDictionaryFolder(dictionaryFolder: DictionaryFolder)

    @Query("DELETE  FROM DictionaryItem")
    suspend fun deleteAllDictionaryItem()

    @Query("DELETE  FROM DictionaryFolder")
    suspend fun deleteAllDictionaryFolder()


    @Query("SELECT * FROM DictionaryItem")
    fun getAllDictionaryItem(): LiveData<List<DictionaryItem>>

    @Query("SELECT * FROM  DictionaryFolder")
    fun getAllDictionaryFolder(): LiveData<List<DictionaryFolder>>

    @Query("SELECT * FROM DictionaryItem WHERE idDictionaryFolder = :folderId")
    fun getDictionaryItemsByFolderId(folderId: Int): LiveData<List<DictionaryItem>>


    @Query("DELETE FROM DictionaryItem WHERE idDictionaryItem = :id")
    suspend fun deleteDictionaryItemById(id: Int)

    @Query("DELETE FROM DictionaryFolder WHERE idDictionaryFolder = :id")
    suspend fun deleteDictionaryFolderById(id: Int)

    @Query("UPDATE DictionaryFolder SET timeUpdate = :newTime WHERE idDictionaryFolder = :id")
    suspend fun updateDictionaryFolderTime(id: Int, newTime: Long): Int

    @Query("SELECT * FROM DictionaryFolder  ORDER BY nameDictionaryFolder ASC ")
     fun getFolderSortIncrease(): LiveData<List<DictionaryFolder>>

    @Query("SELECT * FROM DictionaryFolder  ORDER BY nameDictionaryFolder DESC")
     fun getFolderSortDecrease(): LiveData<List<DictionaryFolder>>
}