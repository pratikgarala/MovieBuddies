package com.example.thatsmepratik.moviebuddies.fragment;

/**
 * Created by pratikgarala on 10/05/2016.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.thatsmepratik.moviebuddies.R;
import com.example.thatsmepratik.moviebuddies.adapters.MovieRecyclerViewAdapter;
import com.example.thatsmepratik.moviebuddies.models.Constants;
import com.example.thatsmepratik.moviebuddies.models.DatabaseHelper;
import com.example.thatsmepratik.moviebuddies.models.Key;
import com.example.thatsmepratik.moviebuddies.models.NowPlayingMovies;
//import com.example.thatsmepratik.moviebuddies.ui.MovieAdapter;
import com.example.thatsmepratik.moviebuddies.ui.AddEventActivity;
import com.example.thatsmepratik.moviebuddies.ui.widget.AutofitRecyclerView;
import com.example.thatsmepratik.moviebuddies.utils.CommonUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MoviesFragment extends Fragment {

    //    private ListView movieList;
    private ArrayList<NowPlayingMovies> movies;

    private AutofitRecyclerView mMoviesRV;
    private TextView mEmptyTextView;
    private GridView mMoviesGridView;
    private RelativeLayout mLoadingView;
    private CoordinatorLayout mCoordinatorLayout;

    private static final String TAG = "MoviesFragment";

    private DatabaseHelper dbHelper;

    @TargetApi(Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies,container,false);
        dbHelper = new DatabaseHelper(getContext());

//        mCoordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator_layout);
        mMoviesRV = (AutofitRecyclerView) view.findViewById(R.id.movie_list);
        mMoviesGridView = (GridView) view.findViewById(R.id.movies_gridview);
        assert mMoviesRV != null;
        if (mMoviesRV != null)
            mMoviesRV.setHasFixedSize(true);
        mEmptyTextView = (TextView) view.findViewById(R.id.empty_TextView);
        mLoadingView = (RelativeLayout) view.findViewById(R.id.loadingView);

        movies = new ArrayList<>(dbHelper.getAllNowPlayingMovies().values());
        refreshAdapter(movies);

        if (dbHelper.getAllNowPlayingMovies().size() == 0) {
            // Talk to server and obtain movies in JSON format
            new SetupMoviesDatasetTask().execute(Constants.JSON_DOWNLOAD_LOCATION);
        }

        // Button launches NewPostActivity
        view.findViewById(R.id.fab_new_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddEventActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
    }

    private class SetupMoviesDatasetTask extends AsyncTask<String, Void, String> { // Download JSON resource and return String representation
        protected String doInBackground(String... urls) {
            try {
                // Setup HTTP client and request
                URL downloadUrl = new URL(urls[0]);
                HttpURLConnection connection =
                        (HttpURLConnection) downloadUrl.openConnection();
                InputStream input = connection.getInputStream();

                // Process each line of response data
                String result = "";
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder sb = new StringBuilder();
                while ((result = reader.readLine()) != null) {
                    sb.append(result);
                }
                // Return response data as String
                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        // Add articles to database after processing resource
        protected void onPostExecute(String result) {
            if (result != null) {
                try {

                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray results = jsonObject.getJSONArray("results");
//                    JSONArray articleContents = results.getJSONArray("item");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject movieJson = results.getJSONObject(i);


                        NowPlayingMovies movie = new NowPlayingMovies(
                                jsonObject.getString("page"),
                                movieJson.getString("poster_path"),
                                movieJson.getString("adult"),
                                movieJson.getString("overview"),
                                movieJson.getString("release_date"),
                                movieJson.getString("id"),
                                movieJson.getString("original_title"),
                                movieJson.getString("original_language"),
                                movieJson.getString("title"),
                                movieJson.getString("backdrop_path"),
                                movieJson.getString("popularity"),
                                movieJson.getString("vote_count"),
                                movieJson.getString("vote_average")
                                );

                        dbHelper.addNowPlayingMovie(movie);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void refreshAdapter(ArrayList<NowPlayingMovies> nowPlayingMovies) {
        Log.d(TAG, "refreshAdapter() called. newMovies:"+nowPlayingMovies);

        toggleLoadingStatus(false);

        if (mMoviesRV != null) {
            if (nowPlayingMovies == null || nowPlayingMovies.size() == 0) {
                CommonUtil.setVisiblity(mEmptyTextView, View.VISIBLE);
                CommonUtil.setVisiblity(mMoviesRV, View.GONE);
                CommonUtil.setVisiblity(mMoviesGridView, View.GONE);
            } else {
                CommonUtil.setVisiblity(mEmptyTextView, View.GONE);
                CommonUtil.setVisiblity(mMoviesRV, View.VISIBLE);
                CommonUtil.setVisiblity(mMoviesGridView, View.VISIBLE);

                if (mMoviesRV.getAdapter() == null) {
                    mMoviesRV.setAdapter(new MovieRecyclerViewAdapter(this, nowPlayingMovies));
                } else {
                    MovieRecyclerViewAdapter adapter = (MovieRecyclerViewAdapter) mMoviesRV.getAdapter();
                    adapter.clearAll();
                    adapter.addAll(nowPlayingMovies);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    void toggleLoadingStatus(boolean showLoading) {
        if(showLoading) {
            CommonUtil.setVisiblity(mLoadingView, View.VISIBLE);
        } else {
            CommonUtil.setVisiblity(mLoadingView, View.GONE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
    }
}