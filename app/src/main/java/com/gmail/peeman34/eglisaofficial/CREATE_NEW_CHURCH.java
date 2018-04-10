package com.gmail.peeman34.eglisaofficial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

/**
 * Created by pee on 8/5/2016.
 */

public class CREATE_NEW_CHURCH extends AppCompatActivity {
    private static final int GALLERY_REQUEST2 = 2;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private ImageButton mChurchImage;
    private EditText mChurchtitle;
    private EditText mChurchDescription;
    private EditText mChurchDate;
    private  EditText Location;
    String mPostKey;
    String churchkey;
    private Button msubmitButton;
    private Uri mImageUri = null;
    private static final int GALLERY_REQUEST = 1;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseCHURCHCHOSEN;
    private ProgressDialog mProgress;
    private FirebaseAuth Auth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUser;

    public CREATE_NEW_CHURCH() {
        super();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_church);

        mChurchImage = (ImageButton) findViewById(R.id.mChurchImage);
        mChurchtitle = (EditText) findViewById(R.id.mChurchlistname);
        mChurchDescription = (EditText) findViewById(R.id.mChurchDescription);
        mChurchDate = (EditText) findViewById(R.id.mChurchDateCreated);
         Location  =(EditText)findViewById(R.id.mChurchLocation);

        msubmitButton = (Button) findViewById(R.id.mChurchPost);

        Auth = FirebaseAuth.getInstance();
        mCurrentUser = Auth.getCurrentUser();


        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("CHURCHCREATED");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        mDatabaseCHURCHCHOSEN = FirebaseDatabase.getInstance().getReference().child("CHURCHCHOSEN");


        mDatabase.keepSynced(true);
        mDatabaseUser.keepSynced(true);
        mDatabaseCHURCHCHOSEN.keepSynced(true);


        //I have to  check to ensure that gallery intent is not placed here for the other classes
        mProgress = new ProgressDialog(this);


        mChurchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }


        });


        msubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startPosting();
                } catch (Exception e) {

                }
            }
        });

    }


    private void startPosting() {
        final String titleval = mChurchtitle.getText().toString();
        final String descval = mChurchDescription.getText().toString();
        final String descdate =mChurchDate.getText().toString();
        final  String location =Location.getText().toString();

        if (!TextUtils.isEmpty(titleval) && !TextUtils.isEmpty(descval) && !TextUtils.isEmpty(descdate) && !TextUtils.isEmpty(location) && mImageUri != null) {
            mProgress.setMessage("BUILDING YOUR CHURCH");

            mProgress.show();
            StorageReference filepath = mStorage.child("ChurchPhotos").child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    final DatabaseReference newPost = mDatabase.push();
                    churchkey = getIntent().getExtras().getString("churchkey");


                    mDatabaseUser.addValueEventListener(new ValueEventListener() {
                        @Override

                        public void onDataChange(final DataSnapshot dataSnapshot) {

                            newPost.child("title").setValue(titleval);

                            newPost.child("location").setValue(location);
                            newPost.child("desc").setValue(descval);
                            newPost.child("image").setValue(downloadUrl.toString());
                            newPost.child("date").setValue(descdate);
                            newPost.child("uid").setValue(mCurrentUser.getUid());
                            newPost.child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {

                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                      churchkey = newPost.getKey();

                                        try {
                                            Intent newchurchview = new Intent(CREATE_NEW_CHURCH.this, MAINVIEW.class);
                                            newchurchview.putExtra("churchcreater", Auth.getCurrentUser().getUid());
                                            newchurchview.putExtra("churchkey", churchkey);
                                            newchurchview.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(newchurchview);


                                        } catch (Exception e

                                                ) {
                                        }


                                    }
                                }

                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    mProgress.dismiss();


                }
            });
        }


    }

    // OnActivity result is lacking behind, I have to get the URi from it
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();

            CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(this);


        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mImageUri = result.getUri();

                mChurchImage.setImageURI(mImageUri);

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }

        }
    }
}

