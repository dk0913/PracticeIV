package model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sweng888.practiceiv.R;
import com.sweng888.practiceiv.SelectListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/*PetStoreAdapter class adapts a list of pet food PetStore data model objects to be displayed and selected in
 * a RecyclerView */
public class PetStoreAdapter extends RecyclerView.Adapter<PetStoreAdapter.ViewHolder> {
    private List<PetStore> petStores;

    private View.OnClickListener onClickListener;
    private SelectListener listener;
    private Context context;

    /*Constructor for PetStoreAdapter with a listener for user selecting items used in MainActivity*/
    public PetStoreAdapter(Context context,List<PetStore> PetStores, SelectListener listener){
        this.context = context;
        this.petStores = PetStores;
        this.listener = listener;
    }
    /*Constructor for PetStoreAdapter without a listener for user selecting items used in SecondActivity*/
    public PetStoreAdapter(Context context,List<PetStore> PetStores){
        this.context = context;
        this.petStores = PetStores;
    }
    public PetStoreAdapter(List<PetStore> petStores){
        this.petStores = petStores;
    }
    /*inflates PetStore_item layout when ViewHolder is created for displaying each individual PetStore
     * in RecyclerView*/
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_store_item, parent, false);
        return new ViewHolder(view);
    }
    /*binds each view in PetStore_item layout to the corresponding field in the PetStore data model
     * object*/
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PetStore PetStore = petStores.get(position);
        holder.nameTextView.setText("Store Name: "+ PetStore.getName());
        holder.addressTextView.setText("Store Address: "+PetStore.getAddress());

        /*uses helper method below to set set the Bitmap for the item's imageView with the URL stored
         * in the PetStore data model object*/
        setImageBitmap(PetStore.getImageURL(),holder);
        /*sets click listener for user tapping on a CardView container to get the item at the
        Recyclerview position and the PetStore in the PetStores list at that position*/
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(petStores.get(holder.getAdapterPosition()));
            }
        });
    }

    /* method to add a new pet store to the list and update petStores list, this will
    * dynamically update the RecyclerView to incorporate the new pet store*/
    public void addPetStore(PetStore petStore){
        petStores.add(petStore);
        notifyDataSetChanged();
    }

    //returns the size of the PetStores list displayed in the RecyclerView
    @Override
    public int getItemCount() {
        if (petStores != null) {
            return petStores.size();
        }else{
            return 0;
        }
    }

    //declares and wires up each view for a PetStore item
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView nameTextView,addressTextView;
        public ImageView imageView;
        public CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.TextView_Name);
            addressTextView = itemView.findViewById(R.id.TextView_Address);
            imageView = itemView.findViewById(R.id.ImageView);
            cardView = itemView.findViewById(R.id.main_container);
        }
    }
    /*helper method for handling setting the Bitmap of the imageView of the ViewHolder for the item to a
    Bitmap created by retrieving the image on a different thread at the stored URL for the PetStore image*/
    private void setImageBitmap(String imageURL, ViewHolder holder){
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
                            holder.imageView.setImageBitmap(image);
                        }
                    });
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
    }

}
