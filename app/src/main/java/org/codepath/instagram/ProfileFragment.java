package org.codepath.instagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.codepath.instagram.Model.Post;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    public File photoFile;

    public TextView userName;
    public Button btLogout;
    public ImageView profile;
    public Button setBio;
    public Button editBio;
    public TextView tvBio;
    public EditText etBio;


    public profilePostAdapter postAdapter;
    public ArrayList<Post> posts;
    public RecyclerView rvPosts;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profile=view.findViewById(R.id.ivProfile);
        setBio=view.findViewById(R.id.btSetBio);
        editBio=view.findViewById(R.id.btEditbio);
        tvBio=view.findViewById(R.id.tvBio);
        etBio=view.findViewById(R.id.etBio);
        rvPosts=view.findViewById(R.id.rvProfile);
        userName=(TextView) view.findViewById(R.id.profileUsername);

        posts=new ArrayList<>();
        postAdapter= new profilePostAdapter(posts);
        //RecyclerView setup (layout manager, use adapter)

        // Define 2 column grid layout
        final GridLayoutManager layout = new GridLayoutManager(getContext(), 2);


        rvPosts.setLayoutManager(layout);
        rvPosts.setAdapter(postAdapter);
        loadTopPosts();

        etBio.setText(tvBio.getText().toString());



        userName.setText(ParseUser.getCurrentUser().getUsername());
        setBio.setVisibility(View.INVISIBLE);
        etBio.setVisibility(View.INVISIBLE);

        if (ParseUser.getCurrentUser().getParseFile("Profile")!=null){
            GlideApp.with(getContext()).load(ParseUser.getCurrentUser().getParseFile("Profile").getUrl()).circleCrop()
                    .into(profile);}


        tvBio.setText( ParseUser.getCurrentUser().getString("Bio"));
        etBio.setText(tvBio.getText().toString());



        btLogout= view.findViewById(R.id.logout);
        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent i= new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dispatchTakePictureIntent();
            }
        });

        editBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBio.setVisibility(View.VISIBLE);
                etBio.setVisibility(View.VISIBLE);


                editBio.setVisibility(View.INVISIBLE);
                tvBio.setVisibility(View.INVISIBLE);
            }
        });

        setBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bio= etBio.getText().toString();

                ParseUser.getCurrentUser().put("Bio", bio);
                ParseUser.getCurrentUser().saveInBackground();

                tvBio.setText( ParseUser.getCurrentUser().getString("Bio"));

                setBio.setVisibility(View.INVISIBLE);
                etBio.setVisibility(View.INVISIBLE);

                editBio.setVisibility(View.VISIBLE);
                tvBio.setVisibility(View.VISIBLE);
            }
        });

    }




    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getActivity(), "org.codepath.instagram", photoFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.

        if (takePictureIntent.resolveActivity(ProfileFragment.this.getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            final Bitmap imageBitmap = (BitmapFactory.decodeFile(photoFile.getAbsolutePath()));
            profile.setImageBitmap(imageBitmap);

            final ParseUser user= ParseUser.getCurrentUser();
            final File file = getPhotoFileUri(photoFileName) ;
            final ParseFile parseFile = new ParseFile(photoFile);
            parseFile.saveInBackground();
            //query parse for post

            user.put("Profile", parseFile);
            user.saveInBackground();


            Log.d("ProfileFragment", "Set Profile!");
        }
    }



    private void loadTopPosts(){
        final Post.Query postQuery= new Post.Query();
        postQuery.getTop().withUser();

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e==null){
                    for (int i=0; i<objects.size(); ++i){
                        Log.d("TimelineFragment", "Post{"+i+"} = "
                                +objects.get(i).getDescription()
                                +"\nusername = "+objects.get(i).getUser().getUsername()
                        );

                        if (objects.get(i).getUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
                            posts.add(0,objects.get(i));
                            postAdapter.notifyDataSetChanged();}

                    }
                }
                else{
                    e.printStackTrace();
                }
            }
        });
    }


}
