package ng.riby.androidtest.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "location_table")
public class Location_Table {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "point_A_lat")
    private String pointALat;

    @ColumnInfo(name = "point_A_long")
    private String pointALong;

    @ColumnInfo(name = "point_B_lat")
    private String pointBLat;

    @ColumnInfo(name = "point_B_long")
    private String pointBLong;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPointALat() {
        return pointALat;
    }

    public void setPointALat(String pointALat) {
        this.pointALat = pointALat;
    }

    public String getPointALong() {
        return pointALong;
    }

    public void setPointALong(String pointALong) {
        this.pointALong = pointALong;
    }

    public String getPointBLat() {
        return pointBLat;
    }

    public void setPointBLat(String pointBLat) {
        this.pointBLat = pointBLat;
    }

    public String getPointBLong() {
        return pointBLong;
    }

    public void setPointBLong(String pointBLong) {
        this.pointBLong = pointBLong;
    }
}
