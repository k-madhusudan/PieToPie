package helios.com.productivityghanta;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class IndividualGoal extends AppCompatActivity {
    static EditText activity;
    static EditText actualMinutes;
    static EditText fromDate;
    static EditText goalName;
    static EditText targetMinutes;

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_goal);
        goalName = (EditText) findViewById(R.id.individGoalName);
        activity = (EditText) findViewById(R.id.individGoalActivityName);
        fromDate = (EditText) findViewById(R.id.individGoalFromTo);
        targetMinutes = (EditText) findViewById(R.id.individGoalTarget);
        actualMinutes = (EditText) findViewById(R.id.individGoalActual);
        DBHandler dbHandler = new DBHandler(this);
        GoalBean goalBean = dbHandler.getGoalDate(getIntent().getLongExtra("id", -1));
        goalName.setText(goalBean.getGoalName());
        activity.setText(goalBean.getActivityName());
        fromDate.setText(goalBean.getFrom() + " to " + goalBean.getTo());
        targetMinutes.setText(goalBean.getMinutes());
        actualMinutes.setText(dbHandler.getGoalDataByTime(goalBean.getActivityName(), goalBean.getFrom(), goalBean.getTo()));
    }

}
