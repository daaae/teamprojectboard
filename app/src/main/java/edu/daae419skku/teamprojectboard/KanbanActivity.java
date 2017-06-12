package edu.daae419skku.teamprojectboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

public class KanbanActivity extends AppCompatActivity {
    public String projectKey;
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
    }

    public void onButtonAddClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), AddTodoActivity.class);
        intent.putExtra("project_key", projectKey);
        startActivity(intent);
    }

}
