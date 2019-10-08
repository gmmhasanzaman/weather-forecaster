package com.example.mweather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mweather.R;
import com.example.mweather.databinding.RvForecasterLayoutBinding;
import com.example.mweather.model.WeatherForecaster.WeatherList;
import com.example.mweather.util.Constant;
import com.example.mweather.util.SDF;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

public class ForeCastAdapter extends RecyclerView.Adapter<ForeCastAdapter.ViewHolder> {

    private Context context;
    private List<WeatherList> weatherLists;


    public ForeCastAdapter(Context context, List<WeatherList> weatherLists) {
        this.context = context;
        this.weatherLists = weatherLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        RvForecasterLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.rv_forecaster_layout,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherList weatherList = weatherLists.get(position);

        Random random = new Random();


        String icon = weatherList.getWeather().get(0).getIcon();
        String iconUrl = Constant.WEATHER_IMAGE_BASE_URL+icon+".png";

        String time = SDF.getTime(weatherList.getDt());
        String temp = weatherList.getMain().getTemp().toString() + context.getString(R.string.degree_celsius);

       /* String dayName = SDF.getDay(weatherList.getDt());
        String date = SDF.getDate(weatherList.getDt());
        String minTemp = weatherList.getMain().getTempMin().toString();
        String maxTemp = weatherList.getMain().getTempMax().toString();
        String status = weatherList.getWeather().get(0).getDescription();*/

        holder.binding.timeTV.setText(time);
        holder.binding.tempTV.setText(temp);
        Picasso.get().load(iconUrl).into(holder.binding.iconIV);



    }

    @Override
    public int getItemCount() {
        return weatherLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RvForecasterLayoutBinding binding;
        public ViewHolder(RvForecasterLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
