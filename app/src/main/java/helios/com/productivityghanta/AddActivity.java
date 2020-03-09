package helios.com.productivityghanta;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddActivity extends AppCompatActivity {    private static String DEFAULTNAME = "Select Activity";
    static EditText editTextDate;
    static EditText editTextMinutes;
    Button addNewType;
    Button cancel;
    Button save;
    Spinner spinner;

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
            AddActivity.editTextDate.setText(sdf.format(cal.getTime()));
        }
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        try {
            this.spinner = (Spinner) findViewById(R.id.spinner);
            editTextMinutes = (EditText) findViewById(R.id.editText);
            editTextDate = (EditText) findViewById(R.id.editText4);
            this.save = (Button) findViewById(R.id.button4);
            this.cancel = (Button) findViewById(R.id.button5);
            this.addNewType = (Button) findViewById(R.id.button);
            SharedPreferences sharedPref = getSharedPreferences("lastUpdated", 0);
            String def = BuildConfigMine.FLAVOR;
            Log.e("lastUpdated", sharedPref.getString("lastUpdated", BuildConfigMine.FLAVOR));
            final DBHandler dbHandler = new DBHandler(getApplicationContext());
            List activitiesList = dbHandler.getAllActivityTypes();
            activitiesList.add(0, DEFAULTNAME);
            ArrayAdapter<CharSequence> spinArrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, activitiesList);
            spinArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            this.spinner.setAdapter(spinArrayAdapter);
            editTextDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
            this.save.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Log.e("clicking", "clinking");
                    String act = BuildConfigMine.FLAVOR;
                    if (AddActivity.this.spinner.getSelectedItem() != null) {
                        act = AddActivity.this.spinner.getSelectedItem().toString();
                    }
                    String minutes = AddActivity.editTextMinutes.getText().toString();
                    String datee = AddActivity.editTextDate.getText().toString();
                    if (act.length() > 0 && !act.equals(AddActivity.DEFAULTNAME) && minutes.length() > 0 && datee.length() > 0 && !dbHandler.isMinutesGreaterForADay(minutes, datee)) {
                        new DBHandler(AddActivity.this.getApplicationContext()).insertTrackedActivity(act, minutes, datee);
                        Toast.makeText(AddActivity.this.getApplicationContext(), "Added successfully", 1).show();
                        Editor editor = AddActivity.this.getSharedPreferences("lastUpdated", 0).edit();
                        editor.putString("lastUpdated", new Date().toString());
                        editor.commit();
                        AddActivity.this.startActivity(new Intent(AddActivity.this, HomePage.class));
                    } else if (act.length() == 0 || act.equals(AddActivity.DEFAULTNAME)) {
                        Toast.makeText(AddActivity.this.getApplicationContext(), "Invalid or Empty Activity", 1).show();
                    } else if (minutes.length() == 0) {
                        Toast.makeText(AddActivity.this.getApplicationContext(), "Invalid number of minutes", 1).show();
                    } else if (minutes.length() > 0 && dbHandler.isMinutesGreaterForADay(minutes, datee)) {
                        Toast.makeText(AddActivity.this.getApplicationContext(), "Invalid no. of minutes , exceeds date limit", 1).show();
                    } else if (datee.length() == 0) {
                        Toast.makeText(AddActivity.this.getApplicationContext(), "Invalid date", 1).show();
                    } else {
                        Toast.makeText(AddActivity.this.getApplicationContext(), "Invalid data", 1).show();
                    }
                }
            });
            this.addNewType.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Log.e("clicking", "clinking");
                    AddActivity.this.startActivity(new Intent(AddActivity.this, AddNewType.class));
                }
            });
            this.cancel.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Log.e("clicking", "clinking");
                    Intent a = new Intent(AddActivity.this, HomePage.class);
                    a.addFlags(67108864);
                    AddActivity.this.startActivity(a);
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error occured", 1).show();
        }
    }

    public void showDatePickerDialog(View v) {
        new DatePickerFrag().show(getSupportFragmentManager(), "datePicker");
    }

}
