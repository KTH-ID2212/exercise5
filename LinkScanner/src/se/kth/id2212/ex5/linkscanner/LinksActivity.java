package se.kth.id2212.ex5.linkscanner;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.example.untitled.R;
import se.kth.id2212.ex5.linkscanner.crawler.PageLink;

import java.util.List;


public class LinksActivity extends ListActivity implements AdapterView.OnItemClickListener
{
    ListView linkList;
    private List<PageLink> links;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        links = (List<PageLink>) getIntent().getSerializableExtra(MainActivity.EXTRA_MESSAGE);
        populateList();
    }

    private void populateList()
    {
        linkList = getListView();
        linkList.setAdapter(new ArrayAdapter<>(this, R.layout.link_item, links));
        linkList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        Toast toast = Toast.makeText(getApplicationContext(), links.get(i).getLink(), Toast.LENGTH_LONG);
        toast.show();
    }
}