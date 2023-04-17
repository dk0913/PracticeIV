package model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class PetStoreDatabaseHelper {
    private List<PetStore> petStoreList;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Pet Stores");
    public PetStoreDatabaseHelper(){}


//    public void addPetStore(String petStoreID,String name, String address, String imageURL){
//        PetStore petStore = new PetStore(name,address,imageURL);
//        mDatabase.child("Pet Stores").child(petStoreID).setValue(petStore);
//    }
//    public void addPetStore(PetStore newPetStore){
//        mDatabase.push().setValue(newPetStore);
//        Toast.makeText(, "Pet Store has been inserted in database",Toast.LENGTH_SHORT).show();
//    }


}

