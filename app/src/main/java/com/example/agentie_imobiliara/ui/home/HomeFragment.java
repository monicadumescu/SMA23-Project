package com.example.agentie_imobiliara.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.agentie_imobiliara.R;
import com.example.agentie_imobiliara.adaptors.HousesAdaptor;
import com.example.agentie_imobiliara.databinding.FragmentHomeBinding;
import com.example.agentie_imobiliara.model.House;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private RecyclerView housesRecycleView;
    private HousesAdaptor housesAdaptor;

    private DatabaseReference databaseReference;
    private List<House> mHouses;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        housesRecycleView = root.findViewById(R.id.recycler_view);
        housesRecycleView.setHasFixedSize(true);
        housesRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        mHouses = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("House");
        
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    House house = dataSnapshot.getValue(House.class);
                    mHouses.add(house);
                }

                housesAdaptor = new HousesAdaptor(getContext(), mHouses);
                housesRecycleView.setAdapter(housesAdaptor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}