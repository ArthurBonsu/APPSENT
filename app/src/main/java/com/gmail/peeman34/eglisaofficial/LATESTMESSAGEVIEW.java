package com.gmail.peeman34.eglisaofficial;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by pee on 8/5/2016.
 */

public class LATESTMESSAGEVIEW extends AppCompatActivity implements MediaPlayer.OnPreparedListener {
    private RecyclerView latestmessageviewrecycler;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseLatestmessage;
    String churchkey;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Query mQueryLatestMessage;
    MediaPlayer mmediaPlayer ;
    boolean playing;

    public LATESTMESSAGEVIEW() {
        super();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.latestmessageviewrecyclerview);


        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("LATESTMESSAGE");
        mDatabaseLatestmessage = FirebaseDatabase.getInstance().getReference().child("LATESTMESSAGE");

        mDatabase.keepSynced(true);
        mDatabaseLatestmessage.keepSynced(true);
        churchkey = getIntent().getExtras().getString("churchkey");

        mQueryLatestMessage = mDatabaseLatestmessage.orderByChild("churchid").equalTo(churchkey);

        mmediaPlayer = new MediaPlayer();

        latestmessageviewrecycler = (RecyclerView) findViewById(R.id.latestmessageviewrecycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // latestmessageviewrecycler.setHasFixedSize(true);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        latestmessageviewrecycler.setLayoutManager(layoutManager);

//        checkUserExist();


    }

    @Override
    public void onPrepared(MediaPlayer mp) {
         mmediaPlayer = mp;
        mmediaPlayer.start();

        if (mmediaPlayer.isPlaying()){
            if (mmediaPlayer !=null){
                mmediaPlayer.pause();
            }


        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mmediaPlayer !=null){
            mmediaPlayer.start();

        }


    }



    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<LatestMessageViewPull, LATESTMESSAGEVIEW.LatestMessageViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<LatestMessageViewPull, LatestMessageViewHolder>(

                LatestMessageViewPull.class,
                R.layout.latestmessageview,
                LatestMessageViewHolder.class,
                mQueryLatestMessage
        ) {

            @Override
            protected void populateViewHolder(final LatestMessageViewHolder viewHolder, final LatestMessageViewPull model, int position) {
                playing =true;

                viewHolder.setTitle(model.getTitle());
                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.setDate(model.getDate());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setPreacher(model.getPreacher());
                viewHolder.setUsername(model.getUsername());
                viewHolder.setAudio(model.getAudio());
                // about creating url button


                viewHolder.StreamAudio.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if (playing){

                            viewHolder.StreamAudio.setImageResource(R.drawable.pausebuttonagainsyy);
                        }else {
                            viewHolder.StreamAudio.setImageResource(R.drawable.playbutton2);
                        }

                                  mmediaPlayer.reset();
                               mmediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        try {

                                mmediaPlayer.setDataSource(String.valueOf(((Uri.parse(model.getAudio())))));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        mmediaPlayer.setOnPreparedListener(LATESTMESSAGEVIEW.this);


                        mmediaPlayer.prepareAsync();

                           playing =false;

                    }


                });

            }


        };



        latestmessageviewrecycler.setAdapter(firebaseRecyclerAdapter);


    }



    private  void checkUserExist() {

        if (mAuth.getCurrentUser() != null) {
            final String user_id = mAuth.getCurrentUser().getUid();


            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(user_id)) {

                        Intent loginintent = new Intent(LATESTMESSAGEVIEW.this, LATESTMESSAGEVIEW.class);
                        loginintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginintent);

                    } else {
                        Intent accountsetup = new Intent(LATESTMESSAGEVIEW.this, ACCOUNTSETUP.class);
                        accountsetup.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(accountsetup);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

        }


    }


    public  static class  LatestMessageViewHolder extends  RecyclerView.ViewHolder {


        DatabaseReference mDatabaseLatestmessage;
        View mView;
        ImageButton StreamAudio;
        FirebaseAuth mAuth;
        boolean playing;
        public LatestMessageViewHolder(View itemView) {
            super(itemView);

            mView=  itemView;
            StreamAudio = (ImageButton)itemView.findViewById(R.id.streamaudio);

            mDatabaseLatestmessage = FirebaseDatabase.getInstance().getReference().child("CHURCHCREATED");
            mAuth =  FirebaseAuth.getInstance();
            mDatabaseLatestmessage.keepSynced(true);
         }






        public void setTitle(String title) {
            TextView latestmessagetitle = (TextView) mView.findViewById(R.id.latestmessagetitleview);
            latestmessagetitle.setText(title);

        }

        public  void setDesc(String desc){
            TextView latestmessagedescribe = (TextView)mView.findViewById(R.id.latestmessagedescribe);
            latestmessagedescribe.setText(desc);
        }

         public  void setimagetopause(MediaPlayer mediaPlayer) {
             if (mediaPlayer.isPlaying()) {
                 StreamAudio.setImageResource(R.drawable.pausebuttonagainsyy);

             }else {
                 StreamAudio.setImageResource(R.drawable.playbutton2);

             }
         }


        public  void setDate(String date){
            TextView latestmessagesetdate= (TextView)mView.findViewById(R.id.latestmessagesetdateview);
            latestmessagesetdate.setText(date);}

        public  void setPreacher(String preacher){
            TextView preachername = (TextView)mView.findViewById(R.id.preachernameview);
            preachername.setText(preacher);
        }
        public void setUsername(String username ){
            TextView usernamer = (TextView)mView.findViewById(R.id.latestmessageusername);
            usernamer.setText(username);

        }

        public void setAudio(String audio ){
            TextView audiio = (TextView)mView.findViewById(R.id.audiotext);
            audiio.setText(audio);

        }

        public void setImage(final Context ctx, final String image) {
            final ImageView imageView = (ImageView) mView.findViewById(R.id.latestmessageimageview);

            Picasso.with(ctx).load(image).resize(400,0).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {




                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(image).resize(100,0).into(imageView);
                }

            });


        }




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_eglisa__official, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.action_about) {
            startActivity(new Intent(LATESTMESSAGEVIEW.this, ABOUTVIEW.class));
        }
        if (item.getItemId() == R.id.Logout) {
            signout();
        }
        return  super.onOptionsItemSelected(item);
    }


    private void signout(){
        mAuth.signOut();
    }


}
