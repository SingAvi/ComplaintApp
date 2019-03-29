package in.SingAvi.complaintapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ComplainHistory extends AppCompatActivity {

    DatabaseReference getComplain,getOnlyName;
    private RecyclerView.Adapter mAdapter;
    FirebaseAuth mAuth;
    String nameOnly;
    private RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    List<String> complainName = new ArrayList<>();
    List<String>  complainTitle = new ArrayList<>();
    List<String>  complainDescription = new ArrayList<>();
    List<String>  complainSatus = new ArrayList<>();
    List<String>  complainDepartment = new ArrayList<>();
    List<String> complainDate = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_history);


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        //UI Links
        recyclerView = findViewById(R.id.recycler_view_complain_history);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);



        getOnlyName = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        getOnlyName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                nameOnly = dataSnapshot.child("Name").getValue().toString().trim();

                addToRecycler();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private void addToRecycler()
    {
        getComplain = FirebaseDatabase.getInstance().getReference().child("Complaints").child(nameOnly);
        getComplain.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                appendPaytmAmount(dataSnapshot);
                mAdapter = new ComplainAdapter(complainName,complainTitle,complainDescription,complainSatus,complainDepartment,complainDate);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                appendPaytmAmount(dataSnapshot);
                mAdapter = new ComplainAdapter(complainName,complainTitle,complainDescription,complainSatus,complainDepartment,complainDate);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void appendPaytmAmount(DataSnapshot dataSnapshot) {
        Iterator i= dataSnapshot.getChildren().iterator();

        while(i.hasNext()){
            String status = (String) ((DataSnapshot)i.next()).getValue();
            String date = (String) ((DataSnapshot) i.next()).getValue();
            String department= (String) ((DataSnapshot)i.next()).getValue();
            String descriptiom = (String) ((DataSnapshot)i.next()).getValue();
            String name= (String) ((DataSnapshot)i.next()).getValue();
            String title = (String) ((DataSnapshot)i.next()).getValue();





            complainName.add(name);
            complainTitle.add(title);
            complainDescription.add(descriptiom);
            complainSatus.add(status);
            complainDepartment.add(department);
            complainDate.add(date);

        }
    }
}
