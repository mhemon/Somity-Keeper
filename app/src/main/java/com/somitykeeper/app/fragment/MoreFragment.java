package com.somitykeeper.app.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.material.tabs.TabLayout;
import com.somitykeeper.app.CategoryViewModel1;
import com.somitykeeper.app.HorizontalAdapter;
import com.somitykeeper.app.MainActivity2;
import com.somitykeeper.app.MainViewModel;
import com.somitykeeper.app.R;

import java.util.ArrayList;
import java.util.List;

public class MoreFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MainViewModel viewModel;

    private FragmentAll fragmentAll;
    private FragmentHealth fragmentHealth;
    private FragmentNews fragmentNews;
    private ProgressBar progressBar;
    //private HorizontalAdapter mAdapter;

    public MoreFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        tabLayout = view.findViewById(R.id.tlFeed);
        viewPager = view.findViewById(R.id.vpFeed);
        progressBar = view.findViewById(R.id.progressBar);

        fragmentAll = new FragmentAll();
        fragmentHealth = new FragmentHealth();
        fragmentNews = new FragmentNews();

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(fragmentAll, "All");
        viewPagerAdapter.addFragment(fragmentNews, "Recently Added");
        viewPagerAdapter.addFragment(fragmentHealth, "Success Story");
        viewPager.setAdapter(viewPagerAdapter);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        //such a way to laod different model.
        //CategoryViewModel1 categoryViewModel1 = new ViewModelProvider(this).get(CategoryViewModel1.class);

        viewModel.getChannel().observe(getActivity(), channel -> {
            if (channel != null) {
                viewPager.setAdapter(viewPagerAdapter);
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                viewModel.fetchFeed();
            }
        });


        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary);
        swipeRefreshLayout.canChildScrollUp();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            //mAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
            viewPager.setAdapter(viewPagerAdapter);
            swipeRefreshLayout.setRefreshing(true);
            viewModel.fetchFeed();
        });
        return view;
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }

    }
}