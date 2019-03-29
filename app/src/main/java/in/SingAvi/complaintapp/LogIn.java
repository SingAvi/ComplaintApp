package in.SingAvi.complaintapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.CharacterPickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {

    EditText email,password;
    Button logIn;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    TextView backToSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //Firebase Login
        mAuth = FirebaseAuth.getInstance();

        //JAVA UI Links
        progressDialog = new ProgressDialog(this);

        //UI Links
        email = findViewById(R.id.phone);
        password = findViewById(R.id.login_pass);
        logIn = findViewById(R.id.logIn);
        backToSignUp = findViewById(R.id.backToSignUp);

        backToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LogIn.this,SignUp.class));
            }
        });


        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String str_email = email.getText().toString().trim();
               String str_pass = password.getText().toString().trim();


                if(!TextUtils.isEmpty(str_pass) || !TextUtils.isEmpty(str_email))
                {
                    progressDialog.setTitle("Logging In !");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMessage("Please wait while check your credentials");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    signInUser(str_pass,str_email);
                }


            }
        });
    }

    private void signInUser(String str_pass, String str_email) {

        mAuth.signInWithEmailAndPassword( str_email,str_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if (task.isSuccessful())
                {

                    progressDialog.dismiss();

                    if (mAuth.getCurrentUser().isEmailVerified())
                    {
                        Intent act= new Intent(LogIn.this,Home.class);
                        act.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(act);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(LogIn.this, "Please Verify and then Login", Toast.LENGTH_SHORT).show();

                    }
                }


                else
                {
                    progressDialog.hide();
                    Toast.makeText(LogIn.this,"Credentials provided are wrong! Please check.",Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
