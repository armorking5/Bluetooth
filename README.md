# DEVELOPMENT PHASE

# Bluetooth Plugin for PhoneGap

This plugin enables s
communication over Bluetooth. It was written for communicating between two Android devices. .
The original code is taken by Tom van Enckevort
Android uses Classic Bluetooth

## Supported Platforms

* Android

## Limitations

 * Data sent over the connection is assumed to be Strings

# Installing 

Install with Cordova cli

    $ cordova plugin add https://github.com/don/BluetoothSerial.git
    
This plugin is also available for [PhoneGap Build](https://build.phonegap.com/plugins/23)

# Examples

There are some [sample projects](https://github.com/don/BluetoothSerial/tree/master/examples) included with the plugin.

# API

## Methods

- [bluetoothSerial.connect](#connect)
- [bluetoothSerial.connectInsecure](#connectInsecure)
- [bluetoothSerial.disconnect](#disconnect)
- [bluetoothSerial.write](#write)
- [bluetoothSerial.available](#available)
- [bluetoothSerial.read](#read)
- [bluetoothSerial.readUntil](#readUntil)
- [bluetoothSerial.subscribe](#subscribe)
- [bluetoothSerial.unsubscribe](#unsubscribe)
- [bluetoothSerial.clear](#clear)
- [bluetoothSerial.list](#list)
- [bluetoothSerial.isEnabled](#isenabled)
- [bluetoothSerial.isConnected](#isconnected)

## connect

Connect to a Bluetooth device.

    bluetoothSerial.connect(macAddress_or_uuid, connectSuccess, connectFailure);

### Description

Function `connect` connects to a Bluetooth device.  The callback is long running.  Success will be called when the connection is successful.  Failure is called if the connection fails, or later if the connection disconnects. An error message is passed to the failure callback.

#### Android
For Android, `connect` takes a macAddress of the remote device.  

#### iOS
For iOS, `connect` takes the UUID of the remote device.  Optionally, you can pass an **empty string** and the plugin will connect to the first BLE peripheral.

### Parameters

- __macAddress_or_uuid__: Identifier of the remote device. 
- __connectSuccess__: Success callback function that is invoked when the connection is successful. 
- __connectFailure__: Error callback function, invoked when error occurs or the connection disconnects.

## connectInsecure

Connect insecurely to a Bluetooth device.

    bluetoothSerial.connectInsecure(macAddress, connectSuccess, connectFailure);

### Description

Function `connectInsecure` works like [connect](#connect), but creates an insecure connection to a Bluetooth device.  See the [Android docs](http://goo.gl/1mFjZY) for more information.

#### Android
For Android, `connectInsecure` takes a macAddress of the remote device.  

#### iOS
`connectInsecure` is **not supported** on iOS.

### Parameters

- __macAddress__: Identifier of the remote device. 
- __connectSuccess__: Success callback function that is invoked when the connection is successful. 
- __connectFailure__: Error callback function, invoked when error occurs or the connection disconnects.


## disconnect

Disconnect.

    bluetoothSerial.disconnect([success], [failure]);

### Description

Function `disconnect` disconnects the current connection.

### Parameters

- __success__: Success callback function that is invoked when the connection is successful. [optional]
- __failure__: Error callback function, invoked when error occurs. [optional]

## write

Writes data to the serial port.

    bluetoothSerial.write(data, success, failure);

### Description

Function `write` data to the serial port.  Data must be a String.

### Parameters

- __success__: Success callback function that is invoked when the connection is successful. [optional]
- __failure__: Error callback function, invoked when error occurs. [optional]

## available

Gets the number of bytes of data available.

    bluetoothSerial.available(success, failure);

### Description

Function `available` gets the number of bytes of data available.  The bytes are passed as a parameter to the success callback.

### Parameters

- __success__: Success callback function that is invoked when the connection is successful. [optional]
- __failure__: Error callback function, invoked when error occurs. [optional]

### Quick Example

    bluetoothSerial.available(function (numBytes) {
        console.log("There are " + numBytes + " available to read.");
    }, failure);

## read

Reads data from the buffer.

    bluetoothSerial.read(success, failure);

### Description

Function `read` reads the data from the buffer. The data is passed to the success callback as a String.  Calling `read` when no data is available will pass an empty String to the callback.

### Parameters

- __success__: Success callback function that is invoked with the number of bytes available to be read.
- __failure__: Error callback function, invoked when error occurs. [optional]

### Quick Example

    bluetoothSerial.read(function (data) {
        console.log(data);
    }, failure);

## readUntil

Reads data from the buffer until it reaches a delimiter.

    bluetoothSerial.readUntil('\n', success, failure);

### Description

Function `readUntil` reads the data from the buffer until it reaches a delimiter.  The data is passed to the success callback as a String.  If the buffer does not contain the delimiter, an empty String is passed to the callback. Calling `read` when no data is available will pass an empty String to the callback.

### Parameters

- __delimiter__: delimiter
- __success__: Success callback function that is invoked with the data.
- __failure__: Error callback function, invoked when error occurs. [optional]

### Quick Example

    bluetoothSerial.readUntil('\n', function (data) {
        console.log(data);
    }, failure);
    
## subscribe 

Subscribe to be notified when data is received.

    bluetoothSerial.subscribe('\n', success, failure);

### Description

Function `subscribe` registers a callback that is called when data is received.  A delimiter must be specified.  The callback is called with the data as soon as the delimiter string is read.  The callback is a long running callback and will exist until `unsubscribe` is called.

### Parameters

- __delimiter__: delimiter
- __success__: Success callback function that is invoked with the data.
- __failure__: Error callback function, invoked when error occurs. [optional]

### Quick Example

    // the success callback is called whenever data is received
    bluetoothSerial.subscribe('\n', function (data) {
        console.log(data);
    }, failure);

## unsubscribe

Unsubscribe from a subscription.

    bluetoothSerial.unsubscribe(success, failure);

### Description

Function `unsubscribe` removes any notification added by `subscribe` and kills the callback.

### Parameters

- __success__: Success callback function that is invoked when the connection is successful. [optional]
- __failure__: Error callback function, invoked when error occurs. [optional]

### Quick Example

    bluetoothSerial.unsubscribe();

## clear

Clears data in the buffer.

    bluetoothSerial.clear(success, failure);

### Description

Function `clear` removes any data from the receive buffer.

### Parameters

- __success__: Success callback function that is invoked when the connection is successful. [optional]
- __failure__: Error callback function, invoked when error occurs. [optional]

## list

Lists bonded devices

    bluetoothSerial.list(success, failure);
    
### Description

#### Android

Function `list` lists the paired Bluetooth devices.  The success callback is called with a list of objects.

Example list passed to success callback.  See [BluetoothDevice](http://developer.android.com/reference/android/bluetooth/BluetoothDevice.html#getName\(\)) and [BluetoothClass#getDeviceClass](http://developer.android.com/reference/android/bluetooth/BluetoothClass.html#getDeviceClass\(\)).

    [{
        "class": 276,
        "address": "10:BF:48:CB:00:00",
        "name": "Nexus 7"
    }, {
        "class": 7936,
        "address": "00:06:66:4D:00:00",
        "name": "RN42"
    }]
    
#### iOS

Function `list` lists the discovered Bluetooth Low Energy peripheral.  The success callback is called with a list of objects.

Example list passed to success callback for iOS.

    [{
        "uuid": "CC410A23-2865-F03E-FC6A-4C17E858E11E",
        "name": "Biscuit"
    }]

    
### Parameters

- __success__: Success callback function that is invoked with a list of bonded devices.
- __failure__: Error callback function, invoked when error occurs. [optional]

### Quick Example

    bluetoothSerial.list(function(devices) {
        devices.forEach(function(device) {
            console.log(device.address);
        })
    }, failure);
    
## isConnected

Reports the connection status. 

    bluetoothSerial.isConnected(success, failure);

### Description

Function `isConnected` calls the success callback when connected to a peer and the failure callback when *not* connected.

### Parameters

- __success__: Success callback function, invoked when device connected.
- __failure__: Error callback function, invoked when device is NOT connected.

### Quick Example

    bluetoothSerial.isConnected(
        function() { 
            console.log("Bluetooth is connected");
        },
        function() { 
            console.log("Bluetooth is *not* connected");
        }
    ); 
    
## isEnabled

Reports if bluetooth is enabled.

    bluetoothSerial.isEnabled(success, failure);

### Description

Function `isEnabled` calls the success callback when bluetooth is enabled and the failure callback when bluetooth is *not* enabled.

### Parameters

- __success__: Success callback function, invoked when Bluetooth is enabled.
- __failure__: Error callback function, invoked when Bluetooth is NOT enabled.

### Quick Example

    bluetoothSerial.isEnabled(
        function() { 
            console.log("Bluetooth is enabled");
        },
        function() { 
            console.log("Bluetooth is *not* enabled");
        }
    );    

# Misc

## Where does this work? 

### Android

Current development is done with Cordova 3.0.0 on Android 4.x. Theoretically this code runs on PhoneGap 2.9 and greater.  It should support Android-10 (2.3.2) and greater, but I only test with Android 4.x.

Development Devices include
 * Samsung Galaxy Tab 10.1 (GT-P7510) with Android 4.0.4 (see [Issue #8](https://github.com/don/BluetoothSerial/issues/8))
 * Google Nexus S with Android 4.1.2
 * Nexus 4 with Android 4.2.2
 * Samsung Galaxy S4 with Android 4.3

On the Arduino side I test with [Sparkfun Mate Silver](https://www.sparkfun.com/products/10393) and the [Seeed Studio Bluetooth Shield](http://www.seeedstudio.com/depot/bluetooth-shield-p-866.html?cPath=19_21). The code should be generic and work with most hardware.
    
### iOS

**NOTE: Currently iOS only works with RedBear Labs BLE Mini Hardware**

This plugin was developed with Cordova 3.0 using iOS 6.x on an iPad 4 connecting to a [RedBearLab BLEMini](http://redbearlab.com/blemini).

Ensure that you have update the BLE Mini firmware to at least [Biscuit-UART_20130313.bin](https://github.com/RedBearLab/Biscuit/tree/master/release).
    
## Props

### Android

Most of the Bluetooth implementation was borrows from the Bluetooth Chat example in the Android SDK.

### iOS

The iOS code uses RedBearLab's [BLE_Framework](https://github.com/RedBearLab/Release/tree/master/BLEMini).

### API

The API for available, read, readUntil was influenced by the [BtSerial Library for Processing for Arduino](https://github.com/arduino/BtSerial)

## Wrong Bluetooth Plugin?

If you don't need **serial** over Bluetooth, try the [PhoneGap Bluetooth Plugin for Android](https://github.com/phonegap/phonegap-plugins/tree/master/Android/Bluetooth)

## What format should the Mac Address be in?
An example a properly formatted mac address is ``AA:BB:CC:DD:EE:FF``

## Feedback
    
Try the code. If you find an problem or missing feature, file an issue or create a pull request.


[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/armorking5/bluetooth/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

