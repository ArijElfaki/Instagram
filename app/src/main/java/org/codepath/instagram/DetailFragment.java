package org.codepath.instagram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;

import org.codepath.instagram.Model.Post;

import java.util.ArrayList;
import java.util.List;


public class DetailFragment extends Fragment {

    public ImageView ivProfileImage;
    public ImageView image;
    public TextView userName;
    public TextView timeStamp;
    public TextView description;
    public ImageView commentIcon;
    public ImageView likeIcon;
    public TextView commentCount;

    public RecyclerView rvComments;
    public commentAdapter commentAdapter;
    public List<Object> comments;



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

        commentIcon = (ImageView) view.findViewById(R.id.ivComment);
        likeIcon = (ImageView) view.findViewById(R.id.ivLikes);
        timeStamp = (TextView) view.findViewById(R.id.tvTimeStamp);
        rvComments= (RecyclerView)view.findViewById(R.id.rvComments);
        commentCount= view.findViewById(R.id.detailCommentCount);
        comments= new ArrayList<>();

        commentAdapter= new commentAdapter(comments);
        //RecyclerView setup (layout manager, use adapter)
        rvComments.setLayoutManager(new LinearLayoutManager(getContext()));
        rvComments.setAdapter(commentAdapter);


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
                    GlideApp.with(getContext()).load(object.getUser().fetchIfNeeded().getParseFile("Profile").getUrl()).circleCrop()
                            .into(ivProfileImage);
                    comments.clear();
                    comments.addAll(object.getList("Comment"));
                    commentCount.setText(""+comments.size());
                    commentAdapter.notifyDataSetChanged();


                } catch (ParseException e1) {
                    e1.printStackTrace();
                }

            }
        });







    }
}
