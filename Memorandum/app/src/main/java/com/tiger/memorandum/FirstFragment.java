package com.tiger.memorandum;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class FirstFragment extends Fragment {
    private final String TAG="FirstFragment";
    private View view;
    private TextView[] tx1;

    private TextView[] tx2;
    private TextView[] tx3;
    private MyDataBase myDataBase;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.first_fragment,container,false);
        initView();
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        update();
    }
    public void initView(){
        myDataBase=new MyDataBase(getActivity(),MyDataBase.DB_FILENAME,null,1);
        tx1=new TextView[7];
        tx2=new TextView[7];
        tx3=new TextView[7];
        tx1[0]=(TextView)view.findViewById(R.id.tv11);
        tx1[1]=(TextView)view.findViewById(R.id.tv21);
        tx1[2]=(TextView)view.findViewById(R.id.tv31);
        tx1[3]=(TextView)view.findViewById(R.id.tv41);
        tx1[4]=(TextView)view.findViewById(R.id.tv51);
        tx1[5]=(TextView)view.findViewById(R.id.tv61);
        tx1[6]=(TextView)view.findViewById(R.id.tv71);

        tx2[0]=(TextView)view.findViewById(R.id.tv12);
        tx2[1]=(TextView)view.findViewById(R.id.tv22);
        tx2[2]=(TextView)view.findViewById(R.id.tv32);
        tx2[3]=(TextView)view.findViewById(R.id.tv42);
        tx2[4]=(TextView)view.findViewById(R.id.tv52);
        tx2[5]=(TextView)view.findViewById(R.id.tv62);
        tx2[6]=(TextView)view.findViewById(R.id.tv72);

        tx3[0]=(TextView)view.findViewById(R.id.tv13);
        tx3[1]=(TextView)view.findViewById(R.id.tv23);
        tx3[2]=(TextView)view.findViewById(R.id.tv33);
        tx3[3]=(TextView)view.findViewById(R.id.tv43);
        tx3[4]=(TextView)view.findViewById(R.id.tv53);
        tx3[5]=(TextView)view.findViewById(R.id.tv63);
        tx3[6]=(TextView)view.findViewById(R.id.tv73);

    }
    public void update(){
        ArrayList<Record> records=myDataBase.getRecord();
        long currentTimeMillis=System.currentTimeMillis();
        int t=(int)(currentTimeMillis/1000);
        t=t-t%(3600*24);
        for(int i=0;i<7;i++){
            String am="";
            StringBuffer amSb = new StringBuffer();
            StringBuffer pmSb = new StringBuffer();
            String pm="";
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

            for(Record r:records){
                String curDate = format.format(new Date(r.getTime()));
                if(t<=r.getTime()&&(t+3600*12)>r.getTime()){

                    if (r.getType() == 0)
                    {
                        amSb.append("行程 : "+ r.getText());
                        amSb.append("\n人员:"+r.getPerson());


                    }
                    else
                    {
                        amSb.append("日记 : "+ r.getText());
                    }
                    amSb.append("\n"+curDate);
                    amSb.append("\n");
                }
                if((t+3600*12)<=r.getTime()&&(t+3600*24)>r.getTime()){
                    if (r.getType() == 0)
                    {
                        pmSb.append("行程 : "+ r.getText());
                        pmSb.append("\n人员:"+r.getPerson());
                    }
                    else
                    {
                        pmSb.append("日记 : "+ r.getText());
                    }
                    pmSb.append("\n时间 ： "+curDate);
                    pmSb.append("\n");
                }
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date((long)t*1000);
            tx1[i].setText(simpleDateFormat.format(date));
            tx2[i].setText(amSb.toString());
            tx3[i].setText(pmSb.toString());
            t+=3600*24;
        }
    }
}
