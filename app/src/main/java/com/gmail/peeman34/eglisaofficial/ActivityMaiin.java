package com.gmail.peeman34.eglisaofficial;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;


import static com.gmail.peeman34.eglisaofficial.R.string.view;

/**
 * Created by pee on 6/1/2016.
 */
public class ActivityMaiin extends AppCompatActivity {


    private EditText Namefield;
    private EditText Emailfied;
    private EditText Passwordfield;
    private ImageButton mRegistration;
    private ProgressDialog mProgress;
    ImageButton Loginthebutton;

    Intent AccountSetup;

    Handler africanhandler;
    Handler thefirsthandler;
    Thread firstthread;


    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;


    public ActivityMaiin() {
        super();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity22);

        Namefield = (EditText) findViewById(R.id.username);
        Emailfied = (EditText) findViewById(R.id.email);
        Passwordfield = (EditText) findViewById(R.id.password);



           mAuth = FirebaseAuth.getInstance();

           mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
           mDatabase.keepSynced(true);

                 mRegistration = (ImageButton) findViewById(R.id.signupbutton);

        mProgress = new ProgressDialog(this);


  /*       firstthread = new Thread(new Myfirstthread()); */

      /*  firstthread.start(); */







        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

          try {

              startRegister();



          }catch (Exception e){


          }
            }
        });






    }







   public void  startaccountsetup() {

    /*   thefirsthandler = new Handler();

       thefirsthandler.post(new Runnable() {


           @Override
           public void run() {

           */
        try {


           Intent accounntsetup = new Intent(ActivityMaiin.this, ACCOUNTSETUP.class);
               startActivity(accounntsetup);

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }

  /*         }





       });

*/



   }




   /* class  Myfirstthread extends Thread{
                           public Myfirstthread() {
                               super();
                           }


                           @Override
                           public void run() {
                               try {
                                   sleep(5000);

                               } catch (InterruptedException e) {
                                   e.printStackTrace();


                               }


                               Looper.prepare();

                                Looper.loop();
                           }
                       }





*/



               public  void startRegister() throws InterruptedException {


                   final String name = Namefield.getText().toString().trim();
            String email = Emailfied.getText().toString().trim();
            String password = Passwordfield.getText().toString().trim();

                   if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                mProgress.setMessage("Signing up");
                mProgress.show();


                try {

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                String user_id = mAuth.getCurrentUser().getUid();

                                DatabaseReference current_user_db = mDatabase.child(user_id);

                                current_user_db.child("name").setValue(name);
                                current_user_db.child("image").setValue("default");

                                mProgress.dismiss();
                                startaccountsetup();


                             }


                        }

                    });
                } catch (Exception e) {

                }

            }


               }



    }







