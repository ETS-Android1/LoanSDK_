package com.intelia.loansdk;

import android.content.Context;

import com.intelia.sdk.eligibility.LoanEligibility;
import com.intelia.sdk.eligibility.models.SmsDataPoint;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class Sampple {

    public void Sam(Context context) {
        LoanEligibility.INSTANCE.init(context, "", "", new JSONObject(), 50)
                .smsData().subscribe(new Observer<List<SmsDataPoint>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<SmsDataPoint> smsDataPoints) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
