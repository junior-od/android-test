package ng.riby.androidtest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Duration {
    @Expose @SerializedName("text")
    private String text;

    @Expose @SerializedName("value")
    private String value;

    public String getText ()
    {
        return text;
    }

    public void setText (String text)
    {
        this.text = text;
    }

    public String getValue ()
    {
        return value;
    }

    public void setValue (String value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [text = "+text+", value = "+value+"]";
    }
}
