/*
** Copyright 2015, Mohamed Naufal
**
** Licensed under the Apache License, Version 2.0 (the "License");
** you may not use this file except in compliance with the License.
** You may obtain a copy of the License at
**
**     http://www.apache.org/licenses/LICENSE-2.0
**
** Unless required by applicable law or agreed to in writing, software
** distributed under the License is distributed on an "AS IS" BASIS,
** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
** See the License for the specific language governing permissions and
** limitations under the License.
*/

package xyz.hexene.localvpn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.VpnService;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ExecutionException;


public class LocalVPN extends ActionBarActivity
{
    private static final int VPN_REQUEST_CODE = 0x0F;

    private boolean waitingForVPNStart;

    private Context context;

    private BroadcastReceiver vpnStateReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (LocalVPNService.BROADCAST_VPN_STATE.equals(intent.getAction()))
            {
                if (intent.getBooleanExtra("running", false))
                    waitingForVPNStart = false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_vpn);
        final Button vpnButton = (Button)findViewById(R.id.vpn);
        vpnButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startVPN();
            }
        });
        waitingForVPNStart = false;
        LocalBroadcastManager.getInstance(this).registerReceiver(vpnStateReceiver,
                new IntentFilter(LocalVPNService.BROADCAST_VPN_STATE));

        getSupportActionBar().setTitle("DFSC Firewall");


        context = getApplicationContext();
    }

    private void startVPN()
    {
        Intent vpnIntent = VpnService.prepare(this);
        if (vpnIntent != null)
            startActivityForResult(vpnIntent, VPN_REQUEST_CODE);
        else
            onActivityResult(VPN_REQUEST_CODE, RESULT_OK, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VPN_REQUEST_CODE && resultCode == RESULT_OK)
        {
            waitingForVPNStart = true;
            startService(new Intent(this, LocalVPNService.class));
            enableButton(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        enableButton(!waitingForVPNStart && !LocalVPNService.isRunning());
    }

    private void enableButton(boolean enable)
    {
        final Button vpnButton = (Button) findViewById(R.id.vpn);
        if (enable)
        {
            vpnButton.setEnabled(true);
            vpnButton.setText(R.string.start_vpn);
        }
        else
        {
            vpnButton.setEnabled(false);
            vpnButton.setText(R.string.stop_vpn);
        }
    }

    public void RefreshList(View v){
        SharedPreferences sharedPreferences = getSharedPreferences("ipAddressTable", Context.MODE_PRIVATE);
        Map<String, ?> prefsMap = sharedPreferences.getAll();

        TextView textView = (TextView) findViewById(R.id.myView);

        textView.setText("----------------------------------------------------------\n" +
                "IP Address [Connections made]\n" +
                "----------------------------------------------------------\n");

        StringBuilder stringBuilder = new StringBuilder();
        JSONObject jsonObject = null;

        for (Map.Entry<String, ?> entry: prefsMap.entrySet()) {
            String [] ipAddr = entry.getKey().split("-");
            urlRequest request = new urlRequest(ipAddr[1]);

            try {
                jsonObject = new JSONObject(request.execute().get());
                stringBuilder.append(String.format("\t%-15s \t\t\t[%s Connections]\n",entry.getKey(), entry.getValue().toString()));

                stringBuilder.append(String.format("\t\t[ISP:    \t\t%s]\n",jsonObject.getString("isp")));
                stringBuilder.append(String.format("\t\t[Org:    \t\t%s]\n",jsonObject.getString("org")));
                stringBuilder.append(String.format("\t\t[City:   \t\t%s]\n",jsonObject.getString("city")));
                stringBuilder.append(String.format("\t\t[Country: \t\t%s]\n\n",jsonObject.getString("country")));


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        textView.append(stringBuilder.toString());
    }

    public void clearList(View v){
        TextView textView = (TextView) findViewById(R.id.myView);
        textView.setText("");

        SharedPreferences sharedPreferences = getSharedPreferences("ipAddressTable", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }
}
