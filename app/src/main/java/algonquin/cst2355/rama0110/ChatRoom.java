package algonquin.cst2355.rama0110;

import static algonquin.cst2355.rama0110.ChatMessageDAO.*;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2355.rama0110.databinding.ActivityChatRoomBinding;
import algonquin.cst2355.rama0110.databinding.ReceiveMessageBinding;
import algonquin.cst2355.rama0110.databinding.SentMessageBinding;
import algonquin.cst2355.rama0110.ChatMessage;
import algonquin.cst2355.rama0110.ChatMessageDAO;
import algonquin.cst2355.rama0110.MessageDatabase;
import data.ChatRoomViewModel;

public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    ChatRoomViewModel chatModel;
    ArrayList<ChatMessage> messages;
     RecyclerView.Adapter myAdapter;

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
            sendMessage();
        });

        binding.recieveButton.setOnClickListener(click -> {
            recieveMessage();
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
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
                holder.messageText.setText(message.getMessage());
                holder.timeText.setText(message.getTimeSent());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position) {
                // Check if the message at this position was sent or received
                ChatMessage obj = messages.get(position);

                // Return 0 for sent messages, 1 for received messages
                return obj.isSentButton() ? 0 : 1;
            }
        });

        // Initialize Room Database and DAO on a separate thread
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            MessageDatabase db = Room.databaseBuilder(getApplicationContext(),
                            MessageDatabase.class, "database-name")
                    .build();
            ChatMessageDAO mDAO = db.cmDAO();
            List<ChatMessage> fetchedMessages = mDAO.getAllMessages();

            runOnUiThread(() -> {
                messages.addAll(fetchedMessages);
                myAdapter.notifyDataSetChanged(); // Notify RecyclerView adapter of data change
            });
        });
    }

    private void sendMessage() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
        String currentDateandTime = sdf.format(new Date());
        ChatMessage message = new ChatMessage(binding.editText1.getText().toString(), currentDateandTime, true);
        messages.add(message);
        myAdapter.notifyItemInserted(messages.size() - 1);
        binding.editText1.setText("");
    }

    private void recieveMessage() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
        String currentDateandTime = sdf.format(new Date());
        ChatMessage message = new ChatMessage(binding.editText1.getText().toString(), currentDateandTime, false);
        messages.add(message);
        myAdapter.notifyItemInserted(messages.size() - 1);
        binding.editText1.setText("");
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
                builder.setMessage( getString(R.string.Deletemsg) + messageText.getText())
                        .setTitle(getString(R.string.title))

                .setNegativeButton(getString(R.string.no), (dialog, cl) -> {})

                .setPositiveButton(getString(R.string.yes), (dialog, cl) -> {

                    ChatMessage removovedMessage = messages.get(position);
                    messages.remove(position);
                    myAdapter.notifyItemRemoved(position);

                    Snackbar.make(messageText, getString(R.string.Deletesuccess) + position,Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.undo) , click -> {
                                messages.add(position,removovedMessage);
                                myAdapter.notifyItemInserted(position);
                            })
                            .show();
                })
                .create().show();

            });
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }
}
