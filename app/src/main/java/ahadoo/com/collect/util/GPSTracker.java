package ahadoo.com.collect.util;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class GPSTracker extends Service implements LocationListener {

    private LocationManager locationManager;

    public GPSTracker(Context context) {

        this.locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
    }

    public Location getLocation() {

        Location location = null;

        // if network is enabled - use the network
        try {

            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                locationManager.requestLocationUpdates(

                        LocationManager.NETWORK_PROVIDER,

                        60 * 1000, // 1 minute interval between updates

                        10, // 10 meters distance between updates

                        this
                );

                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                // if gps is enabled - use the GPS service

                locationManager.requestLocationUpdates(

                        LocationManager.GPS_PROVIDER,

                        60 * 1000,

                        10,

                        this
                );

                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

        } catch (SecurityException e) {

            // You need to allow the location permission

        }

        return location;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public boolean GPSEnabled() {
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
