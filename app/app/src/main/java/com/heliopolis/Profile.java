package com.heliopolis;

import java.util.*;

public class Profile {
    private String deviceId;
    private List<String> apps_installed;
    private Map<String, List<Date>> apps_usage;
    private List locations;

    @Override
    public String toString(){
        return this.deviceId + " " + locations.toString();
    }

    public Profile(String deviceId){
        this.deviceId = deviceId;
        apps_installed = new ArrayList();
        apps_usage = new HashMap<String, List<Date>>();
        locations = new ArrayList();
    }

    public void addAppInstalled(String app){
        if(apps_installed.contains(app)){
            return;
        }
        apps_installed.add(app);
    }

    public void addAppUsed(String app, Date time){
        if(!apps_installed.contains(app)){
            return;
        }
        List times;
        if(apps_usage.containsKey(app)){
            times = apps_usage.get(app);
        } else {
            times = new ArrayList();
        }
        times.add(time);
        apps_usage.put(app, times);
    }

    public void addLocation(String longitude, String latitude, Date time){
        List curr = new ArrayList();
        curr.add(longitude);
        curr.add(latitude);
        curr.add(time);
        locations.add(curr);
    }

}
