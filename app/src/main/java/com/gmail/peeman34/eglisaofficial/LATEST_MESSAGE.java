package com.gmail.peeman34.eglisaofficial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

/**
 * Created by pee on 1/5/2016.
 */


/**
 * Created by pee on 2/3/2016.
 */
  public  class  LATEST_MESSAGE extends AppCompatActivity {

    private static final int GALLERY_REQUEST2 = 2;
    private ImageButton mLatestMessageImage;
    private EditText mLatestMessagetitle;
    private EditText mLatestMessageDescription;
    private EditText mLatestMessageDate;
    private  ImageButton mLatestAudio;
    private  EditText preachername;
    String mPostKey;
    private  Uri file;
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
    UploadTask uploadTask;
      String churchcreater;
    StorageReference storageReference;
    String churchkey;
    boolean saved;
    DatabaseReference newPost;
    public LATEST_MESSAGE()  {
        super();
    }





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.latestmessagelayout);

        mLatestMessageImage = (ImageButton) findViewById(R.id.latestmessageimage);
        mLatestMessagetitle = (EditText) findViewById(R.id.latestmessagetitle);
        mLatestMessageDescription = (EditText) findViewById(R.id.latestmessagedescription);
        mLatestMessageDate = (EditText) findViewById(R.id.latestmessagedate);
        preachername = (EditText)findViewById(R.id.preachername);
        msubmitButton = (Button) findViewById(R.id.latestmessagepost);
        mLatestAudio = (ImageButton)findViewById(R.id.mlatestaudio);


        Auth = FirebaseAuth.getInstance();
        mCurrentUser = Auth.getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("LATESTMESSAGE");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        mDatabaseCHURCHCHOSEN = FirebaseDatabase.getInstance().getReference().child("CHURCHCHOSEN");


        if (Auth.getCurrentUser().getUid() == churchcreater) {

            msubmitButton.setVisibility(View.VISIBLE);
        }
            mProgress = new ProgressDialog(this);
        mLatestMessageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }


        });

         mLatestAudio.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                  Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                 galleryIntent.setType("audio/*");
                 startActivityForResult(galleryIntent, GALLERY_REQUEST2);

             }
         });


        msubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startPosting();
            }
        });


    }


    public void startPosting() {

        final String titleval = mLatestMessagetitle.getText().toString();
        final String descval = mLatestMessageDescription.getText().toString();
        final String descdate = mLatestMessageDate.getText().toString();
        final String preacher = preachername.getText().toString();


        if (!TextUtils.isEmpty(titleval) && !TextUtils.isEmpty(descval) && !TextUtils.isEmpty(descdate) && mImageUri != null && file != null && !TextUtils.isEmpty(preacher)) {
            mProgress.setMessage("Posting to LATEST MESSAGES");

            mProgress.show();


            // File or Blob

            //  file = Uri.fromFile(new File(String.valueOf((new File("path/to/audio.mp3")))));

            //Create the file metadata
            StorageMetadata metadata = new StorageMetadata.Builder().setContentType("audio").build();


            //Upload file and metadata to the path 'audio/audio.mp3'

            uploadTask = storageReference.child("Latestmessageaudio").child(file.getLastPathSegment()).putFile(file, metadata);

            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    saved = true;

                    Uri sessionUri = taskSnapshot.getUploadSessionUri();
                    if (sessionUri != null && !saved) {
                        saved = true;
                        uploadTask = storageReference.child("Latestmessageaudio").child(file.getLastPathSegment()).putFile(file, new StorageMetadata(), sessionUri);

                    }
                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    final Uri downloadaudioUrl = taskSnapshot.getDownloadUrl();

                    newPost = mDatabase.push();

                    churchkey = getIntent().getExtras().getString("churchkey");

                    mDatabaseUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {
                            newPost.child("title").setValue(titleval);
                            newPost.child("desc").setValue(descval);
                            newPost.child("preacher").setValue(preacher);
                            newPost.child("date").setValue(descdate);
                            newPost.child("uid").setValue(mCurrentUser.getUid());
                            newPost.child("churchid").setValue(churchkey);
                            newPost.child("audio").setValue(downloadaudioUrl.toString());

                            newPost.child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {


                                        StorageReference filepath = mStorage.child("LatestMessagePhotos").child(mImageUri.getLastPathSegment());
                                        filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                                newPost = mDatabase.push();

                                                churchcreater = getIntent().getExtras().getString("churchcreater");

                                                mDatabaseUser.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        newPost.child("image").setValue(downloadUrl.toString());
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                                startActivity(new Intent(LATEST_MESSAGE.this, LATESTMESSAGEVIEW.class));

                                            }

                                        });


                                    }
                                }
                            });
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            });


        }


    }






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

                mLatestMessageImage.setImageURI(mImageUri);

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }

        }


        if (requestCode == GALLERY_REQUEST2 && resultCode == RESULT_OK){


                 file = data.getData();


        }

        super.onActivityResult(requestCode, resultCode, data);
    }



}