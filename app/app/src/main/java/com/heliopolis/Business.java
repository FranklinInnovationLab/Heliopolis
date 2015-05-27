package com.heliopolis;

/**
 * Created by lucasyan on 5/26/15.
 */
public class Business {
    public int business_id = 0;
    public String businessName = "";
    public boolean checkedIn = false;

    public Business(int id, String name, boolean subscribed){
        this.business_id = id;
        this.businessName = name;
        this.checkedIn = subscribed;
    }
}
