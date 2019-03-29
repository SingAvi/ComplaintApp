package in.SingAvi.complaintapp;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class Profile extends AppCompatActivity {

    DatabaseReference getUserData;
    private DatabaseReference userDatabase;
    TextView name, phone, email, age;
    StorageReference storageReference;
    CircleImageView profile;
    Button edit;
    private Uri downloadUrl;
    public static final int REQUESTCODE = 1234;
    ProgressDialog progressDialog;
    FirebaseUser firebaseUser;
    private StorageReference ImageStore;
    String namefromBase;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Storage Reference
        ImageStore = FirebaseStorage.getInstance().getReference();

        //Java Ui
        progressDialog = new ProgressDialog(this);

        //Get Current User Id----------
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseUser.getUid();

        //Database Root Call-----------
        getUserData = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);


        //UI Links---------------------
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.email);
        age = findViewById(R.id.age);
        profile = findViewById(R.id.dp);
        edit = findViewById(R.id.editProfile);
        // ----------------------------


        //Get Values from Database for user--------
        getUserData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                namefromBase = dataSnapshot.child("Name").getValue().toString();
                String phonefromBase = dataSnapshot.child("Phone").getValue().toString();
                String emailfromBase = dataSnapshot.child("Email").getValue().toString();
                String agefromBase = dataSnapshot.child("Age").getValue().toString();
                String thumbfromBaase = dataSnapshot.child("image").getValue().toString();
                name.setText(namefromBase);
                phone.setText(phonefromBase);
                email.setText(emailfromBase);
                age.setText(agefromBase);


                Picasso.with(Profile.this).load(thumbfromBaase).placeholder(R.drawable.ic_account_circle_black_24dp).into(profile);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // --------------------------------


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(Profile.this);


            }
        });


    }

    private void UrlFetchAndDisplay() {

        ImageStore.child("profile_images").child(namefromBase).child(uid + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                downloadUrl = uri;

                Map updateHashMap = new HashMap();
                updateHashMap.put("image", downloadUrl.toString());

                getUserData.updateChildren(updateHashMap);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                progressDialog.setTitle("Uploading Image");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("Please wait while upload and process the image");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                Uri resultUri = result.getUri();

                String Current_User_id = firebaseUser.getUid();


                final StorageReference filepath = ImageStore.child("profile_images").child(namefromBase).child(Current_User_id + ".jpg");


                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {

                            UrlFetchAndDisplay();
                            progressDialog.dismiss();
                            Toast.makeText(Profile.this, "Uploaded Image", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.hide();
                            Toast.makeText(Profile.this, "Error In Uploading", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cannot Upload,Image is larfe ", Toast.LENGTH_SHORT).show();
            }


        }


    }
}


