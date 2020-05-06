cordova.define("cordova-plugin-hypertrack.HyperTrackWrapper", function(require, exports, module) {
function HyperTrackWrapper() {
}

HyperTrackWrapper.install = function () {
  if (!window.plugins) {
    window.plugins = {};
  }

  window.plugins.hyperTrackWrapper = new HyperTrackWrapper();
  return window.plugins.hyperTrackWrapper;
};

HyperTrackWrapper.prototype.show = function (successCallback, errorCallback) {
  cordova.exec(successCallback, errorCallback, "HyperTrackWrapper", "coolMethod", []);
};

cordova.addConstructor(HyperTrackWrapper.install);

});
