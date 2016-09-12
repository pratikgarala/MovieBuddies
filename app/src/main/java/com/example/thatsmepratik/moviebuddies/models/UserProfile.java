package com.example.thatsmepratik.moviebuddies.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pratikgarala on 10/06/2016.
 */

// [START post_class]
@IgnoreExtraProperties
public class UserProfile {

    public String uid;
//    public String firstName;
//    public String lastName;
//    public String birthDate;
    public String profilePic;

    public UserProfile() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public UserProfile(String uid, String profilePic) {
        this.profilePic = profilePic;
        this.uid = uid;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
//        result.put("firstName", firstName);
//        result.put("lastName", lastName);
//        result.put("birthDate", birthDate);
        result.put("profilePic", profilePic);

        return result;
    }
    // [END post_to_map]

}
// [END post_class]

