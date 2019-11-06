package ng.riby.androidtest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetDistanceErrorResponse {
    @Expose @SerializedName("error_message")
    private String error_message;

    @Expose @SerializedName("destination_addresses")
    private String[] destination_addresses;

    @Expose @SerializedName("rows")
    private String[] rows;

    @Expose @SerializedName("origin_addresses")
    private String[] origin_addresses;

    @Expose @SerializedName("status")
    private String status;

    public String getError_message ()
    {
        return error_message;
    }

    public void setError_message (String error_message)
    {
        this.error_message = error_message;
    }

    public String[] getDestination_addresses ()
    {
        return destination_addresses;
    }

    public void setDestination_addresses (String[] destination_addresses)
    {
        this.destination_addresses = destination_addresses;
    }

    public String[] getRows ()
    {
        return rows;
    }

    public void setRows (String[] rows)
    {
        this.rows = rows;
    }

    public String[] getOrigin_addresses ()
    {
        return origin_addresses;
    }

    public void setOrigin_addresses (String[] origin_addresses)
    {
        this.origin_addresses = origin_addresses;
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
        return "ClassPojo [error_message = "+error_message+", destination_addresses = "+destination_addresses+", rows = "+rows+", origin_addresses = "+origin_addresses+", status = "+status+"]";
    }
}
