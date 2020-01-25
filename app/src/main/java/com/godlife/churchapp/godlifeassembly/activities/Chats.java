package com.godlife.churchapp.godlifeassembly.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.godlife.churchapp.godlifeassembly.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class Chats extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private StorageReference filesStorage;
    private static final int GALLERY_REQUEST = 23;
    private DatabaseReference rootRef,userRef;
    private RecyclerView mUsersRecycler;
    private String sender_name,receiver_campus,chat_message,sender_id;
    private EmojiconEditText emojiconEditText;
    private ImageView emojiImageView,iv_receiver_online;
    private View rootView;
    private EmojIconActions emojIcon;
    private ImageButton submitButton;
    private final List<MessageModel> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private GlobalMessageAdapter messageAdapter;
    private Uri imageUri = null;
    private KProgressHUD hud;
    private boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        try {
            if (mAuth.getCurrentUser()==null){
                Intent chat = new Intent(Chats.this, Login.class);

                startActivity(chat);
                finish();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        setContentView(R.layout.activity_chats);

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        rootRef = FirebaseDatabase.getInstance().getReference().child("Chats");
        filesStorage = FirebaseStorage.getInstance().getReference().child("Files");


       try {
           sender_id = mAuth.getCurrentUser().getUid();
       } catch (Exception e){
           e.printStackTrace();
       }



        emojiImageView = findViewById(R.id.gemoji_btn);
        submitButton = findViewById(R.id.gsend_message_btn);

        rootView = findViewById(R.id.g_root_view);

        userRef.child(sender_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sender_name = dataSnapshot.child("names").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        hud = KProgressHUD.create(Chats.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("sharing image...")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setBackgroundColor(Color.BLACK)
                .setAutoDismiss(true);


        emojiconEditText = findViewById(R.id.gemojicon_edit_text);
        emojIcon = new EmojIconActions(this, rootView, emojiconEditText, emojiImageView);
        emojIcon.ShowEmojIcon();
        emojIcon.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley);
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("Keyboard Opened", "Keyboard opened!");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("Keyboard Closedgit", "Keyboard closed");
            }
        });


        messageAdapter = new GlobalMessageAdapter(messagesList);
        mUsersRecycler = findViewById(R.id.groups_chat_messages);
        linearLayoutManager = new LinearLayoutManager(this);
        mUsersRecycler.setLayoutManager(linearLayoutManager);
        mUsersRecycler.setHasFixedSize(true);
        mUsersRecycler.setAdapter(messageAdapter);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
//        share_files.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                shareImage();
//
//            }
//        });
        retreiveMessages();

    }

//    public void shareImage(){
//
//        Intent galIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        galIntent.setType("image/*");
//        startActivityForResult(Intent.createChooser(galIntent, "Choose picture"), GALLERY_REQUEST);
//
//    }

    public void sendMessage(){
        chat_message= emojiconEditText.getText().toString();
        emojiconEditText.setText(" ");
        if (chat_message.isEmpty()){

        } else if(!chat_message.isEmpty()) {

            String saveCurrentTime, saveCurrentDate;

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
            saveCurrentDate= currentDate.format(calendar.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
            saveCurrentTime= currentTime.format(calendar.getTime());

            Map<String, Object> messageTextBody = new HashMap<>();
            messageTextBody.put("message",chat_message);
            messageTextBody.put("type","text");
            messageTextBody.put("from",sender_id);
            messageTextBody.put("date",saveCurrentDate);
            messageTextBody.put("time",saveCurrentTime);
            messageTextBody.put("names",sender_name);

            String messageKey = rootRef.push().getKey();
            rootRef.child(messageKey).updateChildren(messageTextBody)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                emojiconEditText.setText(" ");
                            }
                        }
                    });

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK)
//        {
//            imageUri = data.getData();
//
//
//            if (imageUri!=null){
//
//                hud.show();
//
//                String id = rootRef.push().getKey();
//                final StorageReference filePath = filesStorage.child(sender_id).child(id).child("image");
//
//                UploadTask uploadTask = filePath.putFile(imageUri);
//                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                    @Override
//                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                        hud.dismiss();
//                        if (!task.isSuccessful()) {
//                            throw task.getException();
//                        }
//                        return filePath.getDownloadUrl();
//                    }
//                }).addOnCompleteListener(Chats.this, new OnCompleteListener<Uri>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Uri> task) {
//                        hud.dismiss();
//                        if (task.isSuccessful()){
//
//
//                            Uri downloadUrl = task.getResult();
//
//                            sendAsMessage(downloadUrl.toString());
//                            MDToast mdToast = MDToast.makeText(getApplicationContext(),
//                                    "Image shared successfully",
//                                    MDToast.LENGTH_LONG,MDToast.TYPE_SUCCESS);
//                            mdToast.show();
//
//
//                        } else {
//                            MDToast mdToast = MDToast.makeText(getApplicationContext(),
//                                    "Image Sharing Failed!",
//                                    MDToast.LENGTH_LONG,MDToast.TYPE_ERROR);
//                            mdToast.show();
//
//                        }
//
//                    }
//                });
//
//
//            }
//        }
//
//    }

//    public void sendAsMessage(String message){
//        String saveCurrentTime, saveCurrentDate;
//
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
//        saveCurrentDate= currentDate.format(calendar.getTime());
//
//        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
//        saveCurrentTime= currentTime.format(calendar.getTime());
//
//        Map<String, Object> messageTextBody = new HashMap<>();
//        messageTextBody.put("message",message);
//        messageTextBody.put("type","image");
//        messageTextBody.put("from",sender_id);
//        messageTextBody.put("date",saveCurrentDate);
//        messageTextBody.put("time",saveCurrentTime);
//        messageTextBody.put("names",sender_name);
//
//        String messageKey = rootRef.push().getKey();
//        rootRef.child(messageKey).updateChildren(messageTextBody)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()){
//                            emojiconEditText.setText(" ");
//                        }
//                    }
//                });
//
//    }

    public void retreiveMessages(){
        rootRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);

                messagesList.add(messageModel);
                messageAdapter.notifyDataSetChanged();
                mUsersRecycler.smoothScrollToPosition(mUsersRecycler.getAdapter().getItemCount());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
}
