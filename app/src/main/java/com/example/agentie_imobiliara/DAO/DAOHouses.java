package com.example.agentie_imobiliara.DAO;

import com.example.agentie_imobiliara.model.House;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DAOHouses {
    private DatabaseReference databaseReference;

    public  DAOHouses()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference(House.class.getSimpleName());
    }

    public Task<Void> addHouse(House house)
    {
        return databaseReference.push().setValue(house);
    }

    public Task<Void> editHouse(String key, House house)
    {
       return databaseReference.child(key).setValue(house);
    }

    public Task<Void> deleteHouse(String key)
    {
        return databaseReference.child(key).removeValue();
    }
}
