package ng.riby.androidtest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rows {
    @Expose @SerializedName("elements")
    private Elements[] elements;

    public Elements[] getElements ()
    {
        return elements;
    }

    public void setElements (Elements[] elements)
    {
        this.elements = elements;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [elements = "+elements+"]";
    }
}
