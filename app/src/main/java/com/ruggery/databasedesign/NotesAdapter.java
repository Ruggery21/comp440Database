package com.ruggery.databasedesign;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.viewHolder> {

    Context context;
    Activity activity;
    ArrayList<NoteModel> arrayList;
    MultiDBHelper multiDBHelper;
    private SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy");
    private String currentDate = sdf.format(new Date());

    String currentUser;

    public NotesAdapter(Context context,Activity activity, ArrayList<NoteModel> arrayList, String currentUser) {
        this.context = context;
        this.activity  = activity ;
        this.arrayList = arrayList;

        this.currentUser = currentUser;

    }

    @Override
    public NotesAdapter.viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_list, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NotesAdapter.viewHolder holder, final int position) {
        holder.title.setText(arrayList.get(position).getTitle());
        holder.description.setText(arrayList.get(position).getDes());
        holder.tags.setText(arrayList.get(position).getTags());
        multiDBHelper = new MultiDBHelper(context);

        holder.delete.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                int id_pos = position + 1;
                String id = String.valueOf(id_pos); //String.valueOf(pos); //valueOf(id_pos)
                String name = multiDBHelper.getUsername(id);
                if(currentUser.equals(name)) {
                    //deleting note
                    multiDBHelper.delete(arrayList.get(position).getID());
                    //deleting tag
                    multiDBHelper.deleteTags(arrayList.get(position).getID());
                    arrayList.remove(position);
                    notifyDataSetChanged();
                }
                else{
                    Toast.makeText(context, "Current User does not match Creator of post.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "Name from CurrentUser = " + currentUser, Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "Name from Database = " + name, Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                //display edit dialog
                showDialog(position);
            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCommentDialog(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView title, description, tags;
        ImageView delete, edit, comment;
        public viewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            tags = (TextView) itemView.findViewById(R.id.tags);
            delete = (ImageView) itemView.findViewById(R.id.delete);
            edit = (ImageView) itemView.findViewById(R.id.edit);

            comment = (ImageView) itemView.findViewById(R.id.comment);

        }
    }

    public void showDialog(final int pos) {
        final EditText title, des, tags;
        Button submit;
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.dialog);
        params.copyFrom(dialog.getWindow().getAttributes());
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        title = (EditText) dialog.findViewById(R.id.title);
        des = (EditText) dialog.findViewById(R.id.description);
        tags = (EditText) dialog.findViewById(R.id.tags);
        submit = (Button) dialog.findViewById(R.id.submit);

        title.setText(arrayList.get(pos).getTitle());
        des.setText(arrayList.get(pos).getDes());
        tags.setText(arrayList.get(pos).getTags());

        submit.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                int id_pos = pos + 1;
                String id = String.valueOf(id_pos); //String.valueOf(pos); //valueOf(id_pos)
                String name = multiDBHelper.getUsername(id);
                if(currentUser.equals(name)) {
                    if (title.getText().toString().isEmpty()) {
                        title.setError("Please Enter Title");
                    } else if (des.getText().toString().isEmpty()) {
                        des.setError("Please Enter Description");
                    } else if (tags.getText().toString().isEmpty()) {
                        tags.setError("Please Enter Tag(s)");
                    } else {
                        //updating note
                        multiDBHelper.updateNote(title.getText().toString(), des.getText().toString(), currentDate, arrayList.get(pos).getID());
                        multiDBHelper.updateTags(tags.getText().toString(), arrayList.get(pos).getID());
                        arrayList.get(pos).setTitle(title.getText().toString());
                        arrayList.get(pos).setDes(des.getText().toString());
                        arrayList.get(pos).setTags(tags.getText().toString());
                        dialog.cancel();
                        //notify list
                        notifyDataSetChanged();
                    }
                }
                else{
                    Toast.makeText(context, "Current User does not match Creator of post.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "Name from CurrentUser = " + currentUser, Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "Name from Database = " + name, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    public void showCommentDialog(final int pos) {
        final EditText commentDescription;
        final Spinner sentiment;
        Button commentSubmit;
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.comment_layout);
        params.copyFrom(dialog.getWindow().getAttributes());
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        commentDescription = (EditText) dialog.findViewById(R.id.commentdDescription);
        commentSubmit = (Button) dialog.findViewById(R.id.commentSubmit);
        sentiment = (Spinner) dialog.findViewById(R.id.sentiment);

        commentSubmit.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                String senti = sentiment.getSelectedItem().toString();
                int id_pos = pos + 1;
                String id = String.valueOf(id_pos); //String.valueOf(pos); //valueOf(id_pos)
                String name = multiDBHelper.getUsername(id);
                if(!currentUser.equals(name)) {
                    if (commentDescription.getText().toString().isEmpty()) {
                        commentDescription.setError("Please Enter Description");
                    }
                    else {
                        //updating note
                        multiDBHelper.addComment(senti, commentDescription.getText().toString(), currentDate, id, currentUser);
                        dialog.cancel();
                        //notify list
                        notifyDataSetChanged();
                    }
                }
                else{
                    Toast.makeText(context, "Current User cannot post self comment", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
