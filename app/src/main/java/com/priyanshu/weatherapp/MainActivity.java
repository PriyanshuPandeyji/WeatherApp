package com.priyanshu.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.priyanshu.weatherapp.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    String url = "https://api.openweathermap.org/data/2.5/weather";
    String appid = "59e7d73f54203c3a043bc7f550a612a6";
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }

    public void getWeather(View view) {
        String tempUrl = "";
        String city = binding.etCity.getText().toString();
        if (city.equals("")) {
            binding.result.setText("please enter city");
        } else {
            //"https://api.openweathermap.org/data/2.5/weather?q=noida&appid=59e7d73f54203c3a043bc7f550a612a6"
            tempUrl = url + "?q=" + city + "&appid=" + appid;

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    tempUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Log.d("response", response);

                            String output = "";
                            try {
                                JSONObject jsonResponse = new JSONObject(response);

                                JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                                JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);

                                String description = jsonObjectWeather.getString("description");

                                JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                                double temp = jsonObjectMain.getDouble("temp") - 273.15;
                                double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                                float pressure = jsonObjectMain.getInt("pressure");
                                float humidity = jsonObjectMain.getInt("humidity");

                                JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                                String wind = jsonObjectWind.getString("speed");

                                JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                                String clouds = jsonObjectClouds.getString("all");

                                JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");

                                String countryName = jsonObjectSys.getString("country");

                                String cityName = jsonResponse.getString("name");

                                output += "Current weather of: " + cityName + " (" + countryName + ")"
                                        + "\n Temp: " + df.format(temp) + " °C"
                                        + "\n Feels Like: " + df.format(feelsLike) + " °C"
                                        + "\n Humidity: " + humidity + " %"
                                        + "\n Description: " + description
                                        + "\n Wind speed: " + wind + " m/s"
                                        + "\n Cloudiness: " + clouds + " %"
                                        + "\n Pressure: " + pressure + " hpa";

                                binding.result.setText(output);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "Please Enter Correct City Name", Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
            };

            requestQueue.add(stringRequest);
        }
    }
}