package com.sweng888.practiceiv;

import model.PetStore;
/*Custom listener interface for obtaining the product model that the user has tapped within a
 * RecyclerView*/
public interface SelectListener {
    void onItemClicked(PetStore petStore);
}