cordova.define('cordova/plugin_list', function(require, exports, module) {
  module.exports = [
    {
      "id": "cordova-plugin-intelia-loaneligibility.LoanEligibility",
      "file": "plugins/cordova-plugin-intelia-loaneligibility/www/LoanEligibility.js",
      "pluginId": "cordova-plugin-intelia-loaneligibility",
      "clobbers": [
        "window.plugins.loaneligibility"
      ]
    }
  ];
  module.exports.metadata = {
    "cordova-plugin-whitelist": "1.3.4",
    "cordova-plugin-intelia-loaneligibility": "2.0.2"
  };
});