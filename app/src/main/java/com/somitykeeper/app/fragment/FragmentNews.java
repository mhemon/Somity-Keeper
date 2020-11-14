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

import com.somitykeeper.app.ArticleAdapter;
import com.somitykeeper.app.MainViewModel;
import com.somitykeeper.app.R;

public class FragmentNews extends Fragment {


    public FragmentNews() {
        // Required empty public constructor
    }

    private RecyclerView newsRecyclerview;
    private ArticleAdapter articleAdapter;
    private MainViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        newsRecyclerview = view.findViewById(R.id.recycler_view);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsRecyclerview.setItemAnimator(new DefaultItemAnimator());
        newsRecyclerview.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        newsRecyclerview.setLayoutManager(linearLayoutManager);

        viewModel.getChannel().observe(getViewLifecycleOwner(), channel -> {
            if (channel != null) {
                articleAdapter = new ArticleAdapter(channel.getArticles(), getContext());
                newsRecyclerview.setAdapter(articleAdapter);
                articleAdapter.notifyDataSetChanged();
            }
        });
        viewModel.fetchFeed();

        return view;
    }
}