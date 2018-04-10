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
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



/**
 * Created by pee on 1/5/2017.
 */

public class CHURCHLIST extends AppCompatActivity  {


    private  RecyclerView churchlistrecycler;
    private DatabaseReference mDatabase;
    private DatabaseReference mChooseChurchDatabase;
    ImageButton sendtochurchchosen;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
   private  DatabaseReference  mDataBaseUsers;
       private  DatabaseReference mMembersDatabase;
    private  boolean mProcessChoose = false;
    private boolean detailsexist = false;

    public CHURCHLIST() {
        super();


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          setContentView(R.layout.churchlistrecycler);


        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent logintent = new Intent(CHURCHLIST.this, LOGIN.class);
                    logintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(logintent);

                }
            }


        };


        mDatabase = FirebaseDatabase.getInstance().getReference().child("CHURCHCREATED");
        mDataBaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
          mChooseChurchDatabase = FirebaseDatabase.getInstance().getReference().child("CHURCHCHOSEN");
        mMembersDatabase = FirebaseDatabase.getInstance().getReference().child("MEMBERS");

        mDatabase.keepSynced(true);

        mMembersDatabase.keepSynced(true);

        mDataBaseUsers.keepSynced(true);

        mChooseChurchDatabase.keepSynced(true);

        churchlistrecycler = (RecyclerView)findViewById(R.id.churchlistrecycler);

        sendtochurchchosen = (ImageButton)findViewById(R.id.sendtochurchchosen);

        LinearLayoutManager layoutManager = new LinearLayoutManager(CHURCHLIST.this);

//        churchlistrecycler.setHasFixedSize(true);

        churchlistrecycler.setLayoutManager(layoutManager);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

//        checkUserExist();

        sendtochurchchosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent churchchosen = (new Intent(CHURCHLIST.this, CHURCHCHOSENLIST.class));
                    startActivity(churchchosen);

                }
                catch (Exception e){
                  Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);


        FirebaseRecyclerAdapter<ChurchLister, CHURCHLIST.ChurchListViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ChurchLister, CHURCHLIST.ChurchListViewHolder>(

                ChurchLister.class,
                R.layout.churchlist,
                CHURCHLIST.ChurchListViewHolder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder(CHURCHLIST.ChurchListViewHolder viewHolder, final ChurchLister model, int position) {
                final String postkey = getRef(position).getKey();

                viewHolder.setTitle(model.getTitle());

                viewHolder.setImage(getApplicationContext(), model.getImage());

                viewHolder.setDate(model.getDate());

                viewHolder.setLocation(model.getLocation());

                viewHolder.setDesc(model.getDesc());

                viewHolder.setUsername(model.getUsername());


                viewHolder.setChoosechurch(postkey);


                   // Very expensive code to be kept

                  viewHolder.choosechurch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                         mProcessChoose =true;

                    mChooseChurchDatabase.child(postkey).child("title").setValue(model.getTitle());
                    mChooseChurchDatabase.child(postkey).child("image").setValue(model.getImage());
                    mChooseChurchDatabase.child(postkey).child("location").setValue(model.getLocation());


             mChooseChurchDatabase.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(DataSnapshot dataSnapshot) {
                     if (mProcessChoose){

                         if (dataSnapshot.child(postkey).hasChild(mAuth.getCurrentUser().getUid())){

                             mChooseChurchDatabase.child(postkey).child(mAuth.getCurrentUser().getUid()).removeValue();
                            // this is to remove the member
                             mMembersDatabase.child(postkey).child("uid").setValue("RandomValue");
                          //   mChooseChurchDatabase.child(postkey).child("name").removeValue();
                               mProcessChoose = false;

                         }else{
                             mChooseChurchDatabase.child(postkey).child(mAuth.getCurrentUser().getUid()).setValue("RandomValue");
                             // this is to pull the members
                             mMembersDatabase.child(postkey).child("uid").setValue(mAuth.getCurrentUser().getUid());
                      //       mChooseChurchDatabase.child(postkey).child("name").setValue(mAuth.getCurrentUser().getDisplayName());

                                mProcessChoose = false;
                         }
                     }
                 }

                 @Override
                 public void onCancelled(DatabaseError databaseError) {

                 }
             });
                }


            });


            }};


        churchlistrecycler.setAdapter(firebaseRecyclerAdapter);
    }

    private  void checkUserExist() {

        if (mAuth.getCurrentUser() != null) {
            final String user_id = mAuth.getCurrentUser().getUid();


            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(user_id)) {

                        Intent eventviewintent = new Intent(CHURCHLIST.this, CHURCHLIST.class);
                        eventviewintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(eventviewintent);

                    } else {
                        Intent accountsetup = new Intent(CHURCHLIST.this, ACCOUNTSETUP.class);
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
    public  static class  ChurchListViewHolder extends  RecyclerView.ViewHolder {
        View mView;
        ImageButton choosechurch;

        private DatabaseReference mChooseChurchDatabase;


        private FirebaseAuth mAuth;



        public ChurchListViewHolder(View itemView) {
            super(itemView);
            mView= itemView;
            choosechurch = (ImageButton) mView.findViewById(R.id.choosechurch);


            mChooseChurchDatabase = FirebaseDatabase.getInstance().getReference().child("CHURCHCHOSEN");

            mAuth = FirebaseAuth.getInstance();

            mChooseChurchDatabase.keepSynced(true);

        }

        public  void setChoosechurch(final String postkey){
   mChooseChurchDatabase.addValueEventListener(new ValueEventListener() {
       @Override
       public void onDataChange(DataSnapshot dataSnapshot) {
           if (dataSnapshot.child(postkey).hasChild(mAuth.getCurrentUser().getUid())) {

                choosechurch.setImageResource(R.drawable.churchnotchooseputton);
           }
           else {
               choosechurch.setImageResource(R.drawable.churchchoosebutton);
           }
       }

       @Override
       public void onCancelled(DatabaseError databaseError) {

       }
   });
        }





        public void setTitle( String title) {
            TextView churchnames = (TextView) mView.findViewById(R.id.mChurchlistname);
            churchnames.setText(title);

        }

        public  void setDesc(String desc){
            TextView churchdesc = (TextView)mView.findViewById(R.id.mChurchlistdescriber);
            churchdesc.setText( desc);
        }


        public  void setDate(String date){
            TextView churchdate = (TextView)mView.findViewById(R.id.mChurchlistdate);
            churchdate.setText( date);}

        public void setUsername(String username ){
            TextView usernamer = (TextView)mView.findViewById(R.id.churchlistusername);
            usernamer.setText(username);

        }

        public void setLocation(String location){
            TextView locations = (TextView)mView.findViewById(R.id.churchlocation);
            locations.setText( location);

        }

        public void setImage(final Context ctx, final String image) {
            final ImageView imageView = (ImageView) mView.findViewById(R.id.churchlistimage);

            Picasso.with(ctx).load( image).resize(500,0).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {




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
            startActivity(new Intent(CHURCHLIST.this, ABOUTVIEW.class));
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


