package io.intelia.loaneligibility.plugins;

import com.google.gson.Gson;
import com.intelia.sdk.eligibility.LoanEligibility;
import com.intelia.sdk.eligibility.impl.QueryImpl;
import com.intelia.sdk.eligibility.models.Eligibility;
import com.intelia.sdk.eligibility.models.SmsDataPoint;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.intelia.loaneligibility.plugins.constants.PluginActions;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static io.intelia.loaneligibility.plugins.constants.PluginParamsKeys.API_KEY;
import static io.intelia.loaneligibility.plugins.constants.PluginParamsKeys.NAME;


/**
 * This class echoes a string called from JavaScript.
 */
public class EligibilityPlugin extends CordovaPlugin {
    private QueryImpl query;

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (action.equals(PluginActions.INITIALIZE)) {
            JSONObject params = args.optJSONObject(0);
            String name = params.optString(NAME);
            String apiKey = params.optString(API_KEY);
            query = LoanEligibility.INSTANCE.init(cordova.getContext(), name, apiKey, params);
            callbackContext.success("true");
            return true;
        } else if (action.equals(PluginActions.TRANSACTIONAL_DATA)) {
            query.smsData()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<SmsDataPoint>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(List<SmsDataPoint> smsDataPoints) {
                            callbackContext.success(new Gson().toJson(smsDataPoints));
                        }

                        @Override
                        public void onError(Throwable e) {
                            callbackContext.error(e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
            return true;
        } else if (action.equals(PluginActions.CALCULATE_ELIGIBILITY)) {
            query.calculateEligibility()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Eligibility>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Eligibility smsDataPoints) {
                            callbackContext.success(new Gson().toJson(smsDataPoints));
                        }

                        @Override
                        public void onError(Throwable e) {
                            callbackContext.error(e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
            return true;
        }
        return false;
    }

}
