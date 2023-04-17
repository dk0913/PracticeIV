package com.sweng888.practiceiv.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sweng888.practiceiv.LoginActivity;
import com.sweng888.practiceiv.R;
import com.sweng888.practiceiv.databinding.FragmentHomeBinding;
import com.sweng888.practiceiv.ui.pet_stores.PetStoreFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import model.PetStore;
import model.PetStoreAdapter;
import model.User;
/*Home fragment displays user's profile pic, name, and email and a button for user to sign out*/
public class HomeFragment extends Fragment{
    private DatabaseReference mFirebaseDatabaseUsers;
    private String email;
    private User currentUser;
    private CircleImageView mImageView;
    private TextView mNameTextView;
    private TextView mEmailTextView;
    private Button mSignOutButton;
    private List<User> userList = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mNameTextView = view.findViewById(R.id.account_name);
        mEmailTextView = view.findViewById(R.id.account_email);
        mSignOutButton = view.findViewById(R.id.sign_out_Button);
        /*initializing an instance of the Users JSON tree stored in RealtimeDB*/
        mFirebaseDatabaseUsers = FirebaseDatabase.getInstance().getReference("Users");
        /*adding a listener for any updates to data in the user tree stored in RealtimeDB
        * and updating a local list*/
        mFirebaseDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("HomeFragment","Error retrieving user from database",
                        error.toException());
            }
        });
        mFirebaseDatabaseUsers.orderByChild("name").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                userList.add(user);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*getting user data to populate whether the fragment is passed the user's email from the
        * login activity or a User object from the sign up activity*/
        if(email!=null) {
            getUserData(email);
        }else{
           if(currentUser!= null) {
               mNameTextView.setText(currentUser.getName());
               mEmailTextView.setText(currentUser.getEmail());
           }else{
               email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
               getUserData(email);
           }
       }
        /*button for the user to sign out using firebase authentication and takes the user back
        * to the login activity*/
        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void setEmail(String email){
        this.email = email;
    }
    /* getting user data from the passed email to populate name and email*/
    private void getUserData(String email){
        mFirebaseDatabaseUsers = FirebaseDatabase.getInstance().getReference("Users");
        String userName;
        if(email.equals("dkatz122@gmail.com")){
            userName = "dkatz";
        }else if (email.equals("sweng888@psugv.edu")){
            userName = "swengclass";
        }else{
            userName = "invalidUserName";
        }
        mFirebaseDatabaseUsers.child(userName).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        Toast.makeText(getContext(),"Successfully Read",Toast.LENGTH_SHORT).show();
                        DataSnapshot dataSnapshot = task.getResult();
                        String name = String.valueOf(dataSnapshot.child("name").getValue());
                        mNameTextView.setText(name);
                        mEmailTextView.setText(email);
                        currentUser = new User(name,email);
                    }else{
                        Toast.makeText(getContext(),"User does not exist",Toast.LENGTH_SHORT).show();

                    }

                }else{
                    Toast.makeText(getContext(),"Failed to read",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}