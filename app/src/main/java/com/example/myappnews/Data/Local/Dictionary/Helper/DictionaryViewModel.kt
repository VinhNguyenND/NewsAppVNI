package com.example.myappnews.Data.Local.Dictionary.Helper

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.myappnews.Data.Local.Dictionary.Entity.DictionaryFolder
import com.example.myappnews.Data.Local.Dictionary.Entity.DictionaryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DictionaryViewModel(application: Application) : AndroidViewModel(application) {

    val readAllDicItem: LiveData<List<DictionaryItem>>
    val readAllDicFolder: LiveData<List<DictionaryFolder>>
    private val repository: DictionaryRepo

    init {
        val articleDao = DictionaryHelper.getDataBase(application).dictionaryDao()
        repository = DictionaryRepo(articleDao)
        readAllDicItem = repository.readAllDictionaryItem
        readAllDicFolder = repository.readAllDictionaryFolder
    }

    fun deleteAllDic() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllDictionaryItem()
        }
    }

    fun deleteAllFolder() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllDictionaryFolder()
        }
    }

    fun deleteDictionaryItemById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteDictionaryItemById(id)
        }
    }

    fun deleteDictionaryFolderById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteDictionaryFolder(id)
        }
    }

    fun insertDictionaryItem(dictionaryItem: DictionaryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addDictionaryItem(dictionaryItem)
        }

    }

    fun insertDictionaryFolder(dictionaryFolder: DictionaryFolder) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addDictionaryFolder(dictionaryFolder)
        }

    }

    fun getItemNoteByIdFolder(id: Int): LiveData<List<DictionaryItem>> {
        return repository.getItemNoteByIdFolder(id);
    }

    fun updateDictionaryFolderTime(id: Int, newTime: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.upDateTimeDic(id, newTime)
        }
    }

    fun getAllFolder(): LiveData<List<DictionaryFolder>> {
        return repository.readAllDictionaryFolder
    }

    fun getALlDic(): LiveData<List<DictionaryItem>> {
        return repository.readAllDictionaryItem
    }

    fun getFolderSortIncrease(): LiveData<List<DictionaryFolder>> {
        return repository.getFolderSortIncrease()
    }

    fun getFolderSortDecrease(): LiveData<List<DictionaryFolder>> {
        return  repository.getFolderSortDecrease()
    }
}