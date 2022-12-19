package com.example.agentie_imobiliara;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.agentie_imobiliara.DAO.DAOHouses;
import com.example.agentie_imobiliara.model.House;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class AddHousesActivity extends AppCompatActivity {

    private Uri imageUri;
    ActivityResultLauncher<Intent> activityResultLauncher;
    String fileName;
    private StorageReference imagesRef;
    private EditText address, size, rooms, baths, floors, special,price;
    private FirebaseAuth authAction = FirebaseAuth.getInstance();
    private DAOHouses daoHouses = new DAOHouses();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_houses);
        Button addHouse = (Button) findViewById(R.id.addHouseBH);
        Button addPictures = (Button) findViewById(R.id.addPicturesBH);
        address = (EditText) findViewById(R.id.addressH);
        size = (EditText) findViewById(R.id.sizeH);
        rooms = (EditText) findViewById(R.id.roomsH);
        baths = (EditText) findViewById(R.id.bathsH);
        floors = (EditText) findViewById(R.id.floorsH);
        special = (EditText) findViewById(R.id.specialH);
        price = (EditText) findViewById(R.id.priceH);
        ImageView housePicture = (ImageView) findViewById(R.id.imageViewH);


        FirebaseApp.initializeApp(getApplicationContext());

        imagesRef = FirebaseStorage.getInstance().getReference();

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK && result.getData() != null)
                {
                    Toast.makeText(getApplicationContext(),"Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    imageUri = (Uri) result.getData().getData();
                    fileName = imageUri.toString() + ".jpg";
                    housePicture.setImageURI(imageUri);
                }
            }

        });

        addPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                activityResultLauncher.launch(intent);
            }
        });

        addHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(address.getText().toString().equals("") || size.getText().toString().equals("") || rooms.getText().toString().equals("") || baths.getText().toString().equals("") || floors.getText().toString().equals("") || special.getText().toString().equals("") || price.getText().toString().equals("") || imageUri.toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please complete all fields!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    uploadFile();
                }
            }
        });
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile()
    {
        if(imageUri != null)
        {
            StorageReference storageReference = imagesRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Toast.makeText(getApplicationContext(), "Upload was successful!",Toast.LENGTH_SHORT).show();
                            House house = new House(address.getText().toString(), size.getText().toString(),rooms.getText().toString(), baths.getText().toString(),floors.getText().toString(), special.getText().toString(),authAction.getCurrentUser().getEmail().toString(),  uri.toString(), imageUri.toString(), price.getText().toString());
                            daoHouses.addHouse(house);
                            Toast.makeText(getApplicationContext(),"House was added successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

}