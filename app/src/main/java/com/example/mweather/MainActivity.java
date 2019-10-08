package com.example.mweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import android.widget.Toast;

import com.example.mweather.adapter.DistrictAdapter;
import com.example.mweather.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DistrictAdapter adapter;
    private List<String> districtList;


    //private List<String> districtList = Arrays.asList(getResources().getStringArray(R.array.bd_districts));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        //districtList = Arrays.asList(getResources().getStringArray(R.array.bd_districts));


        createList();

        initRecyclerView();
    }

    private void createList() {
        //districtList = new ArrayList<>();
        districtList = Arrays.asList(getResources().getStringArray(R.array.bd_districts));

    }

    private void initRecyclerView() {
        binding.districtRV.setHasFixedSize(true);
        binding.districtRV.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DistrictAdapter(this,districtList);
        binding.districtRV.setAdapter(adapter);
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
                if (!newViewFocus)
                {
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
