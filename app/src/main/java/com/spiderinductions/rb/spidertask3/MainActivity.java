package com.spiderinductions.rb.spidertask3;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.os.Handler;

public class MainActivity extends ActionBarActivity {
    static Integer i;
    static Boolean boo=false;
    TextView textview1,textview2;
    Button button,button2;
    public static String TAG="TAG";
    Thread thread,threadinitial;
    static Handler handler,handlerinitial;
    static String string;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textview1 = (TextView)findViewById(R.id.textview1);
        textview2 = (TextView)findViewById(R.id.textview2);
        button = (Button)findViewById(R.id.button);
        button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boo = true;
            }
        });
        handler = new Handler();
        Message message;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
        try {
            threadinitial = new Thread(new initialthread());
            threadinitial.start();
            handlerinitial = new Handler() {
                Runnable runnable;
                @Override
                public void handleMessage(Message msg) {
                    textview1.setText("" + msg.arg1);
                    if(msg.arg1==0)
                    {
                        threadinitial.interrupt();
                        Log.d(TAG,""+threadinitial.isInterrupted());
                        try {
                            spidertask task = new spidertask();
                            string = task.execute().get();
                            textview2.setText(string);
                            i = string.length() - 1;
                            thread = new Thread(new mythread());
                            thread.start();
                            handler = new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    textview1.setText("" + msg.arg1);
                                }
                            };

                        } catch (Exception e) {}
                    }

                    }
            };
        }catch (Exception e){}

        }

    public class initialthread implements Runnable
    {
        @Override
        public void run()
        {   while(!Thread.currentThread().isInterrupted())
        {
            for(int k=10;k>=0;k--)
            {
                Message message = Message.obtain();
                message.arg1=k;
                try {
                    Thread.sleep(1000);
                    } catch (InterruptedException e) {return;}
                handlerinitial.sendMessage(message);

            }
        }
        }
    }
    public static class mythread implements Runnable
    {
        @Override
        public void run() {
            for(int j=Character.getNumericValue(string.charAt(i));j>=0;j--)
            {   Log.d(TAG,""+j);
                Message message = Message.obtain();
                message.arg1=j;
                if(j==0)
                {
                 i--;
                 j=Character.getNumericValue(string.charAt(i));
                 j++;
                }
                if(i==0)
                    i=string.length()-1;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                handler.sendMessage(message);
            }

        }
    }
    public static class spidertask extends AsyncTask<Void,Void,String>
    {

        @Override
        protected String doInBackground(Void... params) {
            String response = "";
            String S = null;
            if (boo) {
                S = "http://51ab36db.ngrok.io/";
                InputStream inputStream = null;
                try {
                    URL url = new URL(S);
                    try {
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod("GET");
                        inputStream = httpURLConnection.getInputStream();
                        Log.d(TAG, inputStream.toString());
                        BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
                        String t = "";
                        while ((t = buffer.readLine()) != null) {
                            response += t;
                            Log.d(TAG, response);
                        }
                    } catch (IOException e) {
                    }
                } catch (MalformedURLException e) {

                }

            } else
            {
            S = "http://spider.nitt.edu/~vishnu/time.php";
            InputStream inputStream = null;
            try {
                URL url = new URL(S);
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    inputStream = httpURLConnection.getInputStream();
                    Log.d(TAG, inputStream.toString());
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
                    String t = "";
                    while ((t = buffer.readLine()) != null) {
                        response += t;
                        Log.d(TAG, response);
                    }
                } catch (IOException e) {
                }
            } catch (MalformedURLException e) {

            }
        }
            return response;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
