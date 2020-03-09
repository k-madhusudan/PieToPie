package helios.com.productivityghanta;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatsBasic extends ListActivity {
    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_basic);
        DBHandler dbHandler = new DBHandler(getApplicationContext());
        Intent intent = getIntent();
        String fromDate = BuildConfigMine.FLAVOR;
        String toDate = BuildConfigMine.FLAVOR;
        String analyticsType = BuildConfigMine.FLAVOR;
        if (intent.hasExtra("fromDate") && intent.hasExtra("toDate")) {
            fromDate = intent.getStringExtra("fromDate");
            toDate = intent.getStringExtra("toDate");
            analyticsType = "between";
        } else if (intent.hasExtra("fromDate")) {
            fromDate = intent.getStringExtra("fromDate");
            analyticsType = "daily";
        } else {
            analyticsType = "all";
        }
        Log.e("Analyticstype", analyticsType);
        List lists = new ArrayList();
        if (!fromDate.equals(BuildConfigMine.FLAVOR) && !toDate.equals(BuildConfigMine.FLAVOR)) {
            lists = dbHandler.getAllRecords(fromDate, toDate);
        } else if (fromDate.equals(BuildConfigMine.FLAVOR)) {
            lists = dbHandler.getAllRecords();
        } else {
            lists = dbHandler.getAllRecords(fromDate);
        }
        List stats = new ArrayList();
        Map map = new HashMap();
        for (Object itemString : lists) {
            String[] arr = itemString.toString().split("\\n");
            String name = arr[0];
            String minutes = arr[1];
            String date = arr[2];
            String oldValue = "0";
            if (map.containsKey(name)) {
                oldValue = map.get(name).toString();
            }
            float oldNum = Float.parseFloat(oldValue) + (Float.parseFloat(minutes) / ((float) new Integer(60).intValue()));
            Log.e("Total", String.valueOf(oldNum));
            map.put(name, Double.valueOf(((double) Math.round(((double) oldNum) * 100.0d)) / 100.0d));
        }
        double hours = 0.0d;
        if (!analyticsType.equals("between") && analyticsType.equals("daily")) {
        }
        for (Object o : map.keySet()) {
            String key = (String) o;
            double e = ((Double) map.get(o)).doubleValue();
            hours += e;
            stats.add(key + ", " + e + " Hours");
        }
        if (analyticsType.equals("between")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date from = simpleDateFormat.parse(fromDate);
                Date to = simpleDateFormat.parse(toDate);
                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(from);
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(to);
                long diff = (cal2.getTimeInMillis() - cal1.getTimeInMillis()) / 86400000;
                Log.e("Datediff", BuildConfigMine.FLAVOR + cal2.getTimeInMillis());
                Log.e("Datediff", BuildConfigMine.FLAVOR + cal1.getTimeInMillis());
                Log.e("Datediff", BuildConfigMine.FLAVOR + diff);
                stats.add("Unaccounted" + "," + (((double) Math.round(((24.0d * ((double) diff)) - hours) * 100.0d)) / 100.0d) + " Hours");
            } catch (ParseException e2) {
                e2.printStackTrace();
            }
        } else if (analyticsType.equals("daily")) {
            stats.add("Unaccounted" + "," + (((double) Math.round((24.0d - hours) * 100.0d)) / 100.0d) + " Hours");
        }
        setListAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, stats));
    }

}
