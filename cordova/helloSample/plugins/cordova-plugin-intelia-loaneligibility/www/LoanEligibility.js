function LoanEligibility() {
}

LoanEligibility.install = function () {
  if (!window.plugins) {
    window.plugins = {};
  }

  window.plugins.loaneligibility = new LoanEligibility();
  return window.plugins.loaneligibility;
};

LoanEligibility.prototype.init = function (apiKey,name, options,successCallback, errorCallback) {
    options.apiKey = apiKey
    options.name = name
    cordova.exec(successCallback, errorCallback, "LoanEligibility", "INIT", [options]);
};

LoanEligibility.prototype.data = function (options,successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, "LoanEligibility", "DATA", [options]);
};

LoanEligibility.prototype.calculate = function (options,successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, "LoanEligibility", "ELIGIBILITY", [options]);
};

cordova.addConstructor(LoanEligibility.install);
