package helios.com.productivityghanta;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BasicFilterActivity extends AppCompatActivity {

    private static String DEFAULTNAME = "All Activities";
    static EditText editTextFromDate;
    static EditText editTextToDate;
    Button cancel;
    Button showAnalytics;
    Button showList;

    public static class DatePickerFrag2 extends DialogFragment implements OnDateSetListener {
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar c = Calendar.getInstance();
            int year = c.get(1);
            int month = c.get(2);
            int day = c.get(5);
            Log.e("date", c.toString());
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Log.e("year", String.valueOf(year));
            Log.e("month", String.valueOf(month));
            Log.e("day", String.valueOf(day));
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Log.e("date", cal.toString());
            BasicFilterActivity.editTextToDate.setText(sdf.format(cal.getTime()));
        }
    }

    public static class DatePickerFrag extends DialogFragment implements OnDateSetListener {
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar c = Calendar.getInstance();
            int year = c.get(1);
            int month = c.get(2);
            int day = c.get(5);
            Log.e("date", c.toString());
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Log.e("year", String.valueOf(year));
            Log.e("month", String.valueOf(month));
            Log.e("day", String.valueOf(day));
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Log.e("date", cal.toString());
            BasicFilterActivity.editTextFromDate.setText(sdf.format(cal.getTime()));
        }
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_filter);
        try {
            editTextFromDate = (EditText) findViewById(R.id.editText7);
            editTextToDate = (EditText) findViewById(R.id.editText6);
            this.showList = (Button) findViewById(R.id.button11);
            this.cancel = (Button) findViewById(R.id.button5);
            this.showAnalytics = (Button) findViewById(R.id.button10);
            new DBHandler(getApplicationContext()).getAllActivityTypes().add(0, DEFAULTNAME);
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            editTextFromDate.setText(sdf.format(Calendar.getInstance().getTime()));
            Calendar cal = Calendar.getInstance();
            cal.add(5, 1);
            editTextToDate.setText(sdf.format(cal.getTime()));
            this.showList.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Log.e("clicking", "clinking");
                    String act = BuildConfigMine.FLAVOR;
                    String fromDate = BasicFilterActivity.editTextFromDate.getText().toString();
                    String toDate = BasicFilterActivity.editTextToDate.getText().toString();
                    boolean erroneous = false;
                    Date from = Calendar.getInstance().getTime();
                    Date to = Calendar.getInstance().getTime();
                    try {
                        from = sdf.parse(fromDate);
                        to = sdf.parse(toDate);
                    } catch (ParseException e) {
                        erroneous = true;
                    }
                    if (fromDate.length() > 0 && toDate.length() > 0 && !erroneous && from.before(to)) {
                        DBHandler dbHandler = new DBHandler(BasicFilterActivity.this.getApplicationContext());
                        Intent a = new Intent(BasicFilterActivity.this, ActivitiesListActivity.class);
                        a.putExtra("fromDate", fromDate);
                        a.putExtra("toDate", toDate);
                        BasicFilterActivity.this.startActivity(a);
                    } else if (from.before(to)) {
                        Toast.makeText(BasicFilterActivity.this.getApplicationContext(), "Invalid data", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(BasicFilterActivity.this.getApplicationContext(), "From Date is greater than To Date", Toast.LENGTH_LONG).show();
                    }
                }
            });
            this.showAnalytics.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Log.e("clicking", "clinking");
                    String act = BuildConfigMine.FLAVOR;
                    String fromDate = BasicFilterActivity.editTextFromDate.getText().toString();
                    String toDate = BasicFilterActivity.editTextToDate.getText().toString();
                    boolean erroneous = false;
                    Date from = Calendar.getInstance().getTime();
                    Date to = from;
                    try {
                        from = sdf.parse(fromDate);
                        to = sdf.parse(toDate);
                    } catch (ParseException e) {
                        erroneous = true;
                        Log.e("erroneous", "errors");
                    }
                    if (fromDate.length() > 0 && toDate.length() > 0 && !erroneous && from.before(to)) {
                        DBHandler dbHandler = new DBHandler(BasicFilterActivity.this.getApplicationContext());
                        Intent a = new Intent(BasicFilterActivity.this, StatsBasic.class);
                        a.putExtra("fromDate", fromDate);
                        a.putExtra("toDate", toDate);
                        BasicFilterActivity.this.startActivity(a);
                    } else if (from.before(to)) {
                        Toast.makeText(BasicFilterActivity.this.getApplicationContext(), "Invalid data", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(BasicFilterActivity.this.getApplicationContext(), "From Date is greater than To Date", Toast.LENGTH_LONG).show();
                    }
                }
            });
            this.cancel.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Log.e("clicking", "clinking");
                    Intent a = new Intent(BasicFilterActivity.this, HomePage.class);
                    a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    BasicFilterActivity.this.startActivity(a);
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error occured", Toast.LENGTH_LONG).show();
        }
    }

    public void showDatePickerDialogFrom(View v) {
        new DatePickerFrag().show(getSupportFragmentManager(), "datePicker");
    }

    public void showDatePickerDialogTo(View v) {
        new DatePickerFrag2().show(getSupportFragmentManager(), "datePicker");
    }

}
