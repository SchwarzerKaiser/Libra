package com.leewilson.libra.views;

import android.media.Rating;
import android.os.Bundle;
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

    private MyLibraryViewModel mViewModel;

    private ImageView mImageView;
    private RatingBar mRatingBar;
    private TextView mTitle;
    private TextView mDescription;
    private TextView mAuthors;
    private Button mShareButton;
    private EditText mReviewDisplay;

    private float initialRating;
    private String initialReview = "";
    private boolean hasReview = true;
    private boolean ratingChanged = false;
    private Review thisReview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mylibrary_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
            if (review != null) {
                initialRating = review.getRating();
                initialReview = review.getReview();
                thisReview = review;
                updateReviewInfo(review);
            } else {
                // If null, then there is no review yet
                hasReview = false;
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
        super.onDestroyView();

        float rating = mRatingBar.getRating();
        String reviewString = mReviewDisplay.getText().toString();


        if(!hasReview) {

            // New review, review field modified
            if (!ratingChanged && !reviewIsBlank()) {
                mViewModel.saveReview(
                        new Review(
                                0,
                                mViewModel.getSelectedBook().getValue().getId(),
                                reviewString,
                                rating,
                                System.currentTimeMillis()
                        )
                );
            }

            // New review, review left blank, rating modified
            if (ratingChanged && reviewIsBlank()) {
                mViewModel.saveReview(
                        new Review(
                                0,
                                mViewModel.getSelectedBook().getValue().getId(),
                                "",
                                rating,
                                System.currentTimeMillis()
                        )
                );
            }
        } else {
            // Review already exists:
            if (rating != initialRating || !reviewString.equals(initialReview)) {
                thisReview.setRating(mRatingBar.getRating());
                thisReview.setReview(reviewString);
                thisReview.setTimeStamp(System.currentTimeMillis());
                mViewModel.updateReview(thisReview);
            }
        }
    }

    private boolean reviewIsBlank() {
        return mReviewDisplay.getText() == null || mReviewDisplay.getText().toString().equals("");
    }
}
