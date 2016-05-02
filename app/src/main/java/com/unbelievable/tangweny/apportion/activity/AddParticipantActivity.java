package com.unbelievable.tangweny.apportion.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.unbelievable.tangweny.apportion.R;
import com.unbelievable.tangweny.apportion.db.DBHelper;
import com.unbelievable.tangweny.apportion.util.TimeUtil;
import com.unbelievable.tangweny.apportion.util.ToastUtil;

public class AddParticipantActivity extends AppCompatActivity
        implements View.OnClickListener {

    private DBHelper dbHelper ;
    private EditText nameET;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_participant);
        mContext = this;
        ((TextView) findViewById(R.id.title_name)).setText("添加合伙人");
        findViewById(R.id.return_layout).setOnClickListener(this);
        findViewById(R.id.add_button).setOnClickListener(this);
        dbHelper = new DBHelper(this);
        nameET = (EditText)findViewById(R.id.name_edittext);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.return_layout) {
            finish();
        }else if(v.getId() == R.id.add_button){
            /*添加合伙人*/
            String name = nameET.getText().toString().trim();
            if (!name.equals("")){
                /*添加至数据库中*/
                dbHelper.insertParticipant(TimeUtil.getDateTime(System.currentTimeMillis()),name);
                ToastUtil.show(mContext,"添加成功...");
                nameET.setText("");
            }
        }
    }
}
