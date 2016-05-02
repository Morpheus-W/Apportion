package com.unbelievable.tangweny.apportion.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.unbelievable.tangweny.apportion.R;
import com.unbelievable.tangweny.apportion.activity.adapter.ParticipantAdapter;
import com.unbelievable.tangweny.apportion.db.DBHelper;
import com.unbelievable.tangweny.apportion.entity.Check;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;


public class StatisticsActivity extends AppCompatActivity implements View.OnClickListener {

    private DBHelper dbHelper;
    private List<Check> checks;
    private ListView detailListView;
    /*排序后的集合*/
    private List<Check> newChecks = new ArrayList<>();
    private ParticipantAdapter adapter;
    public static void startActivity(Context context){
        Intent intent = new Intent(context,StatisticsActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        ((TextView) findViewById(R.id.title_name)).setText("清单");
        findViewById(R.id.return_layout).setOnClickListener(this);

        dbHelper = new DBHelper(this);
        checks = dbHelper.queryChecks();
        /*获取排序索引数组*/
        String[] indexs = sortIndex(checks);
        sortList(indexs);
        detailListView = (ListView)findViewById(R.id.detail_listview);
        initList();
    }
    private void initList(){
        adapter = new ParticipantAdapter(this,newChecks);
        detailListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        /*返回至首页*/
        if (v.getId() == R.id.return_layout){
            finish();
        }
    }

    /**
     * 把数据排序，并把A-Z顺序加进去
     *
     * @param
     * @return
     */
    public String[] sortIndex(List<Check> checks) {
        TreeSet<String> set = new TreeSet<>();
        for (Check check : checks) {
            String ch = check.getParTime().substring(0,10);
            set.add(ch);// 获取年月日字符串
        }
        String[] names = new String[set.size()];// 新数组，用于保存首字母和车辆类型
        int i = 0;
        for (String string : set) { // 把set中的字母添加到新数组中（前面）
            names[i] = string;
            i++;
        }

        // 自动按照首字母排序
        Arrays.sort(names, String.CASE_INSENSITIVE_ORDER);// 严格按照字母顺序排序，忽略字母大小写，结果为按拼音排序的数组返回
        return names;
    }
    /**
     * 通过排序索引，获取新的列表数据
     *
     * @param
     * @return
     * */
    public void sortList(String[] names) {
        for (int i = 0; i < names.length; i++) {
            Check checkIndex = new Check();
            checkIndex.setParTime(names[i]);
            checkIndex.setParId(-1);
            newChecks.add(checkIndex);
            for (int j = 0; j < checks.size(); j++) {
                if (checks.get(j).getParTime().contains(names[i])) {

                    Check check = checks.get(j);
                    newChecks.add(check);
                }
            }
        }
    }

}
