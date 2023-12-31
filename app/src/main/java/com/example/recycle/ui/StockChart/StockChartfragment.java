package com.example.recycle.ui.StockChart;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import com.example.recycle.R;
import com.example.recycle.data.api.MonthServicey;
import com.example.recycle.data.api.MonthStockApi;
import com.example.recycle.data.api.NowServicey;
import com.example.recycle.data.api.NowStockApi;
import com.example.recycle.data.model.DailyStockModel;
import com.example.recycle.data.model.MonthStockModel;
import com.example.recycle.data.model.StockModel;
import com.example.recycle.data.model.stockDataItem;
import com.example.recycle.databinding.StockPlotChartBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockChartfragment extends Fragment {
    private StockPlotChartBinding binding;
    private TextView txtItem;

    private StockModel from_fragment=new StockModel();

    private stockDataItem Now_Data=new stockDataItem();

    private MonthStockModel Month_Data=new MonthStockModel();

    private ConstraintLayout homeRL;
    private ProgressBar loadingPB;
    private LineChart chart_line;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = StockPlotChartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //接收母視窗的股票代碼
        ReceiveStock(root);
        //網路爬蟲獲取資料到Stockmodel
        ReceiveNowData(root);
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd");
        String monthinform=dateFormat.format(date);
        if(monthinform.equals("01")) {
            date.setDate(date.getDate() - 1);
            ReceiveMonthlyData(root, date);
        }
        else{
            ReceiveMonthlyData(root, date);
        }


        //建立畫圖方塊原生
//        RelativeLayout relativeLayout = (RelativeLayout) root.findViewById(R.id.rect);
//        relativeLayout.addView(new StockChartfragment.Rectangle(getActivity()));
        //建立LineChart

//        chart_line = (LineChart)root.findViewById(R.id.chart_line);
//        chart_line.setData(getLineData());
        return root;
    }
    private LineData getLineData(){
        LineDataSet dataSetA = new LineDataSet(getChartData(), "每月歷史價格");

        List<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSetA); // add the datasets

        return new LineData(getLabels(), dataSets);
    }
    private List<Entry> getChartData(){
        final int DATA_COUNT = Month_Data.getData().size();

        List<Entry> chartData = new ArrayList<>();
        float local_close_price;
        for(int i=0;i<DATA_COUNT;i++){
            local_close_price=Float.parseFloat(Month_Data.getData().get(i).get(6));
            chartData.add(new Entry(local_close_price, i));
        }
        return chartData;
    }
    private List<String> getLabels(){
        int DATA_COUNT = Month_Data.getData().size();

        List<String> chartLabels = new ArrayList<>();
        for(int i=0;i<DATA_COUNT;i++){
            chartLabels.add(Month_Data.getData().get(i).get(0));
        }
        return chartLabels;
    }
    private void ChangeView(View root) {
        //更改內容
        txtItem=root.findViewById(R.id.Stockcode);
        txtItem.setText(from_fragment.getCode()+from_fragment.getName());
        txtItem=root.findViewById(R.id.Stockstate);
        txtItem.setText("上市");
        //處理賣出買入價格
        //賣出
        String Sellstring=Now_Data.getA();
        String transfer = "\\d+.\\d+";
        Pattern pattern = Pattern.compile(transfer);
        Matcher matcher = pattern.matcher(Sellstring);
        matcher.find();
        String sellprice=matcher.group();
        //買入
        String Buystring=Now_Data.getB();
        Pattern.compile(transfer);
        matcher = pattern.matcher(Buystring);
        matcher.find();
        String buyprice= matcher.group();
        //增減量,百分比顯示
        //若已經開盤
        String openprice = Now_Data.getZ() != (null) & Now_Data.getZ().length()>1 ? Now_Data.getZ() : Now_Data.getOa() != (null) & Now_Data.getOa() != ""?Now_Data.getOa():sellprice;


        if(openprice.length()>1 && Now_Data.getY().length()>1) {
            txtItem=root.findViewById(R.id.Stocknowprice);
            txtItem.setText("  "+openprice);
            float changevalue = Float.parseFloat(openprice) - Float.parseFloat(Now_Data.getY());
            txtItem.setTextColor(changevalue > 0 ? Color.RED : Color.GREEN);
            txtItem = root.findViewById(R.id.difference_value);
            txtItem.setText(String.format("%.2f", Float.parseFloat(openprice) - Float.parseFloat(Now_Data.getY())));
            txtItem = root.findViewById(R.id.Stockpercent);
            txtItem.setText(String.format("%.2f", (Float.parseFloat(openprice) - Float.parseFloat(Now_Data.getY()))/Float.parseFloat(Now_Data.getY()))+ "%");
        }
        else//未開盤
        {
            txtItem=root.findViewById(R.id.Stocknowprice);
            txtItem.setText("  "+Now_Data.getY());
            txtItem = root.findViewById(R.id.difference_value);
            txtItem.setText(String.format("%.2f", 0.00));
            txtItem = root.findViewById(R.id.Stockpercent);
            txtItem.setText(String.format("%.2f", 0.00) + "%");
        }
        //買入賣出價格
        txtItem = root.findViewById(R.id.Stocksellprice);
        txtItem.setText(sellprice);
        txtItem = root.findViewById(R.id.Stockbuyprice);
        txtItem.setText(buyprice);
        //均盤(先用賣出價格頂)與開盤價格
        txtItem = root.findViewById(R.id.Stockaverageprice);
        txtItem.setText("均盤 "+String.format("%.2f", (Float.parseFloat(sellprice))));
        txtItem = root.findViewById(R.id.Stockopenprice);
        txtItem.setText("開盤 "+openprice);
        txtItem = root.findViewById(R.id.StockAmplitudeprice);
        txtItem.setText("振幅 "+String.format("%.2f", 100*((Float.parseFloat(Now_Data.getH()) - Float.parseFloat(Now_Data.getL()))/Float.parseFloat(openprice)))+ "%");
        txtItem = root.findViewById(R.id.StockyesterDayprice);
        txtItem.setText("昨收 "+Now_Data.getY());
        //單量總量計算
        String single_quantity=Now_Data.getG();
        transfer = "\\d+";
        pattern = Pattern.compile(transfer);
        matcher = pattern.matcher(single_quantity);
        matcher.find();
        String single_quantity_price=matcher.group();
        txtItem=root.findViewById(R.id.Stock_single_quantity_price);
        txtItem.setText("單量 "+single_quantity_price);
        txtItem=root.findViewById(R.id.Stock_total_amount);
        txtItem.setText("總量 "+Now_Data.getV());


//        //解鎖畫面
//        loadingPB=root.findViewById(R.id.idPgBarWait);
//        loadingPB.setVisibility(View.GONE);
//        homeRL=root.findViewById(R.id.idRLHome);
//        homeRL.setVisibility(View.VISIBLE);

    }

    private void ReceiveStock(View root) {
        Bundle args = getArguments();
        ArrayList<String> argList=new ArrayList<>();
        argList=args.getStringArrayList("Code");
        from_fragment.setCode(argList.get(0));
        from_fragment.setName(argList.get(1));
        Log.v("Tag","Pass code is:"+from_fragment.getCode());
    }
    private void ReceiveNowData(View root){
        NowStockApi stockApi = NowServicey.getNowStockApi();
        Log.v("url","tse_"+from_fragment.getCode()+".tw");
        Call<DailyStockModel> responseCall=stockApi.CallNowStock("tse_"+from_fragment.getCode()+".tw");
        responseCall.enqueue(new Callback<DailyStockModel>(){

            @Override
            public void onResponse(Call<DailyStockModel> call, Response<DailyStockModel> response) {
                Now_Data=response.body().getMsgArray().get(0);
//                Now_Data.setZ(response.body().getMsgArray().get(0).getZ());
//                Now_Data.setOa(response.body().getMsgArray().get(0).getOa());//當前盤中成交價
//                Now_Data.setB(response.body().getMsgArray().get(0).getB());//揭示買價(從高到低，以_分隔資料)
//                Now_Data.setG(response.body().getMsgArray().get(0).getG());//揭示買量(配合b，以_分隔資料)
//                Now_Data.setA(response.body().getMsgArray().get(0).getA());//揭示賣價(從低到高，以_分隔資料)
//                Now_Data.setF(response.body().getMsgArray().get(0).getF());//揭示賣量(配合a，以_分隔資料)
//                Now_Data.setY(response.body().getMsgArray().get(0).getY());//昨日收盤價格
                Log.v("Tag",response.body().getRtmessage());
                ChangeView(root);

            }

            @Override
            public void onFailure(Call<DailyStockModel> call, Throwable t) {
                t.printStackTrace();
                Log.e("Tag", "Request failed: " + t.getMessage());
            }
        });

    }
    private void ReceiveMonthlyData(View root,Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String Today = dateFormat.format(date);
        MonthStockApi stockApi= MonthServicey.getNowStockApi();
        Call<MonthStockModel> responseCall=stockApi.CallMonthStock(Today,from_fragment.getCode());

        responseCall.enqueue(new Callback<MonthStockModel>(){

            @Override
            public void onResponse(Call<MonthStockModel> call, Response<MonthStockModel> response) {
                Log.v("Tag",response.body().getStat());
                Month_Data=response.body();
                //設定chart_line縮放
                chart_line = (LineChart)root.findViewById(R.id.chart_line);
                chart_line.setData(getLineData());
                chart_line.setTouchEnabled(true);//設定可以觸摸chart_line
                chart_line.setDragEnabled(true);//設定可以拖移
                chart_line.setScaleEnabled(true);//設定可以縮放

                //解鎖畫面
                loadingPB=root.findViewById(R.id.idPgBarWait);
                loadingPB.setVisibility(View.GONE);
                homeRL=root.findViewById(R.id.idRLHome);
                homeRL.setVisibility(View.VISIBLE);
//                ChangeView(root);

            }

            @Override
            public void onFailure(Call<MonthStockModel> call, Throwable t) {
                t.printStackTrace();
                Log.e("Tag", "Request failed: " + t.getMessage());
            }
        });

    }



    private class Rectangle extends View {
        Paint paint = new Paint();
        Path path = new Path();
        public Rectangle(Context context) {
            super(context);
        }
        @Override
        public void onDraw(Canvas canvas) {
            paint.setColor(Color.BLUE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2f);
            Path path = new Path();
            // 在路径中添加点
            path.moveTo(100, 100);
            path.lineTo(200, 200);
            path.lineTo(300, 150);
            path.lineTo(400, 250);
            Rect rect = new Rect(0, 0, 300, 500);
            canvas.drawPath(path, paint);
//            canvas.drawRect(rect, paint );
            }
        }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }




}
