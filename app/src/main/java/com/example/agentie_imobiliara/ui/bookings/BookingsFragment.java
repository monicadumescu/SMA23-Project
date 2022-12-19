package com.example.agentie_imobiliara.ui.bookings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.agentie_imobiliara.R;
import com.example.agentie_imobiliara.adaptors.BookingsAdaptor;
import com.example.agentie_imobiliara.adaptors.HousesAdaptor;
import com.example.agentie_imobiliara.model.Booking;
import com.example.agentie_imobiliara.model.Date;
import com.example.agentie_imobiliara.model.House;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class BookingsFragment extends Fragment {

    private RecyclerView bookingRecyclieView;
    private BookingsAdaptor bookingsAdaptor;

    private DatabaseReference databaseReference, houseDatabaseReference;
    private List<Booking> mBookings;

    FirebaseAuth authAction=FirebaseAuth.getInstance();


    public BookingsFragment() {
        // Required empty public constructor
    }

    public static BookingsFragment newInstance(String param1, String param2) {
        BookingsFragment fragment = new BookingsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_bookings, container, false);

        bookingRecyclieView = view.findViewById(R.id.recycler_view_bookings);
        bookingRecyclieView.setHasFixedSize(true);
        bookingRecyclieView.setLayoutManager(new LinearLayoutManager(getContext()));

        mBookings = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Booking");
        houseDatabaseReference = FirebaseDatabase.getInstance().getReference("House");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Booking booking = dataSnapshot.getValue(Booking.class);
                    houseDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                House house = dataSnapshot1.getValue(House.class);
                                if (dataSnapshot1.getKey().equals(booking.getHouse_key())) {
                                    if (house.getOwner().equals(authAction.getCurrentUser().getEmail())) {
                                        booking.setObject_key(dataSnapshot.getKey());
                                        mBookings.add(booking);
                                    }

                                }
                            }
                            bookingsAdaptor = new BookingsAdaptor(getContext(), mBookings);
                            bookingRecyclieView.setAdapter(bookingsAdaptor);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return  view;
    }
}