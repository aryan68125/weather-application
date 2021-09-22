package com.aditya.whetherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    TextView textViewTemperature;
    TextView textViewHumidity;
    EditText editText;
    ImageView cloud;
    ImageView cloudy_fog;
    ImageView haze;
    ImageView pollution;
    ImageView rain;
    ImageView storm;
    ImageView clear;
    ImageView mist;
    ImageView snow;
    ImageView humidPercent;
    ImageView Fog;

    //temperature related images
    ImageView hot;
    ImageView cold;
    ImageView freezing;
    ImageView spring;

    String mainInfo; //will us whether the sky is clear or not
    String descriptionMainInfo=""; //description of the weather

    //extracted temperature is in kelvin
    double tempInfo; //temperature related information extracted from the api will be stored
    // humidity is in percentage
    int humidity2; //stores maximum temperature in a day

    //creating a new class for managing downloads in the background thread
    //it borrows from the existing class the AsyncTask
    //anything written inside the DownloadTask class will happen in the background
    /*
    AsyncTask<String_i, void, String_ii>
    String_i allows us to pass in the string string_ii allows us to pass out the string from the DownloadTask class
    the place where the void is written there we can specify the functionality if the class
     */
    public class DownloadTask extends AsyncTask<String, Void, String> {

        /*
                doInBackground(String... strings) //String...strings allows us to as many strings as we like to them it basically acts
                as an array but its not an array
                 */
        @Override //this method will run the codes written inside in the background
        protected String doInBackground(String... urls) {
            //do not initialize String result = null; instead initialize it as String result = "";
            //otherwise you will get Value null of type org.json.JSONObject$1 cannot be converted to JSONObject error in logcat
            String result = "";
            //creating a url variable
            URL url;
            /*
            HttpURLConnection does the same thing as a browser would we pass the url to this method nd it fetched the informaton from the web
            although it will only able to fetch the text from the internet
             */
            HttpURLConnection urlConnection;

            try {
                url = new URL(urls[0]); //converting strings into url
                urlConnection = (HttpURLConnection) url.openConnection(); //opening connection to that url
                //inputStream will gather the data as its coming through
                InputStream in = urlConnection.getInputStream();
                //InputStreamReader reads the data that is coming in through the InputStream
                InputStreamReader reader = new InputStreamReader(in); //we have pass in the inputstream in here
                /*
                      it will give us back an int
                      this is its way of giving us each individual character
                      when we go through the InputStream we have to go through it character by character
                 */
                int data = reader.read();
                while(data != -1)  //if data becomes -1 then its reader's way of saying that we are done that was the last data that i read there is no more to read anything
                {
                    //convert int data into character data
                    char current = (char) data;
                    //adding character current to the String result
                    result += current;
                    data = reader.read();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Failed!";
            } catch (IOException e) {
                e.printStackTrace();
                return "failed!";
            }
            Log.i("re",result);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {  //the onPostExecute(String s) s is the result that is returned by the doInBackground method
            super.onPostExecute(s);
            //creating a jason object
            try {
                //this will grab the contents inside the weather key in tha json result that we will get from the server
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather"); //jsonObject.getString(weather key extracted from the json Viewer online)
                Log.i("weatherInfo", weatherInfo);
                //since the weather key is returning an array so we need to handel this with an array
                JSONArray arr = new JSONArray(weatherInfo);
                String message = "";
                for(int i=0;i<arr.length();i++)
                {
                    //getting the json object at this particular array
                    //this JSONObject jsonpart is just a part of an array not the whole array
                    JSONObject jsonPart = arr.getJSONObject(i);
                    mainInfo = jsonPart.getString("main");
                    Log.i("main_data", mainInfo); //main inside the weather key
                    descriptionMainInfo = jsonPart.getString("description");
                    Log.i("main_data2",descriptionMainInfo);
                    if(!mainInfo.equals("")&& !descriptionMainInfo.equals("")){
                        message = mainInfo + "\n" +descriptionMainInfo;
                    }
                }
                if(!message.equals("")) {
                    textView.setText(message);
                }

                if(mainInfo.equals("Clouds"))
                {
                    cloud.animate().rotation(360).alpha(1).setDuration(2000);
                    cloudy_fog.animate().rotation(720).alpha(0).setDuration(2000);
                    haze.animate().rotation(1080).alpha(0).setDuration(2000);
                    pollution.animate().rotation(1440).alpha(0).setDuration(2000);
                    rain.animate().rotation(1800).alpha(0).setDuration(2000);
                    storm.animate().rotation(2160).alpha(0).setDuration(2000);
                    clear.animate().rotation(2520).alpha(0).setDuration(2000);
                    mist.animate().rotation(2880).alpha(0).setDuration(2000);
                    snow.animate().rotation(2880).alpha(0).setDuration(2000);
                    Fog.animate().rotation(3240).alpha(0).setDuration(2000);
                }
                else if(mainInfo.equals("Clear"))
                {
                    cloud.animate().rotation(360).alpha(0).setDuration(2000);
                    cloudy_fog.animate().rotation(720).alpha(0).setDuration(2000);
                    haze.animate().rotation(1080).alpha(0).setDuration(2000);
                    pollution.animate().rotation(1440).alpha(0).setDuration(2000);
                    rain.animate().rotation(1800).alpha(0).setDuration(2000);
                    storm.animate().rotation(2160).alpha(0).setDuration(2000);
                    clear.animate().rotation(2520).alpha(1).setDuration(2000);
                    mist.animate().rotation(2880).alpha(0).setDuration(2000);
                    snow.animate().rotation(2880).alpha(0).setDuration(2000);
                    Fog.animate().rotation(3240).alpha(0).setDuration(2000);
                }
                else if(mainInfo.equals("Mist"))
                {
                    cloud.animate().rotation(360).alpha(0).setDuration(2000);
                    cloudy_fog.animate().rotation(720).alpha(0).setDuration(2000);
                    haze.animate().rotation(1080).alpha(0).setDuration(2000);
                    pollution.animate().rotation(1440).alpha(0).setDuration(2000);
                    rain.animate().rotation(1800).alpha(0).setDuration(2000);
                    storm.animate().rotation(2160).alpha(0).setDuration(2000);
                    clear.animate().rotation(2520).alpha(0).setDuration(2000);
                    mist.animate().rotation(2880).alpha(1).setDuration(2000);
                    snow.animate().rotation(2880).alpha(0).setDuration(2000);
                    Fog.animate().rotation(3240).alpha(0).setDuration(2000);
                }
                else if(mainInfo.equals("Cloudy fog"))
                {
                    cloud.animate().rotation(360).alpha(0).setDuration(2000);
                    cloudy_fog.animate().rotation(720).alpha(1).setDuration(2000);
                    haze.animate().rotation(1080).alpha(0).setDuration(2000);
                    pollution.animate().rotation(1440).alpha(0).setDuration(2000);
                    rain.animate().rotation(1800).alpha(0).setDuration(2000);
                    storm.animate().rotation(2160).alpha(0).setDuration(2000);
                    clear.animate().rotation(2520).alpha(0).setDuration(2000);
                    mist.animate().rotation(2880).alpha(0).setDuration(2000);
                    snow.animate().rotation(2880).alpha(0).setDuration(2000);
                    Fog.animate().rotation(3240).alpha(0).setDuration(2000);
                }
                else if(mainInfo.equals("Haze"))
                {
                    cloud.animate().rotation(360).alpha(0).setDuration(2000);
                    cloudy_fog.animate().rotation(720).alpha(0).setDuration(2000);
                    haze.animate().rotation(1080).alpha(1).setDuration(2000);
                    pollution.animate().rotation(1440).alpha(0).setDuration(2000);
                    rain.animate().rotation(1800).alpha(0).setDuration(2000);
                    storm.animate().rotation(2160).alpha(0).setDuration(2000);
                    clear.animate().rotation(2520).alpha(0).setDuration(2000);
                    mist.animate().rotation(2880).alpha(0).setDuration(2000);
                    snow.animate().rotation(2880).alpha(0).setDuration(2000);
                    Fog.animate().rotation(3240).alpha(0).setDuration(2000);
                }
                else if(mainInfo.equals("Smoke"))
                {
                    cloud.animate().rotation(360).alpha(0).setDuration(2000);
                    cloudy_fog.animate().rotation(720).alpha(0).setDuration(2000);
                    haze.animate().rotation(1080).alpha(0).setDuration(2000);
                    pollution.animate().rotation(1440).alpha(1).setDuration(2000);
                    rain.animate().rotation(1800).alpha(0).setDuration(2000);
                    storm.animate().rotation(2160).alpha(0).setDuration(2000);
                    clear.animate().rotation(2520).alpha(0).setDuration(2000);
                    mist.animate().rotation(2880).alpha(0).setDuration(2000);
                    snow.animate().rotation(2880).alpha(0).setDuration(2000);
                    Fog.animate().rotation(3240).alpha(0).setDuration(2000);
                }
                else if(mainInfo.equals("Rain"))
                {
                    cloud.animate().rotation(360).alpha(0).setDuration(2000);
                    cloudy_fog.animate().rotation(720).alpha(0).setDuration(2000);
                    haze.animate().rotation(1080).alpha(0).setDuration(2000);
                    pollution.animate().rotation(1440).alpha(0).setDuration(2000);
                    rain.animate().rotation(1800).alpha(1).setDuration(2000);
                    storm.animate().rotation(2160).alpha(0).setDuration(2000);
                    clear.animate().rotation(2520).alpha(0).setDuration(2000);
                    mist.animate().rotation(2880).alpha(0).setDuration(2000);
                    snow.animate().rotation(2880).alpha(0).setDuration(2000);
                    Fog.animate().rotation(3240).alpha(0).setDuration(2000);
                }
                else if(mainInfo.equals("Storm"))
                {
                    cloud.animate().rotation(360).alpha(0).setDuration(2000);
                    cloudy_fog.animate().rotation(720).alpha(0).setDuration(2000);
                    haze.animate().rotation(1080).alpha(0).setDuration(2000);
                    pollution.animate().rotation(1440).alpha(0).setDuration(2000);
                    rain.animate().rotation(1800).alpha(0).setDuration(2000);
                    storm.animate().rotation(2160).alpha(1).setDuration(2000);
                    clear.animate().rotation(2520).alpha(0).setDuration(2000);
                    mist.animate().rotation(2880).alpha(0).setDuration(2000);
                    snow.animate().rotation(2880).alpha(0).setDuration(2000);
                    Fog.animate().rotation(3240).alpha(0).setDuration(2000);
                }
                else if(mainInfo.equals("Snow"))
                {
                    cloud.animate().rotation(360).alpha(0).setDuration(2000);
                    cloudy_fog.animate().rotation(720).alpha(0).setDuration(2000);
                    haze.animate().rotation(1080).alpha(0).setDuration(2000);
                    pollution.animate().rotation(1440).alpha(0).setDuration(2000);
                    rain.animate().rotation(1800).alpha(0).setDuration(2000);
                    storm.animate().rotation(2160).alpha(0).setDuration(2000);
                    clear.animate().rotation(2520).alpha(0).setDuration(2000);
                    mist.animate().rotation(2880).alpha(0).setDuration(2000);
                    snow.animate().rotation(2880).alpha(1).setDuration(2000);
                    Fog.animate().rotation(3240).alpha(0).setDuration(2000);
                }
                else if(mainInfo.equals("Fog")){
                    cloud.animate().rotation(360).alpha(0).setDuration(2000);
                    cloudy_fog.animate().rotation(720).alpha(0).setDuration(2000);
                    haze.animate().rotation(1080).alpha(0).setDuration(2000);
                    pollution.animate().rotation(1440).alpha(0).setDuration(2000);
                    rain.animate().rotation(1800).alpha(0).setDuration(2000);
                    storm.animate().rotation(2160).alpha(0).setDuration(2000);
                    clear.animate().rotation(2520).alpha(0).setDuration(2000);
                    mist.animate().rotation(2880).alpha(0).setDuration(2000);
                    snow.animate().rotation(2880).alpha(0).setDuration(2000);
                    Fog.animate().rotation(3240).alpha(1).setDuration(2000);
                }
                else
                {
                    cloud.animate().rotation(360).alpha(0).setDuration(2000);
                    cloudy_fog.animate().rotation(720).alpha(0).setDuration(2000);
                    haze.animate().rotation(1080).alpha(0).setDuration(2000);
                    pollution.animate().rotation(1440).alpha(0).setDuration(2000);
                    rain.animate().rotation(1800).alpha(0).setDuration(2000);
                    storm.animate().rotation(2160).alpha(0).setDuration(2000);
                    clear.animate().rotation(2520).alpha(0).setDuration(2000);
                    mist.animate().rotation(2880).alpha(0).setDuration(2000);
                    snow.animate().rotation(2880).alpha(0).setDuration(2000);
                    Fog.animate().rotation(3240).alpha(0).setDuration(2000);
                }

                //this will grab the contents inside the weather key in tha json result that we will get from the server
                JSONObject jsonObject2 = new JSONObject(s);
                String TemperatureInfo = jsonObject2.getString("main"); //jsonObject.getString(weather key extracted from the json Viewer online)
                Log.i("TemperatureInfo", TemperatureInfo);
                List<String> weatherList = Collections.singletonList(TemperatureInfo);

                /*
                weatherList.get(0).substring(8,13) it allows us to cut the value of temperature in string format
                from the TemperatureInfo String
                 */
                Log.i("CurrentTemperature",weatherList.get(0).substring(8,13));
                String CurrentTemperature = weatherList.get(0).substring(8,13);
                //tempInfo in kelvin
                tempInfo = Double.parseDouble(CurrentTemperature); //converting String into double
                int degreeCelsius = (int)tempInfo - 273; //converting kelvin into degree celsius
                String stringdegreeCelsius = Integer.toString(degreeCelsius); //converting double into string
                String celsius  = "℃";
                textViewTemperature.setText(stringdegreeCelsius +" "+celsius);

                if(degreeCelsius>=40)
                {    //hot day
                    hot.animate().rotation(360).alpha(1).setDuration(2000);
                    cold.animate().rotation(1440).alpha(0).setDuration(2000);
                    freezing.animate().rotation(2520).alpha(0).setDuration(2000);
                    spring.animate().rotation(3240).alpha(0).setDuration(2000);
                }
                else if(degreeCelsius<=20 && degreeCelsius>=1)
                {  //cold day
                    hot.animate().rotation(720).alpha(0).setDuration(2000);
                    cold.animate().rotation(1800).alpha(1).setDuration(2000);
                    freezing.animate().rotation(2880).alpha(0).setDuration(2000);
                    spring.animate().rotation(3240).alpha(0).setDuration(2000);
                }
                else if(degreeCelsius<=0)
                {
                    //freezing
                    hot.animate().rotation(1080).alpha(0).setDuration(2000);
                    cold.animate().rotation(2160).alpha(0).setDuration(2000);
                    freezing.animate().rotation(3240).alpha(1).setDuration(2000);
                    spring.animate().rotation(3240).alpha(0).setDuration(2000);
                }
                else
                {
                   //pleasant day
                    hot.animate().rotation(1080).alpha(0).setDuration(2000);
                    cold.animate().rotation(2160).alpha(0).setDuration(2000);
                    freezing.animate().rotation(3240).alpha(0).setDuration(2000);
                    spring.animate().rotation(3240).alpha(1).setDuration(2000);
                }

                /*
                weatherList.get(0).substring(99,100) it allows us to cut the value of temperature in string format
                from the TemperatureInfo String
                 */
                String humidity = weatherList.get(0).substring(99,100);
                humidity2 = Integer.parseInt(humidity);
                String humidmessage = humidity2 + "%";
                textViewHumidity.setText(humidmessage);
                humidPercent.setVisibility(View.VISIBLE);

            } catch (JSONException e) {
                e.printStackTrace();
                String message  = "failed!";
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public void getWeather(View view){

        //create an object of the DownloadTask class
        DownloadTask task = new DownloadTask();
        /*
        now we will execute the downloadTask class by the use of its object task
        method to execute a class is (object of the class).execute("url of the site");
        this url of the website now gets passed to the protected String doInBackground(String... strings) method
        inside the DownloadTask class
         */
        String result=""; //this will hold the returned value "done!" from the DownloadTask class
        try {
            //note the dont forget to include https:// before the pi link
            // https://api.openweathermap.org/data/2.5/weather?id=cityID&appid=api_Key
            result = task.execute("https://api.openweathermap.org/data/2.5/weather?q="+ editText.getText().toString() +"&appid=9c64029f4ada32cf7d8c5cd09c582c68").get(); //.get() will get the returned value from the downloadTask class
        }
        catch(Exception e)
        {
            e.printStackTrace(); // this will print out and log whatever the error there is
            String message  = "failed!";
            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
        }
        Log.i("result",result);

        //making the keyboard disappear when the user hits the button whats the weather?
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textView);
        textViewTemperature = (TextView)findViewById(R.id.temperature);
        textViewHumidity = (TextView)findViewById(R.id.humidity);
        editText = (EditText)findViewById(R.id.editText);
        cloud = (ImageView)findViewById(R.id.cloud);
        cloudy_fog = (ImageView)findViewById(R.id.cloudy_fog);
        haze = (ImageView)findViewById(R.id.haze);
        pollution = (ImageView)findViewById(R.id.pollution);
        rain = (ImageView)findViewById(R.id.rain);
        storm = (ImageView)findViewById(R.id.storm);
        clear = (ImageView)findViewById(R.id.clear);
        mist = (ImageView)findViewById(R.id.mist);
        snow = (ImageView)findViewById(R.id.snow);
        hot = (ImageView)findViewById(R.id.hot);
        cold = (ImageView)findViewById(R.id.cold);
        freezing = (ImageView)findViewById(R.id.freezing);
        humidPercent = (ImageView)findViewById(R.id.humidPercent);
        spring = (ImageView)findViewById(R.id.spring);
        Fog = (ImageView)findViewById(R.id.fogImage);

        humidPercent.setVisibility(View.INVISIBLE);

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(intent);
            }
        });
    }
}