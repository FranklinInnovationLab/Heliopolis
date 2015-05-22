package com.heliopolis;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import java.util.*;
import java.util.concurrent.*;
import java.util.Random;

public class MyService extends Service {
    public static Profile profile;
    public static MyLocationListener locationListener;

    private class MyLocationListener implements LocationListener {
        public String longitude, latitude;

        @Override
        public void onLocationChanged(Location loc) {
            this.longitude = Double.toString(loc.getLongitude());
            this.latitude = Double.toString(loc.getLatitude());
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider,
                                    int status, Bundle extras) {
        }
    }

    public MyService() {
    }
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onCreate() {
        Toast.makeText(this, "Service was Created", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        profile = new Profile(android_id);

        //updates list of installed apps
        ScheduledExecutorService app_installed_scheduler =
                Executors.newSingleThreadScheduledExecutor();
        app_installed_scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                int flags = PackageManager.GET_META_DATA |
                        PackageManager.GET_SHARED_LIBRARY_FILES |
                        PackageManager.GET_UNINSTALLED_PACKAGES;
                PackageManager pm = getPackageManager();
                List<ApplicationInfo> applications = pm.getInstalledApplications(flags);
                for (ApplicationInfo app : applications) {
                    if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                        // System application
                    } else {
                        // Installed by user
                        profile.addAppInstalled(app.packageName);
                    }
                }
            }
        }, 0, 5, TimeUnit.MINUTES);

        //this gets a list of running apps
        ScheduledExecutorService apps_usage_scheduler =
                Executors.newSingleThreadScheduledExecutor();
        apps_usage_scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                final List<ActivityManager.RunningTaskInfo> recentTasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
                for (int i = 0; i < recentTasks.size(); i++)
                {
                    String app = recentTasks.get(i).baseActivity.toShortString();
                    app = app.substring(1, app.length() - 1);
                    if(app.contains("/")){
                        app = app.substring(0,app.indexOf("/"));
                    }
                    profile.addAppUsed(app, new Date());
                }
            }
        }, 0, 5, TimeUnit.SECONDS);

        //this gets curr location
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(LocationManager
                .GPS_PROVIDER, 5000, 10,locationListener);
        ScheduledExecutorService location_scheduler =
                Executors.newSingleThreadScheduledExecutor();
        location_scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                if (locationListener.longitude != null){
                    profile.addLocation(locationListener.longitude, locationListener.latitude, new Date());
                }
            }
        }, 0, 5, TimeUnit.SECONDS);

        //this condenses data and sends it to server
        ScheduledExecutorService server_scheduler =
                Executors.newSingleThreadScheduledExecutor();
        server_scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                profile.summarize();
            }
        }, 0, 30, TimeUnit.SECONDS);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

}