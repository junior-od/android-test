package ng.riby.androidtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.room.Room;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ng.riby.androidtest.adapter.DeviceLocationHistoryRecycleAdapter;
import ng.riby.androidtest.api.ApiInterface;
import ng.riby.androidtest.api.ApiService;
import ng.riby.androidtest.model.GetDistanceResponse;
import ng.riby.androidtest.model.Location_Table;
import ng.riby.androidtest.room.MyApplicationDatabase;
import ng.riby.androidtest.utility.Constants;
import ng.riby.androidtest.utility.NetworkUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationHistoryActivity extends AppCompatActivity implements DeviceLocationHistoryRecycleAdapter.DefaultListener {
    Unbinder unbinder;

    @BindView(R.id.show_location_history_recyclerview) RecyclerView show_location_history_recyclerview;

    List<Location_Table> deviceLocationList;

    DeviceLocationHistoryRecycleAdapter deviceLocationHistoryRecycleAdapter;

    Call<GetDistanceResponse> getDistanseResponse;

    private ApiInterface apiInterface;

    //database
    public static MyApplicationDatabase myApplicationDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_history);

        unbinder = ButterKnife.bind(this);

        apiInterface = ApiService.getAPIService();
        deviceLocationList = new ArrayList<>();
        deviceLocationHistoryRecycleAdapter = new DeviceLocationHistoryRecycleAdapter(this,getApplicationContext());
        show_location_history_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        show_location_history_recyclerview.setAdapter(deviceLocationHistoryRecycleAdapter);


        myApplicationDatabase = Room.databaseBuilder(getApplicationContext(),
                MyApplicationDatabase.class, "user_device_db").allowMainThreadQueries().build();

        deviceLocationList = MainActivity.myApplicationDatabase.myDao().getLocationHistory();
        deviceLocationHistoryRecycleAdapter.swapItems(deviceLocationList);

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (getDistanseResponse!= null) {
            getDistanseResponse.cancel();
            getDistanseResponse = null;
        }
    }

    @Override
    public void onButtonClicked(Location_Table location_table) {
        if(!NetworkUtils.isNetworkAvailable(this)){
            Toast.makeText(getApplicationContext(),"you need to turn on the internet connection to proceed",Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String , String > data = new HashMap<>();
        data.put("units", Constants.IMPERIAL);
        data.put("origins",location_table.getPointALat()+","+location_table.getPointALong());
        data.put("destinations",location_table.getPointBLat()+","+location_table.getPointBLong());
        data.put("key",Constants.GOOGLE_MAP_API_KEY);
        getDistanseResponse = apiInterface.getDistance(data);

        getDistanseResponse.enqueue(new Callback<GetDistanceResponse>() {
            @Override
            public void onResponse(Call<GetDistanceResponse> call, Response<GetDistanceResponse> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),
                            "the distance between "
                                    +response.body().getOrigin_addresses()[0] +" and "+response.body().getDestination_addresses()[0]+" is "
                                    +response.body().getRows()[0].getElements()[0].getDistance().getText()
                            ,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetDistanceResponse> call, Throwable t) {
                if (call.isCanceled()) {

                } else {
                    Toast.makeText(getApplicationContext(),
                            "unable to perform operation, try again"
                            ,Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbinder.unbind();
    }
}
