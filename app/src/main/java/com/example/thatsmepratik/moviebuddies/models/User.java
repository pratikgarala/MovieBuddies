package com.example.thatsmepratik.moviebuddies.models;

/**
 * Created by pratikgarala on 21/05/2016.
 */
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String username;
    public String profilePic;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username) {
        this.username = username;
    }

    public User(String username, String profilePic) {
        this.username = username;
        this.profilePic = profilePic;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", username);
        result.put("profilePic", profilePic);

        return result;
    }


}
// [END blog_user_class]

