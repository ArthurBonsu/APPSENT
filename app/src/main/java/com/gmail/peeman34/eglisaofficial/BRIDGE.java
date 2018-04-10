package com.gmail.peeman34.eglisaofficial;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by pee on 4/27/2017.
 */



public class BRIDGE extends AppCompatActivity {


    private ImageButton signitinhere;
    Handler firsthandler = new Handler();
    Thread mythread;
    Handler handler;
    Intent peethread;
    public BRIDGE() {
        super();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bridge);




         signitinhere = (ImageButton) findViewById(R.id.signituphere);

            Thread mythread = new Thread(new Mythread());
          mythread.start();

        signitinhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signitinhere = new Intent(BRIDGE.this, LOGIN.class);
                startActivity(signitinhere);

            }
        });

    }


    public void startSignup(View view){
           handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                 peethread = new Intent(BRIDGE.this, ActivityMaiin.class);

                startActivity(peethread);

            }
        });




    }


    class Mythread extends Thread {


        private Handler handler;

        public Mythread() {

         }

        @Override
        public void run() {


            try {


                sleep(10000);

                Looper.prepare();
                handler = new Handler();
                Looper.loop();


            } catch (InterruptedException e) {
                e.printStackTrace();
            }




        }





    }




}









