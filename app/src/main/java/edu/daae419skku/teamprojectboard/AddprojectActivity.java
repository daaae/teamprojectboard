package edu.daae419skku.teamprojectboard;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddprojectActivity extends AppCompatActivity {

    Button btnDatePicker;
    TextView txtDate;
    private int year, month, day;
    private DatabaseReference myRef;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproject);

        myRef = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        txtDate=(TextView) findViewById(R.id.date_finish);

    }

    public void onButtonCalendarClicked(View v) {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    public void onButtonSaveClicked(View v) {
        //get the data to save in firebase db
        EditText projectName = (EditText)findViewById(R.id.projectName);
        TextView dateFinish = (TextView)findViewById(R.id.date_finish);

        //save it to the firebase db
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String key = myRef.child("ProjectList").push().getKey();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = user.getUid();

        Project project = new Project();
        project.setProjectName(projectName.getText().toString());
        project.setDate(dateFinish.getText().toString());
        project.setLeader(uid);

        myRef.child("ProjectList").child(key).setValue(project);
        finish();
        startActivity(new Intent(getApplicationContext(), KanbanActivity.class));

    }


}
