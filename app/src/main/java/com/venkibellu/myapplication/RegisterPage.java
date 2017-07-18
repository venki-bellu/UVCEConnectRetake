package com.venkibellu.myapplication;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class RegisterPage extends AppCompatActivity {

    Spinner spinner;
    TextView textView;
    EditText registerNumber;
    EditText yearOfJoining;
    RadioGroup radioGroup;
    private DatabaseReference ref;
    private String key;
    Button registerUser;
    EditText name;
    EditText phoneNumber,enteredOTP;
    Button submitButton, requesetOTP;
    final String TAG="FIREBASE";
    private FirebaseAuth mAuth;
    boolean mVerificationInProgress = false;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        spinner=(Spinner)findViewById(R.id.registerpage_branch);
        textView=(TextView)findViewById(R.id.registerpage_title_rollno);
        registerNumber=(EditText)findViewById(R.id.registerpage_registerno);
        yearOfJoining=(EditText)findViewById(R.id.registerpage_yearofjoining);
        radioGroup=(RadioGroup)findViewById(R.id.registerpage_radiogroup);
        phoneNumber=(EditText)findViewById(R.id.registerpage_phoneNumber);
        enteredOTP=(EditText)findViewById(R.id.registerpage_OTP);
        requesetOTP=(Button)findViewById(R.id.registerpage_button_requestOTP);
        submitButton=(Button)findViewById(R.id.registerpage_button_submit);
        mAuth=FirebaseAuth.getInstance();
        registerUser=(Button)findViewById(R.id.registerpage_button_register);
        name=(EditText) findViewById(R.id.registerpage_name);


        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                    Toast.makeText(getApplicationContext(),"Invalid Mobile number",Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                    Toast.makeText(getApplicationContext(),"Server Error. Unable to register",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                Toast.makeText(getApplicationContext(),"Verification code sent to +91 "+phoneNumber.getText().toString(),Toast.LENGTH_SHORT).show();
                mVerificationId = verificationId;
                mResendToken = token;
                requesetOTP.setEnabled(false);
                submitButton.setEnabled(true);
                phoneNumber.setEnabled(false);
            }
        };

      /*  requesetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "91"+phoneNumber.getText().toString(),     //phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        RegisterPage.this,               // Activity (for callback binding)
                        mCallbacks                         //OnVerificationStateChangedCallbacks
                );
            }
        });*/

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, enteredOTP.getText().toString());
                signInWithPhoneAuthCredential(credential);

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId)
                {
                    case R.id.registerpage_status_student:
                        textView.setVisibility(View.VISIBLE);
                        registerNumber.setVisibility(View.VISIBLE);
                        break;
                    case R.id.registerpage_status_Alumni:
                        textView.setVisibility(View.GONE);
                        registerNumber.setVisibility(View.GONE);
                        break;

                }
            }
        });
    }

    public void registerUser(View view)
    {
        Toast.makeText(getApplicationContext(),"Registration Success",Toast.LENGTH_SHORT).show();
        ref = FirebaseDatabase.getInstance().getReference().child("Registered Users");
        Query query = ref.orderByKey().limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    key = snapshot.getKey();

                    HashMap<String,String> hashMap=new HashMap<String, String>();
                    hashMap.put("Name",name.getText().toString());
                    hashMap.put("Branch",spinner.getSelectedItem().toString());
                    hashMap.put("Year Of Joining",yearOfJoining.getText().toString());
                    String userTYpe;
                    if(radioGroup.getCheckedRadioButtonId()==R.id.registerpage_status_Alumni)
                        userTYpe="Alumni";
                    else {
                        userTYpe = "Student";
                        hashMap.put("Register Number",registerNumber.getText().toString());
                    }
                    hashMap.put("User Type",userTYpe);
                    hashMap.put("Designation","NORMAL");
                    hashMap.put("Phone Number",phoneNumber.getText().toString());
                    hashMap.put("Email Id",Registered_User_Id.registered_user_email);
                    hashMap.put("Google_ID",Registered_User_Id.registered_user_id);

                    ref.child(String.valueOf(Integer.parseInt(key)+1)).setValue(hashMap);
                    Intent intent = new Intent(RegisterPage.this, NewHomePage.class);
                    startActivity(intent);
                    finish();

                }catch(Exception e) {}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(getApplicationContext(),"Phone number verification success",Toast.LENGTH_SHORT).show();
                            registerUser.setEnabled(true);
                            enteredOTP.setEnabled(false);
                            submitButton.setEnabled(false);

                            FirebaseUser user = task.getResult().getUser();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(),"Phone number verification failed",Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    public void requestOTP(View v)
    {
        Toast.makeText(getApplicationContext(),"Button Clicked",Toast.LENGTH_SHORT).show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phoneNumber.getText().toString(),     //phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                RegisterPage.this,               // Activity (for callback binding)
                mCallbacks                         //OnVerificationStateChangedCallbacks
        );
    }
}
