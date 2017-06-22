package edu.daae419skku.teamprojectboard;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by daae0 on 2017-06-23.
 */

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.MyViewHolder> {

    private List<Chat> chatList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView msgUser, msgText, msgTime;
        private ItemClickListener clickListener;

        public MyViewHolder(View view) {
            super(view);
            msgUser = (TextView) view.findViewById(R.id.msgUser);
            msgText = (TextView) view.findViewById(R.id.msgText);
            msgTime = (TextView) view.findViewById(R.id.msgTime);

        }

    }

    public chatAdapter(List<Chat> chatList) {
        this.chatList = chatList;
    }

    @Override
    public chatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.messages, parent, false);

        return new chatAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(chatAdapter.MyViewHolder holder, int position) {
        final Chat chat = chatList.get(position);
        holder.msgUser.setText(chat.getMessageUser());
        holder.msgText.setText(chat.getMessageText());
        holder.msgTime.setText(chat.getMessageTime());

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

}
