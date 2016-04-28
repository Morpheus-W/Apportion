package com.unbelievable.tangweny.apportion.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.unbelievable.tangweny.apportion.R;

public class MainActivity extends AppCompatActivity
implements View.OnClickListener{

    private Context mContext;
    private LinearLayout participantLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        findViewById(R.id.title_add).setOnClickListener(this);
        findViewById(R.id.select_paticipant_imageview).setOnClickListener(this);
        participantLayout = (LinearLayout)findViewById(R.id.participant_layout);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.title_add){
            Intent intent = new Intent(mContext,AddParticipantActivity.class);
            startActivity(intent);
        }else if (id == R.id.participant_layout){
            /*动态添加合伙人*/
            View view = getLayoutInflater().inflate(R.layout.participant_layout,null);
            participantLayout.addView(view);
        }
    }
}
