package edu.daae419skku.teamprojectboard;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class SignupActivity extends AppCompatActivity {


    //define firebase objects
    private FirebaseAuth firebaseAuth;
    private DatabaseReference myRef;

    //define view objects
    private EditText editTextEmail, editTextPassword, editTextPassword1, editTextName, editTextNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //initializing firebase objects
        firebaseAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();

        //initialize views
        editTextEmail = (EditText) findViewById(R.id.userID);
        editTextPassword = (EditText) findViewById(R.id.userPW1);
        editTextPassword1 = (EditText) findViewById(R.id.userPW);
        editTextName = (EditText) findViewById(R.id.userName);
        editTextNumber = (EditText) findViewById(R.id.userNum);


    }

    private void saveUserData(String uid, String regid) {
        String username = editTextName.getText().toString().trim();
        String usernum = editTextNumber.getText().toString();

        User user = new User(username, usernum, regid);

        myRef.child("users").child(uid).setValue(user);
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            // save user data to database
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String uid = user.getUid();
                            String regId = FirebaseInstanceId.getInstance().getToken();
                            saveUserData(uid, regId);
                            finish();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }else{
                            //display some message here
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(SignupActivity.this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(SignupActivity.this, "회원 가입에 실패했습니다." ,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onButtonCancelClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    public void onButtonSignupDoneClicked(View v) {
        registerUser();
    }
}

