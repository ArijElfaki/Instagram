package org.codepath.instagram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;

import org.codepath.instagram.Model.Post;


public class DetailFragment extends Fragment {

    public ImageView ivProfileImage;
    public ImageView image;
    public TextView userName;
    public TextView timeStamp;
    public TextView description;
    public TextView likes;
    public TextView comment;
    public ImageView commentIcon;
    public ImageView likeIcon;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
        image= (ImageView)view.findViewById(R.id.postImage);
        userName= (TextView)view.findViewById(R.id.tvUserName);
        description= (TextView)view.findViewById(R.id.tvdescript);
        likes = (TextView) view.findViewById(R.id.likes);
        comment = (TextView) view.findViewById(R.id.tvcomment);
        commentIcon = (ImageView) view.findViewById(R.id.ivComment);
        likeIcon = (ImageView) view.findViewById(R.id.ivLikes);
        timeStamp = (TextView) view.findViewById(R.id.tvTimeStamp);

        Bundle args= getArguments();
        String id= args.getString("Post");
        //query parse for post

        Post.Query postQuery= new Post.Query().withUser();
        postQuery.getQuery(Post.class).getInBackground(id, new GetCallback<Post>() {
            @Override
            public void done(Post object, ParseException e) {
                try {
                    userName.setText(object.getUser().fetchIfNeeded().getUsername());
                    description.setText(object.getDescription());
                    timeStamp.setText(object.getRelativeTimeAgo());
                    GlideApp.with(getContext()).load(object.getImage().getUrl())
                            .into(image);
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }

            }
        });


    }
}
