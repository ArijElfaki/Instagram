package org.codepath.instagram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.codepath.instagram.Model.Post;

import java.util.List;

public class postAdapter extends RecyclerView.Adapter<postAdapter.ViewHolder> {
    private List<Post> mPost;
    Context context;


    //pass in the Tweets array in the constructor
    public postAdapter(List<Post> posts){
        mPost= posts;
    }

    //for each row, inflate the layout and cache references into ViewHolder

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        LayoutInflater inflater= LayoutInflater.from(context);

        View postView= inflater.inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolder= new ViewHolder(postView);
        return viewHolder;
    }

    // bind the values based on the position of the element

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        // get the data according to position
        Post post= mPost.get(position);
        holder.userName.setText(post.getUser().getUsername());
        holder.description.setText(post.getDescription());
        holder.timeStamp.setText(post.getRelativeTimeAgo());
        GlideApp.with(context).load(post.getImage().getUrl())
               .into(holder.image);

        GlideApp.with(context).load(post.getUser().getParseFile("Profile").getUrl())
                .into(holder.ivProfileImage);

    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }


    // create ViewHolder class

    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivProfileImage;
        public ImageView image;
        public TextView userName;
        public TextView timeStamp;
        public TextView description;
        public TextView likes;
        public EditText comment;
        public ImageView commentIcon;
        public ImageView likeIcon;


        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById lookups
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            image= (ImageView)itemView.findViewById(R.id.postImage);
            userName= (TextView)itemView.findViewById(R.id.tvUserName);
            description= (TextView)itemView.findViewById(R.id.tvdescript);
            commentIcon = (ImageView) itemView.findViewById(R.id.ivComment);
            likeIcon = (ImageView) itemView.findViewById(R.id.ivLikes);
            timeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
            comment= (EditText)itemView.findViewById(R.id.etComment);
            image.setOnClickListener(this);


            commentIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String text= comment.getText().toString();
                    int position = getAdapterPosition();
                    // make sure the position is valid, i.e. actually exists in the view
                    if (position != RecyclerView.NO_POSITION) {
                        // get the post at the position, this won't work if the class is static
                        Post post = mPost.get(position);

                        post.add("Comment",text);
                        post.saveInBackground();
                        Log.d("postAdapter", "Comment added");
                        comment.getText().clear();
                    }


                }
            });

        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the post at the position, this won't work if the class is static
                Post post = mPost.get(position);
                ((HomeActivity)context).openDetails(post);

            }
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
