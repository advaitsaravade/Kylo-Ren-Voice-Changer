package com.advait.s.emokylo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetAvail {
	
	private static InternetAvail instance = new InternetAvail();
    static Context context;
    ConnectivityManager connectivityManager;
    NetworkInfo wifiInfo, mobileInfo;
    boolean connected = false;
 
    public static InternetAvail getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return instance;
    } 
 
    public boolean isOnline() { 
        try { 
            connectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
 
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        connected = networkInfo != null && networkInfo.isAvailable() &&
                networkInfo.isConnected();
        return connected;
 
 
        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
        } 
        return connected;
    } 
}
