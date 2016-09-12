package com.example.thatsmepratik.moviebuddies.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thatsmepratik.moviebuddies.R;
import com.example.thatsmepratik.moviebuddies.models.Constants;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class ForgotPassActivity extends AppCompatActivity {

    private EditText etEmailId;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        etEmailId = (EditText) this.findViewById(R.id.etEmailId);
        btnSend = (Button) this.findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase ref = new Firebase(Constants.FIREBASE_URL);
                ref.resetPassword(etEmailId.getText().toString(), new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        // password reset email sent
                        String successMsg = getString(R.string.PassResetLinkSent);
                        Toast toast = Toast.makeText(ForgotPassActivity.this, successMsg, Toast.LENGTH_LONG);
                        toast.show();
                    }
                    @Override
                    public void onError(FirebaseError firebaseError) {
                        // error encountered
                        String errorMsg = "Error in sending mail..!!";
                        Toast toast = Toast.makeText(ForgotPassActivity.this, errorMsg, Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
            }
        });
    }
}
