package hr.ferit.sumigaborna.dementiahelper.home.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.airbnb.epoxy.TypedEpoxyController
import hr.ferit.sumigaborna.dementiahelper.R
import hr.ferit.sumigaborna.dementiahelper.app.base.ui.BaseFragment
import hr.ferit.sumigaborna.dementiahelper.app.common.showProgressBar
import hr.ferit.sumigaborna.dementiahelper.home.ui.view_holder.articleHolder
import hr.ferit.sumigaborna.dementiahelper.home.view_model.HomeUI
import hr.ferit.sumigaborna.dementiahelper.home.view_model.HomeVM
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class HomeFragment : BaseFragment(),HomeListener{

    override fun getLayout(): Int = R.layout.fragment_home
    private val viewModel by viewModel<HomeVM>()
    private val epoxyController by inject<HomeItemController>{ parametersOf(this)}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.homeData.observe(viewLifecycleOwner, Observer {
            if(it.status!="failed"){
                epoxyController.setData(it)
                tvError.visibility = View.GONE
            }
            else tvError.visibility = View.VISIBLE
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvHome.setController(epoxyController)
        getNewsAndDispose()
        swipeToRefresh.setOnRefreshListener {
            getNewsAndDispose()
        }
    }

    override fun openArticle(articleURL: String) {
        val uri = Uri.parse(articleURL)
        val intent = Intent(Intent.ACTION_VIEW,uri)
        startActivity(intent)
    }

    private fun getNewsAndDispose(){
        showProgressBar(pbHome)
        return viewModel.addDisposable(
            viewModel.getNews()
                .doOnSuccess {
                    swipeToRefresh?.isRefreshing = false
                    //TODO: OVDJE CRASHA KAD PREBRZO KLIKAŠ PO NAVIGATIONU
                    pbHome.visibility = View.GONE
                }
                .doOnError {
                    swipeToRefresh?.isRefreshing = false
                    pbHome.visibility = View.GONE
                }.subscribe()
        )
    }
}

interface HomeListener{
    fun openArticle(articleURL : String)
}

class HomeItemController (private val homeListener: HomeListener): TypedEpoxyController<HomeUI>(){
    override fun buildModels(data: HomeUI) {
        data.newsList.forEach {
            articleHolder {
                id(it.articleId)
                title(it.title)
                description(it.description)
                publishedAt(it.publishedAt)
                urlImage(it.urlToImage)
                clickListener { model, parentView, clickedView, position ->
                    homeListener.openArticle(it.url)
                }
            }
        }
    }
}