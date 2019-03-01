package com.fouronesixplayer.fouronesixplayer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class Upload extends AppCompatActivity {

    Button selectFile, upload;
    TextView notification;
    Uri songUri; //Uri are URLs that are meant for local storage

    FirebaseStorage storage; //upload files
    FirebaseDatabase database; //store urls of uploaded files
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        //FirebaseStorage.getInstance();
        storage= FirebaseStorage.getInstance(); //Returns an object of firebase storage
        database= FirebaseDatabase.getInstance(); // Return an object of firebase database

        selectFile=findViewById(R.id.selectFile);
        upload=findViewById(R.id.upload);
        notification=findViewById(R.id.notification);

        selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ContextCompat.checkSelfPermission(Upload.this, Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
                {
                    selectSong();
                }else
                    ActivityCompat.requestPermissions(Upload.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(songUri!=null)
                    uploadFile(songUri);
                else
                    Toast.makeText(Upload.this,"Please select a file",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadFile(Uri songUri) {

        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file...");
        progressDialog.setProgress(0);
        progressDialog.show();

        final String fileName = System.currentTimeMillis() + "";
        final StorageReference storageReference = storage.getReference();

        storageReference.child("Uploads").child(fileName).putFile(songUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Toast.makeText(Upload.this, "File Uploaded Successfully",Toast.LENGTH_SHORT).show();

                        /*String url=storageReference.getDownloadUrl().toString(); //returns the url of the uploaded file

                        DatabaseReference reference=database.getReference();*/

                       /* reference.child("Uploads").child(fileName).setValue(url).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Upload.this, "File Uploaded Successfully",Toast.LENGTH_SHORT).show();
                            }
                        });*/

                       /* reference.child("Uploads").child(fileName).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>(){
                            @Override
                            public void onComplete(@NonNull Task<Void> task){

                                if(task.isSuccessful())
                                    Toast.makeText(Upload.this, "File Uploaded Successfully",Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(Upload.this, "Upload failed",Toast.LENGTH_SHORT).show();
                            }
                        });*/
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Upload.this, "Upload failed",Toast.LENGTH_SHORT).show();
            }

        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                //track the upload progress
                int currentProgress= (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            selectSong();
        } else
            Toast.makeText(Upload.this, "Please provide permission..",Toast.LENGTH_SHORT).show();
    }

    private void selectSong(){

        Intent intent=new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//to get the files
        startActivityForResult(intent, 86);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //checking if a file is selected or not

        if (requestCode==86 && resultCode==RESULT_OK && data!=null)
        {

            songUri=data.getData();//returns uri of selected file
            notification.setText("A file is selected: "+ data.getData().getLastPathSegment());
        } else
            Toast.makeText(Upload.this, "Please select a file",Toast.LENGTH_SHORT).show();


    }
}
