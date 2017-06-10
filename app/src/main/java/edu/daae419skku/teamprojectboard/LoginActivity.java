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

public class LoginActivity extends AppCompatActivity {

    private EditText inputID, inputPW;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        /*if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, KanbanActivity.class));
            finish();
        }*/

        setContentView(R.layout.activity_login);

        inputID = (EditText) findViewById(R.id.userID);
        inputPW = (EditText) findViewById(R.id.userPW);

    }

    public void Login() {
        String email = inputID.getText().toString().trim();
        String password = inputPW.getText().toString().trim();

        //authenticate user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "failed", Toast.LENGTH_LONG).show();
                        } else {
                            Intent intent = new Intent(LoginActivity.this, KanbanActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }


    public void onButtonLoginClicked(View v) {
        Login();
    }

    public void onButtonSignupClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(intent);
    }

    public void onButtonFindidpwClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), FindidpwActivity.class);
        startActivity(intent);
    }
}
