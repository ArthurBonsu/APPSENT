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

public class HISTORYVIEW extends AppCompatActivity {
    private RecyclerView historyviewrecycler;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseHistoryChurch;
    String churchkey;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Query  mQueryHistoryChurch;



    public HISTORYVIEW() {
        super();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historyviewrecycler);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent logintent = new Intent(com.gmail.peeman34.eglisaofficial.HISTORYVIEW.this, ActivityMaiin.class);
                    logintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(logintent);


                }
            }


        };

        mDatabase = FirebaseDatabase.getInstance().getReference().child("HISTCHURCHES");
        mDatabaseHistoryChurch = FirebaseDatabase.getInstance().getReference().child("HISTCHURCHES");

        mDatabase.keepSynced(true);
        mDatabaseHistoryChurch.keepSynced(true);

        churchkey  = getIntent().getExtras().getString("churchkey");

        mQueryHistoryChurch = mDatabaseHistoryChurch.orderByChild("churchid").equalTo(churchkey);


        historyviewrecycler = (RecyclerView)findViewById(R.id.historyviewrecycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    //    historyviewrecycler.setHasFixedSize(true);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        historyviewrecycler.setLayoutManager(layoutManager);

      //  checkUserExist();


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);


        FirebaseRecyclerAdapter<HistoryViewPull, HISTORYVIEW.HistoryViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<HistoryViewPull, HISTORYVIEW.HistoryViewHolder>(

                HistoryViewPull.class,
                R.layout.history,
                HistoryViewHolder.class,
                mQueryHistoryChurch
        ) {
            @Override
            protected void populateViewHolder(HistoryViewHolder viewHolder, HistoryViewPull model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setImage(getApplicationContext(),model.getImage());
                viewHolder.setDesc(model.getDesc());

            }
        };

        historyviewrecycler.setAdapter(firebaseRecyclerAdapter);
    }

    private  void checkUserExist() {

        if (mAuth.getCurrentUser() != null) {
            final String user_id = mAuth.getCurrentUser().getUid();


            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(user_id)) {

                        Intent historyviewintent = new Intent(com.gmail.peeman34.eglisaofficial.HISTORYVIEW.this, com.gmail.peeman34.eglisaofficial.HISTORYVIEW.class);
                        historyviewintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(historyviewintent);

                    } else {
                        Intent accountsetup = new Intent(com.gmail.peeman34.eglisaofficial.HISTORYVIEW.this, ActivityMaiin.class);
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
    public  static class  HistoryViewHolder extends  RecyclerView.ViewHolder {
        View mView;
        TextView historyviewtitle;
        TextView historyviewdesc;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setTitle(String title) {
            historyviewtitle = (TextView)mView.findViewById(R.id.historylisttitle);

            historyviewtitle.setText(title);

        }

        public  void setDesc(String desc){
            historyviewdesc= (TextView)mView.findViewById(R.id.historydescription);
            historyviewdesc.setText(desc);
        }



        public void setImage(final Context ctx, final String image) {
            final ImageView imageView = (ImageView) mView.findViewById(R.id.historylistimage);

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
            startActivity(new Intent(com.gmail.peeman34.eglisaofficial.HISTORYVIEW.this, ABOUTVIEW.class));
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
