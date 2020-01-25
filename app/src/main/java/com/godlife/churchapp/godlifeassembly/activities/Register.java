package com.godlife.churchapp.godlifeassembly.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.godlife.churchapp.godlifeassembly.R;
import com.godlife.churchapp.godlifeassembly.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kaopiz.kprogresshud.KProgressHUD;

public class Register extends AppCompatActivity {
    private EditText etNames, etEmail, etPassword,etAddress;
    private RadioGroup rgGender, rgMarital;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private KProgressHUD hud;
    private DatabaseReference usersRef;
    private Button submitButton;
    private String  names,gender, marital, password, location, email, uid, device_token;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        etEmail =findViewById(R.id.reg_chat_email);
        etAddress =findViewById(R.id.reg_chat_location);
        etNames =findViewById(R.id.reg_chat_name);
        etPassword =findViewById(R.id.reg_chat_password);
        submitButton = findViewById(R.id.bt_login_sumbmit);


        rgGender = findViewById(R.id.gender_group);
        rgMarital =findViewById(R.id.marital_group);



        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.gender_male){
                    gender="Male";
                } else if (i==R.id.gender_female){
                    gender="Female";
                }
                Log.d("gender",gender);

            }
        });

        rgMarital.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (i==R.id.single){
                    marital="Single";
                } else if (i==R.id.married){
                    marital="Married";
                }
                Log.d("marital",marital);
            }
        });


        mAuth = FirebaseAuth.getInstance();


        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        hud = KProgressHUD.create(Register.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Registering User...")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setBackgroundColor(Color.BLACK)
                .setAutoDismiss(true);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = etEmail.getText().toString().trim();
                location = etAddress.getText().toString().trim();
                names = etNames.getText().toString().trim();
                password = etPassword.getText().toString().trim();

                if (password.isEmpty()){
                    etPassword.setError("password empty");
                    return;
                }

                if (password.length()<6){
                    etPassword.setError("password too short");
                    return;
                }
                if (email.isEmpty() || !email.contains("@") || !email.contains(".")){
                    etEmail.setError("invalid email");
                    return;
                }
                if (location.isEmpty()){
                    etAddress.setError("location empty");
                    return;
                }

                try {
                    registerUser();
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        });


    }

    public void registerUser(){
        hud.show();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hud.dismiss();
                        if (task.isSuccessful()){
                            uid = mAuth.getCurrentUser().getUid();

                            FirebaseMessaging.getInstance().subscribeToTopic("Chats");

                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                @Override
                                public void onSuccess(InstanceIdResult instanceIdResult) {
                                    device_token = instanceIdResult.getToken();

                                }
                            });

                            UserModel model = new UserModel(names,email,location,gender,marital,device_token,uid);
                            usersRef.child(uid).setValue(model);
                            if (gender.matches("Male") && marital.matches("Single")){
                                FirebaseMessaging.getInstance().subscribeToTopic("SingleMale");

                            }

                            if (gender.matches("Female") && marital.matches("Single")){
                                FirebaseMessaging.getInstance().subscribeToTopic("SingleFemale");

                            }
                            FirebaseMessaging.getInstance().subscribeToTopic(gender);
                            FirebaseMessaging.getInstance().subscribeToTopic(marital);


                            Intent intent = new Intent(Register.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }
}
