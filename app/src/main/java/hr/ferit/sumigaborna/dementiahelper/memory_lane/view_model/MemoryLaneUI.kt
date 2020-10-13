package hr.ferit.sumigaborna.dementiahelper.memory_lane.view_model

import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference

data class MemoryLaneUI(val images: MutableList<Image>)

data class Image(val imageId: Int, val imageTitle: String, val imageURL: StorageReference)

fun provideMemoryLaneUI(listResult: ListResult): MemoryLaneUI {
    return MemoryLaneUI(
        listResult.items.mapIndexed { index, it ->
            Image(
                index,
                "Image $index",
                it
            )
        } as MutableList<Image>)
}