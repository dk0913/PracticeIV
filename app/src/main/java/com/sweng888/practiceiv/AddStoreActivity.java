package com.sweng888.practiceiv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.PetStore;
/*Activity for a user to add a new pet store to their list of favorite stores*/
public class AddStoreActivity extends AppCompatActivity {
    private EditText mEditTextName;
    private EditText mEditTextAddress;
    private EditText mEditTextImageURL;
    private Button mConfirmButton;
    private Button mClearButton;
    private DatabaseReference mDBRef;
    /*sets up activity for user to enter pet store name, address, and imageURL and add it to their
    * list of favorite stores*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_store);

        mDBRef= FirebaseDatabase.getInstance().getReference().child("Pet Stores");
        mEditTextName = findViewById(R.id.home_edit_store_name);
        mEditTextAddress = findViewById(R.id.home_edit_address);
        mEditTextImageURL = findViewById(R.id.home_edit_image_URL);
        mConfirmButton = findViewById(R.id.button_confirm);
        mClearButton = findViewById(R.id.button_clear);

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
    /*adds the user-entered store with its fields into Realtime DB and clears the EditTexts*/
    public void confirm(){
        String name = mEditTextName.getText().toString().trim();
        String address = mEditTextAddress.getText().toString().trim();
        String imageURL = mEditTextImageURL.getText().toString().trim();

        if(name.isEmpty()||address.isEmpty()||imageURL.isEmpty()){
            Toast.makeText(AddStoreActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }else{
            PetStore newPetStore = new PetStore(address,imageURL,name);
            insertPetStore(newPetStore);
            Toast.makeText(AddStoreActivity.this,"New Pet Store Added", Toast.LENGTH_SHORT).show();
            clearFields();
        }

    }
    //clears all user input
    public void clearFields(){
        mEditTextName.setText("");
        mEditTextAddress.setText("");
        mEditTextImageURL.setText("");
    }
    //handles insertion of new store information to Realtime DB
    public void insertPetStore(PetStore newPetStore){
        mDBRef.push().setValue(newPetStore);
        Toast.makeText(AddStoreActivity.this, "Pet Store has been inserted in database",Toast.LENGTH_SHORT).show();
    }
}