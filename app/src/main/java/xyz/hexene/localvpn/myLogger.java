package xyz.hexene.localvpn;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Brandon on 4/27/2017.
 */

public class myLogger implements Runnable {
    String ipAddress;
    Context context;
    SharedPreferences sharedPreferences;
    int count;

    public myLogger(String ip, Context cont){
        this.ipAddress = ip;
        this.context = cont;
    }

    @Override
    public void run() {
        sharedPreferences = context.getSharedPreferences("ipAddressTable", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String [] s_ipAddress = ipAddress.split("-");

        editor.putInt(s_ipAddress[0], count++);
        editor.commit();
     //   GlobalVars gv = (GlobalVars) context;
     //   gv.addIpAddress(s_ipAddress[0]);
    }
}
