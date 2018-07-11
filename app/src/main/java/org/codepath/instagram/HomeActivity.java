package org.codepath.instagram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import org.codepath.instagram.Model.Post;

public class HomeActivity extends AppCompatActivity {

    final FragmentManager fragmentManager = getSupportFragmentManager();

    // define your fragments here
    final Fragment TimeLine = new TimelineFragment();
    final Fragment Camera = new CameraFragment();
    final Fragment Profile = new ProfileFragment();

    final Fragment Details= new DetailFragment();

   private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationView=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
       @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.Profile:
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment ,Profile).commit();
                    return true;
                case R.id.Home:
                    FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
                    fragmentTransaction2.replace(R.id.fragment, TimeLine).commit();
                    return true;
                case R.id.Camera:
                    FragmentTransaction fragmentTransaction3 = fragmentManager.beginTransaction();
                    fragmentTransaction3.replace(R.id.fragment, Camera).commit();
                    return true;
            }

            return false;
            }
   };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, TimeLine).commit();

        BottomNavigationView navigation= (BottomNavigationView)findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(bottomNavigationView);

    }




    public void openDetails(Post post){
        fragmentManager.beginTransaction();
        Bundle args= new Bundle();
        args.putString("Post", post.getObjectId());
        Details.setArguments(args);
        FragmentTransaction fragmentTransaction4 = fragmentManager.beginTransaction();
        fragmentTransaction4.replace(R.id.fragment, Details).commit();


    }


}
