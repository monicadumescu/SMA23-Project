package com.example.agentie_imobiliara.adaptors;

import static android.content.ContentValues.TAG;
import static android.provider.Settings.System.getString;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agentie_imobiliara.DAO.DAOBooking;
import com.example.agentie_imobiliara.R;
import com.example.agentie_imobiliara.model.Booking;
import com.example.agentie_imobiliara.ui.bookings.BookingsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

public class BookingsAdaptor extends RecyclerView.Adapter<BookingsAdaptor.ImageViewHolder> {
    private Context mContext;
    private List<Booking> mUploads;
    private DAOBooking daoBooking = new DAOBooking();
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog.Builder alertDIalogReason;

    public BookingsAdaptor(Context context, List<Booking> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @NonNull
    @Override
    public BookingsAdaptor.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.your_bookings_container, parent, false);
        return new ImageViewHolder(view);
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

        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUpload.isAccept_booking()) {
                    Toast.makeText(mContext, "This booking was already approved", Toast.LENGTH_SHORT).show();
                } else {
                    currentUpload.setAccept_booking(true);
                    daoBooking.editBooking(currentUpload.getObject_key(), currentUpload);
                    Toast.makeText(mContext, "The booking was approved successfully!", Toast.LENGTH_SHORT).show();
                    addNotification();
                    ((Activity) mContext).recreate();
                }
            }
        });

        alertDialogBuilder = new AlertDialog.Builder(mContext);
        holder.disapprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!currentUpload.isAccept_booking() && !currentUpload.getRejection_message().equals("")) {
                    Toast.makeText(mContext, "This booking was already disapproved", Toast.LENGTH_SHORT).show();
                } else if (currentUpload.isAccept_booking()) {
                    alertDialogBuilder.setMessage("This booking was approved in the past. Are you sure you want to disapprove it?");
                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDIalogReason = new AlertDialog.Builder(mContext);
                            ViewGroup viewGroup = view.findViewById(androidx.appcompat.R.id.content);
                            View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.disapprove_booking_layout, viewGroup, false);
                            alertDIalogReason.setView(dialogView);
                            AlertDialog alertDialog = alertDIalogReason.create();
                            TextView disapprove_reason_text = (TextView) dialogView.findViewById(R.id.disapprove_reason);
                            Button disapprove_button = (Button) dialogView.findViewById(R.id.disapprove_button);
                            alertDialog.show();

                            disapprove_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (!disapprove_reason_text.equals("")) {
                                        currentUpload.setAccept_booking(false);
                                        currentUpload.setRejection_message(disapprove_reason_text.getText().toString());
                                        daoBooking.editBooking(currentUpload.getObject_key(), currentUpload);
                                        Toast.makeText(mContext, "The booking was disapproved successfully!", Toast.LENGTH_SHORT).show();
                                        alertDialog.cancel();
                                        ((Activity) mContext).recreate();
                                    }
                                }
                            });
                        }

                        ;
                    });
                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(mContext, "Booking was not disapproved!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    alertDialogBuilder.show();
                } else if (!currentUpload.isAccept_booking() && currentUpload.getRejection_message().equals("")) {
                    alertDIalogReason = new AlertDialog.Builder(mContext);
                    ViewGroup viewGroup = view.findViewById(androidx.appcompat.R.id.content);
                    View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.disapprove_booking_layout, viewGroup, false);
                    alertDIalogReason.setView(dialogView);
                    AlertDialog alertDialog = alertDIalogReason.create();
                    TextView disapprove_reason_text = (TextView) dialogView.findViewById(R.id.disapprove_reason);
                    Button disapprove_button = (Button) dialogView.findViewById(R.id.disapprove_button);
                    alertDialog.show();

                    disapprove_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!disapprove_reason_text.equals("")) {
                                currentUpload.setAccept_booking(false);
                                currentUpload.setRejection_message(disapprove_reason_text.getText().toString());
                                daoBooking.editBooking(currentUpload.getObject_key(), currentUpload);
                                Toast.makeText(mContext, "The booking was disapproved successfully!", Toast.LENGTH_SHORT).show();
                                alertDialog.cancel();
                                ((Activity) mContext).recreate();
                            }
                        }
                    });

                }
            }

        });

    }

    private void addNotification() {
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "bookings_notifications";
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,"My notifications", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Sample channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID);
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Booking")
                .setContentTitle("Booking")
                .setContentText("Your booking was accepted")
                .setContentInfo("Information");
        notificationManager.notify(1, builder.build());
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView booking_address, visitor, date, hour, status, reason;
        public FloatingActionButton approve, disapprove;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            booking_address = itemView.findViewById(R.id.house_address);
            visitor = itemView.findViewById(R.id.visitor);
            date = itemView.findViewById(R.id.date_booking);
            hour = itemView.findViewById(R.id.booking_hour);
            status = itemView.findViewById(R.id.status);
            reason = itemView.findViewById(R.id.reason);
            approve = itemView.findViewById(R.id.approve);
            disapprove = itemView.findViewById(R.id.disapprove);
        }

    }
}
