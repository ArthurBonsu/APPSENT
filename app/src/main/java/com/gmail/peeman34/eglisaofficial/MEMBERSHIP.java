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

/**
 * Created by pee on 7/14/2017.
 */

public class MEMBERSHIP extends AppCompatActivity {

    private RecyclerView membersrecycler;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseMembers;
    String churchkey;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Query mQueryMembers;


    public MEMBERSHIP() {
        super();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.members);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent logintent = new Intent(com.gmail.peeman34.eglisaofficial.MEMBERSHIP.this, ActivityMaiin.class);
                    logintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(logintent);


                }
            }


        };
        mDatabase = FirebaseDatabase.getInstance().getReference().child("CHURCHCHOSENLIST");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        mDatabaseMembers = FirebaseDatabase.getInstance().getReference().child("MEMBERS");

        mDatabase.keepSynced(true);
        mDatabaseMembers.keepSynced(true);

        churchkey = getIntent().getExtras().getString("churchkey");

        mQueryMembers = mDatabaseMembers.orderByChild("churchid").equalTo(churchkey);


        membersrecycler = (RecyclerView) findViewById(R.id.membersrecycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //   churchpicicrecycler .setHasFixedSize(true);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        membersrecycler .setLayoutManager(layoutManager);

        //    checkUserExist();


    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);


        FirebaseRecyclerAdapter<MembersList, MEMBERSHIP.MembersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MembersList, MEMBERSHIP.MembersViewHolder>(

                MembersList.class,
                R.layout.members,
                MEMBERSHIP.MembersViewHolder.class,
                mQueryMembers
        ) {
            @Override
            protected void populateViewHolder(final MEMBERSHIP.MembersViewHolder viewHolder, MembersList model, int position) {

                // / AM NOW SORTING IT OUOT BY MEMBERSHIP

                final String uid = model.getUid();


                mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String usernames = (String) dataSnapshot.child(uid).child("name").getValue();
                        String image = (String) dataSnapshot.child(uid).child("image").getValue();
                        viewHolder.setUsername(usernames);
                        viewHolder.setImage(getApplicationContext(), image);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        };

        membersrecycler .setAdapter(firebaseRecyclerAdapter);
    }

    private void checkUserExist() {

        if (mAuth.getCurrentUser() != null) {
            final String user_id = mAuth.getCurrentUser().getUid();


            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(user_id)) {

                        Intent membershipintent = new Intent(com.gmail.peeman34.eglisaofficial.MEMBERSHIP.this, com.gmail.peeman34.eglisaofficial.MEMBERSHIP.class);
                        membershipintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(membershipintent);

                    } else {
                        Intent accountsetup = new Intent(com.gmail.peeman34.eglisaofficial.MEMBERSHIP.this, ActivityMaiin.class);
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
        public static class MembersViewHolder extends RecyclerView.ViewHolder {
            View mView;
            TextView Membername;

            public MembersViewHolder(View itemView) {
                super(itemView);
                mView = itemView;

            }


            public void setUsername(String username) {
                Membername = (TextView) mView.findViewById(R.id.memberusername);
                Membername.setText(username);
            }


            public void setImage(final Context ctx, final String image) {
                final ImageView imageView = (ImageView) mView.findViewById(R.id.memberimageview);

                Picasso.with(ctx).load(image).resize(400, 0).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {


                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(ctx).load(image).resize(100, 0).into(imageView);
                    }

                });


            }


        }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            getMenuInflater().inflate(R.menu.menu_eglisa__official, menu);

            return super.onCreateOptionsMenu(menu);

        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public boolean onOptionsItemSelected (MenuItem item){

            if (item.getItemId() == R.id.action_about) {
                startActivity(new Intent(com.gmail.peeman34.eglisaofficial.MEMBERSHIP.this, ABOUTVIEW.class));
            }
            if (item.getItemId() == R.id.Logout) {
                signout();
            }
            return super.onOptionsItemSelected(item);
        }



    private void signout(){
        mAuth.signOut();
    }



}

