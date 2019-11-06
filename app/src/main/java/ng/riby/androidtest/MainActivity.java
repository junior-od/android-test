package ng.riby.androidtest;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Room;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ng.riby.androidtest.model.Location_Table;
import ng.riby.androidtest.room.MyApplicationDatabase;

public class MainActivity extends AppCompatActivity {
    Unbinder unbinder;

    //list permissions
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    //integer for permission request
    private static final int ALL_PERMISSION_RESULT = 1011;

    //database
    public static MyApplicationDatabase myApplicationDatabase;

    List<Location_Table> deviceLocation = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unbinder = ButterKnife.bind(this);

        myApplicationDatabase = Room.databaseBuilder(getApplicationContext(),
                MyApplicationDatabase.class, "user_device_db").allowMainThreadQueries().build();

        //we add permissions to request location
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionsToRequest = permissionsToRequest(permissions);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(permissionsToRequest.size() > 0){
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),ALL_PERMISSION_RESULT);

            }
        }
    }

    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions){
        ArrayList<String> result = new ArrayList<>();

        for(String perm: wantedPermissions){
            if(!hasPermission(perm)){
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case ALL_PERMISSION_RESULT:
                for (String perm : permissionsToRequest){
                    if(!hasPermission(perm)){
                        permissionsRejected.add(perm);
                    }
                }

                if (permissionsRejected.size() > 0){
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        if(shouldShowRequestPermissionRationale(permissionsRejected.get(0))){
                            new AlertDialog.Builder(MainActivity.this)
                                    .setMessage("These permissions are mandatory to get your location. you need to allow the permissions!!!")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()])
                                                        ,ALL_PERMISSION_RESULT);


                                            }
                                        }
                                    }).setNegativeButton("Cancel", null).create().show();
                            return;
                        }
                    }
                }
//                else{
//
////                    if(mGoogleApiClient != null){
////                        mGoogleApiClient.connect();
////                    }
//                }
                break;
        }
    }



    @OnClick(R.id.start_button)
    public void captureInitialCoordinates(){

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()])
                        ,ALL_PERMISSION_RESULT);
            return;
        }else{
            startActivity(new Intent(getApplicationContext(),CaptureActivity.class));
        }

    }

    @OnClick(R.id.show_location_history)
    public void showPastLocationHistory(){
        deviceLocation = MainActivity.myApplicationDatabase.myDao().getLocationHistory();
        if(deviceLocation.size() > 0){
            startActivity(new Intent(getApplicationContext(),LocationHistoryActivity.class));
        }else{
            Toast.makeText(this, "no location history yet", Toast.LENGTH_SHORT).show();
        }
//        Toast.makeText(this,deviceLocation.size()+"",Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbinder.unbind();
    }
}
