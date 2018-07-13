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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.codepath.instagram.Model.Post;

import java.io.File;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class CameraFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    File photoFile;
    Button btCamera;
    Button btPost;
    ImageView userPic;
    public EditText description;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btCamera=  view.findViewById(R.id.buttonCamera);
        btCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        btPost=view.findViewById(R.id.buttonPost);
        btPost.setVisibility(View.INVISIBLE);

        userPic= view.findViewById(R.id.ivPic);
        userPic.setVisibility(View.INVISIBLE);
        description=view.findViewById(R.id.picDescription);
        description.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            final Bitmap imageBitmap = (BitmapFactory.decodeFile(photoFile.getAbsolutePath()));
            userPic.setImageBitmap(imageBitmap);

            btCamera.setVisibility(View.INVISIBLE);
            userPic.setVisibility(View.VISIBLE);
            description.setVisibility(View.VISIBLE);
            btPost.setVisibility(View.VISIBLE);
            btPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String descript =description.getText().toString();
                    final ParseUser user= ParseUser.getCurrentUser();
                    final File file = getPhotoFileUri(photoFileName) ;
                    final ParseFile parseFile = new ParseFile(file);

                    Log.d("CameraActivity", "Inside onCLick!");
                    createPost(descript, parseFile, user);

                    description.getText().clear();
                    btPost.setVisibility(View.INVISIBLE);
                    userPic.setVisibility(View.INVISIBLE);
                    description.setVisibility(View.INVISIBLE);
                    btCamera.setVisibility(View.VISIBLE);


                }
            });
        }
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

        if (takePictureIntent.resolveActivity(CameraFragment.this.getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void createPost(String description, ParseFile imageFile, ParseUser user){
        final Post newPost= new Post();
        newPost.setDescription(description);
        newPost.setImage(imageFile);
        newPost.setUser(user);
        newPost.put("Comment", new ArrayList<String>());

        Log.d("CameraActivity", "Inside CameraActivity");
        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Log.d("CameraActivity", "Create post success!");

                }
                else{
                    Log.d("CameraActivity", "Create post failure!");
                    e.printStackTrace();
                }
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
}
