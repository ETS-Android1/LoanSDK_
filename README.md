# Loan-SDK
Android SDK for Loan Apps

## Gradle Dependency

Add this to your module's `build.gradle` file:

```gradle
dependencies {

  implementation 'io.intelia.sdk:loanEligibility:1.1.0'
}
```

---

## The Basics

**First**, initialize LoanEligibilty SDK:
To initialise LoanEligibility SDK, call : `LoanEligibility.init(this)`
this will either : 

throw an exception when permission is not granted to read sms (so ensure your application have been granted the proper permission before maing this call)

```java
throw Exception("android.Manifest.permission.READ_SMS permission is required")
```

or it will return `QueryImpl`

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
