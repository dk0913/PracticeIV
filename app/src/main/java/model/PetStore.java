package model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/*data model for pet stores*/
public class PetStore implements Serializable {
    private String address;
    private String imageURL;
    private String name;

    public PetStore(String address, String imageURL, String name){
        this.name = name;
        this.address = address;
        this.imageURL = imageURL;
    }

    public PetStore(){}
    //turns a data model object into a hashmap for updating database
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("address", address);
        result.put("imageURL", imageURL);
        result.put("name", name);

        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
