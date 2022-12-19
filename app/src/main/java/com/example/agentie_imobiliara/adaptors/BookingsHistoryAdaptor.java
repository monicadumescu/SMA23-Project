package com.example.agentie_imobiliara.adaptors;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agentie_imobiliara.DAO.DAOBooking;
import com.example.agentie_imobiliara.R;
import com.example.agentie_imobiliara.model.Booking;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class BookingsHistoryAdaptor extends RecyclerView.Adapter<BookingsHistoryAdaptor.ImageViewHolder>{
    private Context mContext;
    private List<Booking> mUploads;
    private DAOBooking daoBooking = new DAOBooking();
    AlertDialog.Builder alertDialogBuilder;

    public BookingsHistoryAdaptor(Context context, List<Booking> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @NonNull
    @Override
    public BookingsHistoryAdaptor.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.bookings_history_layout, parent, false);
        return new BookingsHistoryAdaptor.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Booking currentUpload = mUploads.get(position);
        holder.booking_address.setText("Booking address: " + currentUpload.getAddress());
        holder.visitor.setText("Visitor: " + currentUpload.getUser());
        holder.date.setText("Date: " + currentUpload.getDate().getDay() + "/" + currentUpload.getDate().getMonth() + "/" + currentUpload.getDate().getYear());
        holder.hour.setText("Hour: " + currentUpload.getHour());
        if (!currentUpload.isAccept_booking() && currentUpload.getRejection_message().equals("")) {
            holder.status.setText("Status: pending");
        } else if (currentUpload.isAccept_booking()) {
            holder.status.setText("Status: accepted");
        } else if (!currentUpload.isAccept_booking() && !currentUpload.getRejection_message().equals("")) {
            holder.status.setText("Status: disapproved");
            holder.reason.setText("Reason: " + currentUpload.getRejection_message());
        }

        alertDialogBuilder = new AlertDialog.Builder(mContext);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogBuilder.setMessage("Are you sure you want to delete this booking?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        daoBooking.deleteBooking(currentUpload.getObject_key()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(mContext, "Booking was deleted successfully!", Toast.LENGTH_SHORT).show();
                                    ((Activity) mContext).recreate();
                                }
                                else{
                                    Toast.makeText(mContext,""+ task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(mContext, "This booking was not deleted!", Toast.LENGTH_SHORT).show();
                    }
                });

                alertDialogBuilder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView booking_address, visitor, date, hour, status, reason;
        public FloatingActionButton delete;


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            booking_address = itemView.findViewById(R.id.house_address);
            visitor = itemView.findViewById(R.id.visitor);
            date = itemView.findViewById(R.id.date_booking);
            hour = itemView.findViewById(R.id.booking_hour);
            status = itemView.findViewById(R.id.status);
            reason = itemView.findViewById(R.id.reason);
            delete = itemView.findViewById(R.id.delte_booking);
        }

    }
}
