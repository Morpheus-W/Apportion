package com.unbelievable.tangweny.apportion.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.unbelievable.tangweny.apportion.R;
import com.unbelievable.tangweny.apportion.entity.Check;
import com.unbelievable.tangweny.apportion.entity.Participant;

import java.util.List;

/**
 * Created by yintangwen on 16/5/2.
 */
public class ParticipantAdapter extends BaseAdapter {
    private Context context;
    private List<Check> list;
    private LayoutInflater layoutInflater;

    public ParticipantAdapter(Context context, List<Check> list) {
        this.context = context;
        this.list = list;
        this.layoutInflater = LayoutInflater.from(this.context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        Check check = list.get(position);
        if (check.getParId() == -1)// 标题
        {
            convertView = this.layoutInflater.inflate(R.layout.participant_title_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.participant_item = (TextView) convertView.findViewById(R.id.participant_title);
            viewHolder.participant_item.setText(check.getParTime());
            convertView.setTag(viewHolder);
        } else {
            convertView = this.layoutInflater.inflate(R.layout.participant_data_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.participant_item = (TextView) convertView.findViewById(R.id.participant_title);
            viewHolder.participant_item.setText(check.getParTime().substring(11));
            viewHolder.parName = (TextView) convertView.findViewById(R.id.parName_textview);
            viewHolder.parName.setText(check.getParName());
            viewHolder.consume = (TextView) convertView.findViewById(R.id.consume_textview);
            viewHolder.consume.setText(check.getConsume()+"");
            viewHolder.remark = (TextView) convertView.findViewById(R.id.remark_textview);
            viewHolder.remark.setText(check.getRemark());
            convertView.setTag(viewHolder);
        }
        return convertView;
    }
    private class ViewHolder{
        private TextView participant_item;
        private TextView parName;
        private TextView consume;
        private TextView remark;
        public ViewHolder(){

        }
    }

}
