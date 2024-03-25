package algonquin.cst2355.rama0110;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

import data.ChatRoomViewModel;

public class ChatRoom extends AppCompatActivity {

    private ActivityChatRoomBinding binding;
    private ChatRoomViewModel chatModel;
    private ArrayList<ChatMessage> messages;
    private RecyclerView.Adapter<MyRowHolder> myAdapter;

    ChatMessageDAO mDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.myToolbar);

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
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                builder.setMessage(getString(R.string.Deletemsg) + messageText.getText())
                        .setTitle(getString(R.string.title))
                        .setNegativeButton(getString(R.string.no), (dialog, cl) -> {})
                        .setPositiveButton(getString(R.string.yes), (dialog, cl) -> {
                            ChatMessage removedMessage = messages.get(position);
                            messages.remove(position);
                            myAdapter.notifyItemRemoved(position);

                            Snackbar.make(messageText, getString(R.string.Deletesuccess), Snackbar.LENGTH_LONG)
                                    .setAction(getString(R.string.undo), click -> {
                                        messages.add(position, removedMessage);
                                        myAdapter.notifyItemInserted(position);
                                    })
                                    .show();
                        })
                        .create()
                        .show();
            });
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        super.onCreateOptionsMenu(menu);

        return true;
    }

   @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            int itemId = item.getItemId();

            if (itemId == R.id.item_1) {
                // Handle delete all messages menu item
                new AlertDialog.Builder(ChatRoom.this)
                        .setMessage(getString(R.string.confirm_delete_all_messages))
                        .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                            // Clear all messages
                            messages.clear();

                            myAdapter.notifyDataSetChanged();
                            Snackbar.make(binding.getRoot(), getString(R.string.all_messages_deleted), Snackbar.LENGTH_LONG)
                                    .setAction(getString(R.string.undo), view -> {
                                        // Restore messages (if needed)
                                        // You can implement this part based on your requirements
                                    })
                                    .show();
                        })
                        .setNegativeButton(getString(R.string.no), null)
                        .create()
                        .show();
                return true;
            } else if (itemId == R.id.item_about) {
                // Show About toast message
                Toast.makeText(this, "Version 1.0, created by Raman", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return super.onOptionsItemSelected(item);
            }
        }

    }



