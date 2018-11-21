package disaster.management;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class RemoteFetch {

    private static final String OPEN_WEATHER_MAP_API =
            "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric";

    public static JSONObject getJSON(Context context, String lat,String lon){
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, lat,lon));
            Log.d("Ritik", "getJSON: "+url.toString());
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("x-api-key",
                    context.getString(R.string.open_weather_maps_app_id));

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                Log.e("Ritik", "getJSON: 404" );
                return null;
            }
            Log.d("Ritik", "getJSON: "+data.toString());
            return data;
        }catch(Exception e){
            Log.e("Ritik", "getJSON: " );
            e.printStackTrace();
            return null;
        }
    }
}