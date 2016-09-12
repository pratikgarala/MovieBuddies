package com.example.thatsmepratik.moviebuddies.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by pratikgarala on 13/05/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    // Set database properties
    public static final String DATABASE_NAME = "MoviesDB";
    public static final int DATABASE_VERSION = 1;

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NowPlayingMovies.SQL_CREATE__TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NowPlayingMovies.TABLE_NAME);
        onCreate(db);
    }

    public void addNowPlayingMovie(NowPlayingMovies movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NowPlayingMovies.PAGE, movie.getPage());
        values.put(NowPlayingMovies.POSTER_PATH, movie.getPosterPath());
        values.put(NowPlayingMovies.ADULT, movie.getAdult());
        values.put(NowPlayingMovies.OVERVIEW, movie.getOverView());
        values.put(NowPlayingMovies.RELEASE_DATE, movie.getReleaseDate());
        values.put(NowPlayingMovies.MOVIE_ID, movie.getMovieId());
        values.put(NowPlayingMovies.ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(NowPlayingMovies.ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
        values.put(NowPlayingMovies.TITLE, movie.getTitle());
        values.put(NowPlayingMovies.BACKDROP_PATH, movie.getBackdropPath());
        values.put(NowPlayingMovies.POPULARITY, movie.getPopularity());
        values.put(NowPlayingMovies.VOTE_COUNT, movie.getVoteCount());
        values.put(NowPlayingMovies.VOTE_AVERAGE, movie.getVoteAverage());

        db.insert(NowPlayingMovies.TABLE_NAME, null, values);
        db.close();
    }

    public HashMap<Long, NowPlayingMovies> getAllNowPlayingMovies() {
        HashMap<Long, NowPlayingMovies> articles = new LinkedHashMap<Long, NowPlayingMovies>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + NowPlayingMovies.TABLE_NAME, null);
        // Add monster to hash map for each row result
        if (cursor.moveToFirst()) {
            do {
                NowPlayingMovies movies = new NowPlayingMovies(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getString(13)
                        );
                articles.put(movies.get_id(), movies);
            } while (cursor.moveToNext());
        }
        // Close cursor
        cursor.close();
        return articles;
    }

}
