package com.gmail.peeman34.eglisaofficial;

import android.content.Context;
import android.content.Intent;
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

/**
 * Created by pee on 8/5/2016.
 */

public class EVENT_GRID_FILE extends AppCompatActivity {
    private  RecyclerView eventviewrecycler;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseEventChurch;
    String churchkey;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Query mQueryEventChurch;



    public EVENT_GRID_FILE() {
        super();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.eventgridfilerecycler);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent logintent = new Intent(EVENT_GRID_FILE.this, ActivityMaiin.class);
                    logintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(logintent);


                }
            }


        };

        mDatabase = FirebaseDatabase.getInstance().getReference().child("EVENTS");
        mDatabaseEventChurch = FirebaseDatabase.getInstance().getReference().child("EVENTS");

        mDatabase.keepSynced(true);
        mDatabaseEventChurch.keepSynced(true);

        churchkey  = getIntent().getExtras().getString("churchkey");

        mQueryEventChurch = mDatabaseEventChurch.orderByChild("churchid").equalTo(churchkey);


        eventviewrecycler = (RecyclerView)findViewById(R.id.eventviewrecycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        eventviewrecycler.setHasFixedSize(true);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        eventviewrecycler.setLayoutManager(layoutManager);

//        checkUserExist();


     }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);


        FirebaseRecyclerAdapter<Event_grid_filee, EVENT_GRID_FILE.Eventviewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Event_grid_filee, Eventviewholder>(

                Event_grid_filee.class,
                R.layout.eventgridfile,
                Eventviewholder.class,
                mQueryEventChurch
        ) {
            @Override
            protected void populateViewHolder(EVENT_GRID_FILE.Eventviewholder viewHolder, Event_grid_filee model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setImage(getApplicationContext(),model.getImage());

                 viewHolder.setDate(model.getDate());

                 viewHolder.setDesc(model.getDesc());

                 viewHolder.setUsername(model.getUsername());

            }
        };

        eventviewrecycler.setAdapter(firebaseRecyclerAdapter);
    }

    private  void checkUserExist() {

        if (mAuth.getCurrentUser() != null) {
            final String user_id = mAuth.getCurrentUser().getUid();


            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(user_id)) {

                        Intent eventviewintent = new Intent(EVENT_GRID_FILE.this, EVENT_GRID_FILE.class);
                        eventviewintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(eventviewintent);

                    } else {
                        Intent accountsetup = new Intent(EVENT_GRID_FILE.this, ActivityMaiin.class);
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
    public  static class  Eventviewholder extends  RecyclerView.ViewHolder {
        View mView;
        TextView eventgridtitle;
        TextView eventdesc;
        TextView eventdate;
        TextView usernamer;

        public Eventviewholder(View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setTitle(String title) {
             eventgridtitle = (TextView)mView.findViewById(R.id.mChurchlistname);

            eventgridtitle.setText(title);

        }

        public  void setDesc(String desc){
             eventdesc = (TextView)mView.findViewById(R.id.eventgriddesc);
            eventdesc.setText(desc);
        }

        public  void setDate(String date){
             eventdate = (TextView)mView.findViewById(R.id.mChurchlistdate);
            eventdate.setText(date);
        }

        public void setUsername(String username ){
          usernamer = (TextView)mView.findViewById(R.id.eventgridusername);
                 usernamer.setText(username);

        }


        public void setImage(final Context ctx, final String image) {
            final ImageView imageView = (ImageView) mView.findViewById(R.id.churchlistimage);

            Picasso.with(ctx).load(image).resize(500,0).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {




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
            startActivity(new Intent(EVENT_GRID_FILE.this, ABOUTVIEW.class));
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
