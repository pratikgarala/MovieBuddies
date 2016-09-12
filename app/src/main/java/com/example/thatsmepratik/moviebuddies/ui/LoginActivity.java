package com.example.thatsmepratik.moviebuddies.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thatsmepratik.moviebuddies.R;
import com.example.thatsmepratik.moviebuddies.models.User;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends BaseActivity {

    private TextView tvRegister;
    private TextView tvForgotPass;
    private EditText etEmailId;
    private EditText etPass;
    private Button btnLogin;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Firebase.setAndroidContext(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        tvRegister = (TextView) this.findViewById(R.id.tvRegister);
        tvForgotPass = (TextView) this.findViewById(R.id.tvForgotPass);
        etEmailId = (EditText) this.findViewById(R.id.etEmailId);
        etPass = (EditText) this.findViewById(R.id.etPass);
        btnLogin = (Button) this.findViewById(R.id.btnLogin);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });

        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,ForgotPassActivity.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Firebase ref = new Firebase(Constants.FIREBASE_URL);
//                ref.authWithPassword(etEmailId.getText().toString(), etPass.getText().toString(), new Firebase.AuthResultHandler() {
//                    @Override
//                    public void onAuthenticated(AuthData authData) {
//                        System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
//                        Intent i = new Intent(LoginActivity.this,MainActivity.class);
//                        startActivity(i);
//                    }
//                    @Override
//                    public void onAuthenticationError(FirebaseError firebaseError) {
//                        // there was an error
//                        String errorMsg = getString(R.string.LoginError);
//                        Toast toast = Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_LONG);
//                        toast.show();
//                    }
//                });

                if (!validateForm()) {
                    return;
                }

                showProgressDialog();
                String email = etEmailId.getText().toString();
                String password = etPass.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                                hideProgressDialog();

                                if (task.isSuccessful()) {
                                    onAuthSuccess(task.getResult().getUser());
                                } else {
                                    Toast.makeText(LoginActivity.this, "Sign In Failed",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        // Check auth on Activity start
        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess(mAuth.getCurrentUser());
        }
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        // Write new user
        writeNewUser(user.getUid(), username);

        // Go to MainActivity
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
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

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(etEmailId.getText().toString())) {
            etEmailId.setError("Required");
            result = false;
        } else {
            etEmailId.setError(null);
        }

        if (TextUtils.isEmpty(etPass.getText().toString())) {
            etPass.setError("Required");
            result = false;
        } else {
            etPass.setError(null);
        }

        return result;
    }
}
