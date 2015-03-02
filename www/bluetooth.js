/*global cordova*/
module.exports = {

    connect: function (macAddress, success, failure) {
        cordova.exec(success, failure, "Bluetooth", "connect", [macAddress]);
    },

    // Android only - see http://goo.gl/1mFjZY
    connectInsecure: function (macAddress, success, failure) {
        cordova.exec(success, failure, "Bluetooth", "connectInsecure", [macAddress]);
    },

    disconnect: function (success, failure) {
        cordova.exec(success, failure, "Bluetooth", "disconnect", []);
    },
    discover: function (discoverid,success, failure) {
        cordova.exec(success, failure, "Bluetooth", "discover", []);
		window.setInterval(function(discoverid){
			
		},1500);
    },

    isEnabled: function (success, failure) {
        cordova.exec(success, failure, "Bluetooth", "isEnabled", []);
    },

    isConnected: function (success, failure) {
        cordova.exec(success, failure, "Bluetooth", "isConnected", []);
    },

    // the number of bytes of data available to read is passed to the success function
    available: function (success, failure) {
        cordova.exec(success, failure, "Bluetooth", "available", []);
    },

    // read all the data in the buffer
    read: function (success, failure) {
        cordova.exec(success, failure, "Bluetooth", "read", []);
    },

    // reads the data in the buffer up to and including the delimiter
    readUntil: function (delimiter, success, failure) {
        cordova.exec(success, failure, "Bluetooth", "readUntil", [delimiter]);
    },

    // writes data to the other device - data must be a string
    write: function (data, success, failure) {
        cordova.exec(success, failure, "Bluetooth", "write", [data]);
    },

    // calls the success callback when new data is available
    subscribe: function (delimiter, success, failure) {
        cordova.exec(success, failure, "Bluetooth", "subscribe", [delimiter]);
    },

    // removes data subscription
    unsubscribe: function (success, failure) {
        cordova.exec(success, failure, "Bluetooth", "unsubscribe", []);
    },

    // clears the data buffer
    clear: function (success, failure) {
        cordova.exec(success, failure, "Bluetooth", "clear", []);
    }

};
