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
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    private void bindData() {
        ArrayAdapter arrAdpter = new ArrayAdapter(
                mCtx,
                android.R.layout.simple_list_item_1,//R.layout.spinner_text_to_right,//android.R.layout.simple_list_item_1,
                mCtx.getResources().getStringArray(R.array.kinds));
        arrAdpter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(arrAdpter);
        sp2.setAdapter(arrAdpter);
        sp3.setAdapter(arrAdpter);
        sp4.setAdapter(arrAdpter);
        sp_operation.setAdapter(arrAdpter);
    }
}
