package com.sweng888.practiceiv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sweng888.practiceiv.ui.pet_stores.PetStoreFragment;
import com.sweng888.practiceiv.ui.home.HomeFragment;

import model.PetStore;
import model.User;

public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private NavigationView mNavigationView;
    private MaterialToolbar mToolBar;
    private TextView mNameTV;
    private TextView mEmailTV;
    private User currentUser;
    private DatabaseReference mFirebaseDatabaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_navigation_drawer);


        /** Step 2: Get the DrawerLayout object from the layout XML file */
        mDrawerLayout = findViewById(R.id.nav_drawer_layout);



        /** Step 3: Get the NavigationView object from the layout SML file */
        mNavigationView = findViewById(R.id.navigation_view);
        mToolBar = findViewById(R.id.topAppBar);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });


        /** Step 4: Set the listener for the NvigationView. The Main Activity shuould
         * imeplement the interface NavigationView.OnNavigationItemSelectedListener */
        mNavigationView.setNavigationItemSelectedListener(this);

        /** Step 5: Set up the ActionBarDrawerToggle */
        mActionBarDrawerToggle = new ActionBarDrawerToggle(
                this, // Activity / Context
                mDrawerLayout, // DrawerLayout
                R.string.navigation_drawer_open, // String to open
                R.string.navigation_drawer_close // String to close
        );

        /** Step 6: Include the mActionBarDrawerToggle as the listener to the DrawerLayout.
         *  The synchState() method is used to synchronize the state of the navigation drawer */
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();



        /** Get the email passed as intent extra from login activity, or user passed as serializable
         * extra from sign up activity */

        String email = getIntent().getStringExtra("email");
        if (email!=null) {
            getUserData2(email);
            //updateNavHeader();

        }else{
            currentUser = (User) getIntent().getSerializableExtra("user");
            email = currentUser.getEmail();
            //updateNavHeader();
        }


        /** Step 7:Set the user information for a new home fragment as the default fragment */
        HomeFragment defaultFragment = new HomeFragment();

        defaultFragment.setEmail(email);

        defaultFragment.setCurrentUser(currentUser);


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, defaultFragment).commit();



    }
    //creates either home or pet store fragments when a user taps the corresponding navigation item
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        item.setChecked(true);

        switch (id) {
            case R.id.nav_home:
                replaceFragment(new HomeFragment());
                break;
            case R.id.nav_pet_stores:
                replaceFragment(new PetStoreFragment());
                break;
        }

        // Close the navigation drawer
        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    //inflates the navigation menu items
    @Override
   public boolean onCreateOptionsMenu(Menu menu) {
       //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
   }
    //not used yet
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        if (id == com.google.android.gms.auth.api.R.id.action0) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
        return false;
    }
    //toggles actionbardrawer between the 2 different configurations (open and closed)
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }
    //syncs the open/closed state of the navigation drawer
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }
    //replaces the current fragment shown with the fragment the user selects to open in the navigation
    //drawer
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
    //not used for now
    private void updateNavHeader(){
        View headerView = mNavigationView.getHeaderView(0);
        mNameTV = headerView.findViewById(R.id.nav_drawer_header_title);
        mEmailTV = headerView.findViewById(R.id.nav_drawer_header_subtitle);
        mNameTV.setText(currentUser.getName());
        mEmailTV.setText(currentUser.getEmail());
    }
    //not used for now
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
                        Toast.makeText(NavigationDrawerActivity.this,"Successfully Read",Toast.LENGTH_SHORT).show();
                        DataSnapshot dataSnapshot = task.getResult();
                        String name = String.valueOf(dataSnapshot.child("name").getValue());

                        currentUser = new User(name,email);
                    }else{
                        Toast.makeText(NavigationDrawerActivity.this,"User does not exist",Toast.LENGTH_SHORT).show();

                    }

                }else{
                    Toast.makeText(NavigationDrawerActivity.this,"Failed to read",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /*retrieves the signed in user's data to populate in navigation drawer header that will be implemented
    later*/
    private void getUserData2(String email){
        String userName;
        if(email.equals("dkatz122@gmail.com")){
            userName = "dkatz";
        }else if (email.equals("sweng888@psugv.edu")){
            userName = "swengclass";
        }else{
            userName = "invalidUserName";
        }
        mFirebaseDatabaseUsers = FirebaseDatabase.getInstance().getReference("Users").child(userName);
        Query nameQuery =  mFirebaseDatabaseUsers.orderByChild("email").equalTo(email);
        nameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot emailSnapshot: dataSnapshot.getChildren()){
                    String name = emailSnapshot.child("name").getValue().toString();
                    currentUser = new User(name,email);
                }
                Toast.makeText(NavigationDrawerActivity.this,"User Read",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Deleting Pet Store From Database","onCancelled", error.toException());
            }
        });
    }
}