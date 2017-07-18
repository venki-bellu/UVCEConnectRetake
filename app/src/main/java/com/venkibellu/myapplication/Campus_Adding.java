package com.venkibellu.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.cast.framework.media.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class Campus_Adding extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 234;
    EditText details;
    Spinner organization;
    Button picture;
    ImageView pictureshow;
    Button add;
    private String organization_image = "";
    private ValueEventListener myevent1;


    //a Uri object to store file path
    private Uri filePath;

    //firebase storage reference
    private StorageReference storageReference;

    public static String newpos = "0";
    private DatabaseReference ref;
    private FirebaseDatabase mfbdb;
    private String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        storageReference = FirebaseStorage.getInstance().getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus__adding);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ref = mfbdb.getInstance().getReference().child("Campus Says");



        Query query = ref.orderByKey().limitToLast(1);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        newpos = snapshot.getKey();
                    }

                } catch (Exception e) {
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        details = (EditText)findViewById(R.id.campus_details_add);
        organization = (Spinner) findViewById(R.id.organisation_campus_add);
        picture = (Button) findViewById(R.id.choose_pic_campus);
        pictureshow = (ImageView)findViewById(R.id.campus_imageview);
        add = (Button) findViewById(R.id.campus_add_button);


        String organ[] = new String[]{"IEEE", "Tatva", "G2C2", "SAE", "Vinimaya", "Chakravyuha", "ಚೇತನ" };
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, organ);
        organization.setAdapter(arrayAdapter);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentfile = new Intent();
                intentfile.setType("image/*");
                intentfile.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intentfile, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });

        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!details.getText().toString().trim().equals("")) {

                    if(Integer.parseInt(newpos)>19)
                    {
                        newpos = "19";
                        ref.addListenerForSingleValueEvent(myevent1 = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for(int i = 0; i<19;i++)
                                {
                                    ref.child(String.valueOf(i+1)).child("Campus_Name").setValue(dataSnapshot.child(String.valueOf(i+2)).child("Campus_Name").getValue().toString());
                                    ref.child(String.valueOf(i+1)).child("Campus_Image").setValue(dataSnapshot.child(String.valueOf(i+2)).child("Campus_Image").getValue().toString());
                                    ref.child(String.valueOf(i+1)).child("Campus_Details").setValue(dataSnapshot.child(String.valueOf(i+2)).child("Campus_Details").getValue().toString());
                                    ref.child(String.valueOf(i+1)).child("Campus_Organization").setValue(dataSnapshot.child(String.valueOf(i+2)).child("Campus_Organization").getValue().toString());
                                }
                                ref.child("21").removeValue();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        ref.removeEventListener(myevent1);

                    }

                    if(organization.getItemAtPosition(organization.getSelectedItemPosition()).toString().equals("IEEE"))
                        organization_image = "campusorganisation/IEEE.jpg";
                    else if(organization.getItemAtPosition(organization.getSelectedItemPosition()).toString().equals("Tatva"))
                        organization_image = "campusorganisation/tatva.jpg";
                    else if(organization.getItemAtPosition(organization.getSelectedItemPosition()).toString().equals("G2C2"))
                        organization_image = "campusorganisation/G2C2.jpg";
                    else if(organization.getItemAtPosition(organization.getSelectedItemPosition()).toString().equals("SAE"))
                        organization_image = "campusorganisation/SAE.jpg";
                    else if(organization.getItemAtPosition(organization.getSelectedItemPosition()).toString().equals("Vinimaya"))
                        organization_image = "campusorganisation/Vinimaya.jpg";
                    else if(organization.getItemAtPosition(organization.getSelectedItemPosition()).toString().equals("Chakravyuha"))
                        organization_image = "campusorganisation/chakravyuha.jpg";
                    else if(organization.getItemAtPosition(organization.getSelectedItemPosition()).toString().equals("ಚೇತನ"))
                        organization_image = "campusorganisation/chethana.jpg";

                    if (filePath != null) {
                        //displaying a progress dialog while upload is going on
                        final ProgressDialog progressDialog = new ProgressDialog(Campus_Adding.this);
                        progressDialog.setTitle("Updating");
                        progressDialog.show();


                        ID = organization.getItemAtPosition(organization.getSelectedItemPosition()).toString();
                        ref.child(String.valueOf(Integer.parseInt(newpos) + 1)).child("Campus_Details").setValue(details.getText().toString());
                        ref.child(String.valueOf(Integer.parseInt(newpos) + 1)).child("Campus_Organization").setValue(organization_image);
                        ref.child(String.valueOf(Integer.parseInt(newpos) + 1)).child("Campus_Name").setValue(ID);
                        ref.child(String.valueOf(Integer.parseInt(newpos) + 1)).child("Campus_Image").setValue("campusimages/" + filePath.getLastPathSegment()+"_"+ID);
                        StorageReference riversRef = storageReference.child("campusimages/" + filePath.getLastPathSegment()+"_"+ID);
                        riversRef.putFile(filePath)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        //if the upload is successfull
                                        //hiding the progress dialog
                                        progressDialog.dismiss();

                                        //and displaying a success toast
                                        Toast.makeText(getApplicationContext(), "Campus Says Successfully Updated", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(Campus_Adding.this, Campus_Says.class);
                                        startActivity(intent);
                                        finish();


                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        //if the upload is not successfull
                                        //hiding the progress dialog
                                        progressDialog.dismiss();

                                        //and displaying error message
                                        Toast.makeText(getApplicationContext(), "Update failed", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        //calculating progress percentage
                                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                                        //displaying percentage in progress dialog
                                        progressDialog.setMessage("Updated " + ((int) progress) + "%...");
                                    }
                                });




                    }
                    //if there is not any file
                    else {
                        //displaying a progress dialog while upload is going on
                        final ProgressDialog progressDialog = new ProgressDialog(Campus_Adding.this);
                        progressDialog.setTitle("Updating");
                        progressDialog.show();

                        ID = organization.getItemAtPosition(organization.getSelectedItemPosition()).toString();
                        ref.child(String.valueOf(Integer.parseInt(newpos) + 1)).child("Campus_Details").setValue(details.getText().toString());
                        ref.child(String.valueOf(Integer.parseInt(newpos) + 1)).child("Campus_Organization").setValue(organization_image);
                        ref.child(String.valueOf(Integer.parseInt(newpos) + 1)).child("Campus_Name").setValue(ID);
                        ref.child(String.valueOf(Integer.parseInt(newpos) + 1)).child("Campus_Image").setValue("");
                        Toast.makeText(getApplicationContext(), "Campus Says Successfully Updated", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Intent intent = new Intent(Campus_Adding.this, Campus_Says.class);
                        startActivity(intent);
                        finish();


                    }
                } else
                    Toast.makeText(getApplicationContext(), "Please Enter all the fields", Toast.LENGTH_SHORT).show();
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                pictureshow.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Campus_Adding.this, Campus_Says.class);
        startActivity(intent);
        finish();
    }
}
