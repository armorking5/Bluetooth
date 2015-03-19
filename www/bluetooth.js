/*global cordova*/
var isDiscovering;
var Bluetooth = {
	
	start: function(success,failure){
		cordova.exec(success,failure,"Bluetooth", "start", []);
	},
	stop: function(success,failure){
		cordova.exec(success,failure,"Bluetooth", "stop", []);
	},
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
	/*List devices with JSON string passed to success function repeatedly
	STRING: name+" "+macAddress
	*/
    discover: function (success, failure) {
        cordova.exec(success, failure, "Bluetooth", "discover", []);
		isDiscovering=true;
		window.setInterval(function(){
			if(isDiscovering)
				cordova.exec(success,failure,"Bluetooth","list", []);
		},1500);
    },
	
	cancelDiscover: function (success,failure){
		cordova.exec(success,failure,"Bluetooth","stopDiscovering", []);
		isDiscovering=false;
	},
	
	makeDiscoverable: function(time,success,failure){
		cordova.exec(success,failure,"Bluetooth","makeDiscoverable", [time]);
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
module.exports = Bluetooth;
