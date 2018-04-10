package com.gmail.peeman34.eglisaofficial;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.AsyncTask;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import static com.gmail.peeman34.eglisaofficial.ImageNicer.decodeSampledBitmapFromResource;

/**
 * Created by pee on 4/9/2017.
 */

public class CHURCHCHOSENLIST extends AppCompatActivity  {


    RecyclerView churchchosenlistrecycler;
    private DatabaseReference mDatabase;
    private DatabaseReference mChooseChurchDatabase;

    String churchid;
    Intent mainview;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private  DatabaseReference  mDataBaseUsers;
    Query mQueryChosenChurchList;

    public CHURCHCHOSENLIST() {
        super();


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.churchchosenlistrecycler);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent logintent = new Intent(CHURCHCHOSENLIST.this, LOGIN.class);
                    logintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(logintent);


                }
            }


        };


         mDatabase = FirebaseDatabase.getInstance().getReference().child("CHURCHCREATED");
        mDataBaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        mChooseChurchDatabase = FirebaseDatabase.getInstance().getReference().child("CHURCHCHOSEN");

        mDataBaseUsers.keepSynced(true);

        mChooseChurchDatabase.keepSynced(true);

        String currentuserid= mAuth.getCurrentUser().getUid();

        mQueryChosenChurchList =  mChooseChurchDatabase.orderByChild(mAuth.getCurrentUser().getUid()).equalTo("RandomValue");






        churchchosenlistrecycler = (RecyclerView)findViewById(R.id.chosenlistscheck);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);


//        churchchosenlistrecycler.setHasFixedSize(true);

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        churchchosenlistrecycler.setLayoutManager(layoutManager);

      //  checkUserExist();


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);


        FirebaseRecyclerAdapter<ChurchChosenLister, CHURCHCHOSENLIST.ChurchChosenViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ChurchChosenLister, CHURCHCHOSENLIST.ChurchChosenViewHolder>(

                ChurchChosenLister.class,
                R.layout.churchchosenlist,
                ChurchChosenViewHolder.class,
                mQueryChosenChurchList
        ) {
            @Override
            protected void populateViewHolder(ChurchChosenViewHolder viewHolder, ChurchChosenLister model, int position) {
                final String churchkey = getRef(position).getKey();




                viewHolder.setTitle(model.getTitle());
                viewHolder.setImage(getApplicationContext(), model.getImage());

                viewHolder.setLocation(model.getLocation());

                  viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          try{
                              mainview = new Intent(CHURCHCHOSENLIST.this, MAINVIEW.class);
                              mainview.putExtra("churchkey",churchkey);
                              mainview.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                              startActivity(mainview);



                          }catch (Exception e){}
                      }
                  });
            }


            };



        churchchosenlistrecycler .setAdapter(firebaseRecyclerAdapter);
    }

    private  void checkUserExist() {

        if (mAuth.getCurrentUser() != null) {
            final String user_id = mAuth.getCurrentUser().getUid();


            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(user_id)) {

                        Intent eventviewintent = new Intent(CHURCHCHOSENLIST.this, CHURCHCHOSENLIST.class);
                        eventviewintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(eventviewintent);

                    } else {
                        Intent accountsetup = new Intent(CHURCHCHOSENLIST.this, ACCOUNTSETUP.class);
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
    public  static class  ChurchChosenViewHolder extends  RecyclerView.ViewHolder {
        View mView;



        public ChurchChosenViewHolder(View itemView) {
            super(itemView);
            mView= itemView;




        }







        public void setTitle( String title) {
            TextView churchchosennamer = (TextView)mView.findViewById(R.id.churchchosenname);
               churchchosennamer.setText(title);

        }






        public void setLocation(String location){
           TextView  locations = (TextView)mView.findViewById(R.id.churchchosenlocation);
            locations.setText( location);

        }

        public void setImage(final Context ctx, final String image) {
            final ImageView imageView = (ImageView)mView.findViewById(R.id.churchchosenimage);

            Picasso.with(ctx).load( image).resize(600,0).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {




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
            startActivity(new Intent(CHURCHCHOSENLIST.this, ABOUTVIEW.class));
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


