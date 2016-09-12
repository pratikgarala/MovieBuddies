package com.example.thatsmepratik.moviebuddies.ui;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.thatsmepratik.moviebuddies.R;
import com.example.thatsmepratik.moviebuddies.models.DatabaseHelper;
import com.example.thatsmepratik.moviebuddies.models.Event;
import com.example.thatsmepratik.moviebuddies.models.NowPlayingMovies;
import com.example.thatsmepratik.moviebuddies.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddEventActivity extends AppCompatActivity {

    private static final String TAG = "AddEventActivity";
    private static final String REQUIRED = "Required";

    private DatabaseReference mDatabase;

    private EditText etEventName;
    private EditText etEventDesc;
    private EditText etMovieName;
    private EditText etEventVenue;
    private EditText etEventDate;
    private EditText etEventTime;

    private ArrayList<NowPlayingMovies> movies;
    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        etEventName = (EditText) this.findViewById(R.id.et_eventName);
        etEventDesc = (EditText) this.findViewById(R.id.et_eventDesc);
        etMovieName = (EditText) this.findViewById(R.id.et_movieName);
        etEventVenue = (EditText) this.findViewById(R.id.et_eventVenue);
        etEventDate = (EditText) this.findViewById(R.id.et_eventDate);
        etEventTime = (EditText) this.findViewById(R.id.et_eventTime);

        this.findViewById(R.id.fab_submit_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });

        etEventDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //To show current date in the datepicker
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker=new DatePickerDialog(AddEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        etEventDate.setText(selectedday + "/" + selectedmonth + "/" + selectedyear);
                    }
                },mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();  }
        });

        etEventTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        etEventTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);//12 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        etMovieName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                dbHelper = new DatabaseHelper(getApplicationContext());
                movies = new ArrayList<> (dbHelper.getAllNowPlayingMovies().values());
                final CharSequence movieTitles[] = new CharSequence[movies.size()];
                int i = 0;
                for (NowPlayingMovies movie: movies) {
                    movieTitles[i++] = movie.getTitle();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(AddEventActivity.this);
                builder.setTitle("Select Movie");
                builder.setItems(movieTitles, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        etMovieName.setText(movieTitles[which]);
                    }
                });
                builder.show();
            }
        });

    }

    private void submitPost() {
        final String eventName = etEventName.getText().toString();
        final String eventDesc = etEventDesc.getText().toString();
        final String movieName = etMovieName.getText().toString();
        final String eventVenue = etEventVenue.getText().toString();
        final String eventTime = etEventTime.getText().toString();
        final String eventDate = etEventDate.getText().toString();

        // eventName is required
        if (TextUtils.isEmpty(eventName)) {
            etEventName.setError(REQUIRED);
            return;
        }

        // movieName is required
        if (TextUtils.isEmpty(movieName)) {
            etMovieName.setError(REQUIRED);
            return;
        }

        // eventDate is required
        if (TextUtils.isEmpty(eventDate)) {
            etEventDate.setError(REQUIRED);
            return;
        }

        // eventTime is required
        if (TextUtils.isEmpty(eventTime)) {
            etEventTime.setError(REQUIRED);
            return;
        }
        // eventVenue is required
        if (TextUtils.isEmpty(eventVenue)) {
            etEventVenue.setError(REQUIRED);
            return;
        }

        // [START single_value_read]
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(AddEventActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            writeNewPost(userId, user.username, eventName, eventDesc, movieName, eventVenue, eventTime, eventDate);
                        }

                        // Finish this Activity, back to the stream
                        finish();
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
        // [END single_value_read]
    }




    // [START write_fan_out]
    private void writeNewPost(String userId, String username, String eventName, String eventDesc, String movieName, String eventVenue, String eventTime, String eventDate) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously

        String key = mDatabase.child("users").push().getKey();
        Event event = new Event(userId, username, eventName, eventDesc, movieName, eventVenue, eventTime, eventDate);
        Map<String, Object> postValues = event.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/events/" + key, postValues);
        childUpdates.put("/user-events/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }
    // [END write_fan_out]
}
