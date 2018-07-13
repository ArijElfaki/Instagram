package org.codepath.instagram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.codepath.instagram.Model.Post;

import java.util.List;

public class profilePostAdapter extends RecyclerView.Adapter<profilePostAdapter.ViewHolder> {
    private List<Post> mPost;
    Context context;


    //pass in the Tweets array in the constructor
    public profilePostAdapter(List<Post> posts){
        mPost= posts;
    }

    //for each row, inflate the layout and cache references into ViewHolder

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        LayoutInflater inflater= LayoutInflater.from(context);

        View postView= inflater.inflate(R.layout.item_profile_post, parent, false);
        ViewHolder viewHolder= new ViewHolder(postView);
        return viewHolder;
    }

    // bind the values based on the position of the element

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        // get the data according to position
        Post post= mPost.get(position);

        GlideApp.with(context).load(post.getImage().getUrl())
                .into(holder.image);


    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }


    // create ViewHolder class

    public  class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;


        public ViewHolder(View itemView) {
            super(itemView);
            image= (ImageView)itemView.findViewById(R.id.profilePhotoPost);
        }

    }


    // Clean all elements of the recycler
    public void clear() {
        mPost.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        mPost.addAll(list);
        notifyDataSetChanged();
    }



}
