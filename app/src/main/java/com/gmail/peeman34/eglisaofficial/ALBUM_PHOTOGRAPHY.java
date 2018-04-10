package com.gmail.peeman34.eglisaofficial;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class ALBUM_PHOTOGRAPHY extends AppCompatActivity {
    private RecyclerView albumpicrecycler;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseGetPic;
    String churchkey;
    String currentid;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Query  mQueryGetPic;



    public ALBUM_PHOTOGRAPHY() {
        super();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.albumphotographyrecycler);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent logintent = new Intent(com.gmail.peeman34.eglisaofficial.ALBUM_PHOTOGRAPHY.this, ActivityMaiin.class);
                    logintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(logintent);


                }
            }


        };

        mDatabase = FirebaseDatabase.getInstance().getReference().child("PERSONALPICS");
        mDatabaseGetPic = FirebaseDatabase.getInstance().getReference().child("PERSONALPICS");

        mDatabase.keepSynced(true);
        mDatabaseGetPic.keepSynced(true);

        churchkey  = getIntent().getExtras().getString("churchkey");
         currentid = mAuth.getCurrentUser().getUid();
         mQueryGetPic= mDatabaseGetPic.orderByChild("uid").equalTo(currentid);


        albumpicrecycler = (RecyclerView)findViewById(R.id.albumphotographyrecycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
   //     albumpicrecycler.setHasFixedSize(true);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        albumpicrecycler.setLayoutManager(layoutManager);

//        checkUserExist();


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);


        FirebaseRecyclerAdapter<AlbumPhotographyList, ALBUM_PHOTOGRAPHY.AlbumViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<AlbumPhotographyList, ALBUM_PHOTOGRAPHY.AlbumViewHolder>(

                AlbumPhotographyList.class,
                R.layout.albumphotography,
                AlbumViewHolder.class,
                mQueryGetPic
        ) {
            @Override
            protected void populateViewHolder(AlbumViewHolder viewHolder, AlbumPhotographyList model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setImage(getApplicationContext(),model.getImage());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setUsername(model.getUsername());
            }
        };

        albumpicrecycler.setAdapter(firebaseRecyclerAdapter);
    }

    private  void checkUserExist() {

        if (mAuth.getCurrentUser() != null) {
            final String user_id = mAuth.getCurrentUser().getUid();


            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(user_id)) {

                        Intent aboutviewintent = new Intent(com.gmail.peeman34.eglisaofficial.ALBUM_PHOTOGRAPHY.this, com.gmail.peeman34.eglisaofficial.ABOUTVIEW.class);
                       aboutviewintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(aboutviewintent);

                    } else {
                        Intent accountsetup = new Intent(com.gmail.peeman34.eglisaofficial.ALBUM_PHOTOGRAPHY.this, ActivityMaiin.class);
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
    public  static class  AlbumViewHolder extends  RecyclerView.ViewHolder {
        View mView;


        TextView albumuser;
        public AlbumViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setTitle(String title) {
            TextView albumviewtitle = (TextView)mView.findViewById(R.id.personalpicturetitle);

            albumviewtitle.setText(title);

        }

        public  void setDesc(String desc){
            TextView albumviewdescribe = (TextView)mView.findViewById(R.id.personalpicturedescription);
            albumviewdescribe.setText(desc);
        }

        public  void setUsername(String username){
            albumuser = (TextView)mView.findViewById(R.id.personalpicusername);
            albumuser.setText(username);
        }


        public void setImage(final Context ctx, final String image) {
            final ImageView imageView = (ImageView) mView.findViewById(R.id.personalpicture);

            Picasso.with(ctx).load(image).resize(600,0).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {




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
            startActivity(new Intent(com.gmail.peeman34.eglisaofficial.ALBUM_PHOTOGRAPHY.this, ABOUTVIEW.class));
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
