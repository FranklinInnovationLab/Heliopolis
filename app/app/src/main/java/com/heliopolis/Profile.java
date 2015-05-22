package com.heliopolis;

import java.util.*;
import org.json.*;

public class Profile {
    private String deviceId;
    private List<String> apps_installed;
    private Map<String, List<Date>> apps_usage;
    private List<List> locations;

    @Override
    public String toString(){
        return this.deviceId + " " + locations.toString();
    }

    public Profile(String deviceId){
        this.deviceId = deviceId;
        this.apps_installed = new ArrayList();
        this.apps_usage = new HashMap<String, List<Date>>();
        this.locations = new ArrayList();
    }

    public void summarize (){
        JSONObject data = new JSONObject();
        try{
            data.put("id", this.deviceId);
            data.put("installed_apps", new JSONArray(this.apps_installed));
            //converts list of time into list of interval
            Map used_apps = new HashMap();
            for (String key : this.apps_usage.keySet()){
                List<Date> times = this.apps_usage.get(key);
                List concise_times = new ArrayList();
                Date start = null, prev = null;

                for (Date time : times){
                    if (start == null){
                        start = time;
                        prev = time;
                        continue;
                    }
                    long diff = (time.getTime() - prev.getTime()) / 1000;
                    //if its continuous with the last time segment
                    if (diff < 60 && times.indexOf(time) != times.size() - 1){
                        prev = time;
                    }else {

                        List interval = new ArrayList();
                        interval.add(Long.toString(start.getTime()));
                        interval.add(Long.toString(prev.getTime()));
                        concise_times.add(new JSONArray(interval));
                        start = time;
                        prev = time;
                    }
                }
                used_apps.put(key, new JSONArray(concise_times));
            }
            data.put("used_apps", new JSONObject(used_apps));
            //converts [(long, lat, time)] to [(long, lat, start_time, end_time)]
            List location_intervals = new ArrayList();
            String prev_long="", prev_lat="";
            Date start=null, prev=null;
            for (List entry : this.locations){
                String longitude = (String)entry.get(0);
                String latitude = (String)entry.get(1);
                Date time = (Date)entry.get(2);
                if(start==null){
                    prev_lat = latitude;
                    prev_long = longitude;
                    start = time;
                    prev = time;
                }
                if(prev_long == longitude && prev_lat == latitude && this.locations.indexOf(entry) != this.locations.size() - 1){
                    prev = time;
                } else {
                    List curr = new ArrayList();
                    curr.add(prev_long);
                    curr.add(prev_lat);
                    curr.add(Long.toString(start.getTime()));
                    curr.add(Long.toString(prev.getTime()));
                    location_intervals.add(new JSONArray(curr));
                    start = time;
                    prev = time;
                    prev_lat = latitude;
                    prev_long = longitude;
                }
            }
            data.put("locations", new JSONArray(location_intervals));
            System.out.println(data);
            //upload the data here

            //erase the old data, so they can be garbage collected
//            this.apps_installed = new ArrayList();
//            this.apps_usage = new HashMap();
//            this.locations = new ArrayList();
        }catch(Exception e){
            System.out.println(e);
        }

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
