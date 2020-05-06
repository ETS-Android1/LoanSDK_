[ ![Download](https://api.bintray.com/packages/kingsmentor/maven/eligibility/images/download.svg) ](https://bintray.com/kingsmentor/maven/eligibility/_latestVersion)

# Loan-SDK
Android SDK for Loan Apps

## Gradle Dependency

Add this to your module's `build.gradle` file:

```gradle
dependencies {

  implementation 'io.intelia.anyi:eligibility:2.0.1'
}
```

---

## The Basics

**Kindly Note**, initialize LoanEligibilty SDK:

To initialise LoanEligibility SDK, call : `LoanEligibility.init(context, YOUR_API_KEY)`

this will either : 

`throw an exception` when permission is not granted to read sms (so ensure your application have been granted the proper permission before maing this call)

```java
throw Exception("android.Manifest.permission.READ_SMS permission is required")
```

or  `return QueryImpl`

### QueryImpl

QueryImpl allows you to perform the following operation with the SDK :


* `fun calculateEligibility(): Observable<Eligibility>`

this calculates loan eligibility for user. 

```kotlin
    fun calculateEligibility() {
        queryImpl.calculateEligibility()
            .doOnError {
                eligibility.postValue(null)
            }
            .onErrorResumeNext(io.reactivex.Observable.empty())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                eligibility.postValue(it)
            }
    }
```

Eligibility contains information duduced from User information contained in the device :

```kotlin
open class Eligibility(
    val eligible: Boolean = false,
    @SerializedName("expense-rate") val expense_rate: Double = 0.0,
    @SerializedName("hold-money-rate") val hold_money_rate: Double = 0.0,
    @SerializedName("risk-profile") val risk_profile: Double = 0.0,
    val status: Int = 0
)
```

* `fun smsData(): Observable<MutableList<SmsDataPoint>>`

this fetches finiancial related sms that can be used to make prediction about user's finiancial habit

```kotlin
usecase.smsData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                smsDataPoint.postValue(it)
            }
```
this returns `data class SmsDataPoint(val category: String, var sms: MutableList<Sms>)`

`category` signifies the category of this transaction.

` var sms: MutableList<Sms>` shows sms under this category

* `fun relevantApp(): RelevantApps`

this returns list of finaincial apps activiely being used by the users. 


* `fun relevantApp(): RelevantApps`

this returns list of finaincial apps activiely being used by the users. 

* `fun isRelevantSms(sms: String): Boolean`

verifies if the string content is transactional. This is useful when intercepting user sms. New transactional sms can triger a new check for user eligibility. 

```kotlin 
  if(queryImpl.isRelevantSms(sms))
      calculateEligibility()
 ```

* `fun isRelevantApp(packageName: String): Boolean`

verifies if a packagename should affect user's eligibility calculation. This largely depends on if the user activiely use the application. 
Otherwise, it returns false immediately. This is particularly useful when your application listens to new app installation. 

```kotlin 
  if(queryImpl.isRelevantApp(packageName))
      calculateEligibility()
 ```
 
### Testing the SDK

You will find a basic text application [here](https://github.com/intelia/Loan-SDK/tree/master/app) .

Kindly go through the implimentation, build and run on your emulator or physical devices.

The test app test the basic functionality of the sdk. So, don't restrict your usage or implementation to what is found therein. 

## Using in Cordova 

```kotlin
open class EligibilityCordovaPlug : CordovaPlugin {
    lateinit var mCordova: CordovaInterface
    lateinit var queryImp: QueryImpl
    lateinit var webView : CordovaWebView


    @Override
    fun initialize(cordova: CordovaInterface, webView: CordovaWebView) {
        super.initialize(cordova, webView)
        mCordova = cordova
        queryImp = LoanEligibility.init(App.appContext,"COMPANY_NAME","SAMPLE_KEY",
            JSONObject()
        ) 
    }

    @Override
    fun execute(action: String, args: JSONArray, callbackContext: CallbackContext): Boolean {
    // val packageName = args.optJSONObject(0).opt("pakcageName")
    // val sms = args.optJSONObject(0).opt("sms")
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
```

You can extend `EligibilityCordovaPlug` as CordovaPlugin as it already extend Cordovaplugin or build a class similar to this.

Extending `EligibilityCordovaPlug` means you will interface with this plugin from javascript with the following actions:

```
object PluginActions {
    const val ELIGIBILITY: String = "ELIGIBILITY" // to perform eligibility check
    const val TRANSACTIONAL_DATA: String = "TRANSACTIONAL_DATA" //  to return transactional sms on user device
    const val IS_TRANSACTIONAL_SMS: String = "IS_TRANSACTIONAL_SMS" // to check if sms or string content is regcognized as transactional by the engine
    const val IS_RELEVANT_APP: String = "IS_RELEVANT_APP" // to check if the application qualifies to be assessed for eligility check
    const val RELEVANT_APPS: String = "RELEVANT_APPS" // check if device actively uses relevant application that can affect eligibility check.
}
```

Don not forget to pass the following in config via javascript when using this plugin:

`api_key` - **at all times**, 

`packagename` - when using **IS_RELEVANT_APP**

`sms` - when using **IS_TRANSACTIONAL_SMS**

```javascript
cordova.exec(function(winParam) {},
    function(error) {},"Plugin_Name","ELIGIBILITY",[{apikey:"YOUR_API_KEY_HERE","sms": "CONTENT_HERE", "packagename": "PACKAGENAME_HERE"}]);
```
