package com.example.mweather.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mweather.R;

import com.example.mweather.databinding.ActivityMainBinding;
import com.example.mweather.model.WeatherApi.WeatherResponse;
import com.example.mweather.model.WeatherForecaster.WeatherForecaster;
import com.example.mweather.model.WeatherForecaster.WeatherList;
import com.example.mweather.retrofit.RetrofitClient;
import com.example.mweather.retrofit.WeatherServiceEndPoints;
import com.example.mweather.util.SDF;
import com.example.mweather.view.adapter.DistrictAdapter;
import com.example.mweather.view.adapter.ForeCastAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DistrictAdapter adapter;
    private List<String> districtList;

    /*private String latitude = "23.7808875";
    private String longitude = "90.2792371";*/
    private String units = "metric";
    private String iconUrl = "https://openweathermap.org/img/wn/";
    private String iconExt = ".png";
    private String appid = "7555f9fc90c8c804298742c06723c8ce";

    private ForeCastAdapter foreCastAdapter;
    private FusedLocationProviderClient locationProviderClient;



    //private List<String> districtList = Arrays.asList(getResources().getStringArray(R.array.bd_districts));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //districtList = Arrays.asList(getResources().getStringArray(R.array.bd_districts));

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        createList();
        initRecyclerView();

        //initForecastRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (checkLocationRuntimePermission()){
            getDeviceLocation();
        }
    }

    private boolean checkLocationRuntimePermission(){
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,permissions,1);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            getDeviceLocation();
        }else {
            // Alert dialog to inform the user for location requirements
            // and explain the user why this permission is important
        }
    }

    private void getDeviceLocation() {


        binding.progressBar.setVisibility(View.VISIBLE);
        locationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    String lat = String.valueOf(location.getLatitude());
                    String lng = String.valueOf(location.getLongitude());

                    Toast.makeText(MainActivity.this, "location: "+lat+","+lng, Toast.LENGTH_SHORT).show();
                    Log.d("location",lat+","+lng);

                    loadCurrentTemperature(lat,lng);
                    loadWeatherForecaster(lat,lng);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("location-error",e.getLocalizedMessage());
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }


    private void createList() {
        //districtList = new ArrayList<>();
        districtList = Arrays.asList(getResources().getStringArray(R.array.bd_districts));

    }


    private void initRecyclerView() {
        binding.districtRV.setHasFixedSize(true);
        binding.districtRV.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DistrictAdapter(this, districtList);
        binding.districtRV.setAdapter(adapter);
    }

    private void initForecastRecyclerView() {

       /* foreCastAdapter = new ForeCastAdapter(this, weatherLists);
        binding.forecastRV.setLayoutManager(new LinearLayoutManager(this));
        binding.forecastRV.setAdapter(foreCastAdapter);*/
    }

    private void loadCurrentTemperature(String latitude, String longitude) {
        //Log.d("hek","fragment");

        WeatherServiceEndPoints apiService = RetrofitClient.getClient().create(WeatherServiceEndPoints.class);
        Call<WeatherResponse> responseCall = apiService.getByLetLng(latitude, longitude, units, appid);

        responseCall.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.code() == 200) {
                    WeatherResponse wr = response.body();
                    String placeName = wr.getName();
                    String iconImg = wr.getWeather().get(0).getIcon();
                    String icon = iconUrl + iconImg + iconExt;
                    String description = wr.getWeather().get(0).getMain().toUpperCase();

                    double currentTemp = wr.getMain().getTemp();
                    //String stringDate = SDF.getDate(wr.getDt());
                    double lowTemp = wr.getMain().getTempMin();
                    double highTemp = wr.getMain().getTempMax();
                    double humidity = wr.getMain().getHumidity();
                    double pressure = wr.getMain().getPressure();
                    String wind = wr.getWind().getSpeed().toString();
                    String sunrise = SDF.getTime(wr.getSys().getSunrise());
                    String sunset = SDF.getTime(wr.getSys().getSunset());

                    /*binding.statusTV.setText(description);
                    binding.dateTV.setText(stringDate);*/

                    binding.placeTV.setText(placeName);
                    binding.tempTV.setText(String.valueOf((int)currentTemp));
                    binding.maxTempTV.setText(String.valueOf(highTemp)+ " °C");
                    binding.minTempTV.setText(String.valueOf(lowTemp) + " °C");
                    binding.humidityTV.setText(String.valueOf(humidity) + " %");
                    binding.windTV.setText(wind + " km/h");
                    binding.pressureTV.setText(String.valueOf(pressure)+" Pa");
                    binding.sunriseTV.setText(sunrise);
                    binding.sunsetTV.setText(sunset);
                    //binding.descriptionTV.setText(description);

                    //Picasso.get().load(icon).resize(100,100).into(binding.iconIV);
                    binding.progressBar.setVisibility(View.GONE);


                } else {
                    Toast.makeText(MainActivity.this, "errorCode- " + response.code(), Toast.LENGTH_LONG).show();
                    binding.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                binding.progressBar.setVisibility(View.GONE);

            }
        });
    }

    private void loadWeatherForecaster(String lat, String lng) {

        WeatherServiceEndPoints weatherServiceEndPoints = RetrofitClient
                .getClient().create(WeatherServiceEndPoints.class);

        weatherServiceEndPoints.getWeatherForecaster(lat, lng, units, appid)
                .enqueue(new Callback<WeatherForecaster>() {
                    @Override
                    public void onResponse(Call<WeatherForecaster> call, Response<WeatherForecaster> response) {
                        if (response.code() == 200) {
                            WeatherForecaster wf = response.body();
                            List<WeatherList> weatherLists = wf.getList();
                            foreCastAdapter = new ForeCastAdapter(MainActivity.this, weatherLists);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false);
                            binding.forecastRV.setLayoutManager(layoutManager);

                            if (adapter != null) {
                                binding.forecastRV.setAdapter(foreCastAdapter);
                            }
                            binding.progressBar.setVisibility(View.GONE);
                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "errorCode- " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherForecaster> call, Throwable t) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);

        final MenuItem searchItem = menu.findItem(R.id.searchID);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                binding.districtRV.setVisibility(View.VISIBLE);
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean newViewFocus) {
                if (!newViewFocus) {
                    //Collapse the action item.
                    searchItem.collapseActionView();
                    binding.districtRV.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Close", Toast.LENGTH_SHORT).show();
                    //Clear the filter/search query.
                    /*myFilterFunction("");*/
                }
            }
        });

       /* searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                binding.districtRV.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Close", Toast.LENGTH_SHORT).show();
                return true;
            }
        });*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        //MenuItem searchItem = menu.findItem(R.id.action_search);

        switch (id) {
            case R.id.homeID:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                break;

            case R.id.settingsID:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.addCityID:
                Toast.makeText(this, "Add City", Toast.LENGTH_SHORT).show();
                break;
            case R.id.aboutID:
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
