package com.somitykeeper.app.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.somitykeeper.app.HorizontalAdapter;

import com.somitykeeper.app.MainViewModel;
import com.somitykeeper.app.R;


public class FragmentAll extends Fragment {

    public FragmentAll() {
        // Required empty public constructor
    }


    /////////////////// news
    public RecyclerView mRecyclerView;
    private HorizontalAdapter mAdapter;
    private MainViewModel viewModel;


    //////////////////// health
    public RecyclerView mRecyclerView2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_all, container, false);

        ////////////////////////////////////////////////////////////////////// News

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        mRecyclerView = view.findViewById(R.id.horizontal_recycler_view);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(mRecyclerView.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        viewModel.getChannel().observe(getViewLifecycleOwner(), channel -> {
            if (channel != null) {
                mAdapter = new HorizontalAdapter(channel.getArticles(), getContext());
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        });
            viewModel.fetchFeed();

        ////////////////////////////////////////////////////////////////////// News



        ////////////////////////////////////////////////////////////////////// Health
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        mRecyclerView2 = view.findViewById(R.id.horizontal_health_recycler_view);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView2.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView2.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        linearLayoutManager2.setOrientation(mRecyclerView2.HORIZONTAL);
        mRecyclerView2.setLayoutManager(linearLayoutManager2);

        viewModel.getChannel().observe(getViewLifecycleOwner(), channel -> {
            if (channel != null) {
                mAdapter = new HorizontalAdapter(channel.getArticles(), getContext());
                mRecyclerView2.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        });
        viewModel.fetchFeed();
        ////////////////////////////////////////////////////////////////////// Health

        return view;
    }
}