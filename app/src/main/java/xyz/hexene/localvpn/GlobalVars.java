package xyz.hexene.localvpn;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by Brandon on 4/26/2017.
 */

public class GlobalVars extends Application {
    ArrayList<String> ipAddress = new ArrayList<String>();

    public ArrayList<String> getIpAddress() {
        return ipAddress;
    }

    public void addIpAddress(String ip){
        ipAddress.add(ip);
    }
}
