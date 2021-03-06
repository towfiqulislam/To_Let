package com.rit.basa_bari.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.rit.basa_bari.MainActivity;
import com.rit.basa_bari.R;
import com.rit.basa_bari.models.OwnerProfile;
import com.rit.basa_bari.models.RenterProfile;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText name,contact,address,password,occupation,email;
    private Button register;
    RadioGroup type;
    String userType;
    private FirebaseAuth mAuth;
    DatabaseReference oDatabaseReference,rDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initializationView();
        mAuth = FirebaseAuth.getInstance();
        clickButton();
        setTitle("Sign Up");
        oDatabaseReference= FirebaseDatabase.getInstance().getReference("OwnerProfile");
        rDatabaseReference= FirebaseDatabase.getInstance().getReference("RenterProfile");

    }


    public void initializationView(){
        name=findViewById(R.id.nameEt);
        contact=findViewById(R.id.contactEt);
        address=findViewById(R.id.addressEt);
        password=findViewById(R.id.passwordEt);
        occupation=findViewById(R.id.occupationdEt);
        email=findViewById(R.id.emailEt);
        type=findViewById(R.id.userType);
        register=findViewById(R.id.registerBtn);

    }

   public void clickButton(){
        register.setOnClickListener(this);

    }

      public boolean checkValidity() {
        View focusView = null;
        boolean cancel = false;
        String rName = name.getText().toString();
        String rContact = contact.getText().toString();
        String rAddress = address.getText().toString();
        String rPassword = password.getText().toString();
        String rOccupation= occupation.getText().toString();
        String rEmail = email.getText().toString();

        if (TextUtils.isEmpty(rName)) {
            // focusView=userName;
            cancel = true;
            name.setError("Enter a valid name");
        } else if (TextUtils.isEmpty(rContact)) {
            // focusView = pass;
            cancel = true;
            contact.setError("Enter a valid contact");
        }else if (TextUtils.isEmpty(rAddress)) {
            // focusView = pass;
            cancel = true;
            address.setError("Enter a valid address");
        }else if (TextUtils.isEmpty(rPassword)) {
            // focusView = pass;
            cancel = true;
            password.setError("Enter a valid password");
        }else if (TextUtils.isEmpty(rOccupation)) {
            // focusView = pass;
            cancel = true;
            occupation.setError("Enter a valid occupation");
        }else if (TextUtils.isEmpty(rEmail)) {
            // focusView = pass;
            cancel = true;
            email.setError("Enter a valid email");
        }
        return cancel;
    }

    @Override
    public void onClick(View view) {
        String vName=name.getText().toString().trim();
        String vContact=contact.getText().toString().trim();
        String vAddress=address.getText().toString().trim();
        String vPassword=password.getText().toString().trim();
        String vOccupation=occupation.getText().toString().trim();
        String vEmail=email.getText().toString().trim();

        if(view.getId()==R.id.registerBtn){


            if(checkValidity()){
                Toast.makeText( this,"Ooopss!! you might have left something",Toast.LENGTH_LONG ).show();
            }else{
                int id=type.getCheckedRadioButtonId();
                RadioButton radioButton=findViewById( id );
                this.userType= radioButton.getText().toString();
                if(id==R.id.ownerRb){
                    String ownerId=oDatabaseReference.push().getKey();
                    OwnerProfile ownerProfile=new OwnerProfile(vName,vContact,vAddress,vPassword,vOccupation,vEmail);

                    oDatabaseReference.child(ownerId).setValue(ownerProfile);
                    mAuth.createUserWithEmailAndPassword(vEmail, vPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));

                            } else {
                                Log.w("msg", "signInWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                               Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });


                }else if(id==R.id.renterRb) {
                    String renterId=rDatabaseReference.push().getKey();
                    RenterProfile renterProfile=new RenterProfile(vName,vContact,vAddress,vPassword,vOccupation,vEmail);
                    rDatabaseReference.child(renterId).setValue(renterProfile);
                   // Toast.makeText(RegisterActivity.this,"Renter's Data Added Succesfully!!",Toast.LENGTH_LONG).show();
                    mAuth.createUserWithEmailAndPassword(vEmail, vPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));

                            } else {
                                Log.w("msg", "signInWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                               Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });
                }

            }
        }
    }
}


