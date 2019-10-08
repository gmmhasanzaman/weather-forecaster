package com.example.mweather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mweather.R;

import java.util.ArrayList;
import java.util.List;

public class DistrictAdapter extends RecyclerView.Adapter<DistrictAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<String> districtList;
    private List<String> districtListFull;

    public DistrictAdapter(Context context, List<String> districtList) {
        this.context = context;
        this.districtList = districtList;
        districtListFull = new ArrayList<>(districtList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rv_district_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String districtName = districtList.get(position);
        holder.districtTV.setText(districtName);

    }

    @Override
    public int getItemCount() {
        return districtList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView districtTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            districtTV = itemView.findViewById(R.id.rvDistrictTV);
        }
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<String> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0){
                filteredList.addAll(districtListFull);
            }else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (String district:districtListFull){
                    if (district.toLowerCase().contains(filterPattern)){
                        filteredList.add(district);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            /*districtList.clear();
            districtList.addAll((ArrayList<String>) filterResults.values);*/
            districtList = (ArrayList<String>)filterResults.values;
            notifyDataSetChanged();

        }
    };
}
