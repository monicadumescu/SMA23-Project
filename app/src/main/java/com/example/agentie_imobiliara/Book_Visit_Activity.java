package com.example.agentie_imobiliara;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.agentie_imobiliara.DAO.DAOBooking;
import com.example.agentie_imobiliara.DAO.DAOHouses;
import com.example.agentie_imobiliara.adaptors.HousesAdaptor;
import com.example.agentie_imobiliara.adaptors.YourHousesAdaptor;
import com.example.agentie_imobiliara.model.Booking;
import com.example.agentie_imobiliara.model.Date;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class Book_Visit_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String hour;
    private Date date_from_calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_visit);

        Intent intent = getIntent();
        String house_key = intent.getStringExtra(HousesAdaptor.EXTRA_TEXT);
        String house_address = intent.getStringExtra(HousesAdaptor.EXTRA_ADDRESS);

        Button save_booking = (Button) findViewById(R.id.save_booking);
        CalendarView calendarView = (CalendarView) findViewById(R.id.date);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.hour_spinner, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

        FirebaseAuth authAction=FirebaseAuth.getInstance();

        FirebaseApp.initializeApp(getApplicationContext());
        DAOBooking daoBooking = new DAOBooking();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                date_from_calendar = new Date(i2, i1+1, i);
            }
        });

        save_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Booking booking = new Booking(house_key, authAction.getCurrentUser().getEmail(),date_from_calendar, hour, false, "", house_address);
                daoBooking.addBooking(booking).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Booking was saved successfully!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),""+ task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        hour = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}