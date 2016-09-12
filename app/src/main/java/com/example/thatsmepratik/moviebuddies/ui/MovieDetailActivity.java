package com.example.thatsmepratik.moviebuddies.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.thatsmepratik.moviebuddies.R;
import com.example.thatsmepratik.moviebuddies.fragment.MoviesFragment;
import com.example.thatsmepratik.moviebuddies.models.Constants;
import com.example.thatsmepratik.moviebuddies.models.Key;
import com.example.thatsmepratik.moviebuddies.models.NowPlayingMovies;
import com.example.thatsmepratik.moviebuddies.ui.widget.SquareImageView;
import com.example.thatsmepratik.moviebuddies.utils.CommonUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = "MovieDetailActivity";
    private NowPlayingMovies movie = null;
    private SquareImageView background;
    private TextView tvTitle;
    private TextView tvReleaseDate;
    private TextView tvRating;
    private TextView tvMovieDetail;
    private TextView tvPopularityCount;
    private TextView tvVoteCount;
    private TextView tvOriginalLanguage;

    private ImageView ivPosterImage;
    private ImageView ivThumbnail;
    private TextView tvReview;
    private TextView tvReviews_status;
    private VideoView vvTrailer;
    private ScrollView svReviewsScroll;

    private String trailerSource;
    private String movieReviews = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Log.d(TAG, "onCreateView");

        Intent i = getIntent();
        movie = (NowPlayingMovies) i.getParcelableExtra("movie");

        background = (SquareImageView) this.findViewById(R.id.background);
        tvTitle = (TextView) this.findViewById(R.id.tvTitle);
        tvReleaseDate = (TextView) this.findViewById(R.id.tvReleaseDate);
        tvRating = (TextView) this.findViewById(R.id.tvRating);
        tvMovieDetail = (TextView) this.findViewById(R.id.tvMovieDetail);
        tvPopularityCount = (TextView) this.findViewById(R.id.tvPopularityCount);
        tvVoteCount = (TextView) this.findViewById(R.id.tvVoteCount);
        tvOriginalLanguage =(TextView) this.findViewById(R.id.tvOriginalLanguage);
        ivPosterImage = (ImageView) this.findViewById(R.id.poster_image_placeholder);
//        vvTrailer = (VideoView) this.findViewById(R.id.vvTrailer);
        ivThumbnail = (ImageView) this.findViewById(R.id.thumbnail);
        tvReview = (TextView) this.findViewById(R.id.tvReview);
        tvReviews_status = (TextView) this.findViewById(R.id.reviews_status_TextView);
        svReviewsScroll = (ScrollView) this.findViewById(R.id.reviews_scroll);


        String movieDetailUrl = Constants.BASE_URL + movie.getMovieId() + "?&api_key=" + Key.getKey() + "&append_to_response=trailers,reviews";

        new SetupMoviesDetailsDatasetTask().execute(movieDetailUrl);


        String url = "http://image.tmdb.org/t/p/w500" + movie.getBackdropPath();
        Picasso.with(this)
                .load(url)
                .into(background);
        tvTitle.setText(movie.getTitle());
        tvReleaseDate.setText(movie.getReleaseDate());
        tvRating.setText(movie.getVoteAverage());
        tvMovieDetail.setText(movie.getOverView());
        tvPopularityCount.setText(movie.getPopularity());
        tvVoteCount.setText(movie.getVoteCount());
        tvOriginalLanguage.setText(movie.getOriginalLanguage());


        url = "http://image.tmdb.org/t/p/w185" + movie.getPosterPath();
//        Uri uri = Uri.parse(url);
        Picasso.with(this)
                .load(url)
//                .placeholder(R.drawable.watermark)
                .into(ivPosterImage);

        Picasso.with(this)
                .load(url)
//                .placeholder(R.drawable.watermark)
                .into(ivThumbnail);

        ivThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Uri uri=Uri.parse(trailerUrl);
//
//
//                VideoView video=(VideoView)findViewById(R.id.vvTrailer);
//                MediaController mediaController = new MediaController(MovieDetailActivity.this);
//                mediaController.setAnchorView(video);
//                video.setMediaController(mediaController);
//                video.setVideoURI(uri);
//                video.start();

                Intent intent = new Intent(MovieDetailActivity.this, MovieTralierActivity.class);
                intent.putExtra("source", trailerSource);
                startActivity(intent);
            }
        });

        findViewById(R.id.fab_new_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MovieDetailActivity.this, AddEventActivity.class));
            }
        });


    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    private class SetupMoviesDetailsDatasetTask extends AsyncTask<String, Void, String> { // Download JSON resource and return String representation
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
                    JSONObject trailers = jsonObject.getJSONObject("trailers");
                    JSONArray youtube = trailers.getJSONArray("youtube");
                    JSONObject movieTrailer = youtube.getJSONObject(0);
                    trailerSource =  movieTrailer.getString("source");

                    JSONObject reviews = jsonObject.getJSONObject("reviews");
                    JSONArray results = reviews.getJSONArray("results");


                    for (int i = 0; i < results.length(); i++) {

                        JSONObject reviewsJson = results.getJSONObject(i);
                        movieReviews = movieReviews + reviewsJson.getString("author") + ": " + reviewsJson.getString("content") + "\n";
                    }
                    if (movieReviews.equals("")){
                        CommonUtil.setVisiblity(tvReviews_status,View.VISIBLE);
                        CommonUtil.setVisiblity(svReviewsScroll,View.INVISIBLE);
                    }else {
                        CommonUtil.setVisiblity(tvReviews_status,View.INVISIBLE);
                        CommonUtil.setVisiblity(svReviewsScroll,View.VISIBLE);
                        tvReview.setText(movieReviews);
                    }
//
//
//                        NowPlayingMovies movie = new NowPlayingMovies(
//                                jsonObject.getString("page"),
//                                movieJson.getString("poster_path"),
//                                movieJson.getString("adult"),
//                                movieJson.getString("overview"),
//                                movieJson.getString("release_date"),
//                                movieJson.getString("id"),
//                                movieJson.getString("original_title"),
//                                movieJson.getString("original_language"),
//                                movieJson.getString("title"),
//                                movieJson.getString("backdrop_path"),
//                                movieJson.getString("popularity"),
//                                movieJson.getString("vote_count"),
//                                movieJson.getString("vote_average")
//                        );
//
//                        dbHelper.addNowPlayingMovie(movie);
                    } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

