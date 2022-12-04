package com.example.group_10;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.group_10.databinding.ActivityMainBinding;
import com.example.group_10.databinding.ActivityYearBookBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class YearBook extends AppCompatActivity {
    @NonNull
    ActivityYearBookBinding binding;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityYearBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        returnbtn();
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(YearBook.this);
                progressDialog.setMessage("Fetching image...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                String first = binding.typeFirst.getText().toString();
                String last = binding.typeLast.getText().toString();
                String year = binding.typeYear.getText().toString();
                String imageId = first + "_" + last + "_" + year;

                storageReference = FirebaseStorage.getInstance().getReference("Images/"+imageId + ".png");
                try {
                    File localfile = File.createTempFile("tempfile", ".png");
                    storageReference.getFile(localfile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    if(progressDialog.isShowing()){
                                        progressDialog.dismiss();
                                        Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                        binding.imageView.setImageBitmap(bitmap);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    if(progressDialog.isShowing()){
                                        progressDialog.dismiss();
                                    }
                                    Toast.makeText(YearBook.this, "Failed to retrieve image", Toast.LENGTH_SHORT).show();
                                }
                            });
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
    }
    public void returnbtn(){
        Button returning = findViewById(R.id.return_button);
        returning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(YearBook.this, Profile.class));
            }
        });
    }
}
