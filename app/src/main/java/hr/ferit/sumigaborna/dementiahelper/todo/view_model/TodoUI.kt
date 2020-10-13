package hr.ferit.sumigaborna.dementiahelper.todo.view_model

import com.google.firebase.firestore.DocumentSnapshot

data class TodoUI(val tasksTodo: MutableList<TaskItem> = mutableListOf(),val tasksDone: MutableList<TaskItem> = mutableListOf())

data class TaskItem(val id:Int, var taskText : String, var isTaskDone:Boolean)

fun provideTodoUI(documentSnapshot: DocumentSnapshot):TodoUI{
    return if(documentSnapshot.data != null){
        val todoList = documentSnapshot.data!!.getValue("tasksTodo")
        val doneList = documentSnapshot.data!!.getValue("tasksDone")
        TodoUI(mapTaskItemHashMapToList(todoList),mapTaskItemHashMapToList(doneList))
    }
    else TodoUI()
}

fun mapTaskItemHashMapToList(hashMap: Any): MutableList<TaskItem> {
    val returnList = mutableListOf<TaskItem>()
    (hashMap as List<HashMap<String,Any>>).forEach {
        returnList.add(
            TaskItem(
                id = it.getValue("id").toString().toInt(),
                taskText = it.getValue("taskText").toString(),
                isTaskDone = it.getValue("taskDone").toString().toBoolean()
            )
        )
    }
    return returnList
}
