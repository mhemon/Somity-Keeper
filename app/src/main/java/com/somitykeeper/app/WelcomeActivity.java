package com.somitykeeper.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pixplicity.easyprefs.library.Prefs;
import com.somitykeeper.app.fragment.FeedFragment;

public class WelcomeActivity extends AppCompatActivity {

    private Button demo,login,tutorial,buynow;
    private FloatingActionButton scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //full screen
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        demo = findViewById(R.id.demo_btn);
        login = findViewById(R.id.login_btn);
        tutorial = findViewById(R.id.tutorial_btn);
        scan = findViewById(R.id.floatingActionButton);
        buynow = findViewById(R.id.buybtn);
        TooltipCompat.setTooltipText(scan,"Scan me!");

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this,BarCodeDetector.class);
                startActivity(intent);
                finish();
            }
        });

        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://somitykeeper.com/buy_now";
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(webIntent);
            }
        });

        demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdemodialog();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdialog();
            }
        });

        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo handle this btn
                String url = getResources().getString(R.string.tutorial);
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(webIntent);
            }
        });
    }

    private void showdemodialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_demo_box);
        Button save,cancel,scan;
        TextView name,somity_name,url;

        save = dialog.findViewById(R.id.save_btn);
        cancel = dialog.findViewById(R.id.cancel_btn);
        name = dialog.findViewById(R.id.name);
        somity_name = dialog.findViewById(R.id.toolbar_name);
        url = dialog.findViewById(R.id.url);

        name.setText("ডেমো ইউজার");
        somity_name.setText("সমিতি কিপার সমবায় সমিতি");
        url.setText("https://demo.oslbd.org/");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String person_name = name.getText().toString();
                String somity = somity_name.getText().toString();
                String web_url = url.getText().toString();
                Prefs.putString("WEB", web_url);
                Prefs.putString("NAME", person_name);
                Prefs.putString("SOMITY", somity);
                Intent mainIntent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void showdialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_box);
        Button save,cancel,scan;
        EditText name,somity_name,url;

        save = dialog.findViewById(R.id.save_btn);
        cancel = dialog.findViewById(R.id.cancel_btn);
        name = dialog.findViewById(R.id.name);
        somity_name = dialog.findViewById(R.id.toolbar_name);
        url = dialog.findViewById(R.id.url);


        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                url.setError(null);
                name.setError(null);
                somity_name.setError(null);

               if (url.getText().toString().isEmpty()){
                   url.setError("Required");
                   return;
               }
                if (name.getText().toString().isEmpty()){
                    name.setError("Required");
                    return;
                }
                if (somity_name.getText().toString().isEmpty()){
                    somity_name.setError("Required");
                    return;
                }

                String web_url = url.getText().toString();
                String person_name = name.getText().toString();
                String somity = somity_name.getText().toString();

                Intent mainIntent = new Intent(WelcomeActivity.this,MainActivity.class);
                mainIntent.putExtra("web_url",web_url);
                mainIntent.putExtra("person_name",person_name);
                mainIntent.putExtra("web_url",somity);
                dialog.dismiss();

                //put data on sharedpref
                Prefs.putString("WEB", web_url);
                Prefs.putString("NAME", person_name);
                Prefs.putString("SOMITY", somity);

                startActivity(mainIntent);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}