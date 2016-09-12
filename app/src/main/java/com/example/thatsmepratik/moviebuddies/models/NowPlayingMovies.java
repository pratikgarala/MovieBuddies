package com.example.thatsmepratik.moviebuddies.models;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by pratikgarala on 13/05/2016.
 */
public class NowPlayingMovies implements Parcelable {


    public static final String TABLE_NAME = "nowPlayingMovies";

    public static final String _ID = "_id";
    public static final String PAGE = "page";
    public static final String POSTER_PATH= "poster_path";
    public static final String ADULT= "adult";
    public static final String OVERVIEW= "overview";
    public static final String RELEASE_DATE= "release_date";
    public static final String MOVIE_ID= "id";
    public static final String ORIGINAL_TITLE= "original_title";
    public static final String ORIGINAL_LANGUAGE= "original_language";
    public static final String TITLE= "title";
    public static final String BACKDROP_PATH= "backdrop_path";
    public static final String POPULARITY= "popularity";
    public static final String VOTE_COUNT= "vote_count";
    public static final String VOTE_AVERAGE= "vote_average";

    public static final String SQL_CREATE__TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            PAGE + "  TEXT,"+
            POSTER_PATH + "  TEXT,"+
            ADULT + "  TEXT,"+
            OVERVIEW + "  TEXT,"+
            RELEASE_DATE + "  TEXT,"+
            MOVIE_ID + "  TEXT,"+
            ORIGINAL_TITLE + "  TEXT,"+
            ORIGINAL_LANGUAGE + "  TEXT,"+
            TITLE + "  TEXT,"+
            BACKDROP_PATH + "  TEXT,"+
            POPULARITY + "  TEXT,"+
            VOTE_COUNT + "  TEXT,"+
            VOTE_AVERAGE + "  TEXT,"+
            "UNIQUE (" + MOVIE_ID + ") ON CONFLICT REPLACE)";


    private long _id;
    private String page;
    private String posterPath;
    private String adult;
    private String overView;
    private String releaseDate;
    private String movieId;
    private String originalTitle;
    private String originalLanguage;
    private String title;
    private String backdropPath;
    private String popularity;
    private String voteCount;
    private String voteAverage;

    public NowPlayingMovies() {
    }

    public NowPlayingMovies(long _id, String page, String posterPath, String adult, String overView, String releaseDate, String movieId, String originalTitle, String originalLanguage, String title, String backdropPath, String popularity, String voteCount, String voteAverage) {
        this._id = _id;
        this.page = page;
        this.posterPath = posterPath;
        this.adult = adult;
        this.overView = overView;
        this.releaseDate = releaseDate;
        this.movieId = movieId;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.title = title;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
    }

    public NowPlayingMovies(String page, String posterPath, String adult, String overView, String releaseDate, String movieId, String originalTitle, String originalLanguage, String title, String backdropPath, String popularity, String voteCount, String voteAverage) {
        this.page = page;
        this.posterPath = posterPath;
        this.adult = adult;
        this.overView = overView;
        this.releaseDate = releaseDate;
        this.movieId = movieId;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.title = title;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
    }


    protected NowPlayingMovies(Parcel in) {
        _id = in.readLong();
        page = in.readString();
        posterPath = in.readString();
        adult = in.readString();
        overView = in.readString();
        releaseDate = in.readString();
        movieId = in.readString();
        originalTitle = in.readString();
        originalLanguage = in.readString();
        title = in.readString();
        backdropPath = in.readString();
        popularity = in.readString();
        voteCount = in.readString();
        voteAverage = in.readString();
    }

    public static final Creator<NowPlayingMovies> CREATOR = new Creator<NowPlayingMovies>() {
        @Override
        public NowPlayingMovies createFromParcel(Parcel in) {
            return new NowPlayingMovies(in);
        }

        @Override
        public NowPlayingMovies[] newArray(int size) {
            return new NowPlayingMovies[size];
        }
    };

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getAdult() {
        return adult;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeString(page);
        dest.writeString(posterPath);
        dest.writeString(adult);
        dest.writeString(overView);
        dest.writeString(releaseDate);
        dest.writeString(movieId);
        dest.writeString(originalTitle);
        dest.writeString(originalLanguage);
        dest.writeString(title);
        dest.writeString(backdropPath);
        dest.writeString(popularity);
        dest.writeString(voteCount);
        dest.writeString(voteAverage);
    }
}
