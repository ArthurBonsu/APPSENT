package com.gmail.peeman34.eglisaofficial;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import  com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pee on 1/5/2016.
 */
public class GENERALLE extends AppCompatActivity {

    private  RecyclerView generallist;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String churchkey;
    MainViewList model;

    public GENERALLE() {
        super();
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generallerecycler);



        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent logintent = new Intent(GENERALLE.this, ActivityMaiin.class);
                    logintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(logintent);


                }

            }


        };

        mDatabase = FirebaseDatabase.getInstance().getReference().child("GENERALLE");
        mDatabase.keepSynced(true);
        generallist= (RecyclerView)findViewById(R.id.generallerecyclers);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
      //  generallist.setHasFixedSize(true);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        generallist.setLayoutManager(layoutManager);

//        checkUserExist();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);


        FirebaseRecyclerAdapter<GeneralLister, GeneralViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<GeneralLister, GeneralViewHolder>(

                GeneralLister.class,
                R.layout.generalle,
                GeneralViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(GeneralViewHolder viewHolder, GeneralLister model, int position) {
                final String title = model.getTitle();
                viewHolder.setTitle(model.getTitle());
                viewHolder.setImage(getApplicationContext(),model.getImage());


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {


                        try {
                            Intent generalling = new Intent(GENERALLE.this, Class.forName("com.gmail.peeman34.eglisaofficial."+ title));
                            churchkey = getIntent().getExtras().getString("churchkey");
                            generalling.putExtra("churchkey", churchkey);
                            startActivity(generalling);

                            Toast.makeText(getApplicationContext(), "create event", Toast.LENGTH_LONG).show();
                        }catch (Exception e){


                        }



                        //       Toast.makeText(MAINVIEW.this, churchkey, Toast.LENGTH_LONG );

                    }
                });



            }


        };

        generallist.setAdapter(firebaseRecyclerAdapter);
    }

    private  void checkUserExist() {

        if (mAuth.getCurrentUser() != null) {
            final String user_id = mAuth.getCurrentUser().getUid();


            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(user_id)) {

                        Intent generallogintent = new Intent(GENERALLE.this, GENERALLE.class);
                        generallogintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(generallogintent);

                    } else {
                        Intent accountsetup = new Intent(GENERALLE.this, ActivityMaiin.class);
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
    public  static class  GeneralViewHolder extends  RecyclerView.ViewHolder {
        View mView;

        public GeneralViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setTitle(String title) {
            TextView generalactivitytitle = (TextView) mView.findViewById(R.id.generactivityname);
            generalactivitytitle.setText(title);

        }

        public void setImage(final Context ctx, final String image) {
            final ImageView imageView = (ImageView) mView.findViewById(R.id.generalpicture);

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
            startActivity(new Intent(GENERALLE.this, ABOUTVIEW.class));
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





