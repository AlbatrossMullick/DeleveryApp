package com.albatross.deleveryapp;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firestore.v1.StructuredQuery;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    List<ModelClass> modelClassList;

    public HomeAdapter(List<ModelClass> modelClassList) {
        this.modelClassList = modelClassList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    String image = modelClassList.get(position).getImage();
        String orderid = modelClassList.get(position).getOrdernumber();
        String toaddtrss = modelClassList.get(position).getToAddress();
        String fromaddress = modelClassList.get(position).getFromAddress();
        String status = modelClassList.get(position).getStatus();
        holder.setData(orderid,image,toaddtrss,fromaddress,status);
    }


    @Override
    public int getItemCount() {
        return modelClassList.size();
    }

    class ViewHolder extends  RecyclerView.ViewHolder{
        private TextView orderid;
        private TextView toAddress;
        private ImageView orderLogo;
        private  TextView fromAddress;
        private TextView orderStatus;
        private RelativeLayout orderslayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderid = itemView.findViewById(R.id.home_ordernumber);
            toAddress = itemView.findViewById(R.id.home_toOrderAddress);
            fromAddress = itemView.findViewById(R.id.home_fromOrderAddress);
            orderLogo = itemView.findViewById(R.id.home_orderIcon);
            orderslayout = itemView.findViewById(R.id.homeorder);
            orderStatus = itemView.findViewById(R.id.home_orderStatus);
        }
        private void setData(final String orderno,String orderico, String orderto,String orderfrom,String status){
            orderid.setText(orderno);

            Glide
                    .with(itemView)
                    .load(orderico)
                    .circleCrop()
                    .placeholder(R.mipmap.loading)
                    .into(orderLogo);

            toAddress.setText(orderto);
            fromAddress.setText(orderfrom);
            orderStatus.setText(status);
            orderslayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(),Order.class);
                    intent.putExtra("orderid",orderno);
                    itemView.getContext().startActivity(intent);

                }
            });

        }

    }



}
