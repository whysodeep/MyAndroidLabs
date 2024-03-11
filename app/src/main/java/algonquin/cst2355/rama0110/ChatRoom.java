package algonquin.cst2355.rama0110;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import algonquin.cst2355.rama0110.databinding.ActivityChatRoomBinding;
import algonquin.cst2355.rama0110.databinding.ReceiveMessageBinding;
import algonquin.cst2355.rama0110.databinding.SentMessageBinding;
import data.ChatRoomViewModel;

public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    ChatRoomViewModel chatModel;
    ArrayList<ChatMessage> messages;
    private RecyclerView.Adapter<MyRowHolder> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();
        if (messages == null) {
            chatModel.messages.postValue(messages = new ArrayList<>());
        }

        binding.sendButton.setOnClickListener(click -> {
            sendMessage(true);
        });

        binding.recieveButton.setOnClickListener(click -> {
            sendMessage(false);
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 0) {
                    // Inflate the layout for sent messages
                    SentMessageBinding binding = SentMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                    return new MyRowHolder(binding.getRoot());
                } else {
                    // Inflate the layout for received messages
                    ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                    return new MyRowHolder(binding.getRoot());
                }
            }


            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ChatMessage message = messages.get(position);
                holder.bind(message);
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position) {
                // Check if the message at this position was sent or received
                boolean isSent = messages.get(position).isSentButton();

                // Return 0 for sent messages, 1 for received messages
                return isSent ? 0 : 1;
            }

        };
        binding.recyclerView.setAdapter(myAdapter);
    }

    private void sendMessage(boolean isSent) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
        String currentDateandTime = sdf.format(new Date());
        ChatMessage message = new ChatMessage(binding.editText1.getText().toString(), currentDateandTime, isSent);
        messages.add(message);
        myAdapter.notifyItemInserted(messages.size() - 1);
        binding.editText1.setText("");
    }

    static class MyRowHolder extends RecyclerView.ViewHolder {
        private final TextView messageText;
        private final TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }

        public void bind(ChatMessage message) {
            messageText.setText(message.getMessage());
            timeText.setText(message.getTimeSent());
        }
    }
}
