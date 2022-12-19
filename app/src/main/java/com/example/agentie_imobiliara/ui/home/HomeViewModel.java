package com.example.agentie_imobiliara.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agentie_imobiliara.adaptors.HousesAdaptor;
import com.example.agentie_imobiliara.model.House;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private RecyclerView housesRecycleView;
    private HousesAdaptor housesAdaptor;

    private DatabaseReference databaseReference;
    private List<House> mHouses;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
        housesRecycleView.setAdapter(housesAdaptor);
    }

    public LiveData<String> getText() {
        return mText;
    }
}