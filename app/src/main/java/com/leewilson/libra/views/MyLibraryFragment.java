package com.leewilson.libra.views;

import android.os.Bundle;
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
import com.leewilson.libra.utils.MyLibraryTab;
import com.leewilson.libra.viewmodels.MyLibraryViewModel;
import com.leewilson.libra.views.tab_fragments.MyLibraryItemsListener;
import com.leewilson.libra.views.tab_fragments.MyLibraryListTabFragment;
import com.leewilson.libra.views.tab_fragments.MyLibraryReviewedTabFragment;

import java.util.List;

public class MyLibraryFragment extends Fragment implements MyLibraryItemsListener {

    private static final int LIST_BOOKS_INDEX = 0;
    private static final int LIST_REVIEWS_INDEX = 1;
    public static final String SELECTED_BOOK_ID_KEY = "SELECTED_BOOK_ID_KEY";

    private static final String TAG = "MyLibraryFragment";

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private MyLibraryViewModel mViewModel;
    private List<Book> mBooks;
    private SearchView mSearchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mylibrary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViewModel();
        setupViewPager(view);
        subscribeObservers();
        setViewPagerListener();
        setSearchViewListener();
    }

    private void setSearchViewListener() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // to do
                return false;
            }
        });
    }

    private void setViewPagerListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float posOffset, int posOffsetPx) { /* Do nothing */ }

            @Override
            public void onPageSelected(int position) {
                switch(position) {
                    case LIST_BOOKS_INDEX: {
                        mViewModel.setCurrentTab(MyLibraryTab.LIST_BOOKS);
                    }
                    case LIST_REVIEWS_INDEX: {
                        mViewModel.setCurrentTab(MyLibraryTab.LIST_REVIEWS);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) { /* Do nothing */ }
        });
    }

    private void setupViewPager(View view) {
        mViewPager = view.findViewById(R.id.mylibrary_viewpager_container);
        mSearchView = view.findViewById(R.id.searchView);

        mSectionsPageAdapter = new SectionsPageAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mSectionsPageAdapter.addFragment(new MyLibraryListTabFragment(mViewModel.getBooks(), this), getString(R.string.mylibrary_tab_all));
        mSectionsPageAdapter.addFragment(new MyLibraryReviewedTabFragment(), getString(R.string.mylibrary_tab_reviewed));
        mViewPager.setAdapter(mSectionsPageAdapter);
        TabLayout tabLayout = view.findViewById(R.id.mylibrary_tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void subscribeObservers() {

    }

    private void setupViewModel() {
        ViewModelStoreOwner owner = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .getViewModelStoreOwner(R.id.mylibrary_nav_graph);
        mViewModel = new ViewModelProvider(owner).get(MyLibraryViewModel.class);
    }

    @Override
    public void onItemClick(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(SELECTED_BOOK_ID_KEY, id);
        Navigation.findNavController(getView())
                .navigate(R.id.myLibraryDetailFragment, bundle);
    }
}
