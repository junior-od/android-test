package ng.riby.androidtest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.room.Room;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
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

import static android.location.LocationManager.GPS_PROVIDER;

public class CaptureActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private String TAG = this.getClass().getSimpleName();
    Unbinder unbinder;
    private Location mLocation;
    private GoogleApiClient mGoogleApiClient;
    private static int PLAY_SERVICE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;


    Call<GetDistanceResponse> getDistanseResponse;

    //offline purposes
    LocationManager locationManager;
    Context mContext;

    String currentLocationLat;
    String currentLocationLong;
    String lastLocationLat;
    String lastLocationLong;

    @BindView(R.id.stop_capture_button) Button stop_cature_button;
    @BindView(R.id.show_distance_button) Button show_distance_button;

    private ApiInterface apiInterface;

    //database
    public static MyApplicationDatabase myApplicationDatabase;

    android.location.LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        unbinder = ButterKnife.bind(this);

        apiInterface = ApiService.getAPIService();

        myApplicationDatabase = Room.databaseBuilder(getApplicationContext(),
                MyApplicationDatabase.class, "user_device_db").allowMainThreadQueries().build();


        //we build the google api client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();



    }



    private void isLocationEnabled() {

        if(!locationManager.isProviderEnabled(GPS_PROVIDER)){
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Enable Location");
            alertDialog.setMessage("Your locations setting is not enabled. Please enabled it in settings menu.");
            alertDialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            AlertDialog alert=alertDialog.create();
            alert.show();
        }
        else{
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Confirm Location");
            alertDialog.setMessage("Your Location is enabled, please enjoy");
            alertDialog.setNegativeButton("Back to interface",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            AlertDialog alert=alertDialog.create();
            alert.show();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }

        //if permission is okay then getLastLocation

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLocation != null) {
            //getLatitude get Longitude.
            if (currentLocationLat == null || currentLocationLong == null ){
                currentLocationLat = mLocation.getLatitude()+"";
                currentLocationLong = mLocation.getLongitude()+"";
//                Toast.makeText(this, currentLocationLat, Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, currentLocationLong, Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(this,"already gotten current location",Toast.LENGTH_SHORT).show();
            }

        } else {
            //offline logic
            mContext = this;

            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    2000, Float.valueOf("10"), locationListenerGPS);
            isLocationEnabled();


            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }

        doLocationUpdates();
    }

    private void doLocationUpdates(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(3000);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "you need to enable the permissions to display location! ", Toast.LENGTH_LONG).show();
        }


        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {

        if(location != null){
            lastLocationLat = location.getLatitude()+"";
            lastLocationLong = location.getLongitude()+"";
//            Toast.makeText(this,"new location lat internet : "+ lastLocationLat,Toast.LENGTH_SHORT).show();
//            Toast.makeText(this,"new location long: internet: "+ lastLocationLong,Toast.LENGTH_SHORT).show();

        }
    }

    //stop location updates
    @OnClick({R.id.stop_capture_button})
    public void stopCapturing(){
        stop_cature_button.setEnabled(false);

//        Toast.makeText(this,"previous lat "+ currentLocationLat,Toast.LENGTH_SHORT).show();
//        Toast.makeText(this,"previous long "+ currentLocationLong,Toast.LENGTH_SHORT).show();
//        Toast.makeText(this,"new lat "+ lastLocationLat,Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "new long "+ lastLocationLong, Toast.LENGTH_SHORT).show();

        if((currentLocationLat != null && currentLocationLong != null)
                && (lastLocationLat != null && lastLocationLong != null)
                ){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);

            Location_Table location_table = new Location_Table();
            location_table.setPointALat(currentLocationLat);
            location_table.setPointALong(currentLocationLong);
            location_table.setPointBLat(lastLocationLat);
            location_table.setPointBLong(lastLocationLong);

            CaptureActivity.myApplicationDatabase.myDao().addLocationOfDevice(location_table);
            Toast.makeText(this,"device previous location added to database sucessfully",Toast.LENGTH_SHORT).show();


            show_distance_button.setVisibility(View.VISIBLE);

        }else{
            stop_cature_button.setEnabled(true);
        }
    }

    @OnClick(R.id.show_distance_button)
    public void showDistanceBetweenPoints(){
        if(!NetworkUtils.isNetworkAvailable(this)){
            Toast.makeText(getApplicationContext(),"you need to turn on the internet connection to proceed",Toast.LENGTH_SHORT).show();
            return;
        }
        show_distance_button.setEnabled(false);
         Map<String , String > data = new HashMap<>();
         data.put("units", Constants.IMPERIAL);
         data.put("origins",currentLocationLat+","+currentLocationLong);
         data.put("destinations",lastLocationLat+","+lastLocationLong);
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

                     show_distance_button.setEnabled(true);
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


    private boolean checkPlayServices(){
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode =  googleApiAvailability.isGooglePlayServicesAvailable(this);

        if(resultCode != ConnectionResult.SUCCESS){
            if(googleApiAvailability.isUserResolvableError(resultCode)){
                googleApiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICE_RESOLUTION_REQUEST);
            }else{
                finish();
            }
        }
        return true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (getDistanseResponse!= null) {
            getDistanseResponse.cancel();
            getDistanseResponse = null;
        }
        //stop location updates
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient != null && stop_cature_button.isEnabled()){
            mGoogleApiClient.connect();
        }
//        mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //offline
        if(locationManager != null && stop_cature_button.isEnabled()){
            isLocationEnabled();

        }

        if(!checkPlayServices()){
            Toast.makeText(getApplicationContext(), "you need to install google play services to use this application",Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


}
