package com.leewilson.libra.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.leewilson.libra.R;
import com.leewilson.libra.adapters.SectionsPageAdapter;
import com.leewilson.libra.model.Book;
import com.leewilson.libra.utils.FilterKt;
import com.leewilson.libra.viewmodels.MyLibraryViewModel;
import com.leewilson.libra.views.tab_fragments.MyLibraryListTabFragment;
import com.leewilson.libra.views.tab_fragments.MyLibraryRatedTabFragment;
import com.leewilson.libra.views.tab_fragments.MyLibraryReviewedTabFragment;
import com.leewilson.libra.views.tab_fragments.Tabbable;

import java.util.ArrayList;
import java.util.List;

public class MyLibraryFragment extends Fragment {

    private static final String TAG = "MyLibraryFragment";
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private MyLibraryViewModel mViewModel;
    private List<Book> mBooks;
    private int mCurrentPage = 0;
    private SearchView mSearchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mylibrary, container, false);
        mViewPager = view.findViewById(R.id.mylibrary_viewpager_container);
        mSearchView = view.findViewById(R.id.searchView);
        setupViewPager(mViewPager);

        TabLayout tabLayout = view.findViewById(R.id.mylibrary_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        ViewModelStoreOwner owner = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .getViewModelStoreOwner(R.id.mylibrary_nav_graph);
        mViewModel = new ViewModelProvider(owner).get(MyLibraryViewModel.class);
        mViewModel.getAllBooksLiveData().observe(getViewLifecycleOwner(), books -> {
            mBooks = books;
            Tabbable tabbable = (Tabbable) mSectionsPageAdapter.getItem(mViewPager.getCurrentItem());
            tabbable.setData(mBooks);
        });
        mViewModel.updateBookList();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Do nothing
            }

            @Override
            public void onPageSelected(int position) {
                Tabbable selectedTab = (Tabbable) mSectionsPageAdapter.getItem(position);
                selectedTab.setData(mBooks);
                Tabbable previousTab = (Tabbable) mSectionsPageAdapter.getItem(mCurrentPage);
                previousTab.clearData();
                mCurrentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Do nothing
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Book> filtered = FilterKt.filterBy(mBooks, newText);
                sendDataToCurrentTab(filtered);
                return false;
            }
        });

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

    private void sendDataToCurrentTab(List<Book> books) {
        Tabbable tab = (Tabbable) mSectionsPageAdapter.getItem(mViewPager.getCurrentItem());
        tab.setData(books);
    }
}
