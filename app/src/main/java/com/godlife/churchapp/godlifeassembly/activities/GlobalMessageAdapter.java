package com.godlife.churchapp.godlifeassembly.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.godlife.churchapp.godlifeassembly.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GlobalMessageAdapter extends RecyclerView.Adapter <GlobalMessageAdapter.MessageViewHolder>{

    private List<MessageModel> userMessagesList;
    private FirebaseAuth mAuth;
    private Context context;



    public GlobalMessageAdapter(List<MessageModel> userMessagesList) {
        this.userMessagesList = userMessagesList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_globalmessage_layout, parent, false);

        mAuth = FirebaseAuth.getInstance();
        context = parent.getContext();

        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, int position) {

        String messageSenderId = mAuth.getCurrentUser().getUid();
        final MessageModel messages = userMessagesList.get(position);

        String fromUserId = messages.getFrom();

        String fromMessageType = messages.getType();
        String todayDate, yesterdayDate;

        try {


            if (fromMessageType.equals("text")){
                holder.receiverLinear.setVisibility(View.INVISIBLE);
                holder.senderLinear.setVisibility(View.INVISIBLE);

                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
                {
                    LocalDate localDate = LocalDate.now();
                    LocalDate yesterday = LocalDate.now().minusDays(1);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
                    todayDate = localDate.format(formatter);
                    yesterdayDate = yesterday.format(formatter);
                }
                else
                {
                    Date c = Calendar.getInstance().getTime();
                    DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM);
                    todayDate = df.format(c);
                    final Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, -1);
                    yesterdayDate=df.format(cal.getTime());
                }

                if (fromUserId.equals(messageSenderId)){
                    holder.senderLinear.setVisibility(View.VISIBLE);
                    //holder.senderLinear.setBackgroundResource(R.drawable.sender_message_layout);
                    holder.senderMessageText.setText(messages.getMessage());
                    holder.senderMessageTime.setText(messages.getTime());
                    if (todayDate.matches(messages.getDate())){
                        holder.senderMessageDate.setText("Today ");
                    } else if (yesterdayDate.matches(messages.getDate())){
                        holder.senderMessageDate.setText("Yesterday ");


                    }
                    else {
                        holder.senderMessageDate.setText(messages.getDate());
                    }

                    if (messageSenderId!=null){
                        holder.senderSentStatus.setVisibility(View.VISIBLE);
                        holder.senderSentStatus.setImageResource(R.drawable.ic_sent);

                    } else {
                        holder.senderSentStatus.setVisibility(View.INVISIBLE);
                    }

                } else {
                    holder.receiverLinear.setVisibility(View.VISIBLE);
                    holder.receiverName.setText(messages.getNames());
                    //holder.receiverLinear.setBackgroundResource(R.drawable.receiver_message_layout);
                    holder.receiverMessageText.setText(messages.getMessage());
                    holder.receiverMessTime.setText(messages.getTime());
                    if (todayDate.matches(messages.getDate())){
                        holder.senderMessageDate.setText("Today ");
                    } else if (yesterdayDate.matches(messages.getDate())){
                        holder.senderMessageDate.setText("Yesterday ");


                    }
                    else {
                        holder.receiverMessageDate.setText(messages.getDate());

                    }

                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder{
        public TextView senderMessageText,receiverMessageText, senderMessageTime, receiverMessTime;
        public TextView senderMessageDate,receiverMessageDate, receiverName;
        public ImageView senderSentStatus;
        public LinearLayout receiverLinear, senderLinear;

        public MessageViewHolder(View itemView) {
            super(itemView);

            senderMessageText = itemView.findViewById(R.id.gchat_sender_message_text);
            senderMessageDate = itemView.findViewById(R.id.gchat_sender_message_date);
            senderMessageTime = itemView.findViewById(R.id.gchat_sender_message_time);
            receiverMessageText = itemView.findViewById(R.id.gchat_receiver_message_text);
            receiverMessTime = itemView.findViewById(R.id.gchat_receiver_message_time);
            receiverMessageDate = itemView.findViewById(R.id.gchat_receiver_message_date);
            receiverLinear = itemView.findViewById(R.id.gchat_receiver_linear1);
            senderLinear = itemView.findViewById(R.id.gchat_sender_linear1);
            senderSentStatus = itemView.findViewById(R.id.gsent_status);
            receiverName = itemView.findViewById(R.id.gchat_receiver_name);

        }



    }
}


