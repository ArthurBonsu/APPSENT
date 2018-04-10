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

/**
 * Created by pee on 4/9/2017.
 */


public class GROUPCHOSENLIST extends AppCompatActivity {
    private RecyclerView mgroupchosenrecycler;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mGroupChosenList;
    String churchkey;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Query  mQueryCurrentGroup;



    public GROUPCHOSENLIST() {
        super();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupchosenlistrecycler);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent logintent = new Intent(com.gmail.peeman34.eglisaofficial.GROUPCHOSENLIST.this, ActivityMaiin.class);
                    logintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(logintent);


                }
            }


        };

        mDatabase = FirebaseDatabase.getInstance().getReference().child("GROUPCREATED");
        mGroupChosenList = FirebaseDatabase.getInstance().getReference().child("GROUPCHOSEN");

        mDatabase.keepSynced(true);
        mGroupChosenList.keepSynced(true);
        String currentUserId   = mAuth.getCurrentUser().getUid();


        churchkey  = getIntent().getExtras().getString("churchkey");

        mQueryCurrentGroup= mGroupChosenList.orderByChild(currentUserId).equalTo("RandomValue");


        mgroupchosenrecycler = (RecyclerView)findViewById(R.id.groupchosenliste);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
      //  mgroupchosenrecycler .setHasFixedSize(true);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mgroupchosenrecycler .setLayoutManager(layoutManager);

//        checkUserExist();


    }
    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);


        FirebaseRecyclerAdapter<GroupChosenLister, GROUPCHOSENLIST.GroupChosenHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<GroupChosenLister, GROUPCHOSENLIST.GroupChosenHolder>(


                GroupChosenLister.class,
                R.layout.groupchosenlist,
                GROUPCHOSENLIST.GroupChosenHolder.class,
                mQueryCurrentGroup
        )
        {
            @Override
            protected void populateViewHolder(GroupChosenHolder viewHolder, final GroupChosenLister model, int position) {

                final String groupkey = getRef(position).getKey();
                final String groupname = model.getTitle();
                viewHolder.setTitle(model.getTitle());
                viewHolder.setImage(getApplicationContext(),model.getImage());




                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent grouplistview = new Intent(GROUPCHOSENLIST.this, MYCHATPAGE.class);
                            grouplistview.putExtra("churchkey", churchkey);
                            grouplistview.putExtra("groupkey", groupkey);
                            grouplistview.putExtra("groupname",groupname);

                            startActivity(grouplistview);

                            Toast.makeText(GROUPCHOSENLIST.this, "groupkey", Toast.LENGTH_LONG).show();
                        }catch (Exception e){


                        }
                    }


                });
            }

        };

        mgroupchosenrecycler.setAdapter(firebaseRecyclerAdapter);
    }

    private  void checkUserExist() {

        if (mAuth.getCurrentUser() != null) {
            final String user_id = mAuth.getCurrentUser().getUid();


            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(user_id)) {

                        Intent mainviewintent = new Intent(GROUPCHOSENLIST.this, GROUPCHOSENLIST.class);
                        mainviewintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainviewintent);

                    } else {
                        Intent accountsetup = new Intent(GROUPCHOSENLIST.this, ActivityMaiin.class);
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
    public  static class  GroupChosenHolder extends  RecyclerView.ViewHolder {
        View mView;

        public GroupChosenHolder(View itemView) {
            super(itemView);
            mView=itemView;

        }

        public void setTitle(String title) {
            TextView groupchosennames = (TextView) mView.findViewById(R.id.groupchosenname);
            groupchosennames.setText(title);

        }



        public void setImage(final Context ctx, final String image) {
            final ImageView groupchosenimages = (ImageView) mView.findViewById(R.id.groupchosenimage);

            Picasso.with(ctx).load(image).resize(500,0).networkPolicy(NetworkPolicy.OFFLINE).into(groupchosenimages, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(image).resize(100,0).into(groupchosenimages);
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
            startActivity(new Intent(GROUPCHOSENLIST.this, ABOUTVIEW.class));
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

