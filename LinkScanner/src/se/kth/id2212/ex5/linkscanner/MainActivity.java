package se.kth.id2212.ex5.linkscanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.example.untitled.R;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import se.kth.id2212.ex5.linkscanner.crawler.LinkScanner;
import se.kth.id2212.ex5.linkscanner.crawler.PageLink;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.List;

public class MainActivity extends Activity
{
    public final static String EXTRA_MESSAGE = "com.example.myActivity.MESSAGE";
    private ProgressBar mProgress;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setupActivity();
    }

    private void setupActivity()
    {
        final Button button = (Button) findViewById(R.id.crawlBtn);
        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(v.getContext().CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected())
                {
                    EditText editText = (EditText) findViewById(R.id.editText);
                    mProgress.setVisibility(View.VISIBLE);
                    new ScanLinks().execute(editText.getText().toString());
                } else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(getString(R.string.no_internet))
                            .setTitle(getString(R.string.no_connection));
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    private class ScanLinks extends AsyncTask<String, Void, List<PageLink>>
    {
        @Override
        protected List<PageLink> doInBackground(String... url)
        {
            String pageUrl = reformat(url[0]);
            String content = getPageContent(pageUrl);
            List<PageLink> links = LinkScanner.scan(content);
            return links;
        }

        private String reformat(String link)
        {
            if (!link.startsWith("http://") && !link.startsWith("https://"))
                return "http://" + link;
            return link;
        }

        private String getPageContent(String url)
        {
            String response = "";
            DefaultHttpClient client = new DefaultHttpClient();
            try
            {
                HttpGet httpGet = new HttpGet(url);
                HttpResponse execute = client.execute(httpGet);
                InputStream content = execute.getEntity().getContent();

                BufferedReader buffer = new BufferedReader(
                        new InputStreamReader(content));
                String s;
                while ((s = buffer.readLine()) != null)
                {
                    response += s;
                }

            } catch (Exception e)
            {
                Log.e("ParseError", e.getMessage());
            }

            return response;
        }


        @Override
        protected void onPostExecute(List<PageLink> links)
        {
            mProgress.setVisibility(View.GONE);
            if (!links.isEmpty())
            {
                new DisplayLinksDialog("Found " + links.size() + " links. Want to check them?", links).show(getFragmentManager(), "Links");
            } else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(getString(R.string.nolinks))
                        .setTitle(getString(R.string.noresults));
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    private class DisplayLinksDialog extends DialogFragment
    {
        private final String msg;
        private final List<PageLink> links;

        private DisplayLinksDialog(String msg, List<PageLink> links)
        {
            this.msg = msg;
            this.links = links;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(msg)
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            Intent intent = new Intent(MainActivity.this, LinksActivity.class);
                            intent.putExtra(EXTRA_MESSAGE, (Serializable) links);
                            startActivity(intent);
                        }
                    });
            return builder.create();
        }
    }


}

