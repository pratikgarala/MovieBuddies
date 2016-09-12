package com.example.thatsmepratik.moviebuddies.adapters;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.thatsmepratik.moviebuddies.R;
import com.example.thatsmepratik.moviebuddies.fragment.MoviesFragment;
import com.example.thatsmepratik.moviebuddies.models.NowPlayingMovies;
import com.example.thatsmepratik.moviebuddies.ui.MovieDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by pratikgarala on 14/05/2016.
 */
public class MovieRecyclerViewAdapter
        extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {

    private MoviesFragment moviesFragment;
    private ArrayList<NowPlayingMovies> mValues;
    private static final String TAG = "MovieRecyclerViewAdapter";
    private boolean isLoggingEnabled = true;

    public MovieRecyclerViewAdapter(MoviesFragment moviesFragment, ArrayList<NowPlayingMovies> items) {
        this.moviesFragment = moviesFragment;
        mValues = items;
    }

    public void clearAll() {
        mValues.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_movie, parent, false);
        return new ViewHolder(view);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        Log.d(isLoggingEnabled, TAG, " item:" + mValues.get(position));

        holder.mItem = mValues.get(position);

        String url = "http://image.tmdb.org/t/p/w185" + mValues.get(position).getPosterPath();
//        Uri uri = Uri.parse(url);
        Picasso.with(moviesFragment.getContext())
                .load(url)
//                .placeholder(R.drawable.watermark)
                .into(holder.mPosterView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NowPlayingMovies movie = mValues.get(position);

                Intent intent = new Intent(moviesFragment.getActivity(), MovieDetailActivity.class);
                intent.putExtra("movie", movie);
                moviesFragment.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void addAll(ArrayList<NowPlayingMovies> movies) {
        if (mValues == null) {
            mValues = new ArrayList<>();
        }
        mValues.addAll(movies);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mPosterView;
        public NowPlayingMovies mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPosterView = (ImageView) view.findViewById(R.id.poster);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }
    }
}
