package xyz.hexene.localvpn;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Brandon on 4/27/2017.
 */

public class myLogger implements Runnable {
    String ipAddress;
    Context context;
    SharedPreferences sharedPreferences;
    int count, s_count;

    public myLogger(String ip, Context cont){
        this.ipAddress = ip;
        this.context = cont;
    }

    @Override
    public void run() {
        sharedPreferences = context.getSharedPreferences("ipAddressTable", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String [] s_ipAddress = ipAddress.split(":");
        s_count = sharedPreferences.getInt(s_ipAddress[0], 0);
        s_count++;

        editor.putInt(s_ipAddress[0], s_count);
        editor.commit();
        Log.d("MyLogger", s_ipAddress[0] + ": " + s_count);
     //   GlobalVars gv = (GlobalVars) context;
     //   gv.addIpAddress(s_ipAddress[0]);
    }
}
