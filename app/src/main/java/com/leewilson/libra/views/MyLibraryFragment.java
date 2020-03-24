package com.leewilson.libra.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.leewilson.libra.R;
import com.leewilson.libra.adapters.SectionsPageAdapter;
import com.leewilson.libra.views.tab_fragments.MyLibraryListTabFragment;
import com.leewilson.libra.views.tab_fragments.MyLibraryRatedTabFragment;
import com.leewilson.libra.views.tab_fragments.MyLibraryReviewedTabFragment;

public class MyLibraryFragment extends Fragment {

    private static final String TAG = "MyLibraryFragment";
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mylibrary, container, false);
        mViewPager = view.findViewById(R.id.mylibrary_viewpager_container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = view.findViewById(R.id.mylibrary_tabs);
        tabLayout.setupWithViewPager(mViewPager);
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        mSectionsPageAdapter = new SectionsPageAdapter(getActivity().getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mSectionsPageAdapter.addFragment(new MyLibraryListTabFragment(), getString(R.string.mylibrary_tab_all));
        mSectionsPageAdapter.addFragment(new MyLibraryRatedTabFragment(), getString(R.string.mylibrary_tab_rated));
        mSectionsPageAdapter.addFragment(new MyLibraryReviewedTabFragment(), getString(R.string.mylibrary_tab_reviewed));
        viewPager.setAdapter(mSectionsPageAdapter);
    }
}
