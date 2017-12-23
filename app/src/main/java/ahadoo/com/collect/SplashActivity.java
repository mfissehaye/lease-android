package ahadoo.com.collect;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import ahadoo.com.collect.util.UserPreferencesManager;

public class SplashActivity extends AppCompatActivity {

    boolean isRegistered;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_splash);

//        userPreferencesManager = new UserPreferencesManager(this);

        isRegistered = UserPreferencesManager.isARegisteredUser(this);

        sleep();
    }


    public synchronized void getNextActivity() {

        if (isRegistered) {

            Intent intent = new Intent(this, MainActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);

            finish();

        } else {

            Intent intent = new Intent(this, LoginActivity.class);

            startActivity(intent);

            finish();
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    public void sleep() {

        Thread splash = new Thread() {

            @Override
            public void run() {

                try {

                    sleep(500); //todo change this back to 2000

                } catch (InterruptedException e) {

                } finally {

                    getNextActivity();
                }
                //super.run();
            }
        };

        splash.start();
    }
}
