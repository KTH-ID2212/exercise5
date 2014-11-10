package com.example.WordLooper;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity implements ResultAppender
{
    private Button connectBtn;
    private Button submitBtn;
    private EditText strField;
    private ProgressBar mProgressBar;
    private ServerConnection srvConnection;
    private TextView resField;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setupUI();
    }


    private void setupUI()
    {
        connectBtn = (Button) findViewById(R.id.button);

        submitBtn = (Button) findViewById(R.id.button2);
        strField = (EditText) findViewById(R.id.editText);
        resField = (TextView) findViewById(R.id.textView2);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);


        connectBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                connectBtn.setEnabled(false);
                mProgressBar.setVisibility(View.VISIBLE);
                new ConnectServer().execute();
            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                submitBtn.setEnabled(false);
                mProgressBar.setVisibility(View.VISIBLE);
                srvConnection.reverse(strField.getText().toString());
            }
        });

    }

    @Override
    public void showResult(String result)
    {
        resField.setText(result);
        mProgressBar.setVisibility(View.GONE);
        submitBtn.setEnabled(false);
    }

    private class ConnectServer extends AsyncTask<Void, Void, ServerConnection>
    {
        @Override
        protected ServerConnection doInBackground(Void... _)
        {
            String srv_host = getString(R.string.SRV);
            Integer srv_port = Integer.valueOf(getString(R.string.SRV_PORT));
            ServerConnection srvConnection = new ServerConnection(MainActivity.this,
                    srv_host, srv_port);
            srvConnection.connect(); //this blocks
            return srvConnection;
        }

        @Override
        protected void onPostExecute(ServerConnection srvConnection)
        {
            mProgressBar.setVisibility(View.GONE);
            strField.setVisibility(View.VISIBLE);
            submitBtn.setVisibility(View.VISIBLE);
            resField.setVisibility(View.VISIBLE);
            MainActivity.this.srvConnection = srvConnection;
            new Thread(srvConnection).start();
        }
    }


}
