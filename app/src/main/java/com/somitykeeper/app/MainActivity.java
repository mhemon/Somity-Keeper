package com.somitykeeper.app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private String web_url;
    private String person_name;
    private String somity;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private static final String TAG = "Token";

    private TextView toolbar_name;
    private TextView counttxt;
    private ImageView toolbar_bell,toolbar_info,toolbar_share;
    private ImageView toolbar_shape;
    private AdView mAdView;
    private LinearLayout linearLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ads container banner ads
        linearLayout = findViewById(R.id.container);
        mAdView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        boolean hasnoti = Prefs.getBoolean("hasnoti",false);

        //show notificationdialog
        if (hasnoti){
            shownotidialog();
            Prefs.putBoolean("hasnoti",false);
        }

        ///////////check intenet
        if (!isNetworkAvailable()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.alert_message)
                    .setTitle(R.string.alert_title)
                    .setCancelable(false)
                    .setPositiveButton(R.string.alert_positive,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    finish();
                                }
                            });

            AlertDialog alert = builder.create();
            alert.show();

        }

        //fierbase instant id
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        Log.d(TAG, token);
                        //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });


        // navigation
        BottomNavigationView nav = findViewById(R.id.nav_view);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(nav, navController);

        //toolbar
        Set<Integer> topLevelDestinations = new HashSet<>();
        topLevelDestinations.add(R.id.homeFragment);
        topLevelDestinations.add(R.id.manageFragment);
        //topLevelDestinations.add(R.id.moreFragment);
        appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations).build();
        Toolbar toolbar = findViewById(R.id.toolbar);

        NavigationUI.setupWithNavController(
                toolbar, navController, appBarConfiguration);

        // custom control on bottom navigation & toolbar
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.feedFragment) {
                    toolbar.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);
                    //nav.setVisibility(View.GONE);
                } else {
                    linearLayout.setVisibility(View.VISIBLE);
                    toolbar.setVisibility(View.VISIBLE);
                    nav.setVisibility(View.VISIBLE);
                }
            }
        });


        // get data from intent todo only first lunch
        web_url = getIntent().getStringExtra("web_url");
        person_name = getIntent().getStringExtra("person_name");
        somity = getIntent().getStringExtra("somity");


        /// setup find view by id toolbar activitymain.xml
        toolbar_name = findViewById(R.id.toolbar_name);
        toolbar_bell = findViewById(R.id.toolbar_bell);
        toolbar_info = findViewById(R.id.toolbar_info);
        toolbar_share = findViewById(R.id.toolbar_share);
        toolbar_shape = findViewById(R.id.toolbar_shape);
        counttxt = findViewById(R.id.count_text);


        String somityname = Prefs.getString("SOMITY", getResources().getString(R.string.app_name));
        toolbar_name.setText(somityname);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        myRef.child("Notification").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {   // Check for data snapshot has some value
                    long value = snapshot.getChildrenCount();
                    toolbar_shape.setVisibility(View.VISIBLE);
                    counttxt.setVisibility(View.VISIBLE);
                    counttxt.setText(String.valueOf(value));
                }else {
                    toolbar_shape.setVisibility(View.GONE);
                    counttxt.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        
        //handle notify icon & about icon
        toolbar_bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NotificationBell.class);
                startActivity(intent);
            }
        });

        toolbar_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdialog();
            }
        });

        toolbar_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out Somity Keeper app at: https://play.google.com/store/apps/details?id=" + getPackageName());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

    }



    private void shownotidialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_for_notification);

        ImageView imag;
        TextView title,description;
        Button closebtn;

        imag = dialog.findViewById(R.id.notify_img);
        title = dialog.findViewById(R.id.notify_title);
        description = dialog.findViewById(R.id.notfy_des);
        closebtn = dialog.findViewById(R.id.close_btn);

        String notiTitle = Prefs.getString("not_title","title");
        String notiDesc = Prefs.getString("not_description","description");
        String Images = Prefs.getString("not_imageurl","images");

        Glide.with(this)
                .load(Images)
                .fitCenter()
                .placeholder(R.drawable.placeholder)
                .into(imag);
        title.setText(notiTitle);
        description.setText(notiDesc);
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void showdialog() {
        androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(R.string.app_name);
        alertDialog.setMessage(Html.fromHtml(MainActivity.this.getString(R.string.info_text) +
                " <a href='https://somitykeeper.com/'>Somity Keeper.</a>" +
                        MainActivity.this.getString(R.string.version) +
                MainActivity.this.getString(R.string.author)+" <a href='https://facebook.com/emon.k.5'>Mehedi Emon</a>" +
                MainActivity.this.getString(R.string.software)));
        alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

        ((TextView) Objects.requireNonNull(alertDialog.findViewById(android.R.id.message))).setMovementMethod(LinkMovementMethod.getInstance());
    }


    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}