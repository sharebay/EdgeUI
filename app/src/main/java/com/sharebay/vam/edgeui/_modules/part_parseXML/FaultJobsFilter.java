package com.sharebay.vam.edgeui._modules.part_parseXML;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.sharebay.vam.edgeui.R;

import java.util.Arrays;

/**
 * Created by RuanJian-GuoYong on 2017/11/10.
 */

public class FaultJobsFilter extends LinearLayout {
    Context mCtx;

    Spinner sp1;
    Spinner sp2;
    Spinner sp3;
    Spinner sp4;
    Spinner sp_operation;



    public FaultJobsFilter(Context context) {
        super(context);
        init(context);
    }

    public FaultJobsFilter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FaultJobsFilter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mCtx = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.jobs_filter,this);

        initData();
        initView();
        bindData();

    }

    private void initData() {
    }

    private void initView() {
        sp1 = findViewById(R.id.sp_stationType);
        sp2 = findViewById(R.id.sp_vol);
        sp3 = findViewById(R.id.sp_deviceType);
        sp4 = findViewById(R.id.sp_deviceSet);
        sp_operation = findViewById(R.id.sp_operation);
    }

    /*
    * 前4个spn的下拉数据都是从数据库中获取到的。
    * 第5个spn是通过上边4个的数据查询出来的FalutJob的title信息，具体的详情需要通过
    * 同时，第5个spinner切换的时候，下边的列表也需要切换显示
    * */
    private void bindData() {
        sp1.setAdapter(getArrayAdapter(R.array.CZLX));
        sp2.setAdapter(getArrayAdapter(R.array.DYDJ));
        sp3.setAdapter(getArrayAdapter(R.array.kinds));
        sp4.setAdapter(getArrayAdapter(R.array.kinds));


        //sp_operation.setAdapter(arrAdpter);
    }

    private ArrayAdapter getArrayAdapter(int resId){
        ArrayAdapter arrAdpter = new ArrayAdapter(
                mCtx,
                R.layout.simple_list_item_1_gy,//R.layout.spinner_text_to_right,//android.R.layout.simple_list_item_1,
                mCtx.getResources().getStringArray(resId));
        arrAdpter.setDropDownViewResource(
                R.layout.simple_spinner_dropdown_item_gy);//android.R.layout.simple_spinner_dropdown_item
        return arrAdpter;
    }
}
