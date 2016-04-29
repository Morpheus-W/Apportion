package com.unbelievable.tangweny.apportion.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unbelievable.tangweny.apportion.R;
import com.unbelievable.tangweny.apportion.db.DBHelper;
import com.unbelievable.tangweny.apportion.entity.Participant;
import com.unbelievable.tangweny.apportion.view.FixGridLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    private Context mContext;
    private FixGridLayout participantLayout;
    private LinearLayout check_layout;
    private DBHelper dbHelper;
    private List<Participant> pars = new ArrayList<>();
    private boolean enableParticipant = true;
    private boolean enableCheck = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        findViewById(R.id.title_add).setOnClickListener(this);
        findViewById(R.id.select_paticipant_imageview).setOnClickListener(this);
        findViewById(R.id.select_check_imageview).setOnClickListener(this);
        participantLayout = (FixGridLayout) findViewById(R.id.participant_layout);
//        participantLayout.setmCellHeight(30);
//        participantLayout.setmCellWidth(100);
        check_layout = (LinearLayout) findViewById(R.id.check_layout);
        dbHelper = new DBHelper(mContext);

    }

    private void initParticipants() {
        String sql = "select * from " + DBHelper.PARTICIPANT;
        Cursor cursor = dbHelper.query(sql, null);
        if (cursor.moveToNext()) {
            int count = cursor.getCount();
            if (count == 0)
                return;
            for (int i = 0; i < count; i++) {
                cursor.moveToPosition(i);
                int parId = cursor.getInt(0);
                String parName = cursor.getString(1);
                String parTime = cursor.getString(2);
                double consume = cursor.getDouble(3);
                int isCheck = cursor.getInt(4);
                Participant par = new Participant();
                par.setParId(parId);
                par.setParTime(parTime);
                par.setParName(parName);
                par.setConsume(consume);
                par.setIsCheck(isCheck);
                pars.add(par);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initParticipants();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.title_add) {
            Intent intent = new Intent(mContext, AddParticipantActivity.class);
            startActivity(intent);
        } else if (id == R.id.select_paticipant_imageview) {
            if (enableParticipant) {
                for (Participant par : pars) {
                /*动态添加合伙人*/
                    View view = getLayoutInflater().inflate(R.layout.participant_layout, null);
                    TextView parTV = (TextView) view.findViewById(R.id.single_textview);
                    parTV.setText(par.getParName());
                    participantLayout.addView(view);
                }
                enableParticipant = false;
            }else{
                participantLayout.removeAllViews();
                enableParticipant = true;
            }
        } else if (id == R.id.select_check_imageview) {
            if (enableCheck) {
                for (Participant par : pars) {
                /*动态添加合伙人*/
                    View view = getLayoutInflater().inflate(R.layout.check_layout, null);
                    TextView parTV = (TextView) view.findViewById(R.id.single_textview);
                    parTV.setText(par.getParName());
                    check_layout.addView(view);
                }
                enableCheck = false;
            }else{
                check_layout.removeAllViews();
                enableCheck = true;
            }
        }
    }
}
