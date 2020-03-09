package helios.com.productivityghanta;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class GoalsListActivityCurrent extends ListActivity {
    SimpleCursorAdapter mAdapter;

    public class ClientCursorAdapter extends ResourceCursorAdapter {
        public ClientCursorAdapter(Context context, int layout, Cursor cursor, int flags) {
            super(context, layout, cursor, flags);
        }

        public void bindView(View view, Context context, Cursor cursor) {
            TextView tv2 = (TextView) view.findViewById(R.id.text2);
            ((TextView) view.findViewById(R.id.text1)).setText(cursor.getString(2) + ", " + cursor.getString(3).toUpperCase() + ", " + cursor.getString(7) + " minutes");
            tv2.setText(cursor.getString(4) + " to  " + cursor.getString(5) + BuildConfigMine.FLAVOR);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals_list_current);
        final DBHandler dbHandler = new DBHandler(getApplicationContext());
        Cursor c = dbHandler.getCurrentGoals();
        ListView listView = getListView();
        final ClientCursorAdapter adapter2 = new ClientCursorAdapter(this, R.layout.list_row_1, c, 0);
        setListAdapter(adapter2);
        getListView().setLongClickable(true);
        getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                Toast.makeText(GoalsListActivityCurrent.this.getApplicationContext(), "Deleting", Toast.LENGTH_LONG).show();
                dbHandler.deleteCurrentGoal(arg3);
                adapter2.swapCursor(dbHandler.getCurrentGoals());
                return true;
            }
        });
    }

    /* Access modifiers changed, original: protected */
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.e("Hello", "You clicked Item: " + id + " at position:" + position);
        Intent a = new Intent(this, IndividualGoal.class);
        a.putExtra("id", id);
        startActivity(a);
    }

}
