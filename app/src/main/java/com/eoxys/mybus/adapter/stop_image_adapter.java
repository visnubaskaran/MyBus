package com.eoxys.mybus.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eoxys.mybus.R;
import com.eoxys.mybus.controller.PhotoActivity;
import com.eoxys.mybus.controller.PhotoDisplayActivity;
import com.eoxys.mybus.model.stop_image_model;

import java.util.List;

public class stop_image_adapter extends RecyclerView.Adapter<stop_image_adapter.ViewHolder> {

    private List<stop_image_model> listItems;
    private Context context;

    public stop_image_adapter(List<stop_image_model> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_image_list, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final stop_image_model list_item = listItems.get(position);

        holder.image_name.setText(list_item.getIamge_name());
        Glide.with(context).load(list_item.getImage_location()).into(holder.image_view);

        holder.img_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PhotoDisplayActivity.class);
                intent.putExtra("Img_path", list_item.getImage_location());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView image_name;
        public ImageView image_view;
        public LinearLayout img_parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_name = (TextView) itemView.findViewById(R.id.stop_name);
            image_view = (ImageView) itemView.findViewById(R.id.busStop_img);
            img_parent = (LinearLayout) itemView.findViewById(R.id.img_parent);
        }
    }
}

