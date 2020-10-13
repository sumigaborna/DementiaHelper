package hr.ferit.sumigaborna.dementiahelper.app.base.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel(){

    private var compositeDisposable = CompositeDisposable()

    internal fun addDisposable(disposable: Disposable){
        if(compositeDisposable.isDisposed) compositeDisposable = CompositeDisposable()
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}