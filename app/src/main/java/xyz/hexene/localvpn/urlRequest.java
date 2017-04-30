package xyz.hexene.localvpn;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Brandon on 4/30/2017.
 */

public class urlRequest extends AsyncTask<Void, Void, String> {

    String urlLink;

    public urlRequest(String ipAddress){
        this.urlLink = "http://ip-api.com/json/" + ipAddress;
        Log.d("urlRequest", "Link: " + urlLink);
    }

    @Override
    protected String doInBackground(Void... params) {
        return ping();
    }

    private String ping(){
        URL url = null;
        String buffer = "";
        try {
            url = new URL(urlLink.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;
        StringBuilder httpResponse = new StringBuilder();

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            while((buffer = reader.readLine()) != null)
                httpResponse.append(buffer);

            buffer = httpResponse.toString();
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("urlRequest", "Buffer: " + buffer);
        return buffer;
    }
}

