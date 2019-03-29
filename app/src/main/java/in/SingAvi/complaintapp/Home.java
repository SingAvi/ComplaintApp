package in.SingAvi.complaintapp;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity {

    LinearLayout complaintHistory,addComplaint,profile;

    DatabaseReference userData;
    String namePass;
    TextView HelloUser;
    ImageView signOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = firebaseUser.getUid();

        userData = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        //UI Links

        complaintHistory = findViewById(R.id.complaintHistory);
        addComplaint = findViewById(R.id.addComplaint);
        HelloUser = findViewById(R.id.helloUser);
        signOut = findViewById(R.id.signOut);
        profile = findViewById(R.id.profilePage);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Home.this,LogIn.class));
                finish();
            }
        });

        userData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("Name").getValue().toString().trim();
                HelloUser.setText("Hello "+name);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sharedIntent = new Intent(Home.this,AddComplain.class);
                ActivityOptions activityOptions = null;


                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    activityOptions = (ActivityOptions) ActivityOptions.makeSceneTransitionAnimation(Home.this, new Pair[]{Pair.create(addComplaint, "Complain")});
                }

                startActivity(sharedIntent,activityOptions.toBundle());
            }
        });

        complaintHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharedIntent = new Intent(Home.this,ComplainHistory.class);
                ActivityOptions activityOptions = null;


                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    activityOptions = (ActivityOptions) ActivityOptions.makeSceneTransitionAnimation(Home.this, new Pair[]{Pair.create(complaintHistory, "ComplainHis")});
                }

                startActivity(sharedIntent,activityOptions.toBundle());
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sharedIntent = new Intent(Home.this,Profile.class);
                ActivityOptions activityOptions = null;


                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    activityOptions = (ActivityOptions) ActivityOptions.makeSceneTransitionAnimation(Home.this, new Pair[]{Pair.create(profile, "Profile")});
                }

                startActivity(sharedIntent,activityOptions.toBundle());
            }
        });






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_settings:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Home.this,SignUp.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
