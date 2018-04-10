package com.gmail.peeman34.eglisaofficial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by pee on 3/26/2017.
 */

    public class LOGIN extends AppCompatActivity {

    private EditText mLoginemailField;
    private EditText mLoginPasswordField;
    Handler handler;
    private Button mLoginBtn;
    private Button signupagain;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private ProgressDialog mProgress;
    private SignInButton GoogleBtn;
    private static final int RC_SIGN_IN = 1;
    Intent signInIntent;
    GoogleApiClient mGoogleApiClient;
    private FirebaseAuth.AuthStateListener mAuthListener;


    private static final String TAG = "LoginActivity";

    public LOGIN() {
        super();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginin22);

        mAuth = FirebaseAuth.getInstance();

        startthesignupagain.start();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };


        signupagain = (Button) findViewById(R.id.signupagainhere);

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);
        mProgress = new ProgressDialog(this);

        mLoginemailField = (EditText) findViewById(R.id.signinemail);
        mLoginPasswordField = (EditText) findViewById(R.id.signinpassword);

        mLoginBtn = (Button) findViewById(R.id.signin);
        GoogleBtn = (SignInButton) findViewById(R.id.signinbtngoogle);




        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();


       mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,
        new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();



        GoogleBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                signIn();
            }
        });




         mLoginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                checkLogin();

            }
        });

        signupagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startsignupagain();
            }
        });




       }

    private  void signIn(){
        signInIntent  = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);



    }




    public  void startsignupagain(){


        handler.post(new Runnable() {
            @Override
            public void run() {
                handler = new Handler();
                Intent startsignup = new Intent(LOGIN.this, ActivityMaiin.class);
                startsignup.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(startsignup);





            }
        });



    }



    Thread startthesignupagain = new Thread() {

        @Override
        public void run() {

            try {
                sleep(1000);


                Looper.prepare();
                handler = new Handler();
                Looper.loop();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



    };






          private void checkLogin(){
        String email = mLoginemailField.getText().toString().trim();
        String password =mLoginPasswordField.getText().toString().trim();
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                 mProgress.setMessage("Checking login");
                 mProgress.show();

             mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                     if(task.isSuccessful()){
                             mProgress.dismiss();
                     //        checkUserExist();


                         Intent mainviewintent= new Intent(LOGIN.this, CHURCHLIST.class);
                         mainviewintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                         startActivity(mainviewintent);

                     }
                 else {

                               mProgress.dismiss();
                         Toast.makeText(LOGIN.this, "Error Login", Toast.LENGTH_LONG).show();
                     }
             }

            });

    }}
    private  void checkUserExist(){

            if (mAuth.getCurrentUser() != null){

    final  String user_id = mAuth.getCurrentUser().getUid();

        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user_id)) {

                  Intent mainviewintent= new Intent(LOGIN.this, CHURCHLIST.class);
                    mainviewintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainviewintent);

                } else {
                    Intent mainviewintent= new Intent(LOGIN.this, ACCOUNTSETUP.class);
                    mainviewintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainviewintent);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

            });



      }}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if (requestCode==RC_SIGN_IN){
             GoogleSignInResult result =  Auth.GoogleSignInApi.getSignInResultFromIntent(data);
               mProgress.setMessage("Starting sign in");
               mProgress.show();
             if (result.isSuccess()){
                 GoogleSignInAccount account =result.getSignInAccount();
                     firebaseAuthWithGoogle(account);


             }else {
              mProgress.dismiss();
             }

         }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
             mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                     Log.d(TAG, "signInWithCredential:onComplete" + task.isSuccessful());
                         if (!task.isSuccessful()) {
                             Log.w(TAG, "signInWithCredential", task.getException());
                                 Toast.makeText(LOGIN.this, "Authentication failed", Toast.LENGTH_LONG).show();
                         }
                         else {
                   mProgress.dismiss();
                              checkUserExist();

                         }
                 }


                 });

             }

}







