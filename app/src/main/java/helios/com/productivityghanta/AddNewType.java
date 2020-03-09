package helios.com.productivityghanta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddNewType extends AppCompatActivity {
    Button cancel;
    EditText isProd;
    Button save;
    EditText tvActivityName;

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_type);
        this.save = (Button) findViewById(R.id.button2);
        this.cancel = (Button) findViewById(R.id.button3);
        this.tvActivityName = (EditText) findViewById(R.id.editText2);
        this.isProd = (EditText) findViewById(R.id.editText3);
        this.save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.e("clicking", "clinking");
                if (AddNewType.this.tvActivityName.length() > 0 && (AddNewType.this.isProd.getText().toString().equals("Y") || AddNewType.this.isProd.getText().toString().equals("N"))) {
                    new DBHandler(AddNewType.this.getApplicationContext()).insertActivityType(AddNewType.this.tvActivityName.getText().toString(), AddNewType.this.isProd.getText().toString());
                    Toast.makeText(AddNewType.this.getApplicationContext(), "Added successfully", Toast.LENGTH_LONG).show();
                    AddNewType.this.startActivity(new Intent(AddNewType.this, AddActivity.class));
                } else if (AddNewType.this.tvActivityName.length() == 0) {
                    Toast.makeText(AddNewType.this.getApplicationContext(), "Empty Name", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AddNewType.this.getApplicationContext(), "Invalid Productive Value , only Y or N allowed", Toast.LENGTH_LONG).show();
                }
            }
        });
        this.cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.e("clicking", "clinking");
                Intent a = new Intent(AddNewType.this, AddActivity.class);
                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                AddNewType.this.startActivity(a);
            }
        });
    }
}
