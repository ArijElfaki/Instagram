package org.codepath.instagram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.codepath.instagram.Model.Post;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String imagePath= "/storage/emulated/0/DCIM/Camera/IMG_20180709_175425.jpg";

    private EditText descriptionInput;
    private Button createButton;
    private Button refreshButton;
    private Button logoutButton;



    final FragmentManager fragmentManager = getSupportFragmentManager();

    // define your fragments here
    final Fragment TimeLine = new TimelineFragment();
    final Fragment Camera = new CameraFragment();
    final Fragment Profile = new ProfileFragment();

   private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationView=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
       @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.Profile:
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_profile, Profile).commit();
                    return true;
                case R.id.Home:
                    FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
                    fragmentTransaction2.replace(R.id.fragment_timeline, TimeLine).commit();
                    return true;
                case R.id.Camera:
                    FragmentTransaction fragmentTransaction3 = fragmentManager.beginTransaction();
                    fragmentTransaction3.replace(R.id.fragment_camera, Camera).commit();
                    return true;
            }

            return false;
            }
   };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



       /* descriptionInput= findViewById(R.id.etDescription);
        createButton= findViewById(R.id.btCreate);
        refreshButton= findViewById(R.id.btRefresh);
        logoutButton= findViewById(R.id.btlogout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();
                Log.d("HomeActivity", "Logged out");
                final Intent intent= new Intent ( HomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        createButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final String description= descriptionInput.getText().toString();
                final ParseUser user= ParseUser.getCurrentUser();
                final File file = new File (imagePath);
                final ParseFile parseFile = new ParseFile(file);

                createPost(description, parseFile, user);
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadTopPosts();;

            }
        });
        loadTopPosts();

        */


    }

    private void createPost(String description, ParseFile imageFile, ParseUser user){
        final Post newPost= new Post();
        newPost.setDescription(description);
        newPost.setImage(imageFile);
        newPost.setUser(user);

        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Log.d("HomeActivity", "Create post success!");
                   
                }
                else{
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadTopPosts(){
        final Post.Query postQuery= new Post.Query();
        postQuery.getTop().withUser();

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e==null){
                    for (int i=0; i<objects.size(); ++i){
                        Log.d("HomeActivity", "Post{"+i+"} = "
                                +objects.get(i).getDescription()
                                +"\nusername = "+objects.get(i).getUser().getUsername()
                        );
                    }
                }
                else{
                    e.printStackTrace();
                }
            }
        });
    }

}
