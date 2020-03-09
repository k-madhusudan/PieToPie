package helios.com.productivityghanta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class HomePage extends AppCompatActivity {
    Button export;
    Button filter;
    Button goals;
    Button importData;
    Button statsSimpleDaily;
    Button viewListDaily;

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ((FloatingActionButton) findViewById(R.id.fab)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                HomePage.this.startActivity(new Intent(HomePage.this, AddActivity.class));
            }
        });
        TextView tv1 = (TextView) findViewById(R.id.text11);
        List activities = new DBHandler(getApplicationContext()).getAllRecords();
        SharedPreferences sharedPref = getSharedPreferences("lastUpdated", 0);
        ((TextView) findViewById(R.id.textView30)).setText("Your Last Updated Time is:" + sharedPref.getString("lastUpdated", BuildConfigMine.FLAVOR));
        Log.e("lastUpdatedinHomePage", sharedPref.getString("lastUpdated", BuildConfigMine.FLAVOR));
        this.viewListDaily = (Button) findViewById(R.id.button7);
        this.viewListDaily.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String fromDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
                Intent a = new Intent(HomePage.this, ActivitiesListActivity.class);
                a.putExtra("fromDate", fromDate);
                HomePage.this.startActivity(a);
            }
        });
        this.statsSimpleDaily = (Button) findViewById(R.id.button6);
        this.statsSimpleDaily.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String fromDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
                Intent a = new Intent(HomePage.this, StatsBasic.class);
                a.putExtra("fromDate", fromDate);
                HomePage.this.startActivity(a);
            }
        });
        this.filter = (Button) findViewById(R.id.button12);
        this.filter.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                HomePage.this.startActivity(new Intent(HomePage.this, BasicFilterActivity.class));
            }
        });
        this.export = (Button) findViewById(R.id.button8);
        this.export.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String filename = "data_" + new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()) + ".csv";
                Log.e("Hello", filename);
                String contents = BuildConfigMine.FLAVOR;
                DBHandler dbHandler = new DBHandler(HomePage.this.getApplicationContext());
                ArrayList arrayList = new ArrayList();
                for (String s : dbHandler.export()) {
                    contents = contents + s;
                }
                File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "helios.com.pietopie");
                if (!path.exists()) {
                    path.mkdirs();
                    Log.e("hellopath", String.valueOf(path.exists()));
                }
                File mypath = new File(path, filename);
                try {
                    mypath.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    PrintWriter out = new PrintWriter(new FileWriter(mypath));
                    out.println(contents);
                    out.close();
                    Toast.makeText(HomePage.this.getApplicationContext(), "Export successful", Toast.LENGTH_LONG).show();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
        this.goals = (Button) findViewById(R.id.button13);
        this.goals.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                HomePage.this.startActivity(new Intent(HomePage.this, GoalsMainPage.class));
            }
        });
        this.importData = (Button) findViewById(R.id.button9);
        this.importData.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //HomePage.this.startActivity(new Intent(HomePage.this, ImportActivity.class));
                try {
                    File sdcard = Environment.getExternalStorageDirectory();
                    //Toast.makeText(HomePage.this.getApplicationContext(), sdcard.getPath(), Toast.LENGTH_LONG).show();
                    File file = new File(sdcard, "PieToPieImport.csv");
                    Toast.makeText(HomePage.this.getApplicationContext(), file.getPath(), Toast.LENGTH_LONG).show();
                    StringBuilder text = new StringBuilder();
                    ArrayList<String> allValues = new ArrayList<>();

                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;

                    while ((line = br.readLine()) != null) {
                        allValues.add(line);
                    }
                    br.close();

                    DBHandler dbHandler = new DBHandler(HomePage.this.getApplicationContext());
                    dbHandler.deleteAllActivity();
                    Map<String,String> mActivities = new HashMap<String,String>();
                    for(String s : allValues){
                        String activityType = s.split(",")[0];
                        //dbHandler.insertActivityType(activityType,"Y");
                        mActivities.put(activityType,"");
                    }
                    Toast.makeText(HomePage.this.getApplicationContext(), "Import "+mActivities.size(), Toast.LENGTH_LONG).show();
                    for(Map.Entry<String, String> s: mActivities.entrySet()){
                        dbHandler.insertActivityType(s.getKey(),"Y");
                        //Log.d("insert1",s.getKey());
                    }
                    Toast.makeText(HomePage.this.getApplicationContext(), "Import "+dbHandler.getAllActivityTypes().size(), Toast.LENGTH_LONG).show();
                    int counterw = 0;
                    for(String s : allValues){
                        String activityType = s.split(",")[0];
                        String minutes = s.split(",")[1];
                        String dateOfActivity = s.split(",")[2];
                        counterw++;
                        dbHandler.insertTrackedActivity(activityType,minutes,dateOfActivity);
                    }
                    Toast.makeText(HomePage.this.getApplicationContext(), "Import successful-----"+counterw, Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    //You'll need to add proper error handling here
                    e.printStackTrace();
                    //Log.getStackTraceString(e);
                    Toast.makeText(HomePage.this.getApplicationContext(), "Error During Import"+e.getMessage(), Toast.LENGTH_LONG).show();
                }
                catch (Exception e) {
                    //You'll need to add proper error handling here
                    e.printStackTrace();;
                    Log.getStackTraceString(e);
                    Toast.makeText(HomePage.this.getApplicationContext(), "Error During Import"+e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    /* Access modifiers changed, original: protected */
    public void onPause() {
        super.onPause();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_page, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
