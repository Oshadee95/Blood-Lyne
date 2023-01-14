package com.bloodlyne.donor.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bloodlyne.donor.databinding.FragmentChatBinding;
import com.bloodlyne.models.ChatMessage;

import org.json.JSONException;

import java.util.ArrayList;

public class ChatFragment extends Fragment {

    private FragmentChatBinding binding;
    //creating variables for our widgets in xml file.
    private RecyclerView chatsRV;
    private ImageView sendMsgIB;
    private EditText userMsgEdt;
    private final String USER_KEY = "user";
    private final String BOT_KEY = "bot";
    //creating a variable for our volley request queue.
    private RequestQueue mRequestQueue;
    //creating a variable for array list and adapter class.
    private ArrayList<ChatMessage> messageModalArrayList;
    private ChatAdapter chatAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentChatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //on below line we are initializing all our views.
        chatsRV = binding.idRVChats;
        sendMsgIB = binding.idIBSend;
        sendMsgIB.setClickable(true);
        userMsgEdt = binding.idEdtMessage;
        //below line is to initialize our request queue.
        mRequestQueue = Volley.newRequestQueue(getContext());
        mRequestQueue.getCache().clear();
        //creating a new array list
        messageModalArrayList = new ArrayList<>();
        //adding on click listener for send message button.
        sendMsgIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checking if the message entered by user is empty or not.
                if (userMsgEdt.getText().toString().isEmpty()) {
                    //if the edit text is empty display a toast message.
                    Toast.makeText(getContext(), "Please enter your message..", Toast.LENGTH_SHORT).show();
                    return;
                }
                //calling a method to send message to our bot to get response.
                sendMessage(userMsgEdt.getText().toString());
                //below line we are setting text in our edit text as empty
                userMsgEdt.setText("");

            }
        });

        //on below line we are initialiing our adapter class and passing our array lit to it.
        chatAdapter = new ChatAdapter(getContext(), messageModalArrayList);
        //below line we are creating a variable for our linear layout manager.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        //below line is to set layout manager to our recycler view.
        chatsRV.setLayoutManager(linearLayoutManager);
        //below line we are setting adapter to our recycler view.
        chatsRV.setAdapter(chatAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void sendMessage(String userMsg) {
        //below line is to pass message to our array list which is entered by the user.
        messageModalArrayList.add(new ChatMessage(userMsg, USER_KEY));
        chatAdapter.notifyDataSetChanged();
        //url for our brain
        //make sure to add mshape for uid.
        String url = "http://api.brainshop.ai/get?bid=165780&key=3vuFCY9K2P1ZDPbY&uid=[uid]&msg=" + userMsg;
        //creating a variable for our request queue.
        RequestQueue queue = Volley.newRequestQueue(getContext());
        //on below line we are making a json object request for a get request and passing our url .
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                //in on response method we are extracting data from json response and adding this response to our array list.
                String botResponse = response.getString("cnt");
                messageModalArrayList.add(new ChatMessage(botResponse, BOT_KEY));
                //notifying our adapter as data changed.
                chatAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
                //handling error response from bot.
                messageModalArrayList.add(new ChatMessage("No response", BOT_KEY));
                chatAdapter.notifyDataSetChanged();

            }
        }, error -> {
            //error handling.
            messageModalArrayList.add(new ChatMessage("Sorry no response found", BOT_KEY));
            Toast.makeText(getContext(), "No response from the bot..", Toast.LENGTH_SHORT).show();
        });
        //at last adding json object request to our queue.
        queue.add(jsonObjectRequest);
    }
}