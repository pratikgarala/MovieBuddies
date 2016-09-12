package com.example.thatsmepratik.moviebuddies.ui;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.thatsmepratik.moviebuddies.R;
import com.example.thatsmepratik.moviebuddies.models.Event;
import com.example.thatsmepratik.moviebuddies.models.User;
import com.example.thatsmepratik.moviebuddies.models.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private static final String REQUIRED = "Required";
    private static int RESULT_LOAD_IMAGE = 1;
    private ImageView ivProfilePic;
    private ImageView ivEditProfilePic;
    private DatabaseReference mDatabase;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etBirthDate;
    private FloatingActionButton fabSubmitProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ivProfilePic = (ImageView) findViewById(R.id.ivProfilePic);
        ivEditProfilePic = (ImageView) findViewById(R.id.ivEditProfilePic);
        etFirstName = (EditText) findViewById(R.id.et_firstName);
        etLastName = (EditText) findViewById(R.id.et_LastName);
        etBirthDate = (EditText) findViewById(R.id.et_birthDate);
        fabSubmitProfile = (FloatingActionButton) findViewById(R.id.fabSubmitProfile);

        ivEditProfilePic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        etBirthDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //To show current date in the datepicker
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker=new DatePickerDialog(ProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        etBirthDate.setText(selectedday + "/" + selectedmonth + "/" + selectedyear);
                    }
                },mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();  }
        });

//        fabSubmitProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                submitProfile();
//            }
//        });
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Glide.with(this)
                    .load(selectedImage)
                    .centerCrop()
                    .crossFade()
                    .into(ivProfilePic);


            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
            byte[] ba = bao.toByteArray();
            final String ba1 = Base64.encodeToString(ba, Base64.DEFAULT);


            mDatabase = FirebaseDatabase.getInstance().getReference();

            final String userId = getUid();
            writeNewUserProfile(userId,ba1);
            }

        }


    // [START write_fan_out]
    private void writeNewUserProfile(String userId, String profilePic) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously

        UserProfile userProfile = new UserProfile(userId, profilePic);
        Map<String, Object> postValues = userProfile.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/userProfiles/" + userId , postValues);

        mDatabase.updateChildren(childUpdates);
    }
//    // [END write_fan_out]

    private void submitProfile() {
        final String firstName = etFirstName.getText().toString();
        final String lastName = etLastName.getText().toString();
        final String birthDate = etBirthDate.getText().toString();

        // First Name is required
        if (TextUtils.isEmpty(firstName)) {
            etFirstName.setError(REQUIRED);
            return;
        }

        // Last Name is required
        if (TextUtils.isEmpty(lastName)) {
            etLastName.setError(REQUIRED);
            return;
        }

        // Birth Date is required
        if (TextUtils.isEmpty(birthDate)) {
            etBirthDate.setError(REQUIRED);
            return;
        }


    }
}
