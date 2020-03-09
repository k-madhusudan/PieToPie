package helios.com.productivityghanta;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GoalsMainPage extends AppCompatActivity {
    Button archivedGoals;
    Button currentGoals;
    Button setNewGoal;

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals_main_page);
        this.setNewGoal = (Button) findViewById(R.id.button16);
        this.currentGoals = (Button) findViewById(R.id.button14);
        this.archivedGoals = (Button) findViewById(R.id.button15);
        this.setNewGoal.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Log.e("clicking", "clinking");
                GoalsMainPage.this.startActivity(new Intent(GoalsMainPage.this, SetGoal.class));
            }
        });
        this.currentGoals.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Log.e("clicking", "clinking");
                GoalsMainPage.this.startActivity(new Intent(GoalsMainPage.this, GoalsListActivityCurrent.class));
            }
        });
        this.archivedGoals.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Log.e("clicking", "clinking");
                GoalsMainPage.this.startActivity(new Intent(GoalsMainPage.this, GoalsListActivityArchived.class));
            }
        });
    }

}
