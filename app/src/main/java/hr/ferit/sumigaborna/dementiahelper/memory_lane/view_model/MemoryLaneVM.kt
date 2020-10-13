package hr.ferit.sumigaborna.dementiahelper.memory_lane.view_model

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import hr.ferit.sumigaborna.dementiahelper.app.base.view_model.BaseViewModel

class MemoryLaneVM(private val firebaseAuth: FirebaseAuth, private val firebaseStorage: FirebaseStorage) : BaseViewModel(){

    val memoryLaneData = MutableLiveData<MemoryLaneUI>()
    val uploadData = MutableLiveData<String>()
    val downloadData = MutableLiveData<String>()

    fun getImages(){
        firebaseStorage
            .reference
            .child(firebaseAuth.currentUser!!.uid)
            .listAll()
            .addOnSuccessListener {
                memoryLaneData.value =
                    provideMemoryLaneUI(
                        it
                    )
            }
            .addOnFailureListener {
                downloadData.value = "Failed to download images"
            }
    }

    fun uploadImage(file:Uri) {
        val lastSegment = file.lastPathSegment!!
        val userId = firebaseAuth.currentUser!!.uid
        firebaseStorage
            .reference
            .child(userId)
            .child(lastSegment)
            .putFile(file)
            .addOnSuccessListener {
                uploadData.value = "Successfully uploaded image"
                getImages()
            }
            .addOnFailureListener{
                uploadData.value = "Failed to uploaded image"
            }
    }

    fun deleteImage(imageId: Int) {
        val temp = memoryLaneData.value!!
        firebaseStorage
            .reference
            .child(firebaseAuth.currentUser!!.uid)
            .child(temp.images.find { it.imageId == imageId }!!.imageURL.path.substringAfterLast("/"))
            .delete()
            .addOnSuccessListener {
                temp.images.remove(temp.images.find { it.imageId == imageId }!!)
                memoryLaneData.value = temp
            }
            .addOnFailureListener {
                uploadData.value = "Failed to delete image"
            }
    }
}