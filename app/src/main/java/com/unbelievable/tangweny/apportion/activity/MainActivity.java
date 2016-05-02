package com.unbelievable.tangweny.apportion.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unbelievable.tangweny.apportion.R;
import com.unbelievable.tangweny.apportion.db.DBHelper;
import com.unbelievable.tangweny.apportion.entity.Check;
import com.unbelievable.tangweny.apportion.entity.Participant;
import com.unbelievable.tangweny.apportion.view.FixGridLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Context mContext;
    private DrawerLayout drawerLayout;
    private RelativeLayout drawerContent;
    private FixGridLayout participantLayout;
    private LinearLayout check_layout;
    private DBHelper dbHelper;
    private List<Participant> pars = new ArrayList<>();
    private List<Check> checks = new ArrayList<>();
    private boolean enableParticipant = true;
    private boolean enableCheck = true;
    /*存储check与edit的map*/
    private Map<String, EditText> map = new HashMap<>();
    /*参与者名称*/
    private List<String> parNames = new ArrayList<>();
    private List<String> checkNames = new ArrayList<>();
    /*备注内容*/
    private EditText remarkEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerContent  = (RelativeLayout)findViewById(R.id.drawer_content);
        findViewById(R.id.title_add).setOnClickListener(this);
        findViewById(R.id.select_paticipant_imageview).setOnClickListener(this);
        findViewById(R.id.select_check_imageview).setOnClickListener(this);
        findViewById(R.id.submit_button).setOnClickListener(this);
        /*触发清单视图*/
        findViewById(R.id.history_layout).setOnClickListener(this);
        participantLayout = (FixGridLayout) findViewById(R.id.participant_layout);
        check_layout = (LinearLayout) findViewById(R.id.check_layout);
        remarkEditText = (EditText)findViewById(R.id.remark_edittext);
        dbHelper = new DBHelper(mContext);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pars = dbHelper.queryParticipants();
        clearCheckBox();
    }
    private void clearCheckBox(){
        participantLayout.removeAllViews();
        check_layout.removeAllViews();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.title_menu) {
            drawerLayout.openDrawer(drawerContent);
        }else if (id == R.id.title_add) {
            Intent intent = new Intent(mContext, AddParticipantActivity.class);
            startActivity(intent);
        } else if (id == R.id.select_paticipant_imageview) {
            if (enableParticipant) {
                /*添加前清空数据*/
//                list.clear();
                for (Participant par : pars) {
                    /*动态添加合伙人*/
                    View view = getLayoutInflater().inflate(R.layout.participant_layout, null);
                    CheckBox checkBox = (CheckBox) view.findViewById(R.id.single_checkbox);
                    checkBox.setText(par.getParName());
                    checkBox.setOnCheckedChangeListener(this);
                    participantLayout.addView(view);
                }
                enableParticipant = false;
            } else {
                participantLayout.removeAllViews();
                enableParticipant = true;
            }
        } else if (id == R.id.select_check_imageview) {
            if (enableCheck) {
                /*添加前清空数据*/
                map.clear();
                for (Participant par : pars) {
                    /*动态添加合伙人*/
                    View view = getLayoutInflater().inflate(R.layout.check_layout, null);
                    CheckBox checkBox = (CheckBox) view.findViewById(R.id.single_checkbox);
                    checkBox.setText(par.getParName());
                    checkBox.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
                    EditText editText = (EditText) view.findViewById(R.id.single_edittext);
                    map.put(par.getParName(), editText);
                    check_layout.addView(view);
                }
                enableCheck = false;
            } else {
                check_layout.removeAllViews();
                enableCheck = true;
            }
        } else if (id == R.id.submit_button) {
            /*获取备注信息*/
            String remark = remarkEditText.getText().toString().trim();
            /*总消费*/
            double total = 0;
            for (String checkName : checkNames){
                if (map.containsKey(checkName)){
                    EditText editText = map.get(checkName);
                    double itemConsume = Double.parseDouble(editText.getText().toString());
                    total += itemConsume;
                }
            }
            /*平均数*/
            double avg = (double)(Math.round((total / parNames.size())*10))/10;
            for (Participant par : pars){
                Check check = new Check();
                String parName = par.getParName();
                check.setParName(parName);
                if (checkNames.contains(parName)){
                    EditText editText = map.get(parName);
                    String consumeStr = editText.getText().toString().trim();
                    if (!consumeStr.equals("")){
                        double itemConsume = Double.parseDouble(consumeStr);
                        /*如果买单人参与了活动*/
                        if (parNames.contains(parName)){
                        /*买单金额-平均*/
                            check.setConsume(itemConsume - avg);
                        }else{
                            check.setConsume(itemConsume);
                        }
                    }
                }else if (parNames.contains(parName)){
                    check.setConsume(-avg);
                }
                check.setIsCheck(1);
                check.setRemark(remark);
                checks.add(check);
            }
            /*存储至数据库*/
            dbHelper.bulkInsert(checks);
            /*提交完成后对集合清空*/
            parNames.clear();
            checkNames.clear();
            checks.clear();
            /*清空多选项*/
            clearCheckBox();
            /*跳转至统计页*/
            StatisticsActivity.startActivity(mContext);
        }else if (id == R.id.history_layout){
            StatisticsActivity.startActivity(mContext);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        /*这里将buttonView转换成checkbox是redundant（多余的，累赘的）*/
        String parName = buttonView.getText().toString();
        /*获取参与者人数*/
        if (isChecked && !parNames.contains(parName)) {
            parNames.add(parName);
        } else {
            if (parNames.contains(parName)) {
                parNames.remove(parName);
            }
        }

    }

    private class MyOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            String parName = buttonView.getText().toString();
            /*获取参与者消费，考虑买单人不参与活动情况*/
            if (isChecked && !checkNames.contains(parName)) {
                checkNames.add(parName);
            } else {
                if (checkNames.contains(parName)) {
                    checkNames.remove(parName);
                }
            }
        }
    }


}
