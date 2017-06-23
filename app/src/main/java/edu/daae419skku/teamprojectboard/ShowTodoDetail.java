package edu.daae419skku.teamprojectboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.sql.DriverManager.println;

public class ShowTodoDetail extends AppCompatActivity {

    public String todoKey, projectKey;
    private RecyclerView recyclerView;
    chatAdapter adapter;
    private List<Chat> messageList = new ArrayList<>();
    private List<String> msgBundle = new ArrayList<>();
    private DatabaseReference myRef;
    private FirebaseAuth firebaseAuth;
    TextView name, person, txtdate, comment;
    EditText message;
    String messageUser;

    String regid;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        todoKey = intent.getStringExtra("todo_key");
        projectKey = intent.getStringExtra("project_key");
        setContentView(R.layout.activity_show_todo_detail);

        name = (TextView)findViewById(R.id.todoName);
        person = (TextView)findViewById(R.id.person);
        txtdate = (TextView)findViewById(R.id.date_finish);
        comment = (TextView)findViewById(R.id.comment);

        myRef = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        Button sendButton = (Button) findViewById(R.id.btn_push);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });
        queue = Volley.newRequestQueue(getApplicationContext());


        myRef.child("ProjectList").child(projectKey).child("todo").child(todoKey).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Todo todo = dataSnapshot.getValue(Todo.class);
                        name.setText(todo.getTodoName());
                        txtdate.setText(todo.getDate());
                        comment.setText(todo.getMemo());

                        myRef.child("users").child(todo.getPerson()).addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        User user = dataSnapshot.getValue(User.class);
                                        person.setText(user.username);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                }
                        );

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );


        recyclerView = (RecyclerView)findViewById(R.id.myrecycleView);

        adapter = new chatAdapter(messageList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        myRef = FirebaseDatabase.getInstance().getReference();

        myRef.child("ProjectList").child(projectKey).child("todo").child(todoKey).child("chats").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        msgBundle.clear();
                        messageList.clear();
                        for (DataSnapshot data : dataSnapshot.getChildren())
                            msgBundle.add(data.getKey());

                        for (final String key : msgBundle) {
                            myRef.child("ProjectList").child(projectKey).child("todo").child(todoKey).child("chats").child(key).addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            // Get Project value
                                            Chat chat = dataSnapshot.getValue(Chat.class);
                                            messageList.add(chat);
                                            adapter.notifyDataSetChanged();

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    }
                            );
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

    }

    public void onButtonChatClicked(View v) {

        //save it to the firebase db
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();

        message = (EditText)findViewById(R.id.editText_chat);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = user.getUid();

        myRef.child("users").child(uid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        User msgUser = dataSnapshot.getValue(User.class);
                        messageUser = msgUser.username;

                        Chat chat = new Chat(message.getText().toString(), messageUser);
                        String key = myRef.child("ProjectList").child(projectKey).child("todo").child(todoKey).child("chats").push().getKey();
                        myRef.child("ProjectList").child(projectKey).child("todo").child(todoKey).child("chats").child(key).setValue(chat);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );


        finish();
        onResume();

    }

    public void send() {
        myRef.child("ProjectList").child(projectKey).child("todo").child(todoKey).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Todo todo = dataSnapshot.getValue(Todo.class);

                        myRef.child("users").child(todo.getPerson()).addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        User user = dataSnapshot.getValue(User.class);
                                        regid = user.userreg;

                                        JSONObject requestData = new JSONObject();
                                        try {
                                            requestData.put("priority", "high");
                                            JSONObject dataObj = new JSONObject();
                                            dataObj.put("contents", "재촉 알림이 왔습니다.");
                                            requestData.put("data", dataObj);

                                            JSONArray idArray = new JSONArray();
                                            idArray.put(0, regid);
                                            requestData.put("registration_ids", idArray);
                                        } catch (Exception e) {
                                        }

                                        sendData(requestData, new SendResponseListener() {
                                            @Override
                                            public void onRequestCompleted() {
                                                println("호출");
                                            }
                                            @Override
                                            public void onRequestStarted() {
                                                println("호출");
                                            }
                                            @Override
                                            public void onRequestWithError(VolleyError error) {
                                                println("호출");
                                            }
                                        });


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                }
                        );

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }


    public interface SendResponseListener {
        public void onRequestStarted();
        public void onRequestCompleted();
        public void onRequestWithError(VolleyError error);

    }

    public void sendData(JSONObject requestData, final SendResponseListener listener) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "https://fcm.googleapis.com/fcm/send",
                requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onRequestCompleted();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onRequestWithError(error);
                    }
            }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String,String>();

                return params;
            }
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String, String>();
                headers.put("Authorization","key=0000000000000000000000");
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        request.setShouldCache(false);
        listener.onRequestStarted();
        queue.add(request);
    }

}
