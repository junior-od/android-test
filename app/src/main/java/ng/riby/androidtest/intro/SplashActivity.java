package ng.riby.androidtest.intro;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import ng.riby.androidtest.CaptureActivity;
import ng.riby.androidtest.MainActivity;
import ng.riby.androidtest.R;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 5500;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity( new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        },SPLASH_TIME_OUT);




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //This resolves the memory leak by removing the handler references.
        handler.removeCallbacksAndMessages(null);
    }
}
