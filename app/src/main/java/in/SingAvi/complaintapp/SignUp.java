package in.SingAvi.complaintapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;



public class SignUp extends AppCompatActivity {

    TextInputEditText name,email,phone,password,confirmPassowrd,age;
    TextView AccountAlready;
    Button SignUp;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    DatabaseReference userDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();

        // Java UI Links
        progressDialog = new ProgressDialog(this);

        // UI LINKS
        name = findViewById(R.id.et_name);
        email  = findViewById(R.id.et_email);
        phone = findViewById(R.id.et_phone);
        password = findViewById(R.id.et_password);
        confirmPassowrd = findViewById(R.id.et_confirmPass);
        age = findViewById(R.id.et_age);
        SignUp = findViewById(R.id.signUp);
        AccountAlready = findViewById(R.id.loginAct);
        

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SignUpUser();

            }
        });

        AccountAlready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this,LogIn.class));
                finish();
            }
        });

    }

    private void SignUpUser()
    {

        String str_name = name.getText().toString().trim();
        String str_email = email.getText().toString().trim();
        String str_phone = phone.getText().toString().trim();
        String str_confirmPass = confirmPassowrd.getText().toString().trim();
        String str_password = password.getText().toString().trim();
        String str_age = age.getText().toString().trim();



        if (!TextUtils.isEmpty(str_name) || !TextUtils.isEmpty(str_email) || !TextUtils.isEmpty(str_phone) || !TextUtils.isEmpty(str_age) || !TextUtils.isEmpty(str_password) || !TextUtils.isEmpty(str_confirmPass))
        {
            progressDialog.setTitle("Registering User");
            progressDialog.setMessage("Please wait while we create your account");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            register_user(str_name,str_email,str_phone,str_confirmPass,str_password,str_age);
        }
        else
        {
            Toast.makeText(this, "Please fill every detail to register", Toast.LENGTH_SHORT).show();

        }

    }

    private void register_user(final String str_name, final String str_email, final String str_phone, String str_confirmPass, final String str_password, final String str_age) {
        firebaseAuth.createUserWithEmailAndPassword(str_email , str_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = currentUser.getUid();
                    userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    HashMap<String, String> userBase = new HashMap<>();

                    userBase.put("Name", str_name.toUpperCase());
                    userBase.put("Email", str_email);
                    userBase.put("Phone", str_phone);
                    userBase.put("Age", str_age);
                    userBase.put("Password", str_password);
                    userBase.put("image", "default");
                    userBase.put("thumbnail", "default");

                    userDatabase.setValue(userBase).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful())
                                        {
                                            progressDialog.dismiss();
                                            Intent act = new Intent(SignUp.this, LogIn.class);
                                            act.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(act);
                                            Toast.makeText(SignUp.this, "Verification Mail sent, Verify and Login", Toast.LENGTH_LONG).show();

                                        }
                                        else
                                        {
                                            Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                        }                                    }
                                });




                            } else {
                                progressDialog.hide();
                                Toast.makeText(SignUp.this, "Error In Connection", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
        });
    }
}
