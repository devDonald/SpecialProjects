package com.godlife.churchapp.godlifeassembly.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.godlife.churchapp.godlifeassembly.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.valdesekamdem.library.mdtoast.MDToast;

public class Login extends AppCompatActivity {

    private EditText mEtEmail, mEtPassword;
    private TextView mSignUp,tvReg;
    private Button signIn;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference users;
    private KProgressHUD hud;
    private String email,password,message,uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth= FirebaseAuth.getInstance();
        users = FirebaseDatabase.getInstance().getReference().child("Users");

        try {
            mUser=mAuth.getCurrentUser();
            uid=mUser.getUid();

        } catch (Exception e){
            e.printStackTrace();
        }
        if (mAuth.getCurrentUser()!=null){

            Intent login = new Intent(Login.this, Chats.class);
            startActivity(login);
            finish();
        }


        hud = KProgressHUD.create(Login.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Authenticating User...")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setBackgroundColor(Color.BLACK)
                .setAutoDismiss(true);

        mEtEmail = findViewById(R.id.et_login_email);
        mEtPassword = findViewById(R.id.et_login_password);
        signIn = findViewById(R.id.bt_user_login);
        tvReg=findViewById(R.id.tv_register_account);

        tvReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent reg = new Intent(Login.this, Register.class);
                reg.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                reg.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(reg);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = mEtEmail.getText().toString().trim();
                password = mEtPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)||!email.contains("@")||!email.contains(".com")) {
                    message = "Invalid email address";
                    mEtEmail.setError(message);
                    return;
                }

                if (password.length() < 6) {
                    message = "Minimum password length is 6 digits";
                    mEtPassword.setError(message);
                    return;
                }
                if (isNetworkAvailable(Login.this)){
                    try {
                        hud.show();
                        mAuth.signInWithEmailAndPassword(email,password)
                                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        hud.dismiss();
                                        if (task.isSuccessful()){
                                            String current_user = mAuth.getCurrentUser().getUid();

                                            MDToast.makeText(Login.this,"Login Successful"
                                                    ,MDToast.LENGTH_LONG,MDToast.TYPE_SUCCESS).show();

                                            Intent login = new Intent(Login.this, Chats.class);
                                            login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(login);
                                            finish();

                                        } else {
                                            MDToast.makeText(Login.this,"Login Failed, Check email/password"
                                                    ,MDToast.LENGTH_LONG,MDToast.TYPE_ERROR).show();

                                        }
                                    }
                                });


                    } catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    MDToast.makeText(Login.this,"Check your network connectivity"
                            ,MDToast.LENGTH_LONG,MDToast.TYPE_ERROR).show();
                }

            }
        });



    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectMgr;
        connectMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectMgr.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
