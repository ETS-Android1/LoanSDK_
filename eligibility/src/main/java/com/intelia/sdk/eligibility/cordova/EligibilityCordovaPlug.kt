package com.intelia.sdk.eligibility.cordova

import com.google.gson.Gson
import com.intelia.sdk.eligibility.LoanEligibility
import com.intelia.sdk.eligibility.cordova.PluginActions.ELIGIBILITY
import com.intelia.sdk.eligibility.cordova.PluginActions.TRANSACTIONAL_DATA
import com.intelia.sdk.eligibility.impl.QueryImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray



open class EligibilityCordovaPlug : CordovaPlugin {
    lateinit var mCordova: CordovaInterface
    lateinit var queryImp: QueryImpl


    @Override
    fun initialize(cordova: CordovaInterface, webView: CordovaWebView) {
        super.initialize(cordova, webView)
        mCordova = cordova
        queryImp = LoanEligibility.init(webView.context)
    }

    @Override
    fun execute(action: String, args: JSONArray, callbackContext: CallbackContext): Boolean {
        when {
            ELIGIBILITY == action -> {
                queryImp.calculateEligibility()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        callbackContext.success(Gson().toJson(it))
                    }

            }
            TRANSACTIONAL_DATA == action -> {
                queryImp.smsData()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        callbackContext.success(Gson().toJson(it))
                    }
            }
        }
        return super.execute(action, args, callbackContext);
    }
}