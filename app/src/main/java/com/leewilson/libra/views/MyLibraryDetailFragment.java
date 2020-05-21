package com.leewilson.libra.views;

import android.media.Rating;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;

import com.leewilson.libra.R;
import com.leewilson.libra.model.Book;
import com.leewilson.libra.model.Review;
import com.leewilson.libra.viewmodels.MyLibraryViewModel;
import com.squareup.picasso.Picasso;

public class MyLibraryDetailFragment extends Fragment {

    private static final String TAG = "MyLibraryDetailFragment";

    private MyLibraryViewModel mViewModel;

    private ImageView mImageView;
    private RatingBar mRatingBar;
    private TextView mTitle;
    private TextView mDescription;
    private TextView mAuthors;
    private Button mShareButton;
    private EditText mReviewDisplay;

    private Review thisReview;
    private boolean ratingChanged = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mylibrary_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated called.");

        initViews(view);
        setupViewModel();
        subscribeObservers();
        setRatingBarListener();

        int currentBookId = getArguments().getInt(MyLibraryFragment.SELECTED_BOOK_ID_KEY);
        mViewModel.setSelectedBook(currentBookId);
        mViewModel.setSelectedReview(currentBookId);
    }

    private void setRatingBarListener() {
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingChanged = true;
            }
        });
    }

    private void initViews(View v) {
        mImageView = v.findViewById(R.id.detailToolbarBookImage);
        mRatingBar = v.findViewById(R.id.ratingBar);
        mTitle = v.findViewById(R.id.mylibrary_detail_title);
        mDescription = v.findViewById(R.id.mylibrary_detail_description);
        mAuthors = v.findViewById(R.id.mylibrary_detail_authors);
        mShareButton = v.findViewById(R.id.mylibrary_detail_share_btn);
        mReviewDisplay = v.findViewById(R.id.review_edittext);
    }

    private void setupViewModel() {
        ViewModelStoreOwner owner = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .getViewModelStoreOwner(R.id.mylibrary_nav_graph);
        mViewModel = new ViewModelProvider(owner).get(MyLibraryViewModel.class);
    }

    private void subscribeObservers() {
        mViewModel.getSelectedBook().observe(getViewLifecycleOwner(), this::updateBookInfo);

        mViewModel.getSelectedReview().observe(getViewLifecycleOwner(), review -> {
            if (review == null) {
                // No review. Do nothing.
            } else {
                // Review exists already. Initialise 'thisReview'
                thisReview = review;
                updateReviewInfo(thisReview);
            }
        });
    }

    private void updateBookInfo(Book book) {
        mAuthors.setText(book.getAuthors());
        mDescription.setText(book.getDescription());
        mTitle.setText(book.getTitle());
        Picasso.get().load(book.getThumbnailURL()).into(mImageView);
    }

    private void updateReviewInfo(Review review) {
        if(!review.getReview().isEmpty())
            mReviewDisplay.setText(review.getReview());
        mRatingBar.setRating(review.getRating());
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView called.");

        if (thisReview == null) {
            // New review, check for any changes
            if (!reviewIsBlank() || ratingChanged) {
                Review newReview = new Review(
                        0,
                        mViewModel.getSelectedBook().getValue().getId(),
                        mReviewDisplay.getText().toString(),
                        mRatingBar.getRating(),
                        System.currentTimeMillis()
                );
                mViewModel.saveReview(newReview);
            }

        } else {
            // Existing review, check if changed.
            String displayedReview = mReviewDisplay.getText().toString();
            if (!displayedReview.equals(thisReview.getReview()) || ratingChanged) {
                thisReview.setReview(displayedReview);
                thisReview.setRating(mRatingBar.getRating());
                mViewModel.updateReview(thisReview);
            }
        }

        nullify();
        super.onDestroyView();
    }

    private void nullify() {
        thisReview = null;
        mImageView = null;
        mRatingBar = null;
        mTitle = null;
        mDescription = null;
        mAuthors = null;
        mShareButton = null;
        mReviewDisplay = null;
    }

    private boolean reviewIsBlank() {
        return mReviewDisplay.getText() == null || mReviewDisplay.getText().toString().equals("");
    }
}
