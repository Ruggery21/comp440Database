package com.ruggery.databasedesign;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {

    Context context;
    ArrayList<NoteModel> arrayList;
    public CommentAdapter(Context context, ArrayList<NoteModel> arrayList){
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
        view = inflater.inflate(R.layout.single_blog, null);

        TextView blogSubject = (TextView)view.findViewById(R.id.blogSubject);
        TextView blogDescription = (TextView)view.findViewById(R.id.blogDescription);
        TextView blogTags = (TextView)view.findViewById(R.id.blogTags);

        NoteModel note = arrayList.get(position);

        blogSubject.setText(note.getTitle());
        blogDescription.setText(note.getDes());
        blogTags.setText(note.getTags());

        return view;
    }
}
