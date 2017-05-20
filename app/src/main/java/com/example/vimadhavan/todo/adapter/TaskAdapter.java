package com.example.vimadhavan.todo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vimadhavan.todo.R;
import com.example.vimadhavan.todo.model.Task;

import java.util.ArrayList;

/**
 * Created by vimadhavan on 5/18/2017.
 */

public class TaskAdapter extends BaseAdapter {

    private Context _c;
    private ArrayList<Task> _data;

    public TaskAdapter(Context _c, ArrayList<Task> _data) {

        this._data = _data;
        this._c = _c;
    }

    @Override
    public int getCount() {
        return _data.size();
    }

    @Override
    public Object getItem(int position) {
        return _data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.task_item, parent,false);

            holder = new ViewHolder();

            holder.title = (TextView) convertView.findViewById(R.id.taskTitle);
            holder.desx = (TextView) convertView.findViewById(R.id.taskDesc);
            holder.date = (TextView) convertView.findViewById(R.id.targetDateTxt);
            holder.statusImg= (ImageView) convertView.findViewById(R.id.statusImg);

           // Log.d("DEbug:Vignesh IF",holder.title.getText().toString());

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }

       // Log.d("DEbug:Vignesh",_data.get(position).getTitle());

        Task task = (Task) _data.get(position);
        holder.title.setText(task.getTitle());
        holder.desx.setText(task.getDescription());
        holder.date.setText(task.getTargetDate());

        if (task.getStatus()!=0){
            holder.statusImg.setImageResource(R.drawable.thumb_up);
        }else{
            holder.statusImg.setImageResource(R.drawable.hands_up);
        }

        // Set image if exists

        return convertView;
    }

    static class ViewHolder {
        ImageView statusImg;
        TextView title, desx,date;

    }
}
