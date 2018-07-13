package org.codepath.instagram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.ViewHolder> {
    private List<Object> comments;
    Context context;
    public ParseUser user;


    //pass in the Tweets array in the constructor
    public commentAdapter(List<Object> commentList){
        comments= commentList;
    }

    //for each row, inflate the layout and cache references into ViewHolder

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        LayoutInflater inflater= LayoutInflater.from(context);

        View postView= inflater.inflate(R.layout.item_comment, parent, false);
        ViewHolder viewHolder= new ViewHolder(postView);
        return viewHolder;
    }


    // bind the values based on the position of the element

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        // get the data according to position
        String comment= (String)(comments.get(position));
        final String [] commentSplit= comment.split(" ",2);
        String userId= commentSplit[0];


        ParseQuery<ParseUser> commentUser=  ParseUser.getQuery().whereEqualTo("objectId", userId);
        commentUser.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                for (int i=0; i<objects.size(); ++i) {
                    user = objects.get(i);
                    GlideApp.with(context).load(user.getParseFile("Profile").getUrl()).circleCrop()
                            .into(holder.ivProfile);
                    holder.tvComment.setText(Html.fromHtml("<b>@"+user.getUsername()+"</b> "+commentSplit[1]));

                }
            }
        });




    }

    @Override
    public int getItemCount() {
        return comments.size();
    }


    // create ViewHolder class

    public  class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfile;
        public TextView tvComment;

        public ViewHolder(View itemView) {
            super(itemView);
            // perform findViewById lookups
            ivProfile = (ImageView) itemView.findViewById(R.id.ivCommentProfile);
            tvComment = (TextView) itemView.findViewById(R.id.tvPostComment);
        }

    }


}