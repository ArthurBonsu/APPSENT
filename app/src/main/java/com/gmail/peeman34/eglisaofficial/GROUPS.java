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
import com.google.firebase.database.Query;
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

public class GROUPS extends AppCompatActivity {
    private RecyclerView groupicrecycler;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseGroup;
    private  DatabaseReference mDatabaseGroupChosen;
    String churchkey;
    String groupkey;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Query  mQueryGroup;
     boolean mProcessChoose;
     ImageButton groupchosen;

    public GROUPS() {
        super();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grouplistrecycler);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent logintent = new Intent(com.gmail.peeman34.eglisaofficial.GROUPS.this, ActivityMaiin.class);
                    logintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(logintent);


                }
            }


        };

        mDatabase = FirebaseDatabase.getInstance().getReference().child("GROUPCREATED");
        mDatabaseGroup = FirebaseDatabase.getInstance().getReference().child("GROUPCREATED");
        mDatabaseGroupChosen =FirebaseDatabase.getInstance().getReference().child("GROUPCHOSEN");
        mDatabase.keepSynced(true);
        mDatabaseGroup.keepSynced(true);

        churchkey  = getIntent().getExtras().getString("churchkey");
         groupkey = getIntent().getExtras().getString("groupkey");
        mQueryGroup= mDatabaseGroup.orderByChild("churchid").equalTo(churchkey);

           groupchosen = (ImageButton)findViewById(R.id.groupchosen);
        groupicrecycler = (RecyclerView)findViewById(R.id.grouplistrecycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
      //  groupicrecycler.setHasFixedSize(true);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        groupicrecycler.setLayoutManager(layoutManager);



//        checkUserExist();

        groupchosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent groupchosen = (new Intent(GROUPS.this, GROUPCHOSENLIST.class));
                     groupchosen.putExtra("groupkey", groupkey);
                    groupchosen.putExtra("churchkey", churchkey);

                     startActivity(groupchosen);

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


        FirebaseRecyclerAdapter<GroupLister, GROUPS.GroupListViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<GroupLister, GROUPS.GroupListViewHolder>(

                GroupLister.class,
                R.layout.groups,
                GROUPS.GroupListViewHolder.class,
                mQueryGroup
        ) {
            @Override
            protected void populateViewHolder(GROUPS.GroupListViewHolder viewHolder, final GroupLister model, int position) {
                final String postkey = getRef(position).getKey();

                viewHolder.setTitle(model.getTitle());

                viewHolder.setImage(getApplicationContext(), model.getImage());

                viewHolder.setDate(model.getDate());



                viewHolder.setDesc(model.getDesc());

                viewHolder.setUsername(model.getUsername());


                viewHolder.setGroup(postkey);




                viewHolder.choosethegroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mProcessChoose =true;

                        mDatabaseGroupChosen.child(postkey).child("title").setValue(model.getTitle());
                        mDatabaseGroupChosen.child(postkey).child("image").setValue(model.getImage());



                        mDatabaseGroupChosen.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (mProcessChoose){

                                    if (dataSnapshot.child(postkey).hasChild(mAuth.getCurrentUser().getUid())){

                                        mDatabaseGroupChosen.child(postkey).child(mAuth.getCurrentUser().getUid()).removeValue();
                                        mProcessChoose = false;

                                    }else{
                                        mDatabaseGroupChosen.child(postkey).child(mAuth.getCurrentUser().getUid()).setValue("RandomValue");
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


        groupicrecycler.setAdapter(firebaseRecyclerAdapter);
    }

    private  void checkUserExist() {

        if (mAuth.getCurrentUser() != null) {
            final String user_id = mAuth.getCurrentUser().getUid();


            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(user_id)) {

                        Intent groupviewintent = new Intent(GROUPS.this, LOGIN.class);
                        groupviewintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(groupviewintent);

                    } else {
                        Intent accountsetup = new Intent(GROUPS.this, ACCOUNTSETUP.class);
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
    public  static class  GroupListViewHolder extends  RecyclerView.ViewHolder {
        View mView;
        ImageButton choosethegroup;

        private DatabaseReference mChooseChurchDatabase;


        private FirebaseAuth mAuth;



        public GroupListViewHolder(View itemView) {
            super(itemView);
            mView= itemView;
            choosethegroup = (ImageButton) mView.findViewById(R.id.choosegroup);


            mChooseChurchDatabase = FirebaseDatabase.getInstance().getReference().child("GROUPCHOSEN");

            mAuth = FirebaseAuth.getInstance();

            mChooseChurchDatabase .keepSynced(true);

        }

        public  void setGroup(final String postkey){
            mChooseChurchDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(postkey).hasChild(mAuth.getCurrentUser().getUid())) {

                        choosethegroup .setImageResource(R.drawable.churchnotchooseputton);
                    }
                    else {
                        choosethegroup .setImageResource(R.drawable.churchchoosebutton);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }





        public void setTitle( String title) {
            TextView groupnames = (TextView) mView.findViewById(R.id.mgroupname);
            groupnames.setText(title);

        }

        public  void setDesc(String desc){
            TextView groupdesc = (TextView)mView.findViewById(R.id.mgroupdesc);
            groupdesc.setText( desc);
        }


        public  void setDate(String date){
            TextView groupdate = (TextView)mView.findViewById(R.id.mgroupdate);
            groupdate.setText( date);}

        public void setUsername(String username ){
            TextView usernamer = (TextView)mView.findViewById(R.id.churchcreater);
            usernamer.setText(username);

        }


        public void setImage(final Context ctx, final String image) {
            final ImageView imageView = (ImageView) mView.findViewById(R.id.grouplistimage);

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
            startActivity(new Intent(GROUPS.this, ABOUTVIEW.class));
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


