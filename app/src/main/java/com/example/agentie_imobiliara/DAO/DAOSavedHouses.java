package com.example.agentie_imobiliara.DAO;

import com.example.agentie_imobiliara.model.House;
import com.example.agentie_imobiliara.model.SavedHouses;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DAOSavedHouses {

    private DatabaseReference databaseReference;

    public  DAOSavedHouses()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference(SavedHouses.class.getSimpleName());
    }

    public Task<Void> addHouse(SavedHouses house)
    {
        return databaseReference.push().setValue(house);
    }

    public Task<Void> deleteHouse(String key)
    {
        return databaseReference.child(key).removeValue();
    }
}
