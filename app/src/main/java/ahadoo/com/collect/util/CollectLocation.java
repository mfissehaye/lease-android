package ahadoo.com.collect.util;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

// https://stackoverflow.com/a/3145655/4688650
public class CollectLocation {

    Timer timer;

    private Context context;

    private LocationManager lm;

    private boolean gPSEnabled;

    private boolean networkEnabled;

    CollectLocationResult result;

    public boolean getLocation(Context context, CollectLocationResult result) {

        gPSEnabled = false;

        networkEnabled = false;

        this.result = result;

        this.context = context;

        lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        try {

            gPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        } catch(Exception e) {}

        if(!gPSEnabled && !networkEnabled) return false;

        if(gPSEnabled) {

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGPS);
        }

        if(networkEnabled) {

            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
        }

        timer = new Timer();

        timer.schedule(new GetLastLocation(), 5000);

        return true;
    }

    LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            timer.cancel();
            callGotLocationOnUiThread(location);
            lm.removeUpdates(this);
            lm.removeUpdates(locationListenerNetwork);
        }

        @Override public void onStatusChanged(String s, int i, Bundle bundle) {}
        @Override public void onProviderEnabled(String s) {}
        @Override public void onProviderDisabled(String s) {}
    };

    LocationListener locationListenerNetwork = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            timer.cancel();
            callGotLocationOnUiThread(location);
            lm.removeUpdates(this);
            lm.removeUpdates(locationListenerGPS);
        }

        @Override public void onStatusChanged(String s, int i, Bundle bundle) {}
        @Override public void onProviderEnabled(String s) {}
        @Override public void onProviderDisabled(String s) {}
    };

    class GetLastLocation extends TimerTask {

        @Override
        public void run() {

            lm.removeUpdates(locationListenerGPS);

            lm.removeUpdates(locationListenerNetwork);

            Location networkLocation = null, gPSLocation = null;

            if(gPSEnabled) {

                gPSLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            if(networkEnabled) {

                networkLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if(gPSLocation != null && networkLocation != null) {

                if(gPSLocation.getTime() > networkLocation.getTime()) {

                    callGotLocationOnUiThread(gPSLocation);

                } else {

                    callGotLocationOnUiThread(networkLocation);
                }

                return;
            }

            if(gPSLocation != null) {

                callGotLocationOnUiThread(gPSLocation);

                return;
            }

            if(networkLocation != null) {

                callGotLocationOnUiThread(networkLocation);

                return;
            }

            callGotLocationOnUiThread(null);
        }
    }

    private void callGotLocationOnUiThread(final Location location) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                result.gotLocation(location);
            }
        });
    }

    public interface CollectLocationResult {
        void gotLocation(Location location);
    }
}
