package com.gmail.peeman34.eglisaofficial;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.common.api.Api;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Created by pee on 3/27/2017.
 */

class EGLISAOFFICIAL extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

   FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG);

        if (!FirebaseApp.getApps(this).isEmpty()) {


            FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        }

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
       Picasso built  =builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);



    }

}
