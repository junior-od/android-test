package ng.riby.androidtest.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import ng.riby.androidtest.R;
import ng.riby.androidtest.model.Location_Table;

public class DeviceLocationHistoryRecycleAdapter extends RecyclerView.Adapter<DeviceLocationHistoryRecycleAdapter.DeviceLocationViewHolder> {
    private List<Location_Table> location_tableList;
    DefaultListener listener;
    Context context;
    String temp1;
    String temp2;

    public interface DefaultListener{
        void onButtonClicked(Location_Table location_table);
    }

    public DeviceLocationHistoryRecycleAdapter(DefaultListener listener, Context context){
        this.listener = listener;
        this.context = context;
    }

    public void swapItems(List<Location_Table> location_table){
        if(location_table == null) return;

        if(location_tableList != null) location_tableList.clear();
        location_tableList = location_table;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DeviceLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_single_device_location, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceLocationViewHolder deviceLocationViewHolder, int i) {
        Location_Table table = location_tableList.get(i);
        if(table == null){
            return;
        }
        deviceLocationViewHolder.bindType(table);
        deviceLocationViewHolder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return location_tableList == null? 0 : location_tableList.size();
    }


    public abstract class DeviceLocationViewHolder extends RecyclerView.ViewHolder{
        DeviceLocationViewHolder(View itemView){
            super(itemView);
        }
        public abstract void bindType(Location_Table location_table);
    }


    public class ItemViewHolder extends DeviceLocationViewHolder implements View.OnClickListener{
        ConstraintLayout constraintLayout;
        TextView pointALoc;
        TextView pointBLoc;
        Button show_distance_button;

        public ItemViewHolder(View itemView){
            super(itemView);

            constraintLayout = itemView.findViewById(R.id.single_location_view);
            pointALoc = itemView.findViewById(R.id.pointALocation);
            pointBLoc = itemView.findViewById(R.id.pointBLocation);
            show_distance_button = itemView.findViewById(R.id.show_distance_but);
        }

        @Override
        public void bindType(Location_Table location_table){
            show_distance_button.setOnClickListener(this);
            temp1 = location_table.getPointALat()+","+location_table.getPointALong();
            pointALoc.setText(temp1);
            temp2 = location_table.getPointBLat()+","+location_table.getPointBLong();
            pointBLoc.setText(temp2);


        }

        @Override
        public void onClick(View v) {
            Location_Table locationTable = location_tableList.get(getAdapterPosition());
            if(locationTable == null){
                return;
            }
            if(listener != null){
                listener.onButtonClicked(locationTable);
            }
        }
    }

}
