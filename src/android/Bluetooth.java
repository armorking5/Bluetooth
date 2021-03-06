package com.megster.cordova;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import org.apache.cordova.*;
import org.json.JSONException;

// kludgy imports to support 2.9 and 3.0 due to package changes
// import org.apache.cordova.CordovaArgs;
// import org.apache.cordova.CordovaPlugin;
// import org.apache.cordova.CallbackContext;
// import org.apache.cordova.PluginResult;
// import org.apache.cordova.LOG;

/**
 * PhoneGap Plugin for Communication over Bluetooth
 */
public class Bluetooth extends CordovaPlugin {

    // actions
	private static final String START = "start";
	private static final String STOP = "stop";
	private static final String DISCOVERING = "discover";
	private static final String LIST = "listDevices";
	private static final String STOP_DISCOVERING = "stopDiscovering";
	private static final String MAKE_DISCOVERABLE = "makeDiscoverable";
    private static final String CONNECT = "connect";
    private static final String CONNECT_INSECURE = "connectInsecure";
    private static final String DISCONNECT = "disconnect";
    private static final String WRITE = "write";
    private static final String AVAILABLE = "available";
    private static final String READ = "read";
    private static final String READ_UNTIL = "readUntil";
    private static final String SUBSCRIBE = "subscribe";
    private static final String UNSUBSCRIBE = "unsubscribe";
    private static final String IS_ENABLED = "isEnabled";
    private static final String IS_CONNECTED = "isConnected";
    private static final String CLEAR = "clear";

    // callbacks
    private CallbackContext connectCallback;
    private CallbackContext dataAvailableCallback;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothService services;

    // Debugging
    private static final String TAG = "BluetoothSerial";
    private static final boolean D = true;

    // Message types sent from the BluetoothService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    StringBuffer buffer = new StringBuffer();
    private String delimiter;

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {

        LOG.d(TAG, "action = " + action);

        if (bluetoothAdapter == null) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }

        if (services == null) {
            services = new BluetoothService(mHandler);
        }

        boolean validAction = true;
        if (action.equals(START)){
			if (!bluetoothAdapter.isEnabled()) {
				bluetoothAdapter.enable();
			}
		} else if (action.equals(STOP)){
			if (bluetoothAdapter.isEnabled()) {
				bluetoothAdapter.disable();
			}
		} else if (action.equals(DISCOVERING)) {
			services.discover();
			
        } else if (action.equals(STOP_DISCOVERING)) {
			services.stopDiscovering();
			
        } else if(action.equals(MAKE_DISCOVERABLE)){
			services.makeDiscoverable(args.getInt(0));
			
		}else if (action.equals(LIST)) {
			String[] devices=services.getDevices();
			callbackContext.success((new JSONArray(devices)).toString());

        } else if (action.equals(CONNECT)) {

            boolean secure = true;
            connect(args, secure, callbackContext);
			
        } else if (action.equals(CONNECT_INSECURE)) {

            // see Android docs about Insecure RFCOMM http://goo.gl/1mFjZY
            boolean secure = false;
            connect(args, false, callbackContext);

        } else if (action.equals(DISCONNECT)) {

            connectCallback = null;
            services.stop();
            callbackContext.success();

        } else if (action.equals(WRITE)) {

            String data = args.getString(0);
            services.write(data.getBytes());
            callbackContext.success();

        } else if (action.equals(AVAILABLE)) {

            callbackContext.success(available());

        } else if (action.equals(READ)) {

            callbackContext.success(read());

        } else if (action.equals(READ_UNTIL)) {

            String interesting = args.getString(0);
            callbackContext.success(readUntil(interesting));

        } else if (action.equals(SUBSCRIBE)) {

            delimiter = args.getString(0);
            dataAvailableCallback = callbackContext;
            
            BluetoothService.start();

            PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
            result.setKeepCallback(true);
            callbackContext.sendPluginResult(result);

        } else if (action.equals(UNSUBSCRIBE)) {

            delimiter = null;
            dataAvailableCallback = null;

            callbackContext.success();

        } else if (action.equals(IS_ENABLED)) {

            if (bluetoothAdapter.isEnabled()) {
                callbackContext.success();                
            } else {
                callbackContext.error("Bluetooth is disabled.");
            }            

        } else if (action.equals(IS_CONNECTED)) {
            
            if (services.getState() == services.STATE_CONNECTED) {
                callbackContext.success();                
            } else {
                callbackContext.error("Not connected.");
            }

        } else if (action.equals(CLEAR)) {

            buffer.setLength(0);
            callbackContext.success();

        } else {

            validAction = false;

        }

