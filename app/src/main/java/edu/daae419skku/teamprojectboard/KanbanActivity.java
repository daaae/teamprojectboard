package edu.daae419skku.teamprojectboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class KanbanActivity extends AppCompatActivity {
    public String projectKey;
    TodosAdapter adapter0, adapter1, adapter2;
    private RecyclerView recyclerView0, recyclerView1, recyclerView2;
    private List<Todo> todoList0 = new ArrayList<>();
    private List<Todo> todoList1 = new ArrayList<>();
    private List<Todo> todoList2 = new ArrayList<>();
    private List<String> mytodos = new ArrayList<>();
    private DatabaseReference myRef;
    private ArrayList<String> memberlist, members;
    private String[] arr;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        projectKey = intent.getStringExtra("project_key");
        setContentView(R.layout.activity_kanban);

        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("시작 전");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("진행 중");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.tab3);
        spec.setIndicator("완료함");
        host.addTab(spec);

        recyclerView0 = (RecyclerView)findViewById(R.id.recycler_kanban0);
        recyclerView1 = (RecyclerView)findViewById(R.id.recycler_kanban1);
        recyclerView2 = (RecyclerView)findViewById(R.id.recycler_kanban2);

        adapter0 = new TodosAdapter(todoList0);
        adapter1 = new TodosAdapter(todoList1);
        adapter2 = new TodosAdapter(todoList2);

        RecyclerView.LayoutManager mLayoutManager0 = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getApplicationContext());

        recyclerView0.setLayoutManager(mLayoutManager0);
        recyclerView0.setItemAnimator(new DefaultItemAnimator());
        recyclerView0.setAdapter(adapter0);

        recyclerView1.setLayoutManager(mLayoutManager1);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setAdapter(adapter1);

        recyclerView2.setLayoutManager(mLayoutManager2);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(adapter2);

        myRef = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        members = new ArrayList<>();
        memberlist = new ArrayList<>();
        memberlist.add("담당자");

        myRef.child("ProjectList").child(projectKey).child("members").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user projects value
                        members.clear();

                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            members.add(data.getKey());
                        }
                        for (final String key : members) {
                            myRef.child("users").child(key).addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            // Get Project value

                                            User user = dataSnapshot.getValue(User.class);
                                            memberlist.add(user.username);
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

        arr = memberlist.toArray(new String[memberlist.size()]);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myRef.child("ProjectList").child(projectKey).child("todo").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user projects value
                        mytodos.clear();
                        todoList0.clear();
                        todoList1.clear();
                        todoList2.clear();
                        for (DataSnapshot data : dataSnapshot.getChildren())
                            mytodos.add(data.getKey());

                        for (final String key : mytodos) {
                            myRef.child("ProjectList").child(projectKey).child("todo").child(key).addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            // Get Project value
                                            Todo todo = dataSnapshot.getValue(Todo.class);
                                            if (todo.getState() == 0)
                                                todoList0.add(todo);
                                            else if (todo.getState() == 1)
                                                todoList1.add(todo);
                                            else
                                                todoList2.add(todo);

                                            adapter0.notifyDataSetChanged();
                                            adapter1.notifyDataSetChanged();
                                            adapter2.notifyDataSetChanged();
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

    public void onButtonAddClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), AddTodoActivity.class);
        intent.putExtra("project_key", projectKey);
        intent.putStringArrayListExtra("memberL", memberlist);
        intent.putStringArrayListExtra("memberUid", members);

        startActivity(intent);
    }

}
