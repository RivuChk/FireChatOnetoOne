package com.massoftind.rnd.firechatonetoone.utils;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

/**
 * Created by developer on 9/5/16.
 */
public class GPSTracker extends Service implements LocationListener {

    private Context mContext;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;
    private Location location;
    private double latitude;
    private double longitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10L;
    private static final long MIN_TIME_BW_UPDATES = 60000L;
    protected LocationManager locationManager;
    private Activity activity;

    public GPSTracker() {
    }

    public GPSTracker(Context context) {
        this.mContext = context;
        this.getLocation();
    }

    public void setActivity(Activity a) {
        this.activity = a;
    }

    public Location getLocation() {
        try {
            this.locationManager = (LocationManager) this.mContext.getSystemService("location");
            this.isGPSEnabled = this.locationManager.isProviderEnabled("gps");
            this.isNetworkEnabled = this.locationManager.isProviderEnabled("network");
            if (this.isGPSEnabled || this.isNetworkEnabled) {
                this.canGetLocation = true;
                if (this.isNetworkEnabled) {

                    this.locationManager.requestLocationUpdates("network", 60000L, 10.0F, this);
                    if(this.locationManager != null) {
                        this.location = this.locationManager.getLastKnownLocation("network");
                        if(this.location != null) {
                            this.latitude = this.location.getLatitude();
                            this.longitude = this.location.getLongitude();
                        }
                    }
                }
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

        return this.location;
    }

    public void stopUsingGPS() {
        if(this.locationManager != null) {
            this.locationManager.removeUpdates(this);
        }

    }

    public double getLatitude() {
        if(this.location != null) {
            this.latitude = this.location.getLatitude();
        }

        return this.latitude;
    }

    public double getLongitude() {
        if(this.location != null) {
            this.longitude = this.location.getLongitude();
        }

        return this.longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert() {
        Builder alertDialog = new Builder(this.mContext);
        alertDialog.setTitle("GPS is settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
                GPSTracker.this.mContext.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void onLocationChanged(Location location) {
    }

    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }
}
