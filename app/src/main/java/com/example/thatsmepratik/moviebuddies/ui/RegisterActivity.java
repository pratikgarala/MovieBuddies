package com.example.thatsmepratik.moviebuddies.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thatsmepratik.moviebuddies.R;
import com.example.thatsmepratik.moviebuddies.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends BaseActivity {

//    private TextView tvMemberLogin;
    private EditText etEmailID;
    private EditText etPass;
    private EditText etCnfPass;
    private Button btnRegister;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

//        tvMemberLogin = (TextView) this.findViewById(R.id.tvMemberLogin);
        etEmailID = (EditText) this.findViewById(R.id.etEmailId);
        etPass = (EditText) this.findViewById(R.id.etPass);
        etCnfPass = (EditText) this.findViewById(R.id.etCnfPass);
        btnRegister = (Button) this.findViewById(R.id.btnRegister);


//        tvMemberLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
//                startActivity(i);
//            }
//        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPass.getText().toString().equals(etCnfPass.getText().toString())){

//                    Firebase ref = new Firebase(Constants.FIREBASE_URL);
//                    ref.createUser(etEmailID.getText().toString(), etCnfPass.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>() {
//                        @Override
//                        public void onSuccess(Map<String, Object> result) {
//                            System.out.println("Successfully created user account with uid: " + result.get("uid"));
//                        }
//                        @Override
//                        public void onError(FirebaseError firebaseError) {
//                            String errorMsg = getString(R.string.RegisterError);
//                            Toast toast = Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_LONG);
//                            toast.show();
//                        }
//                    });

                    showProgressDialog();
                    String email = etEmailID.getText().toString();
                    String password = etCnfPass.getText().toString();

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                                    hideProgressDialog();

                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Registration Successful..!!",
                                                Toast.LENGTH_SHORT).show();
                                        onAuthSuccess(task.getResult().getUser());

                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Registration Failed",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }else{
                    String errorMsg = getString(R.string.PasswordMismatch);
                    Toast toast = Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        // Write new user
        writeNewUser(user.getUid(), username);

        // Go to MainActivity
        startActivity(new Intent(RegisterActivity.this, ProfileActivity.class));
        finish();
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    // [START basic_write]
    private void writeNewUser(String userId, String name) {
        User user = new User(name);

        mDatabase.child("users").child(userId).setValue(user);
    }
    // [END basic_write]
}
