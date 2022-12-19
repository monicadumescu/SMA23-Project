package com.example.agentie_imobiliara.ui.yourHouses;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.agentie_imobiliara.AddHousesActivity;
import com.example.agentie_imobiliara.DAO.DAOHouses;
import com.example.agentie_imobiliara.MainPage;
import com.example.agentie_imobiliara.R;
import com.example.agentie_imobiliara.adaptors.HousesAdaptor;
import com.example.agentie_imobiliara.adaptors.YourHousesAdaptor;
import com.example.agentie_imobiliara.model.House;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class YourHousesFragment extends Fragment{

    private RecyclerView housesRecycleView;
    private YourHousesAdaptor yourHousesAdaptor;

    private DatabaseReference databaseReference;
    private List<House> mHouses;

    public YourHousesFragment() {
        // Required empty public constructor
    }


    public static YourHousesFragment newInstance(String param1, String param2) {
        YourHousesFragment fragment = new YourHousesFragment();
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
        View view = inflater.inflate(R.layout.fragment_your_houses, container, false);

        Button addHouseButton = (Button) view.findViewById(R.id.addHouses);


        addHouseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddHouses();
            }
        });

        FirebaseAuth authAction=FirebaseAuth.getInstance();

        housesRecycleView = view.findViewById(R.id.recycler_view_yourHouses);
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
                    if(house.getOwner().equals(authAction.getCurrentUser().getEmail())) {
                        house.setKey(dataSnapshot.getKey());
                        mHouses.add(house);

                    }
                }

                yourHousesAdaptor = new YourHousesAdaptor(getContext(), mHouses);
                housesRecycleView.setAdapter(yourHousesAdaptor);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return  view;
    }
    public void openAddHouses() {
        startActivity(new Intent(getContext(), AddHousesActivity.class));
    }

}