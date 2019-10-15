package locationetworkplugincordova;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class LocationNetwork extends CordovaPlugin {

    private static final int REQUEST_CODE_ENABLE_PERMISSION = 55433;
    CallbackContext newcallbackContext;
    private static final String TAG="LocationNetwork";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        newcallbackContext=callbackContext;
        if (action.equals("coolMethod")) {
            //String message = args.getString(0);
            this.cordova.getActivity().runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    try{
                        Log.d(TAG,"Into getThreadPool");
                        coolMethod(newcallbackContext);
                    }catch(Exception e){
                        newcallbackContext.error(e.getMessage());
                        Log.d(TAG,"Error:"+e.getMessage());
                    }
                    
                }
            });
            return true;
        }
        return false;
    }

    private void coolMethod(CallbackContext callbackContext) {
        Log.e(TAG," Init coolMethod");
        try{
            checkPermission();
            Location location = getLocationNetwork();
            String response = "{'lat':'"+location.getLatitude()+"','lng':'"+location.getLongitude()+"'}";
            Log.e(TAG,"Response Location:"+response);
            callbackContext.success(response);
            //PluginResult pluginResult = new PluginResult(PluginResult.Status.OK,response);
            //callbackContext.sendPluginResult(pluginResult);
        }catch (Exception e){
            Log.e(TAG,"Fail Location:"+e.getMessage());
            callbackContext.error(e.getMessage());
            //PluginResult pluginResult = new PluginResult(PluginResult.Status.ERROR,e.getMessage());
            //callbackContext.sendPluginResult(pluginResult);
        }

    }

    private void checkPermission() {
        Log.e(TAG," Init getLocationNetwork");
        Log.e(TAG," Coarse:"+cordova.hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION));
        Log.e(TAG," Fine:"+cordova.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.e(TAG,Build.VERSION.SDK_INT+">="+ Build.VERSION_CODES.M);
            if (!cordova.hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION) && !cordova.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                grandPermissions();
            }
        }
    }

    private void grandPermissions(){
        Log.e(TAG," Init grandPermissions");
        cordova.requestPermissions(this,REQUEST_CODE_ENABLE_PERMISSION,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION});
    }

    @SuppressLint("MissingPermission")
    private Location getLocationNetwork(){
        Log.e(TAG," Init getLocationNetwork");
        LocationManager manager = (LocationManager) cordova.getActivity().getSystemService(Context.LOCATION_SERVICE);
        manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    }

}