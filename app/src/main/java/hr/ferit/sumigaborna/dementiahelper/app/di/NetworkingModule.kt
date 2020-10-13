package hr.ferit.sumigaborna.dementiahelper.app.di

import hr.ferit.sumigaborna.dementiahelper.app.common.*
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkingModule = module {

    //CACHE
    val networkCacheInterceptor = Interceptor{
        val response = it.proceed(it.request())
        val cacheControl = CacheControl.Builder()
            .maxAge(10, TimeUnit.MINUTES)
            .build()
        response.newBuilder()
            .header("Cache-Control",cacheControl.toString())
            .build()
    }

    //OKHTTP
    single (named(HTTP_CLIENT)){
        val cacheSize = (10*1024*1024).toLong()
        val myCache = Cache(androidApplication().cacheDir,cacheSize)
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(networkCacheInterceptor)
            .cache(myCache)
            .build()
    }

    //RETROFIT
    single(named(NEWS_RETROFIT)){
        Retrofit.Builder()
            .client(get(named(HTTP_CLIENT)))
            .baseUrl(NEWS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    single(named(WEATHER_RETROFIT)){
        Retrofit.Builder()
            .client(get(named(HTTP_CLIENT)))
            .baseUrl(WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    single(named(QUOTES_RETROFIT)){
        Retrofit.Builder()
            .baseUrl(QUOTES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
}