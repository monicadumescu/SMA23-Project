package com.example.agentie_imobiliara.DAO;

import com.example.agentie_imobiliara.model.Booking;
import com.example.agentie_imobiliara.model.House;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DAOBooking {
    private DatabaseReference databaseReference;

    public DAOBooking()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference(Booking.class.getSimpleName());
    }

    public Task<Void> addBooking(Booking booking)
    {
        return databaseReference.push().setValue(booking);
    }

    public Task<Void> editBooking(String key, Booking booking)
    {
        return  databaseReference.child(key).setValue(booking);
    }

    public Task<Void> deleteBooking(String key)
    {
        return databaseReference.child(key).removeValue();
    }
}
