package com.tuangh.notepad;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class NoteItemAdapter extends ArrayAdapter<NoteItem> {
    private Activity context;
    private int layout;
    private List<NoteItem> arrNoteList;
    public NoteItemAdapter(Activity context, int layout, List<NoteItem> arrNoteList) {
        super(context,layout,arrNoteList);
        this.context = context;
        this.layout = layout;
        this.arrNoteList = arrNoteList;
    }
    private class ViewHolder{
        TextView txtTitle;
        TextView txtDate;
        TextView txtNoiDung;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(layout, null);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            holder.txtNoiDung=(TextView) convertView.findViewById(R.id.txtNoiDung);
            holder.txtDate=(TextView) convertView.findViewById(R.id.txtDate);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
//        LayoutInflater inflater = context.getLayoutInflater();
//        convertView = inflater.inflate(layout, null);
//        TextView txtTieuDe=(TextView) convertView.findViewById(R.id.txtTitle);
//        TextView txtDate=(TextView) convertView.findViewById(R.id.txtDate);
//        TextView txtNoiDung=(TextView) convertView.findViewById(R.id.txtNoiDung);
        final NoteItem noteItem = arrNoteList.get(position);

        holder.txtNoiDung.setText(noteItem.getContent());
        holder.txtTitle.setText(noteItem.getTitle());
        holder.txtDate.setText(noteItem.getCreatedDate());




        return convertView;
    }

}
