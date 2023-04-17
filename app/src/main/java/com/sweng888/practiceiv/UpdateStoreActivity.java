package com.sweng888.practiceiv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import model.PetStore;
import model.User;
/*Activity for user to modify information stored for one of their favorite pet stores*/
public class UpdateStoreActivity extends AppCompatActivity {
    private EditText mEditTextName;
    private EditText mEditTextAddress;
    private EditText mEditTextImageURL;
    private Button mConfirmButton;
    private Button mClearButton;
    private DatabaseReference mDBRef;
    private PetStore petStoreBeingUpdated;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_store);

        mDBRef= FirebaseDatabase.getInstance().getReference().child("Pet Stores");
        mEditTextName = findViewById(R.id.update_edit_store_name);
        mEditTextAddress = findViewById(R.id.update_edit_address);
        mEditTextImageURL = findViewById(R.id.update_edit_image_URL);
        mConfirmButton = findViewById(R.id.update_button_confirm);
        mClearButton = findViewById(R.id.update_button_clear);

        /*Gets selected pet store from intent extra and set EditTexts text to existing values for
        the pet store being updated
         */
        petStoreBeingUpdated = (PetStore) getIntent().getSerializableExtra("selectedPetStore");
        mEditTextName.setText(petStoreBeingUpdated.getName());
        mEditTextAddress.setText(petStoreBeingUpdated.getAddress());
        mEditTextImageURL.setText(petStoreBeingUpdated.getImageURL());



        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm();
            }
        });
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFields();
            }
        });
    }
    /*Overwrites existing pet store data in DB with user input*/
    public void confirm(){
        String name = mEditTextName.getText().toString().trim();
        String address = mEditTextAddress.getText().toString().trim();
        String imageURL = mEditTextImageURL.getText().toString().trim();

        if(name.isEmpty()||address.isEmpty()||imageURL.isEmpty()){
            Toast.makeText(UpdateStoreActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }else{
            PetStore modifiedPetStore = new PetStore(address,imageURL,name);
            updatePetStore(modifiedPetStore);
            Toast.makeText(UpdateStoreActivity.this,"New Pet Store Added", Toast.LENGTH_SHORT).show();
            clearFields();
        }
    }
    public void clearFields(){
        mEditTextName.setText("");
        mEditTextAddress.setText("");
        mEditTextImageURL.setText("");
    }
    /*Finds the selected pet store in Realtime DB using a query and then overwrites all fields
    * with user input*/
    private  void updatePetStore(PetStore modifiedPetStore){
        Map<String, Object> updatedPetStoreFields = modifiedPetStore.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        String nameToSearch = petStoreBeingUpdated.getName();
        mDBRef = FirebaseDatabase.getInstance().getReference("Pet Stores");
        Query nameQuery =  mDBRef.orderByChild("name").equalTo(nameToSearch);
        nameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    PetStore petStore = snapshot.getValue(PetStore.class);
                    if(petStore.getName().equals(nameToSearch)){
                        String key = snapshot.getKey();
                        childUpdates.put( key, updatedPetStoreFields);
                        mDBRef.updateChildren(childUpdates);
                        Toast.makeText(UpdateStoreActivity.this,"Pet Store Successfully Updated",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}