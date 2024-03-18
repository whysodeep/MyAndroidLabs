package algonquin.cst2355.rama0110;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {


    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name ="id")
    public int id;

    @ColumnInfo(name ="message")
    protected String message;

    @ColumnInfo(name ="TimeSent")
    protected String timeSent;

    @ColumnInfo(name ="IsSentButton")
    protected boolean isSentButton;


    public ChatMessage(String message, String timeSent, boolean isSentButton) {
        this.message = message;
        this.timeSent = timeSent;
        this.isSentButton = isSentButton;
    }

    public String getMessage() {
        return message;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public boolean isSentButton() {
        return isSentButton;
    }
}
