package com.example.doorbell;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    EditText el;
    public TextView gor;
    Button btnCamera;
    Button btnDoor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        el=(EditText)findViewById(R.id.editText);
        gor=(TextView) findViewById(R.id.gosterilecek);

        Thread myThread= new Thread(new MyServerThread());
        myThread.start();
        btnCamera = (Button)findViewById(R.id.openCamera);



        btnCamera.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Uri linkimiz=Uri.parse("http://192.168.43.166:8081/"); //video linki için
                Intent intentimiz=new Intent(Intent.ACTION_VIEW,linkimiz);
                startActivity(intentimiz);
            }});



    }
    class MyServerThread implements Runnable{
        Socket s;
        ServerSocket ss;
        InputStreamReader isr;
        BufferedReader bufferedReader;
        Handler h = new Handler();
        String message;

        @Override
        public void run() {
            try {
                ss=new ServerSocket(5000);
                while(true){
                    s=ss.accept();
                    isr=new InputStreamReader(s.getInputStream());
                    bufferedReader=new BufferedReader(isr);
                    message=bufferedReader.readLine();


                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            if(message.equals("Zil")){
                                Toast.makeText(getApplicationContext(),"Doorbell is ringing!!",Toast.LENGTH_SHORT).show();

                            }
                            else {

                                gor.append("Raspberry : " + message + "\n");

                            }
                        }
                    });
                }
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }
    public void send(View v){

        MessageSender messagesender=new MessageSender();
        messagesender.execute(el.getText().toString());

            gor.append("Android : " + el.getText().toString() + "\n");
            el.setText("");

    }


}
