package com.sweng888.practiceiv;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import model.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEditTextUser;
    private EditText mEditTextPassword;
    private Button mLoginButton;
    private Button mSignUpButton;

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mFirebaseDatabaseUsers;
    private List<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditTextUser = findViewById(R.id.edit_text_user);
        mEditTextPassword = findViewById(R.id.edit_text_password);
        mLoginButton = findViewById(R.id.button_sign_in);
        mSignUpButton = findViewById(R.id.button_sign_up);

        mLoginButton.setOnClickListener(this);
        mSignUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        /** Implement the integration with Firebase Authentication */
        String email = mEditTextUser.getText().toString();
        String password = mEditTextPassword.getText().toString();

        /** Create an instance of the Firebase Authentication component */
        mFirebaseAuth = FirebaseAuth.getInstance();

        /** Check if the user email and password are entered*/
        if (TextUtils.isEmpty(email)) {
            mEditTextUser.setHint("An email is required.");
            mEditTextUser.setHintTextColor(Color.RED);
            //return;
        }

        if (TextUtils.isEmpty(password)) {
            mEditTextPassword.setHint("Please type the correct password.");
            mEditTextPassword.setHintTextColor(Color.RED);
            //return;
        }
        switch (v.getId()){
            case R.id.button_sign_in: SignIn(email, password); break;
            case R.id.button_sign_up: SignUp(); break;
        }
    }
    /*uses Firebase Authenticatin to sign in an existing user and pass their email to the navigation
    * drawer activity*/
    private void SignIn(String email, String password){
        mFirebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this,"Logging in...",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this,NavigationDrawerActivity.class);
                            intent.putExtra("email",email);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this,"AUTHENTICATION FAILED",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    /*launches the sign up page for a new user to create an account*/
    private void SignUp(){
        Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
        startActivity(intent);
    }



}