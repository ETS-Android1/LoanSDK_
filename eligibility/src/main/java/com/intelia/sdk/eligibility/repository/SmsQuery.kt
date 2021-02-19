package com.intelia.sdk.eligibility.repository

import android.content.Context
import android.provider.Telephony
import com.intelia.sdk.eligibility.models.FilterParams
import com.intelia.sdk.eligibility.models.Sms
import com.intelia.sdk.eligibility.models.SmsDataPoint
import io.reactivex.Observable
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.LinkedHashMap

open class SmsQuery {

    fun smsSearch(context: Context, maxSms: Int): Observable<MutableList<SmsDataPoint>> {
        return Observable.create<MutableList<SmsDataPoint>> { emitter ->
            val cr = context.contentResolver

            val c = cr.query(Telephony.Sms.CONTENT_URI, null, null, null, null)
            if (c != null) {
                val smsList = LinkedHashMap<String, MutableList<Sms>>()
                var count = 0
                while (c.moveToNext()) {
                    if (count == maxSms)
                        break
                    val smsDate = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.DATE))
                    val number = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                    val body = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.BODY))
                    val dateFormat = Date(smsDate.toLong())
                    val type =
                        Integer.parseInt(c.getString(c.getColumnIndexOrThrow(Telephony.Sms.TYPE)))
                    if (type == Telephony.Sms.MESSAGE_TYPE_INBOX) {
//                        if (number.toLowerCase().startsWith("gtbankrvsl")) {
//                            Log.e("body", body.replace("\n", " ").toLowerCase())
//                        }
                        FilterParams.query.forEach outter@{ dataPointCategory ->
                            dataPointCategory.contentFilter.forEach inner@{
                                val p = Pattern.compile(it)
                                val m = p.matcher(body.replace("\n", " ").toLowerCase())
                                if (m.find() && number.first().isLetter()) {
                                    count++
                                    if (smsList.containsKey(dataPointCategory.category))
                                        smsList[dataPointCategory.category]?.add(
                                            Sms(
                                                number,
                                                body,
                                                dateFormat
                                            )
                                        )
                                    else {
                                        smsList[dataPointCategory.category] = mutableListOf(
                                            Sms(
                                                number,
                                                body,
                                                dateFormat
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                }
                c.close()
                emitter.onNext(smsList.map {
                    SmsDataPoint(
                        it.key,
                        it.value
                    )
                }.toMutableList())
            }
            emitter.onComplete()
        }
    }
}