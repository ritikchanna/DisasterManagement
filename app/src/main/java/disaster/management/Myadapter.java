package disaster.management;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import disaster.management.Activities.RequestActivity;

public class Myadapter extends RecyclerView.Adapter<Myadapter.MyViewHolder> {
    private ArrayList<Request> mDataset;
    private Context mContext;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public MyViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.rv_tv);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Myadapter(ArrayList<Request> myDataset,Context context) {
        mDataset = myDataset;
        mContext=context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Myadapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
//        TextView v = (TextView) LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_request, parent, false);
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_request, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
       Request request=mDataset.get(position);
        holder.mTextView.setText(request.getName());
        String status=request.getStatus();
        if(status.equalsIgnoreCase("denied"))
            holder.mTextView.setTextColor(Color.parseColor("#FF0000"));
        else if(status.equalsIgnoreCase("accepted"))
            holder.mTextView.setTextColor(Color.parseColor("#00FF00"));
        else
            holder.mTextView.setTextColor(Color.parseColor("#FFCC00"));
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(mContext,RequestActivity.class);
                i.putExtra("status",request.getStatus());
                i.putExtra("contact",request.getContact());
                i.putExtra("location",request.getLocation());
                i.putExtra("message",request.getMessage());
                i.putExtra("name",request.getName());
                i.putExtra("time",request.getTime());
                i.putExtra("uid",request.getUID());
                i.putExtra("category",request.getCategory());
                mContext.startActivity(i);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}