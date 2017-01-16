package com.toranado.bestafe.utils;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class CheckConnectivity {

	    ConnectivityManager connectivityManager;
	    NetworkInfo wifiInfo, mobileInfo;
	 
	    public Boolean checkNow(Context con){
	         
	        try{
	        	Log.v("tag", "1");
	            connectivityManager = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
	            wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	            mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);  
	             
	            if(wifiInfo.isConnected() || mobileInfo.isConnected())
	            {
	            	Log.v("tag", "1 true");
	                return true;
	            }
	        }
	        catch(Exception e){
	            System.out.println("CheckConnectivity Exception: " + e.getMessage());
	        }
	        Log.v("tag", "1 false");
	        return false;
	    }


}
