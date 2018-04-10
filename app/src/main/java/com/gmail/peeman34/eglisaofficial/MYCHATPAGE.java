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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import  com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;

/**
 * Created by pee on 1/5/2016.
 */
public class MYCHATPAGE extends AppCompatActivity {

    private  RecyclerView mychatpagerecycler;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseChatPage;
    private  DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String churchkey;
    String churchcreater;
    String groupname;
    ChatMessage model;
    String groupkey;
    TextView conversation;
    private Query mChatPageQuery;
    ImageButton fab;
    EditText input;
   String username;


    public MYCHATPAGE() {
        super();
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mychatpage);

               churchkey= getIntent().getExtras().getString("churchkey");
          churchcreater= getIntent().getExtras().getString("churchcreater");
        groupkey = getIntent().getExtras().getString("groupkey");
        groupname = getIntent().getExtras().getString("groupname");

        conversation = (TextView)findViewById(R.id.thisisgroupname);

        conversation.setText(groupname);

        fab = (ImageButton) findViewById(R.id.fab);
         input = (EditText)findViewById(R.id.input);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener =new FirebaseAuth.AuthStateListener() {


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent logintent = new Intent(MYCHATPAGE.this, ActivityMaiin.class);
                    logintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(logintent);



                }



            }


        };



        mDatabase = FirebaseDatabase.getInstance().getReference().child("GROUPCREATED");
        mDatabaseChatPage = FirebaseDatabase.getInstance().getReference().child("MYCHATPAGE");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid());
        mDatabaseUsers.keepSynced(true);
        mDatabase.keepSynced(true);
        mDatabaseChatPage.keepSynced(true);
        mychatpagerecycler = (RecyclerView)findViewById(R.id.mychatpagerecycler);
        mChatPageQuery = mDatabaseChatPage.orderByChild("groupkey").equalTo(groupkey);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        mychatpagerecycler.setHasFixedSize(true);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mychatpagerecycler.setLayoutManager(layoutManager);
  //      checkUserExist();


/*        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 username = dataSnapshot.child("name").getValue().toString();
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         }); */


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                            username = mAuth.getCurrentUser().getDisplayName();
                        mDatabaseChatPage.push()
                        .setValue(new ChatMessage(input.getText().toString(),username, churchkey, groupkey)
                                 );


                // Clear the input
                input.setText("");
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);


        FirebaseRecyclerAdapter<ChatMessage, ChatPageViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ChatMessage, ChatPageViewHolder>(

                ChatMessage.class,
                R.layout.message,
                ChatPageViewHolder.class,
                mChatPageQuery
        )
        {
            @Override
            protected void populateViewHolder(ChatPageViewHolder viewHolder, ChatMessage model, int position) {


                final String messageText = model.getMessageText();

                viewHolder.setMessageText(model.getMessageText());
                viewHolder.setMessageUser(model.getMessageUser());
                viewHolder.setMessageTime(DateFormat.getTimeInstance(), model.getMessageTime()   );



            }
        };

        mychatpagerecycler.setAdapter(firebaseRecyclerAdapter);
    }


    private  void checkUserExist() {

        if (mAuth.getCurrentUser() != null) {
            final String user_id = mAuth.getCurrentUser().getUid();


            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(user_id)) {

                        Intent mainviewintent = new Intent(MYCHATPAGE.this, MYCHATPAGE.class);
                        mainviewintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainviewintent);

                    } else {
                        Intent accountsetup = new Intent(MYCHATPAGE.this, ACCOUNTSETUP.class);
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
    public  static class  ChatPageViewHolder extends  RecyclerView.ViewHolder {
        View mView;

        public ChatPageViewHolder(View itemView) {
            super(itemView);
            mView =itemView;

        }

        public void setMessageText(String messageText) {
            TextView messageTexts = (TextView) mView.findViewById(R.id.message_text);
            messageTexts.setText(messageText);

        }

        public void setMessageUser(String  username) {
            TextView messageUsers = (TextView) mView.findViewById(R.id.message_user);
             messageUsers.setText(username);

        }

           public  void setMessageTime(DateFormat timeinstance, long messageTime){
               TextView message_times = (TextView) mView.findViewById(R.id.message_time);
               message_times.setText(timeinstance.format(messageTime) );

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
            startActivity(new Intent(MYCHATPAGE.this, ABOUTVIEW.class));
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





