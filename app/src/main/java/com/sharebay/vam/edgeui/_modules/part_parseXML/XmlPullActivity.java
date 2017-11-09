package com.sharebay.vam.edgeui._modules.part_parseXML;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Xml;

import com.sharebay.vam.edgeui.R;
import com.sharebay.vam.edgeui.beans.FaultJob;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlPullActivity extends AppCompatActivity {

    private static final String TAG = "XmlPullActivity";
    RecyclerView rvXmlParseResult;
    String xmlPath = "FaultJobs.xml";
    private static final String TAG_FAULT_JOB = "FaultJob";
    private static final String TAG_OPERATE_STEPS = "OperateSteps";
    private static final String TAG_OPERATE_STEPS_ITEM = "item";


    List<FaultJob> faultJobs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xml_pull);
        initData();
        initView();
    }

    private void initView() {
        rvXmlParseResult = (RecyclerView) findViewById(R.id.rvXmlParseResult);
        rvXmlParseResult.setLayoutManager(new LinearLayoutManager(this));


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
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        tag = xmlParser.getName();
                        if (tag.equalsIgnoreCase(TAG_FAULT_JOB)){
                            //实例化
                            faultJob= new FaultJob();
                            Log.e(TAG, "FaultJob->stationType=" + xmlParser.getAttributeValue(null,"stationType")
                                    + ",vol=" + xmlParser.getAttributeValue(null,"vol")
                                    + ",deviceType=" + xmlParser.getAttributeValue(null,"deviceType")
                                    + ",deviceSetDiff=" + xmlParser.getAttributeValue(null,"deviceSetDiff")
                                    + ",titel=" + xmlParser.getAttributeValue(null,"titel")
                                    + ",desc=" + xmlParser.getAttributeValue(null,"desc")
                            );

                            faultJob.setStationType(xmlParser.getAttributeValue(null,"stationType"));
                            faultJob.setVol(Integer.parseInt(xmlParser.getAttributeValue(null,"vol")));
                            faultJob.setDeviceSetDiff(xmlParser.getAttributeValue(null,"deviceSetDiff"));
                            faultJob.setTitle(xmlParser.getAttributeValue(null,"titel"));
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
                            String text = xmlParser.nextText();//看下源码的注释就可以知道 这里除了获取到下个字符串还会执行next()方法
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
                        if (tag.equalsIgnoreCase(TAG_OPERATE_STEPS)&& null!=faultJob){
                            if (faultJob != null){
                                Log.e(TAG, "END_TAG->TAG_OPERATE_STEPS: "+"gy.Here");
                                faultJob.setOperateAftSteps(pres);
                                faultJob.setOperateAftSteps(afts);
                                pres = null;
                                afts = null;
                            }

                        }
                        break;
                    default:
                        break;
                }
                eventType=xmlParser.next();
            }
            //Log.e(TAG, "initData: "+faultJobs.get(0).toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

    }
}