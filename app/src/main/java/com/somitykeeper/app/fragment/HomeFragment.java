package com.somitykeeper.app.fragment;


import android.graphics.Color;
import android.os.Bundle;


import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixplicity.easyprefs.library.Prefs;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.somitykeeper.app.CategoryAdapter1;
import com.somitykeeper.app.CategoryAdapter2;
import com.somitykeeper.app.CategoryAdapter3;
import com.somitykeeper.app.CategoryViewModel1;
import com.somitykeeper.app.CategoryViewModel2;
import com.somitykeeper.app.CategoryViewModel3;
import com.somitykeeper.app.HorizontalAdapter;
import com.somitykeeper.app.MainViewModel;
import com.somitykeeper.app.R;
import com.somitykeeper.app.SliderAdapter;
import com.somitykeeper.app.model.SliderItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class HomeFragment extends Fragment {

    /// firebase dependency
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    // slider view & adapter & progressbar
    SliderView sliderView;
    private SliderAdapter sliderAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private static List<SliderItem> list;

    ///category1 in layout_xml --> horizontal scroll news

    public RecyclerView mRecyclerView;
    private CategoryAdapter1 mAdapter1;
    private CategoryViewModel1 categoryViewModel1;


    //////////////////// category 2 in layout_xml --> horizonatal scroll health
    public RecyclerView mRecyclerView2;
    private CategoryAdapter2 mAdapter2;
    private CategoryViewModel2 categoryViewModel2;


    //////////////////////category 3 in layout_xml --> horizontal scroll category3

    public RecyclerView mRecyclerView3;
    private CategoryAdapter3 mAdapter3;
    private CategoryViewModel3 categoryViewModel3;

    private InterstitialAd mInterstitialAd;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-6721723187603610/3451694698");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            getActivity().finish();
                        }
                    });
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //////////initilize view
        TextView hello_person,grettingText;
        RelativeLayout relativeLayout;

        hello_person = view.findViewById(R.id.hello_person);
        grettingText = view.findViewById(R.id.greating_text);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        progressBar = view.findViewById(R.id.progressBar);
        relativeLayout = view.findViewById(R.id.hide);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary);

        checkTime();

        String hello = Prefs.getString("NAME","Guest");
        String grett = Prefs.getString("TIME","Welcome, Back!");

        hello_person.setText("Hello, "+hello);
        grettingText.setText(grett);

        //slider
        sliderView = view.findViewById(R.id.imageSlider);
        //new code for firebase
        list = new ArrayList<>();
        final SliderAdapter sliderAdapter = new SliderAdapter(list);
        sliderView.setSliderAdapter(sliderAdapter);
        progressBar.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.VISIBLE);
        myRef.child("Slider").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    list.add(new SliderItem(dataSnapshot1.child("imageTitle").getValue().toString(),
                            dataSnapshot1.child("imageUrl").getValue().toString(),
                            dataSnapshot1.child("destinationUrl").getValue().toString()
                    ));
                }
                sliderAdapter.notifyDataSetChanged();
                sliderView.setIndicatorAnimation(IndicatorAnimationType.DROP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                sliderView.setSliderTransformAnimation(SliderAnimations.FANTRANSFORMATION);
                sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                sliderView.setIndicatorSelectedColor(Color.WHITE);
                sliderView.setIndicatorUnselectedColor(Color.GRAY);
                sliderView.setScrollTimeInSec(3);
                sliderView.setAutoCycle(true);
                sliderView.startAutoCycle();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.GONE);
                sliderView.setVisibility(View.GONE);
            }

        });

        ////////////////////////////////////////////////////////////////////// Category 1

        categoryViewModel1 = new ViewModelProvider(this).get(CategoryViewModel1.class);

        mRecyclerView = view.findViewById(R.id.horizontal_recycler_view);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        linearLayoutManager.setOrientation(mRecyclerView.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //mRecyclerView.setAdapter(mAdapter1);

        categoryViewModel1.getChannel().observe(getViewLifecycleOwner(), channel1 -> {
            if (channel1 != null) {
                mAdapter1 = new CategoryAdapter1(channel1.getArticles(),getContext());
                mRecyclerView.setAdapter(mAdapter1);
                mAdapter1.notifyDataSetChanged();
            }
        });
        categoryViewModel1.fetchFeed();

        ////////////////////////////////////////////////////////////////////// category 1

        ////////////////////////////////////////////////////////////////////// category 2
        categoryViewModel2 = new ViewModelProvider(this).get(CategoryViewModel2.class);

        mRecyclerView2 = view.findViewById(R.id.horizontal_health_recycler_view);
        mRecyclerView2.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView2.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        linearLayoutManager2.setOrientation(mRecyclerView2.HORIZONTAL);
        mRecyclerView2.setLayoutManager(linearLayoutManager2);

        categoryViewModel2.getChannel().observe(getViewLifecycleOwner(), channel -> {
            if (channel != null) {
                mAdapter2 = new CategoryAdapter2(channel.getArticles(),getContext());
                mRecyclerView2.setAdapter(mAdapter2);
                mAdapter2.notifyDataSetChanged();
                relativeLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
        });
        categoryViewModel2.fetchFeed();
        ////////////////////////////////////////////////////////////////////// Category 2


        ////////////////////////////////////////////////////////////////////// category 3
        categoryViewModel3 = new ViewModelProvider(this).get(CategoryViewModel3.class);

        mRecyclerView3 = view.findViewById(R.id.horizontal_scroll_category3_rv);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView3.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView3.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getContext());
        linearLayoutManager3.setOrientation(mRecyclerView3.HORIZONTAL);
        mRecyclerView3.setLayoutManager(linearLayoutManager3);

        categoryViewModel3.getChannel().observe(getViewLifecycleOwner(), channel -> {
            if (channel != null) {
                mAdapter3 = new CategoryAdapter3(channel.getArticles(),getContext());
                mRecyclerView3.setAdapter(mAdapter3);
                mAdapter3.notifyDataSetChanged();
                relativeLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
        });
        categoryViewModel3.fetchFeed();
        ////////////////////////////////////////////////////////////////////// Category 3

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                categoryViewModel1.fetchFeed();
                categoryViewModel2.fetchFeed();
                categoryViewModel3.fetchFeed();
                sliderAdapter.notifyDataSetChanged();
                mAdapter1.notifyDataSetChanged();
                mAdapter2.notifyDataSetChanged();
                mAdapter3.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }



    private void checkTime() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            Prefs.putString("TIME","Good Morning");
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            Prefs.putString("TIME","Good Afternoon");
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            Prefs.putString("TIME","Good Evening");
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            Prefs.putString("TIME","Good Night");
        }
    }
}