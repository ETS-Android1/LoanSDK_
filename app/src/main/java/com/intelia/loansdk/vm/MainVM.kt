package com.intelia.loansdk.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intelia.sdk.eligibility.models.Eligibility
import com.intelia.sdk.eligibility.models.SmsDataPoint
import com.intelia.sdk.eligibility.usecase.QueryUsecase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


open class MainVM(private val usecase: QueryUsecase) : ViewModel() {


    private val compositeDisposable = CompositeDisposable()
    val smsDataPoint = MutableLiveData<MutableList<SmsDataPoint>>()
    val eligibility = MutableLiveData<Eligibility>()
    fun querySms() {
        compositeDisposable.addAll(
            usecase.smsData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    smsDataPoint.postValue(it)
                }
        )
    }

    fun calculateEligibility() {
        compositeDisposable.addAll(
            usecase.calculateEligibility()
                .doOnError {
                    eligibility.postValue(null)
                }
                .onErrorResumeNext(io.reactivex.Observable.empty())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.e("log", it.toString())
                    eligibility.postValue(it)
                }
        )
    }


    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }


}