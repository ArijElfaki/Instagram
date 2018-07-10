package org.codepath.instagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import org.codepath.instagram.Model.Post;

public class ParseApp extends Application{

    @Override
    public void onCreate(){
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        final Parse.Configuration configuration= new Parse.Configuration.Builder(this)
                .applicationId("arijelfaki-fbu-instagram")
                .clientKey("lidaikafledrowssap")
                .server("http://arijelfaki-fbu-instagram.herokuapp.com/parse")
                .build();
        Parse.initialize(configuration);
    }


}
