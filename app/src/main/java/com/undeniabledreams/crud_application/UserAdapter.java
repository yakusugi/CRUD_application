package com.undeniabledreams.crud_application;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>{
    private Context context;
    private List<UserModel> userModelList;

    public UserAdapter(Context context, List<UserModel> userModelList) {
        this.context = context;
        this.userModelList = userModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Initialize View Object and inflate the usersList layout from the XML Layout Activity
        View view = LayoutInflater.from(context).inflate(R.layout.users_list, parent, false);
        // Create an Instance of MyViewHolder class and the view to the MyViewHolder class
        final MyViewHolder viewHolder = new MyViewHolder(view);

        // Create a click listener and attach it to the layout moving the to the Activity
        viewHolder.layoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int id = userModelList.get(viewHolder.getAdapterPosition()).getId();
                Date date = userModelList.get(viewHolder.getAdapterPosition()).getDate();
                String storeName = userModelList.get(viewHolder.getAdapterPosition()).getStoreName();
                String productName = userModelList.get(viewHolder.getAdapterPosition()).getProductName();
                String productType = userModelList.get(viewHolder.getAdapterPosition()).getProductType();
                Double price = userModelList.get(viewHolder.getAdapterPosition()).getPrice();

                Intent intent = new Intent(context, Details_Activity.class);

                intent.putExtra("id", id);
                intent.putExtra("date", date);
                intent.putExtra("storeName", storeName);
                intent.putExtra("productName", productName);
                intent.putExtra("productType", productType);
                intent.putExtra("price", price);

                context.startActivity(intent);

            }
        });

        // Then return the view as is not a void method
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserModel userModel = userModelList.get(position);

        String productName = userModel.getProductName();

        holder.textViewName.setText(productName);
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private LinearLayout layoutContainer;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.list_name);
            layoutContainer = itemView.findViewById(R.id.list_container);
        }
    }
}
