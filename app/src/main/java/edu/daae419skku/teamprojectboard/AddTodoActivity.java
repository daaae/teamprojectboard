package edu.daae419skku.teamprojectboard;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddTodoActivity extends AppCompatActivity {

    private DatabaseReference myRef;
    private String projectKey, person;
    private List memberlist;
    private ArrayList<String> users, usersID;
    private FirebaseAuth firebaseAuth;

    TextView txtDate, selection;
    private int year, month, day;

    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        projectKey = intent.getStringExtra("project_key");
        users = intent.getStringArrayListExtra("memberL");
        usersID = intent.getStringArrayListExtra("memberUid");
        setContentView(R.layout.activity_add_todo);

        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        selection=(TextView)findViewById(R.id.selection);

        // Spinner click listener
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                person = usersID.get(position);
            }
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, users);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        txtDate = (TextView) findViewById(R.id.date_finish);



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
        EditText todoName = (EditText)findViewById(R.id.todoName);
        EditText memo = (EditText)findViewById(R.id.editText_comment);
        TextView dateFinish = (TextView)findViewById(R.id.date_finish);


        //save it to the firebase db
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();

        Todo todo = new Todo();
        todo.setTodoName(todoName.getText().toString());
        todo.setDate(dateFinish.getText().toString());
        todo.setPerson(person);
        todo.setMemo(memo.getText().toString());


        String key = myRef.child("ProjectList").child(projectKey).child("todo").push().getKey();
        myRef.child("ProjectList").child(projectKey).child("todo").child(key).setValue(todo);

        finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));

    }


}



