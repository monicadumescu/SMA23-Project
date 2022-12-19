package com.example.agentie_imobiliara.ui.likedHouses;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.agentie_imobiliara.R;
import com.example.agentie_imobiliara.adaptors.HousesAdaptor;
import com.example.agentie_imobiliara.adaptors.LikedHousesAdaptor;
import com.example.agentie_imobiliara.model.House;
import com.example.agentie_imobiliara.model.SavedHouses;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LikedHousesFragment extends Fragment {

    private RecyclerView housesRecycleView;
    private LikedHousesAdaptor housesAdaptor;

    private List<House> mHouses;
    FirebaseAuth authAction=FirebaseAuth.getInstance();

    public LikedHousesFragment() {
        // Required empty public constructor
    }

    public static LikedHousesFragment newInstance(String param1, String param2) {
        LikedHousesFragment fragment = new LikedHousesFragment();
        Bundle args = new Bundle();
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
        View view = inflater.inflate(R.layout.fragment_liked_houses, container, false);

        housesRecycleView = view.findViewById(R.id.recycler_view_liked_houses);
        housesRecycleView.setHasFixedSize(true);
        housesRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        mHouses = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SavedHouses");
        DatabaseReference databaseReferenceHouses = FirebaseDatabase.getInstance().getReference("House");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    SavedHouses savedHouses = dataSnapshot.getValue(SavedHouses.class);
                   if(savedHouses.getEmail().equals(authAction.getCurrentUser().getEmail()))
                   {
                        databaseReferenceHouses.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                for(DataSnapshot dataSnapshot : snapshot2.getChildren())
                                {
                                    House house = dataSnapshot.getValue(House.class);
                                    if(savedHouses.getKey().equals(dataSnapshot.getKey()))
                                    {
                                        house.setKey(dataSnapshot.getKey());
                                        mHouses.add(house);
                                    }
                                    housesAdaptor = new LikedHousesAdaptor(getContext(), mHouses);
                                    housesRecycleView.setAdapter(housesAdaptor);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
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