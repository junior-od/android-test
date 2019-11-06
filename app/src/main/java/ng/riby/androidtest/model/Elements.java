package ng.riby.androidtest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Elements {
    @Expose @SerializedName("duration")
    private Duration duration;

    @Expose @SerializedName("distance")
    private Distance distance;

    @Expose@SerializedName("status")
    private String status;

    public Duration getDuration ()
    {
        return duration;
    }

    public void setDuration (Duration duration)
    {
        this.duration = duration;
    }

    public Distance getDistance ()
    {
        return distance;
    }

    public void setDistance (Distance distance)
    {
        this.distance = distance;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [duration = "+duration+", distance = "+distance+", status = "+status+"]";
    }
}
