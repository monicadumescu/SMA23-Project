package com.example.agentie_imobiliara.ui.home_page;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.agentie_imobiliara.R;
import com.example.agentie_imobiliara.adaptors.HousesAdaptor;
import com.example.agentie_imobiliara.model.House;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class HomePageFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private RecyclerView housesRecycleView;
    private HousesAdaptor housesAdaptor;
    private Button filter;

    private DatabaseReference databaseReference;
    private List<House> mHouses;
    private List<House> allHouses;

    Dialog addFilters;
    private String rooms, floors, baths;
    private Spinner spinner_floors, spinner_rooms, spinner_baths;
    private EditText location, special, price, size;
    private Button apply;
    private CheckBox location_check, rooms_check, size_check, floors_check, baths_check, special_check, price_check;
    private boolean is_location_check = false, is_rooms_check = false, is_size_check = false, is_floors_check = false, is_baths_check = false, is_special_check = false, is_price_check = false;

    public HomePageFragment() {
        // Required empty public constructor
    }

    public static HomePageFragment newInstance(String param1, String param2) {
        HomePageFragment fragment = new HomePageFragment();
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
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        housesRecycleView = view.findViewById(R.id.recycler_view);
        housesRecycleView.setHasFixedSize(true);
        housesRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        filter = view.findViewById(R.id.filter_data);

        mHouses = new ArrayList<>();
        allHouses = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("House");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    House house = dataSnapshot.getValue(House.class);
                    house.setKey(dataSnapshot.getKey());
                    mHouses.add(house);
                    allHouses.add(house);
                }

                housesAdaptor = new HousesAdaptor(getContext(), mHouses);
                housesRecycleView.setAdapter(housesAdaptor);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFilterDialog();
            }
        });

        return view;
    }

    private void openFilterDialog() {
        addFilters = new Dialog(getContext());
        addFilters.setContentView(R.layout.filters_layout);
        addFilters.setTitle("Add filters");

        location = addFilters.findViewById(R.id.location);
        special = addFilters.findViewById(R.id.special_req);
        apply = addFilters.findViewById(R.id.apply);

        location_check = addFilters.findViewById(R.id.location_check);
        size = addFilters.findViewById(R.id.size_t);
        rooms_check = addFilters.findViewById(R.id.rooms_check);
        baths_check = addFilters.findViewById(R.id.baths_check);
        floors_check = addFilters.findViewById(R.id.floors_check);
        special_check = addFilters.findViewById(R.id.special_check);
        price_check = addFilters.findViewById(R.id.price_check);
        size_check = addFilters.findViewById(R.id.min_size_check);
        price = addFilters.findViewById(R.id.price_t);

        is_location_check = false;
        is_rooms_check = false;
        is_size_check = false;
        is_floors_check = false;
        is_baths_check = false;
        is_special_check = false;
        is_price_check = false;

        spinner_floors = (Spinner) addFilters.findViewById(R.id.floors_no);
        ArrayAdapter<CharSequence> adapter_floors = ArrayAdapter.createFromResource(addFilters.getContext(),
                R.array.floors_spinner, android.R.layout.simple_spinner_item);

        adapter_floors.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_floors.setAdapter(adapter_floors);

        spinner_floors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                floors = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getContext(), "LOOOOL", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                floors = adapterView.getItemAtPosition(0).toString();
            }
        });


        spinner_baths = (Spinner) addFilters.findViewById(R.id.baths_no);
        ArrayAdapter<CharSequence> adapter_baths = ArrayAdapter.createFromResource(getContext(),
                R.array.baths_spinner, android.R.layout.simple_spinner_item);

        adapter_baths.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_baths.setAdapter(adapter_baths);

        spinner_baths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                baths = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                baths = adapterView.getItemAtPosition(0).toString();
            }
        });


        spinner_rooms = (Spinner) addFilters.findViewById(R.id.rooms_no);
        ArrayAdapter<CharSequence> adapter_rooms = ArrayAdapter.createFromResource(getContext(),
                R.array.room_spinner, android.R.layout.simple_spinner_item);

        adapter_rooms.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_rooms.setAdapter(adapter_rooms);

        spinner_rooms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                rooms = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                rooms = adapterView.getItemAtPosition(0).toString();
            }
        });

        location_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                is_location_check = !is_location_check;
            }
        });

        size_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                is_size_check = !is_size_check;
            }
        });

        rooms_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                is_rooms_check = !is_rooms_check;
            }
        });

        floors_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                is_floors_check = !is_floors_check;
            }
        });

        special_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                is_special_check = !is_special_check;
            }
        });

        price_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                is_price_check = !is_price_check;
            }
        });


        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<House> filteredHouses = new ArrayList<>();
                filteredHouses.addAll(allHouses);

                if (is_location_check) {
                    filteredHouses = filteredHouses.stream().filter(house -> house.getAddress().contains(location.getText())).collect(Collectors.toList());
                }
                if (is_size_check) {
                    filteredHouses = filteredHouses.stream().filter(house -> Integer.parseInt(house.getSize()) >= (Integer.parseInt(size.getText().toString()) - 50) && Integer.parseInt(house.getSize()) <= (Integer.parseInt(size.getText().toString()) + 50)).collect(Collectors.toList());
                }
                if (is_rooms_check) {
                    filteredHouses = filteredHouses.stream().filter(house -> house.getRooms().equals(rooms)).collect(Collectors.toList());
                }
                if (is_floors_check) {
                    filteredHouses = filteredHouses.stream().filter(house -> house.getFloors().equals(floors)).collect(Collectors.toList());
                }
                if (is_special_check) {
                    filteredHouses = filteredHouses.stream().filter(house -> house.getSpecial().contains(special.getText())).collect(Collectors.toList());
                }
                if (is_price_check) {
                    filteredHouses = filteredHouses.stream().filter(house -> Integer.parseInt(house.getPrice()) >= (Integer.parseInt(price.getText().toString()) - 500) && Integer.parseInt(house.getPrice()) <= (Integer.parseInt(price.getText().toString()) + 500)).collect(Collectors.toList());
                }
                if (filteredHouses.isEmpty()) {
                    Toast.makeText(getContext(), "Sorry... There are no houses like this!", Toast.LENGTH_LONG).show();
                }
                housesAdaptor.setmUploads(filteredHouses);
                housesAdaptor.notifyDataSetChanged();
                addFilters.cancel();
            }
        });

        addFilters.setCancelable(true);
        addFilters.show();

    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.location_check:
                is_location_check = !is_location_check;
                break;
            case R.id.min_size_check:
                is_size_check = !is_size_check;
                break;
            case R.id.rooms_check:
                is_rooms_check = !is_rooms_check;
                break;
            case R.id.baths_check:
                is_baths_check = !is_baths_check;
                break;
            case R.id.floors_check:
                is_floors_check = !is_floors_check;
                break;
            case R.id.special_check:
                is_special_check = !is_special_check;
                break;
            case R.id.price_check:
                is_price_check = !is_price_check;
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (view.getId()) {
            case R.id.floors_no:
                floors = adapterView.getItemAtPosition(i).toString();
                break;
            case R.id.rooms_no:
                rooms = adapterView.getItemAtPosition(i).toString();
                break;
            case R.id.baths_no:
                baths = adapterView.getItemAtPosition(i).toString();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}