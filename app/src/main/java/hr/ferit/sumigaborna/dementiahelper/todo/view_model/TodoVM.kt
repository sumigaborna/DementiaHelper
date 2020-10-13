package hr.ferit.sumigaborna.dementiahelper.todo.view_model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.sumigaborna.dementiahelper.R
import hr.ferit.sumigaborna.dementiahelper.app.base.view_model.BaseViewModel
import hr.ferit.sumigaborna.dementiahelper.app.common.EMPTY_STRING
import hr.ferit.sumigaborna.dementiahelper.app.common.TODO_LIST

class TodoVM(firebaseAuth: FirebaseAuth,private val firebaseFirestore: FirebaseFirestore):BaseViewModel(){

    val todoData = MutableLiveData<TodoUI>()
    private val userEmail = firebaseAuth.currentUser!!.email.toString()

    fun getTasks(selectedItemId: Int){
        val collectionName = getCollectionNameBySelectedMenuItem(selectedItemId)
        firebaseFirestore.collection("$collectionName-$userEmail")
            .document(TODO_LIST)
            .get()
            .addOnSuccessListener {
                todoData.value = provideTodoUI(it)
            }
            .addOnFailureListener {
                Log.d("ragetag","Failed to get Tasks for user in TodoVM")
            }
    }

    fun editNote(selectedItemId: Int, id: Int, taskDone: Boolean, newTaskText: String){
        val temp = todoData.value!!
        val collectionName = getCollectionNameBySelectedMenuItem(selectedItemId)
        //update task text
        if(taskDone) temp.tasksDone.find { it.id == id }!!.taskText = newTaskText
        else temp.tasksTodo.find { it.id == id }!!.taskText = newTaskText
        //update firebase
        firebaseFirestore.collection("$collectionName-$userEmail")
            .document(TODO_LIST)
            .set(temp)
            .addOnSuccessListener {
                todoData.value = temp
            }
            .addOnFailureListener {
                Log.d("ragetag","Failed to edit task in TodoVM")
            }
    }

    fun addNewTask(selectedItemId: Int,taskText: String) {
        if(taskText.isNotBlank()){
            val temp = todoData.value!!
            temp.tasksTodo.add(TaskItem(getTodoListSize(),taskText,false))
            val collectionName = getCollectionNameBySelectedMenuItem(selectedItemId)
            firebaseFirestore.collection("$collectionName-$userEmail")
                .document(TODO_LIST)
                .set(temp)
                .addOnSuccessListener {
                    todoData.value = temp
                }
                .addOnFailureListener {
                    Log.d("ragetag","Failed to add new task in TodoVM")
                }
        }
    }

    private fun getTodoListSize(): Int = todoData.value!!.tasksDone.size+todoData.value!!.tasksTodo.size

    fun markTaskDone(selectedItemId: Int,id: Int) {
        val temp = todoData.value!!
        val task = temp.tasksTodo.find { it.id==id }!!
        task.isTaskDone = true
        temp.tasksTodo.remove(task)
        temp.tasksDone.add(task)
        val collectionName = getCollectionNameBySelectedMenuItem(selectedItemId)
        firebaseFirestore.collection("$collectionName-$userEmail")
            .document(TODO_LIST)
            .set(temp)
            .addOnSuccessListener {
                todoData.value = temp
            }
            .addOnFailureListener {
                Log.d("ragetag","Failed to mark task done at TodoVM")
            }
    }

    fun markTaskNotDone(selectedItemId: Int,id: Int) {
        val temp = todoData.value!!
        val task = temp.tasksDone.find { it.id==id }!!
        task.isTaskDone = false
        temp.tasksDone.remove(task)
        temp.tasksTodo.add(task)
        val collectionName = getCollectionNameBySelectedMenuItem(selectedItemId)
        firebaseFirestore.collection("$collectionName-$userEmail")
            .document(TODO_LIST)
            .set(temp)
            .addOnSuccessListener {
                todoData.value = temp
            }
            .addOnFailureListener {
                Log.d("ragetag","Failed to mark task not done at TodoVM")
            }
    }

    fun deleteTask(selectedItemId: Int,isTaskDone:Boolean,id: Int) {
        val temp = todoData.value!!
        //remove task from list
        if(isTaskDone) temp.tasksDone.remove(temp.tasksDone.find { it.id==id }!!)
        else temp.tasksTodo.remove(temp.tasksTodo.find { it.id==id }!!)
        //update in firebase
        val collectionName = getCollectionNameBySelectedMenuItem(selectedItemId)
        firebaseFirestore.collection("$collectionName-$userEmail")
            .document(TODO_LIST)
            .set(temp)
            .addOnSuccessListener {
                todoData.value = temp
            }
            .addOnFailureListener {
                Log.d("ragetag","Failed to mark task done at TodoVM")
            }
    }

    private fun getCollectionNameBySelectedMenuItem(selectedMenuItem:Int): String {
        var collectionName = EMPTY_STRING
        when (selectedMenuItem) {
            R.id.patientNavButton -> collectionName = "todo-patient"
            R.id.carerNavButton -> collectionName = "todo-carer"
            else -> Log.d("ragetag","Selected index out of range: TodoFragment->TodoVM")
        }
        return collectionName
    }
}