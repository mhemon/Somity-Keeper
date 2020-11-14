package com.somitykeeper.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;

import com.pixplicity.easyprefs.library.Prefs;

public class SplashScreen extends AppCompatActivity {

    String description = "";
    String title = "";
    String img_url = "";
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Prefs.putBoolean("hasnoti",false);
        //firebase initilization
        if (getIntent().getExtras() != null) {
            title = getIntent().getExtras().getString("title");
            description = getIntent().getExtras().getString("description");
            img_url = getIntent().getExtras().getString("img_url");
            if (title != null & description != null & img_url!= null){
                Prefs.putString("not_title",title);
                Prefs.putString("not_description",description);
                Prefs.putString("not_imageurl",img_url);
                Prefs.putBoolean("hasnoti",true);
            }
        }
        if (title == null & description == null & img_url == null) {
            title = "";
            description = "";
            img_url = "";
        }
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);

    }

//    private void checkfirstlunch() {
//        boolean isFirstStart = Prefs.getBoolean("firstStart",true);
//        if (isFirstStart) {
//            //  Launch welcome screen
//            Intent i = new Intent(this, WelcomeActivity.class);
//            startActivity(i);
//            Prefs.putBoolean("firstStart", false);
//            finish();
//        }else {
//            //Launch Main Screen
//            Intent intent = new Intent(this,MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }

}