package com.example.thatsmepratik.moviebuddies.fragment;

/**
 * Created by pratikgarala on 10/05/2016.
 */

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.thatsmepratik.moviebuddies.R;
import com.example.thatsmepratik.moviebuddies.models.Event;
import com.example.thatsmepratik.moviebuddies.models.User;
import com.example.thatsmepratik.moviebuddies.models.UserProfile;
import com.example.thatsmepratik.moviebuddies.ui.EventDetailActivity;
import com.example.thatsmepratik.moviebuddies.ui.EventViewHolder;
import com.example.thatsmepratik.moviebuddies.ui.ProfileActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment{

    private static final String TAG = "ProfileFragment";

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    private FirebaseRecyclerAdapter<Event, EventViewHolder> mAdapter;
    private FirebaseRecyclerAdapter<UserProfile, EventViewHolder> mProfilePicAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private CardView cvProfile;
    private CircleImageView circleImageView;

    public ProfileFragment() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

        mRecycler = (RecyclerView) rootView.findViewById(R.id.event_list);
        mRecycler.setHasFixedSize(true);

        cvProfile = (CardView) rootView.findViewById(R.id.profileCardView);
        cvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ProfileActivity.class);
                startActivity(intent);
            }
        });

        circleImageView = (CircleImageView) rootView.findViewById(R.id.img_avatar);
//        Glide.with(this)
//                .load(selectedImage)
//                .centerCrop()
//                .crossFade()
//                .into(circleImageView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query eventsQuery = getQuery(mDatabase);
//        Query profilePicQuery = getProfilePicQuery(mDatabase);


        final String userId = getUid();
        mDatabase.child("userProfiles").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);

                        if(userProfile != null){
                            byte[] decodedString = Base64.decode(userProfile.profilePic, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            circleImageView.setImageBitmap(decodedByte);
                        }

                        // ...
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });

        mAdapter = new FirebaseRecyclerAdapter<Event, EventViewHolder>(Event.class, R.layout.row_event,
                EventViewHolder.class, eventsQuery) {
            @Override
            protected void populateViewHolder(final EventViewHolder viewHolder, final Event model, final int position) {
                final DatabaseReference eventRef = getRef(position);

                // Set click listener for the whole post view
                final String eventKey = eventRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch PostDetailActivity
                        Intent intent = new Intent(getActivity(),EventDetailActivity.class);
                        intent.putExtra(EventDetailActivity.EXTRA_EVENT_KEY, eventKey);
                        startActivity(intent);
                    }
                });

                // Determine if the current user has liked this Event and set UI accordingly
                if (model.stars.containsKey(getUid())) {
                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_24);
                } else {
                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_outline_24);
                }

                // Bind Post to ViewHolder, setting OnClickListener for the star button
                viewHolder.bindToPost(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View starView) {
                        // Neeed to write to both places the post is stored
                        DatabaseReference globalPostRef = mDatabase.child("events").child(eventRef.getKey());
                        DatabaseReference userPostRef = mDatabase.child("user-events").child(model.uid).child(eventRef.getKey());

                        // Run two transactions
                        onStarClicked(globalPostRef);
                        onStarClicked(userPostRef);
                    }
                });
            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    // [START post_stars_transaction]
    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Event e = mutableData.getValue(Event.class);
                if (e == null) {
                    return Transaction.success(mutableData);
                }

                if (e.stars.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    e.starCount = e.starCount - 1;
                    e.stars.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    e.starCount = e.starCount + 1;
                    e.stars.put(getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(e);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }
    // [END post_stars_transaction]

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    public Query getQuery(DatabaseReference databaseReference) {

        return databaseReference.child("user-events")
                .child(getUid());
    }

//    public Query getProfilePicQuery(DatabaseReference databaseReference) {
//
//        return databaseReference.child("userProfiles")
//                .child(getUid());
//    }

}
