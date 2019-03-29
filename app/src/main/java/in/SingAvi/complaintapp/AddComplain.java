package in.SingAvi.complaintapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddComplain extends AppCompatActivity {

    Spinner chooseDepartment;
    Button submitComplain;
    TextInputEditText title,description,name;
    DatabaseReference DatacomplainBase,userData;
    String namefromBase;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_complain);


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        //Java UI Links
        progressDialog = new ProgressDialog(this);



        //UI Links
        chooseDepartment = findViewById(R.id.departments);
        submitComplain = findViewById(R.id.submit);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        name = findViewById(R.id.compalainerName);

//      Choose Department from Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Department, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseDepartment.setAdapter(adapter);


        submitComplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_departmentChoosen = chooseDepartment.getSelectedItem().toString();
                String str_title = title.getText().toString().trim();
                String str_description = description.getText().toString().trim();
                String str_name = name.getText().toString().trim();

                if (!TextUtils.isEmpty(str_departmentChoosen) || !TextUtils.isEmpty(str_title) || !TextUtils.isEmpty(str_description) || !TextUtils.isEmpty(str_name))
                {
                    progressDialog.setTitle("Adding Complain");
                    progressDialog.setMessage("Complain is being uploaded");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    submitAction(str_departmentChoosen,str_description,str_title,str_name);
                }
                else
                {
                    Toast.makeText(AddComplain.this, "Fill all required data for complaint upload", Toast.LENGTH_SHORT).show();

                }

            }
        });


        userData = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        userData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                namefromBase = dataSnapshot.child("Name").getValue().toString();
            name.setText(namefromBase);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void submitAction(String str_departmentChoosen, String str_description, String str_title,String str_name)
    {
        String key = FirebaseDatabase.getInstance().getReference().push().getKey();
        DatacomplainBase = FirebaseDatabase.getInstance().getReference().child("Complaints").child(str_name).child(key);

        Calendar calendar =  Calendar.getInstance();
        String  currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        HashMap<String,String> complainBase = new HashMap<>();

        complainBase.put("Department",str_departmentChoosen);
        complainBase.put("Title",str_title);
        complainBase.put("Name",str_name);
        complainBase.put("Description",str_description);
        complainBase.put("'Status","-");
        complainBase.put("Date",currentDate);

        DatacomplainBase.setValue(complainBase).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    title.getText().clear();
                    description.getText().clear();
                    name.getText().clear();
                    progressDialog.dismiss();
                    name.setText(namefromBase);
                    Toast.makeText(AddComplain.this, "Complaint Successfully Added", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(AddComplain.this, "Error in Connection", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}
