package ng.riby.androidtest.interfaces;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import ng.riby.androidtest.model.Location_Table;
import retrofit2.http.DELETE;

@Dao
public interface MyDao {

    @Insert
    public void addLocationOfDevice(Location_Table location_table);

    @Query("select * from location_table")
    public List<Location_Table> getLocationHistory();
//
//    @Query("DELETE from location_table")
//    public void deleteAll();



}
