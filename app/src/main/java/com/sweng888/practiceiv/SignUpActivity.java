package com.sweng888.practiceiv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.PetStore;
import model.User;

public class SignUpActivity extends AppCompatActivity {
    private EditText mNameEditText;
    private EditText mUserNameEditText;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mCreateAccountButton;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mNameEditText = findViewById(R.id.edit_text_nameSignUp);
        mUserNameEditText = findViewById(R.id.edit_text_userSignUp);
        mEmailEditText = findViewById(R.id.edit_text_emailSignUp);
        mPasswordEditText = findViewById(R.id.edit_text_passwordSignUp);
        mCreateAccountButton = findViewById(R.id.button_sign_up);
        mFirebaseAuth = FirebaseAuth.getInstance();
        /*Create account button click triggers retrieval of all the user-entered information and
        * passes to signUp method*/
        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mNameEditText.getText().toString();
                String userName = mUserNameEditText.getText().toString();
                String email = mEmailEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                signUp(name,userName, email, password);


            }
        });

    }
    /*adds new information to Realtime DB and launches NavigationDrawer activity*/
    private void signUp(String name,String userName,String email, String password){
        mFirebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(name, email);
                            addUserToDatabase(userName,user);
                            Intent intent = new Intent(SignUpActivity.this,NavigationDrawerActivity.class);
                            intent.putExtra("user", user);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

//    public void addUserToDatabase(String userID,String name, String email){
//        User user = new User(name,email);
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase.child("Users").child(userID).setValue(user);
//    }
    /*handles the insert operation of user data into Realtime DB user tree*/
    public void addUserToDatabase(String userID,User user){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(userID).setValue(user);
    }
}