package com.ztobit.firebaseotp;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.FeedImageView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<String> values;
    JSONArray jsonArrayvalues;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ad_profile_name;
        public TextView ad_content;
        public NetworkImageView profile_pic;
        public View layout;
        public FeedImageView feed_image;
        public ViewHolder(View v) {
            super(v);
            layout = v;
            ad_profile_name = (TextView) v.findViewById(R.id.profile_name);
            ad_content = (TextView) v.findViewById(R.id.content);
        }
    }

//    public void add(int position, String item) {
//        values.add(position, item);
//        notifyItemInserted(position);
//    }
//
//    public void remove(int position) {
//        values.remove(position);
//        notifyItemRemoved(position);
//    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(JSONArray myDataset) {
        jsonArrayvalues = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.recycler_view_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        try {

            JSONObject jsonObject = jsonArrayvalues.getJSONObject(position);
            holder.ad_profile_name.setText(jsonObject.getString("phone"));
            holder.ad_content.setText(jsonObject.getString("content"));
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        final String name = values.get(position);
//        holder.txtHeader.setText(name);
//        holder.txtHeader.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                remove(position);
//            }
//        });

//        holder.txtFooter.setText("Footer: " + name);


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return jsonArrayvalues.length();
    }

}