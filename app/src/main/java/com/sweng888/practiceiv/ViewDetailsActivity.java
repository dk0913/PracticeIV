package com.sweng888.practiceiv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import model.PetStore;
import model.PetStoreAdapter;
/*Activity displaying stored information of a pet store in the user's list of favorites*/
public class ViewDetailsActivity extends AppCompatActivity {
    private TextView mStoreNameTV;
    private TextView mStoreAddressTV;
    private ImageView mStoreImage;
    private PetStore selectedPetStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);

        mStoreNameTV = findViewById(R.id.details_text_view_store_name);
        mStoreAddressTV = findViewById(R.id.details_text_view_address);
        mStoreImage = findViewById(R.id.details_store_image);

        selectedPetStore = (PetStore) getIntent().getSerializableExtra("selectedPetStore");
        mStoreNameTV.setText(selectedPetStore.getName());
        mStoreAddressTV.setText(selectedPetStore.getAddress());
        setImageBitmap(selectedPetStore.getImageURL());


    }
    /*sets the image for the store given an image URL*/
    private void setImageBitmap(String imageURL){
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new android.os.Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    InputStream inputStream = new URL(imageURL).openStream();
                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mStoreImage.setImageBitmap(image);
                        }
                    });
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
    }
}