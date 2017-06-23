package edu.daae419skku.teamprojectboard;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by daae0 on 2017-06-22.
 */

public class TodosAdapter0 extends RecyclerView.Adapter<TodosAdapter0.MyViewHolder> {


    private List<Todo> todoList;
    private DatabaseReference myRef;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title, date, leader;
        public ImageButton btn_delete;
        public Button btn_next;
        private ItemClickListener clickListener;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            date = (TextView) view.findViewById(R.id.date);
            leader = (TextView) view.findViewById(R.id.leader);

            btn_delete = (ImageButton) view.findViewById(R.id.imageButton_bin);
            btn_next = (Button) view.findViewById(R.id.button_next);


            view.setOnClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }
        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition(), false);
        }



    }

    public TodosAdapter0(List<Todo> todoList) {
        this.todoList = todoList;
    }

    @Override
    public TodosAdapter0.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.kanban0_list_row, parent, false);

        return new TodosAdapter0.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TodosAdapter0.MyViewHolder holder, final int position) {
        final Todo todo = todoList.get(position);
        myRef = FirebaseDatabase.getInstance().getReference();

        myRef.child("users").child(todo.getPerson()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        User user = dataSnapshot.getValue(User.class);
                        String name = user.username;
                        holder.leader.setText(name);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        holder.title.setText(todo.getTodoName());
        holder.date.setText(todo.getDate());


        final String projectKey = todo.getProjectKey();
        final String todoKey = todo.getKey();

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(view.getContext(), ShowTodoDetail.class);
                intent.putExtra("todo_key", todo.getKey());
                intent.putExtra("project_key", projectKey);
                view.getContext().startActivity(intent);
            }
        });


        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myRef.child("ProjectList").child(projectKey).child("todo").child(todoKey).removeValue();
                todoList.remove(position);
                notifyItemRemoved(position);
                //this line below gives you the animation and also updates the
                //list items after the deleted item
                notifyItemRangeChanged(position, getItemCount());
            }
        });

        holder.btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("ProjectList").child(projectKey).child("todo").child(todoKey).child("state").setValue(1);
                todoList.remove(position);
                notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

}