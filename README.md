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

throw Exception("android.Manifest.permission.READ_SMS permission is required")

or it will return `QueryImpl`

### QueryImpl

QueryImpl allows you to perform the following operation with the SDJ :

**calculateEligibility**
