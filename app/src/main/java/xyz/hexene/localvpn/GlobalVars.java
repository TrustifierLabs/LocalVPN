package xyz.hexene.localvpn;

import android.app.Application;
import android.util.Log;

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
        Log.d("GlobalVars", "Adding IP" + ip);
        ipAddress.add(ip);
    }

    public String getIPAddresses(){
        StringBuilder buffer = null;

        if(ipAddress.size() < 1) {
            buffer.append("No IP Addresses recorded");
            return buffer.toString();
        }

        for( int i = 0; i < ipAddress.size(); i++){
            buffer.append(ipAddress.get(i) + "\n");
        }

        return buffer.toString();
    }
}
