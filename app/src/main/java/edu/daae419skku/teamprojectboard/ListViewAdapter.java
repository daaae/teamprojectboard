package edu.daae419skku.teamprojectboard;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<MemberNames> memberNamesList = null;
    private ArrayList<MemberNames> arraylist;

    public ListViewAdapter(Context context, List<MemberNames> memberNamesList) {
        mContext = context;
        this.memberNamesList = memberNamesList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<MemberNames>();
        this.arraylist.addAll(memberNamesList);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return memberNamesList.size();
    }

    @Override
    public MemberNames getItem(int position) {
        return memberNamesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(memberNamesList.get(position).getMemberName()+"/"+memberNamesList.get(position).getMemberNum());
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        memberNamesList.clear();
        if (charText.length() == 0) {
            memberNamesList.addAll(arraylist);
        } else {
            for (MemberNames wp : arraylist) {
                if (wp.getMemberName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    memberNamesList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}