package helios.com.productivityghanta;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class ActivitiesListActivity extends ListActivity {
    ArrayAdapter adapter;
    List lists;
    SimpleCursorAdapter mAdapter;

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_list);
        final DBHandler dbHandler = new DBHandler(getApplicationContext());
        Intent intent = getIntent();
        String fromDate = "";
        String toDate = "";
        if (intent.hasExtra("fromDate") && intent.hasExtra("toDate")) {
            fromDate = intent.getStringExtra("fromDate");
            toDate = intent.getStringExtra("toDate");
        } else if (intent.hasExtra("fromDate")) {
            fromDate = intent.getStringExtra("fromDate");
        }
        this.lists = new ArrayList();
        if (!fromDate.equals(BuildConfigMine.FLAVOR) && !toDate.equals(BuildConfigMine.FLAVOR)) {
            this.lists = dbHandler.getAllRecords(fromDate, toDate);
        } else if (fromDate.equals(BuildConfigMine.FLAVOR)) {
            this.lists = dbHandler.getAllRecords();
        } else {
            this.lists = dbHandler.getAllRecords(fromDate);
        }
        this.adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, this.lists);
        setListAdapter(this.adapter);
        getListView().setLongClickable(true);
        final ListView lv = getListView();
        getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                Toast.makeText(ActivitiesListActivity.this.getApplicationContext(), "Deleting" + arg3, Toast.LENGTH_LONG).show();
                String val = lv.getItemAtPosition(arg2).toString();
                Log.e("valusueuieueu:", lv.getItemAtPosition(arg2).toString());
                Log.e("act:", val.split("\\n")[0]);
                dbHandler.deleteActivity(val.split("\\n")[0], val.split("\\n")[1],val.split("\\n")[2]);
                ActivitiesListActivity.this.lists.remove(Long.valueOf(arg3));
                ActivitiesListActivity.this.adapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    /* Access modifiers changed, original: protected */
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.e("Hello", "You clicked Item: " + id + " at position:" + position);
    }

}