        return validAction;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (services != null) {
            services.stop();
        }
    }
        


    private void connect(CordovaArgs args, boolean secure, CallbackContext callbackContext) throws JSONException {
        String macAddress = args.getString(0);
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(macAddress);

        if (device != null) {
            connectCallback = callbackContext;
            services.connect(device, secure);

            PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
            result.setKeepCallback(true);
            callbackContext.sendPluginResult(result);

        } else {
            callbackContext.error("Could not connect to " + macAddress);
        }
    }

    // The Handler that gets information back from the BluetoothService
    // Original code used handler because it was talking to the UI.
    // Consider replacing with normal callbacks
    private final Handler mHandler = new Handler() {

         public void handleMessage(Message msg) {
             switch (msg.what) {
                 case MESSAGE_READ:
                    buffer.append((String)msg.obj);

                    if (dataAvailableCallback != null) {
                        sendDataToSubscriber();
                    }
                    break;
                 case MESSAGE_STATE_CHANGE:

                    if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Log.i(TAG, "BluetoothService.STATE_CONNECTED");
                            notifyConnectionSuccess();
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Log.i(TAG, "BluetoothService.STATE_CONNECTING");
                            break;
                        case BluetoothService.STATE_LISTEN:
                            Log.i(TAG, "BluetoothService.STATE_LISTEN");
                            break;
						case BluetoothService.STATE_DISCOVERING:
                            Log.i(TAG, "BluetoothService.STATE_DISCOVERING");
                            break;
                        case BluetoothService.STATE_NONE:
                            Log.i(TAG, "BluetoothService.STATE_NONE");
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    //  byte[] writeBuf = (byte[]) msg.obj;
                    //  String writeMessage = new String(writeBuf);
                    //  Log.i(TAG, "Wrote: " + writeMessage);
                    break;
                case MESSAGE_DEVICE_NAME:
                    Log.i(TAG, msg.getData().getString(DEVICE_NAME));
                    break;
                case MESSAGE_TOAST:
                    String message = msg.getData().getString(TOAST);
                    notifyConnectionLost(message);
                    break;
             }
         }
    };

    private void notifyConnectionLost(String error) {
        if (connectCallback != null) {
            connectCallback.error(error);
            connectCallback = null;
        }
    }

    private void notifyConnectionSuccess() {
        if (connectCallback != null) {
            PluginResult result = new PluginResult(PluginResult.Status.OK);
            result.setKeepCallback(true);
            connectCallback.sendPluginResult(result);
        }
    }

    private void sendDataToSubscriber() {
        String data = readUntil(delimiter);
        if (data != null && data.length() > 0) {
            PluginResult result = new PluginResult(PluginResult.Status.OK, data);
            result.setKeepCallback(true);
            dataAvailableCallback.sendPluginResult(result);

            sendDataToSubscriber();
        }
    }

    private int available() {
        return buffer.length();
    }

    private String read() {
        int length = buffer.length();
        String data = buffer.substring(0, length);
        buffer.delete(0, length);
        return data;
    }

    private String readUntil(String c) {
        String data = "";
        int index = buffer.indexOf(c, 0);
        if (index > -1) {
            data = buffer.substring(0, index + c.length());
            buffer.delete(0, index + c.length());
        }
        return data;
    }
}
