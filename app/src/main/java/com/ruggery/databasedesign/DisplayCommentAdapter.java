package com.ruggery.databasedesign;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DisplayCommentAdapter extends BaseAdapter {
    Context context;
    ArrayList<NoteModel> arrayList;

    public DisplayCommentAdapter(Context context, ArrayList<NoteModel> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return this.arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.single_comment, null);

        TextView commentSentiment = (TextView)view.findViewById(R.id.commentSentiment);
        TextView commentDescription = (TextView)view.findViewById(R.id.commentDescription);
        TextView commentCreator = (TextView)view.findViewById(R.id.commentCreator);

        NoteModel note = arrayList.get(position);

        commentSentiment.setText(note.getTitle());
        commentDescription.setText(note.getDes());
        commentCreator.setText(note.getTags());

        return view;
    }
}
