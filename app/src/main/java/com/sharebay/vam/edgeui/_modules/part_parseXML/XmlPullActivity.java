package com.sharebay.vam.edgeui._modules.part_parseXML;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Toast;

import com.sharebay.vam.edgeui.R;
import com.sharebay.vam.edgeui.beans.FaultJob;
import com.sharebay.vam.edgeui.commons.recyclerview.CommonAdapterHelper.RecyclerViewCommonAdapter;
import com.sharebay.vam.edgeui.commons.recyclerview.CommonAdapterHelper.ViewHolder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlPullActivity extends AppCompatActivity {

    private static final String TAG = "XmlPullActivity";
    private Context mCtx;
    RecyclerView rvXmlParseResult;
    RecyclerViewCommonAdapter adapter;

    FaultJobsFilter filter ;

    String xmlPath = "FaultJobs.xml";
    private static final String TAG_FAULT_JOB = "FaultJob";
    private static final String TAG_OPERATE_STEPS = "OperateSteps";
    private static final String TAG_OPERATE_STEPS_ITEM = "item";

    List<FaultJob> faultJobs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xml_pull);
        mCtx = this;
        initData();
        Log.e(TAG, "onCreate: "+faultJobs.get(1).getOperateAftSteps().get(1));
        initView();
    }

    private void initView() {
        filter = (FaultJobsFilter) findViewById(R.id.include1);

        rvXmlParseResult = (RecyclerView) findViewById(R.id.rvXmlParseResult);
        rvXmlParseResult.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecyclerViewCommonAdapter<FaultJob>(mCtx,R.layout.job_item_layout,faultJobs) {
            @Override
            public void convert(ViewHolder holder, FaultJob job) {
                holder.setText(R.id.tv_index,holder.getAdapterPosition()+1+"");
                holder.setText(R.id.tv_title,job.getTitle());
                holder.setOnItemClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转到显示作业缺陷的详情页面
                        Toast.makeText(mContext, "1", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        rvXmlParseResult.setAdapter(adapter);

    }

    private void initData() {

        FaultJob faultJob = null;
        List<String> pres = null;
        List<String> afts = null;

        int POS_0 = 0;
        int POS_1 = 1;
        int curPos = -1;

        //直接解析数据
        InputStream inputStream = null;
        XmlPullParser xmlParser = Xml.newPullParser();
        try {
            inputStream = this.getResources().getAssets().open(xmlPath);
            xmlParser.setInput(inputStream,"utf-8");
            String tag = null;
            //获取事件类别：开始文档、开始标签、结束标签等
            int eventType = xmlParser.getEventType();
            while (eventType!=XmlPullParser.END_DOCUMENT){
                tag = xmlParser.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:

                        if (tag.equalsIgnoreCase(TAG_FAULT_JOB)){
                            //实例化
                            faultJob= new FaultJob();
                            Log.e(TAG, "FaultJob->stationType=" + xmlParser.getAttributeValue(null,"stationType")
                                    + ",vol=" + xmlParser.getAttributeValue(null,"vol")
                                    + ",deviceType=" + xmlParser.getAttributeValue(null,"deviceType")
                                    + ",deviceSetDiff=" + xmlParser.getAttributeValue(null,"deviceSetDiff")
                                    + ",title=" + xmlParser.getAttributeValue(null,"title")
                                    + ",desc=" + xmlParser.getAttributeValue(null,"desc")
                            );

                            faultJob.setStationType(xmlParser.getAttributeValue(null,"stationType"));
                            faultJob.setVol(Integer.parseInt(xmlParser.getAttributeValue(null,"vol")));
                            faultJob.setDeviceSetDiff(xmlParser.getAttributeValue(null,"deviceSetDiff"));
                            faultJob.setTitle(xmlParser.getAttributeValue(null,"title"));
                            faultJob.setDesc(xmlParser.getAttributeValue(null,"desc"));
                        }
                        if (tag.equalsIgnoreCase(TAG_OPERATE_STEPS)&& null!=faultJob){
                            Log.e(TAG, "OperateSteps->desc: "+xmlParser.getAttributeValue(null,"desc"));
                            if (xmlParser.getAttributeValue(null,"desc").equalsIgnoreCase("安全措施")){
                                pres = new ArrayList<>();
                                curPos = POS_0;
                            }
                            if (xmlParser.getAttributeValue(null,"desc").equalsIgnoreCase("校验内容")){
                                afts = new ArrayList<>();
                                curPos = POS_1;
                            }
                        }
                        if (tag.equalsIgnoreCase(TAG_OPERATE_STEPS_ITEM)&& null!=faultJob){
                            String text = xmlParser.nextText() ;//看下源码的注释就可以知道 这里除了获取到下个字符串还会执行next()方法
                            Log.e(TAG, "OperateSteps_item->item: "+text);
                            if (curPos == POS_0){
                                pres.add(text);
                            } else if(curPos == POS_1){
                                afts.add(text);
                            }
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        if (tag.equalsIgnoreCase(TAG_FAULT_JOB)){
                            if (faultJob != null){
                                Log.e(TAG, "END_TAG->TAG_FAULT_JOB: "+"gy.Here");
                                faultJobs.add(faultJob);
                                faultJob = null;
                            }

                        }
                        if (tag.equalsIgnoreCase(TAG_OPERATE_STEPS)){
                            Log.e(TAG, "initData: " + ((null!=faultJob)?"非空":"空"));
                            if (faultJob != null){
                                Log.e(TAG, "END_TAG->TAG_OPERATE_STEPS: "+"gy.Here");
                                if (faultJob.getOperatePreSteps()==null){
                                    faultJob.setOperatePreSteps(pres);
                                    pres = null;
                                }
                                if (faultJob.getOperateAftSteps()==null){
                                    faultJob.setOperateAftSteps(afts);
                                    afts = null;
                                }
                            }

                        }
                        break;
                    default:
                        break;
                }
                eventType=xmlParser.next();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        for (FaultJob job: faultJobs){
            Log.e(TAG, "initData: "+job.toString());
        }
    }
}
