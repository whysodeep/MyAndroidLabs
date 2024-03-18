package algonquin.cst2355.rama0110;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import algonquin.cst2355.rama0110.ChatMessageDAO;

@Database(entities = {ChatMessage.class}, version = 1)
public abstract class MessageDatabase extends RoomDatabase {

    public abstract ChatMessageDAO cmDAO();
}
