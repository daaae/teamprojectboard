package edu.daae419skku.teamprojectboard;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

public class AddprojectActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {


    Button btnDatePicker;
    TextView txtDate;
    private int year, month, day;
    private DatabaseReference myRef;
    private FirebaseAuth firebaseAuth;

    ListView list, list2;
    ListViewAdapter adapter, adapter2;
    SearchView editsearch;
    String[] memberList;
    public List<MemberNames> arraylist = new ArrayList<MemberNames>();
    public List<MemberNames> arraylist2 = new ArrayList<MemberNames>();
    ArrayList<String> memberUids = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproject);
        list = (ListView) findViewById(R.id.listview);
        list2 = (ListView) findViewById(R.id.listview2);

        myRef = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        final String uid = user.getUid();


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                TextView textview = (TextView) view.findViewById(R.id.name);
                String data = textview.getText().toString();

                MemberNames memberNames = new MemberNames(data);
                arraylist2.add(memberNames);
                list2.setAdapter(adapter2);


                final String query = memberNames.getMemberNum();
                Query findUidQuery = FirebaseDatabase.getInstance().getReference().child("users").orderByKey();
                findUidQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        DataSnapshot userSnapshot = dataSnapshot.child("usernum");
                        if (userSnapshot.getValue().toString().equals(query)) {
                            String useruid = dataSnapshot.getKey();
                            memberUids.add(useruid);
                        }

                    }


                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        });

        txtDate = (TextView) findViewById(R.id.date_finish);


        // Pass result to ListViewAdapter Class
        adapter = new ListViewAdapter(this, arraylist);
        adapter2 = new ListViewAdapter(this, arraylist2);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);
        // Locate the EditText in listview_main.xml
        editsearch = (SearchView) findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);

    }

    @Override
    public boolean onQueryTextSubmit(final String query) {
        // email이 submit 되면 retrieve uid from database
        Query findUidQuery = FirebaseDatabase.getInstance().getReference().child("users").orderByKey();
        findUidQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DataSnapshot userSnapshot = dataSnapshot.child("usernum");
                if (userSnapshot.getValue().toString().equals(query)) {
                    String username = dataSnapshot.child("username").getValue().toString();
                    String usernum = dataSnapshot.child("usernum").getValue().toString();
                    String userinfo = username + "/" + usernum;
                    MemberNames memberNames = new MemberNames(userinfo);
                    arraylist.add(memberNames);
                    list.setAdapter(adapter);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
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
        myRef.child("users").child(uid).child("userprojects").child(key).setValue(true);

        for (String memberUid : memberUids) {
            myRef.child("users").child(memberUid).child("userprojects").child(key).setValue(true);
            myRef.child("ProjectList").child(key).child("members").child(memberUid).setValue(true);
        }

        finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));

    }

    public void onButtonListviewClicked(View v) {

    }






}
