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

public class QUOTES extends AppCompatActivity {
    private RecyclerView quoterecycler;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseQuoteChurch;
    String churchkey;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Query  mQueryQuoteChurch;



    public QUOTES() {
        super();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quoteviewrecycler);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent logintent = new Intent(com.gmail.peeman34.eglisaofficial.QUOTES.this, ActivityMaiin.class);
                    logintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(logintent);


                }
            }


        };

        mDatabase = FirebaseDatabase.getInstance().getReference().child("QUOTES");
        mDatabaseQuoteChurch = FirebaseDatabase.getInstance().getReference().child("QUOTES");

        mDatabase.keepSynced(true);


        churchkey  = getIntent().getExtras().getString("churchkey");

        mQueryQuoteChurch =mDatabaseQuoteChurch.orderByChild("churchid").equalTo(churchkey);


        quoterecycler = (RecyclerView)findViewById(R.id.quoteviewrecycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
       //  quoterecycler.setHasFixedSize(true);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        quoterecycler.setLayoutManager(layoutManager);

//        checkUserExist();


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);


        FirebaseRecyclerAdapter<Quote, QUOTES.QuoteViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Quote, QUOTES.QuoteViewHolder>(

                Quote.class,
                R.layout.quoteview,
                QuoteViewHolder.class,
                mQueryQuoteChurch
        ) {
            @Override
            protected void populateViewHolder(QuoteViewHolder viewHolder, Quote model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setImage(getApplicationContext(),model.getImage());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setDate(model.getDate());
            }
        };

        quoterecycler.setAdapter(firebaseRecyclerAdapter);
    }

    private  void checkUserExist() {

        if (mAuth.getCurrentUser() != null) {
            final String user_id = mAuth.getCurrentUser().getUid();


            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(user_id)) {

                        Intent quoteintent = new Intent(com.gmail.peeman34.eglisaofficial.QUOTES.this, com.gmail.peeman34.eglisaofficial.QUOTES.class);
                        quoteintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(quoteintent);

                    } else {
                        Intent accountsetup = new Intent(com.gmail.peeman34.eglisaofficial.QUOTES.this, ActivityMaiin.class);
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
    public  static class  QuoteViewHolder extends  RecyclerView.ViewHolder {
        View mView;
        TextView quotetitle;
        TextView quotedesc;
        TextView quotedate;

        public QuoteViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setTitle(String title) {
            quotetitle = (TextView)mView.findViewById(R.id.viewquotetitle);

            quotetitle.setText(title);

        }

        public void setDate(String date) {
            quotedate = (TextView)mView.findViewById(R.id.viewquotedate);

            quotedate.setText(date);

        }

        public  void setDesc(String desc){
            quotedesc = (TextView)mView.findViewById(R.id.quotedescriber);
            quotedesc.setText(desc);
        }



        public void setImage(final Context ctx, final String image) {
            final ImageView imageView = (ImageView) mView.findViewById(R.id.viewquoteimage);

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
            startActivity(new Intent(com.gmail.peeman34.eglisaofficial.QUOTES.this, ABOUTVIEW.class));
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
