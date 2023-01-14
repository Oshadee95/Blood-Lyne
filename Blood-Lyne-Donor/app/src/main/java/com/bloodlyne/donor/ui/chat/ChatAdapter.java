package com.bloodlyne.donor.ui.chat;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodlyne.donor.R;
import com.bloodlyne.models.ChatMessage;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<ChatMessage> chatMessageList;
    private ProgressDialog progressDialog;

    ChatAdapter(Context context, List<ChatMessage> chatMessageList) {
        this.context = context;
        this.chatMessageList = chatMessageList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        //below code is to switch our layout type along with view holder.
        switch (viewType) {
            case 0:
                //below line we are inflating user message layout.
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_msg, parent, false);
                return new ChatAdapter.UserViewHolder(view);
            case 1:
                //below line we are inflating bot message layout.
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_msg, parent, false);
                return new ChatAdapter.BotViewHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        //this method is use to set data to our layout file.
        ChatMessage modal = chatMessageList.get(position);
        switch (modal.getSender()) {
            case "user":
                //below line is to set the text to our text view of user layout
                ((ChatAdapter.UserViewHolder) holder).userTV.setText(modal.getMessage());
                break;
            case "bot":
                //below line is to set the text to our text view of bot layout
                ((ChatAdapter.BotViewHolder) holder).botTV.setText(modal.getMessage());
                break;
        }
    }


    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        public NotificationViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        //below line of code is to set position.
        switch (chatMessageList.get(position).getSender()) {
            case "user":
                return 0;
            case "bot":
                return 1;
            default:
                return -1;
        }

    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        //creating a variable for our text view.
        TextView userTV;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            //initializing with id.
            userTV = itemView.findViewById(R.id.idTVUser);
        }
    }

    public static class BotViewHolder extends RecyclerView.ViewHolder {
        //creating a variable for our text view.
        TextView botTV;

        public BotViewHolder(@NonNull View itemView) {
            super(itemView);
            //initializing with id.
            botTV = itemView.findViewById(R.id.idTVBot);
        }
    }
}
