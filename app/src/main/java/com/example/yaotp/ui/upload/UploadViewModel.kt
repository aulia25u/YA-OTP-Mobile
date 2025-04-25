package com.example.yaotp.ui.upload

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UploadViewModel : ViewModel() {
    private val _selectedImage = MutableLiveData<Uri?>()
    val selectedImage: LiveData<Uri?> = _selectedImage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _uploadResult = MutableLiveData<Result<String>>()
    val uploadResult: LiveData<Result<String>> = _uploadResult

    fun setSelectedImage(uri: Uri?) {
        _selectedImage.value = uri
    }

    // Dummy upload function
    fun uploadImage() {
        viewModelScope.launch {
            _isLoading.value = true
            // Simulate network delay
            delay(2000)
            _uploadResult.value = Result.success("Image uploaded successfully")
            _isLoading.value = false
        }
    }
}
