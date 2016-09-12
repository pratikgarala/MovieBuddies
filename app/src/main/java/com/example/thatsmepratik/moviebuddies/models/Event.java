package com.example.thatsmepratik.moviebuddies.models;

import com.google.firebase.auth.api.model.StringList;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pratikgarala on 21/05/2016.
 */

// [START post_class]
@IgnoreExtraProperties
public class Event {

    public String uid;
    public String author;
    public String eventName;
    public String eventDesc;
    public String movieName;
    public String eventVenue;
    public String eventTime;
    public String eventDate;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public Event() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Event(String uid, String author, String eventName, String eventDesc, String movieName, String eventVenue, String eventTime, String eventDate) {
        this.uid = uid;
        this.author = author;
        this.eventName = eventName;
        this.eventDesc = eventDesc;
        this.movieName = movieName;
        this.eventVenue = eventVenue;
        this.eventTime = eventTime;
        this.eventDate = eventDate;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("eventName", eventName);
        result.put("eventDesc", eventDesc);
        result.put("movieName", movieName);
        result.put("eventVenue", eventVenue);
        result.put("eventTime", eventTime);
        result.put("eventDate", eventDate);
        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }
    // [END post_to_map]

}
// [END post_class]
