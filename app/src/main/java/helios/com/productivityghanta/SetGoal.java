package helios.com.productivityghanta;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SetGoal extends AppCompatActivity {
    private static String DEFAULTNAME = "Select Activity";
    static Button fromDate;
    static Button toDate;
    Button cancel;
    EditText editTextMinutes;
    EditText editTextgoalName;
    Button save;
    Spinner spinner;

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
            SetGoal.toDate.setText(sdf.format(cal.getTime()));
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
            SetGoal.fromDate.setText(sdf.format(cal.getTime()));
        }
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goal);
        try {
            this.save = (Button) findViewById(R.id.button19);
            this.cancel = (Button) findViewById(R.id.button20);
            fromDate = (Button) findViewById(R.id.button17);
            toDate = (Button) findViewById(R.id.button18);
            this.spinner = (Spinner) findViewById(R.id.spinner2);
            this.editTextMinutes = (EditText) findViewById(R.id.editText8);
            this.editTextgoalName = (EditText) findViewById(R.id.editText5);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            fromDate.setText(sdf.format(Calendar.getInstance().getTime()));
            Calendar cal = Calendar.getInstance();
            cal.add(5, 1);
            toDate.setText(sdf.format(cal.getTime()));
            final DBHandler dbHandler1 = new DBHandler(getApplicationContext());
            List activitiesList = dbHandler1.getAllActivityTypes();
            activitiesList.add(0, DEFAULTNAME);
            ArrayAdapter<CharSequence> spinArrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, activitiesList);
            spinArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            this.spinner.setAdapter(spinArrayAdapter);
            this.save.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Log.e("clicking", "clinking");
                    String act = BuildConfigMine.FLAVOR;
                    if (SetGoal.this.spinner.getSelectedItem() != null) {
                        act = SetGoal.this.spinner.getSelectedItem().toString();
                    }
                    String fromDateText = SetGoal.fromDate.getText().toString();
                    String toDateText = SetGoal.toDate.getText().toString();
                    String minutes = SetGoal.this.editTextMinutes.getText().toString();
                    String goalName = act + "_" + fromDateText;
                    if (!SetGoal.this.editTextgoalName.getText().toString().equals(BuildConfigMine.FLAVOR)) {
                        goalName = SetGoal.this.editTextgoalName.getText().toString();
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    boolean erroneous = false;
                    Date from = Calendar.getInstance().getTime();
                    Date to = Calendar.getInstance().getTime();
                    try {
                        from = sdf.parse(fromDateText);
                        to = sdf.parse(toDateText);
                    } catch (ParseException e) {
                        erroneous = true;
                    }
                    if (act.length() > 0 && !act.equals(SetGoal.DEFAULTNAME) && SetGoal.fromDate.length() > 0 && SetGoal.toDate.length() > 0 && !erroneous && from.before(to)) {
                        Toast.makeText(SetGoal.this.getApplicationContext(), "Added successfully", Toast.LENGTH_LONG).show();
                        dbHandler1.addNewGoal(goalName, act, minutes, fromDateText, toDateText, "true");
                        Intent a = new Intent(SetGoal.this, GoalsMainPage.class);
                        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        SetGoal.this.startActivity(a);
                    } else if (from.before(to)) {
                        Toast.makeText(SetGoal.this.getApplicationContext(), "Invalid data", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SetGoal.this.getApplicationContext(), "From Date is greater than To Date", Toast.LENGTH_LONG).show();
                    }
                }
            });
            this.cancel.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Log.e("clicking", "clinking");
                    Intent a = new Intent(SetGoal.this, GoalsMainPage.class);
                    a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    SetGoal.this.startActivity(a);
                }
            });
        } catch (Exception e) {
        }
    }

    public void showDatePickerDialogFrom(View v) {
        new DatePickerFrag().show(getSupportFragmentManager(), "datePicker");
    }

    public void showDatePickerDialogTo(View v) {
        new DatePickerFrag2().show(getSupportFragmentManager(), "datePicker");
    }
}
