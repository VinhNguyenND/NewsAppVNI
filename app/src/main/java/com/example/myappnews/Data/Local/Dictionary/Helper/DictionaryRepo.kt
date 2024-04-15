package com.example.myappnews.Data.Local.Dictionary.Helper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myappnews.Data.Local.Dictionary.Entity.DictionaryFolder
import com.example.myappnews.Data.Local.Dictionary.Entity.DictionaryItem

class DictionaryRepo (private val dictionaryDao: DictionaryDAO){
    val readAllDictionaryItem: LiveData<List<DictionaryItem>> = dictionaryDao.getAllDictionaryItem();
    val readAllDictionaryFolder:LiveData<List<DictionaryFolder>> =dictionaryDao.getAllDictionaryFolder()



    suspend fun addDictionaryItem(dictionaryItem: DictionaryItem) {
       dictionaryDao.insertDictionaryItem(dictionaryItem)
    }

    suspend fun addDictionaryFolder(dictionaryFolder: DictionaryFolder){
        dictionaryDao.insertDictionaryFolder(dictionaryFolder);
    }

    suspend fun deleteAllDictionaryItem() {
        dictionaryDao.deleteAllDictionaryItem()
    }

    suspend fun deleteAllDictionaryFolder(){
        dictionaryDao.deleteAllDictionaryFolder()
    }

    suspend fun deleteDictionaryItemById(id:Int){
        dictionaryDao.deleteDictionaryItemById(id);
    }

    suspend fun deleteDictionaryFolder(id:Int){
        dictionaryDao.deleteDictionaryFolderById(id);
    }

    suspend fun upDateTimeDic(id:Int,newTime:Long){
        dictionaryDao.updateDictionaryFolderTime(id, newTime)
    }

     fun getItemNoteByIdFolder(id:Int):LiveData<List<DictionaryItem>>{
      return dictionaryDao.getDictionaryItemsByFolderId(id)
    }
}