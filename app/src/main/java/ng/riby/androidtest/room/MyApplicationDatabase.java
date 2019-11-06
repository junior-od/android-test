package ng.riby.androidtest.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import ng.riby.androidtest.interfaces.MyDao;
import ng.riby.androidtest.model.Location_Table;

@Database(entities = {Location_Table.class}, version = 1,exportSchema = false)
public abstract class MyApplicationDatabase extends RoomDatabase {
    public abstract MyDao myDao();

}
