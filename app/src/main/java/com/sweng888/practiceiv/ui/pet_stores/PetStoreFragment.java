package com.sweng888.practiceiv.ui.pet_stores;

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
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sweng888.practiceiv.AddStoreActivity;
import com.sweng888.practiceiv.NavigationDrawerActivity;
import com.sweng888.practiceiv.R;
import com.sweng888.practiceiv.SelectListener;
import com.sweng888.practiceiv.UpdateStoreActivity;
import com.sweng888.practiceiv.ViewDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import model.PetStore;
import model.PetStoreAdapter;

/*PetStoreFragment displays RecyclerView with the user's favorite pet store, and four buttons at the
* bottom for list management, one button for each CRUD operation*/
public class PetStoreFragment extends Fragment implements SelectListener{
    private TextView mTextViewGreetUser;
    private RecyclerView mRecyclerView;
    private DatabaseReference mFirebaseDatabase;

    private PetStoreAdapter mPetStoreAdapter;
    private Button mAddButton;
    private Button mVDButton;
    private Button mUpdateButton;
    private Button mDeleteButton;
    private PetStore selectedPetStore;



    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet_stores, container, false);
        mTextViewGreetUser = view.findViewById(R.id.title_name_TextView);
        mRecyclerView = view.findViewById(R.id.recycler_view_main);
        mAddButton = view.findViewById(R.id.button_add_store);
        mVDButton = view.findViewById(R.id.button_view_details);
        mUpdateButton = view.findViewById(R.id.button_update_store);
        mDeleteButton = view.findViewById(R.id.button_delete_store);
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("Pet Stores");
        //loading list of pet stores and setting adapter and layout manager of RecyclerView
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<PetStore> petStoreList = new ArrayList<>();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    PetStore petStore = dataSnapshot.getValue(PetStore.class);
                    petStoreList.add(petStore);
                }
                mPetStoreAdapter = new PetStoreAdapter(getContext(),petStoreList,PetStoreFragment.this);
                mRecyclerView.setAdapter(mPetStoreAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PetStoreFragment","Error retrieving pet stores from database",
                        error.toException());
            }
        });
        //button click listeners for each CRUD operation button
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddStoreActivity();
            }
        });
        mVDButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToViewDetailsActivity(selectedPetStore);
            }
        });
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUpdateActivity(selectedPetStore);
            }
        });
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteStoreFromDB(selectedPetStore);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }
    /*stores the pet store that the user tapped to update, view details, or delete*/
    @Override
    public void onItemClicked(PetStore petStore) {
        selectedPetStore = petStore;
        Toast.makeText(this.getContext(),petStore.getName()+" has been selected!",Toast.LENGTH_SHORT).show();
    }
    private void goToAddStoreActivity(){
        Intent intent = new Intent(getContext(), AddStoreActivity.class);
        startActivity(intent);
    }

    private void goToUpdateActivity(PetStore selectedPetStore){
        Intent intent = new Intent(getContext(), UpdateStoreActivity.class);
        intent.putExtra("selectedPetStore",selectedPetStore);
        startActivity(intent);
    }
    /*querying the database for the selected pet store and deleting it from the database*/
    private void deleteStoreFromDB(PetStore selectedPetStore){
       Query nameQuery =  mFirebaseDatabase.orderByChild("name").equalTo(selectedPetStore.getName());
       nameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for(DataSnapshot nameSnapshot: dataSnapshot.getChildren()){
                   nameSnapshot.getRef().removeValue();
               }
               Toast.makeText(getContext(),"Pet Store Successfully Deleted",Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               Log.e("Deleting Pet Store From Database","onCancelled", error.toException());
           }
       });
    }

    private void goToViewDetailsActivity(PetStore selectedPetStore){
        Intent intent = new Intent(getContext(), ViewDetailsActivity.class);
        intent.putExtra("selectedPetStore",selectedPetStore);
        startActivity(intent);
    }

}