package com.eoxys.mybus.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eoxys.mybus.R;
import com.eoxys.mybus.controller.PhotoActivity;
import com.eoxys.mybus.model.Stop_list_item;

import java.util.List;

public class Stop_adapter extends RecyclerView.Adapter<Stop_adapter.ViewHolder> {

    private List<Stop_list_item> listItems;
    private Context context;

    public Stop_adapter(List<Stop_list_item> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Stop_list_item list_item = listItems.get(position);

        holder.stopname.setText(list_item.getStopname());

        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, list_item.getStopname(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(context, PhotoActivity.class);
                intent.putExtra("stop_name", list_item.getStopname());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView stopname;
        public LinearLayout parent_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            stopname = (TextView) itemView.findViewById(R.id.stopname);
            parent_layout = (LinearLayout) itemView.findViewById(R.id.parent_layout);
        }
    }
}
