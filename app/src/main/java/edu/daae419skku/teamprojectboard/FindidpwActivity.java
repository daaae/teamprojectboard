package edu.daae419skku.teamprojectboard;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FindidpwActivity extends AppCompatActivity {

    EditText username, usernum, userEmail;
    TextView resultView, resultPW;
    private boolean userexist = false;
    private boolean emailexist = false;
    private String useremail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findidpw);

        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("이메일 찾기");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("비밀번호 재설정");
        host.addTab(spec);

        username = (EditText)findViewById(R.id.userName);
        usernum = (EditText)findViewById(R.id.userNum);
        userEmail = (EditText)findViewById(R.id.userEmail);

        resultView = (TextView)findViewById(R.id.resultEmail);
        resultPW = (TextView)findViewById(R.id.resultPW);

    }

    public void onButtonNewPWClicked(View v) {
        final String email = userEmail.getText().toString().trim();

        Query findUidQuery = FirebaseDatabase.getInstance().getReference().child("users").orderByKey();
        findUidQuery.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DataSnapshot userSnapshot = dataSnapshot.child("useremail");
                if (userSnapshot.getValue().toString().equals(email)) {
                    emailexist = true;
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

        if (emailexist) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String emailAddress = email;

            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                resultPW.setText("비밀번호 재설정 메일을 보냈습니다.");
                            }
                        }
                    });
        }
        else
            resultPW.setText("일치하는 결과가 없습니다. 이메일을 다시 확인해주세요.");

    }

    public void onButtonFindEmailClicked(View v) {

        final String name = username.getText().toString().trim();
        final String num = usernum.getText().toString();

        Query findUidQuery = FirebaseDatabase.getInstance().getReference().child("users").orderByKey();
        findUidQuery.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DataSnapshot userSnapshot = dataSnapshot.child("username");
                if (userSnapshot.getValue().toString().equals(name)) {
                    if (dataSnapshot.child("usernum").getValue().toString().equals(num)) {
                        useremail = dataSnapshot.child("useremail").getValue().toString();
                        userexist = true;
                    }
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

        if (userexist) {
            int len = useremail.length();
            String stars = "";
            for (int i = 0; i < len - 3; i++)
                stars += "*";
            String showResult = useremail.substring(0, 3) + stars;

            resultView.setText(showResult);
        }
        else {
            resultView.setText("일치하는 결과가 없습니다.");
        }

    }

}
