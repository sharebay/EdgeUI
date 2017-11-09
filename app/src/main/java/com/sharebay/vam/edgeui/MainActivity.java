package com.sharebay.vam.edgeui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sharebay.vam.edgeui._modules.part_parseXML.XmlPullActivity;
import com.sharebay.vam.edgeui._modules.part_recyclerview.DroppableRecyclerActivity;
import com.sharebay.vam.edgeui.commons.listview.CommonAdapterHelper.ListViewCommonAdapter;
import com.sharebay.vam.edgeui.commons.listview.CommonAdapterHelper.ViewHolder;
import com.sharebay.vam.edgeui.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
* 主界面，显示所有的items
* */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ListView listView = null;
    List<MainItem> datas = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intiData();
        initView();
    }

    private void intiData() {
        datas = new ArrayList<>();
        List<String> items = Arrays.asList(this.getResources().getStringArray(R.array.mainItems));
        for (String str : items){
            String[] title_desc = str.split("\\$\\$");
            MainItem mainItem = new MainItem();
            mainItem.title = title_desc[0];
            mainItem.desc = title_desc[1];
            datas.add(mainItem);
        }
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.listSamples);
        listView.setOnItemClickListener(this);
        ListViewCommonAdapter listAdapter = new ListViewCommonAdapter<MainItem>(this,datas,R.layout.sample_item) {
            @Override
            public void convert(ViewHolder viewHolder, MainItem itemBean) {
                viewHolder.setText(R.id.tvIndex, viewHolder.getPosition()+1+"");
                viewHolder.setText(R.id.tvName, itemBean.title);
                viewHolder.setText(R.id.tvDesc, itemBean.desc);
            }
        };
        listView.setAdapter(listAdapter);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i){
            case 0 :
                Utils.startActivity(this, DroppableRecyclerActivity.class);
                break;
            case 1:
                Utils.showToast(this,"无功能");
                break;
            case 2:
                Utils.startActivity(this, XmlPullActivity.class);
        }
    }


    class MainItem{
        String title;
        String desc;
    }
}
