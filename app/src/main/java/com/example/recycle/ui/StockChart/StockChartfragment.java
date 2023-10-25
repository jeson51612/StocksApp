package com.example.recycle.ui.StockChart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.RelativeLayout;
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
import com.example.recycle.databinding.FragmentAutochsBinding;
import com.example.recycle.databinding.StockPlotChartBinding;
import com.example.recycle.ui.AutoChs.AutoChsFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockChartfragment extends Fragment {
    private StockPlotChartBinding binding;
    private TextView txtItem;
    private String Code;

    private StockModel from_fragment=new StockModel();

    private stockDataItem Now_Data=new stockDataItem();

    private ConstraintLayout homeRL;
    private ProgressBar loadingPB;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = StockPlotChartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //接收母視窗的股票代碼
        ReceiveStock(root);
        //網路爬蟲獲取資料到Stockmodel
        ReceiveNowData(root);
        ReceiveMonthlyData(root);
        //建立畫圖方塊
        RelativeLayout relativeLayout = (RelativeLayout) root.findViewById(R.id.rect);
        relativeLayout.addView(new StockChartfragment.Rectangle(getActivity()));
        return root;
    }

    private void ChangeView(View root) {
        txtItem=root.findViewById(R.id.Stockcode);
        txtItem.setText(from_fragment.getCode()+from_fragment.getName());
        txtItem=root.findViewById(R.id.Stocknowprice);
        txtItem.setText(Now_Data.getZ());
        homeRL=root.findViewById(R.id.idRLHome);
        homeRL.setVisibility(View.VISIBLE);
        loadingPB=root.findViewById(R.id.idPgBarWait);
        loadingPB.setVisibility(View.GONE);

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
                Now_Data.setZ(response.body().getMsgArray().get(0).getZ());//當前盤中成交價
                Now_Data.setB(response.body().getMsgArray().get(0).getB());//揭示買價(從高到低，以_分隔資料)
                Now_Data.setG(response.body().getMsgArray().get(0).getG());//揭示買量(配合b，以_分隔資料)
                Now_Data.setA(response.body().getMsgArray().get(0).getA());//揭示賣價(從低到高，以_分隔資料)
                Now_Data.setF(response.body().getMsgArray().get(0).getF());//揭示賣量(配合a，以_分隔資料)
                Now_Data.setY(response.body().getMsgArray().get(0).getY());//昨日收盤價格
                ChangeView(root);
                Log.v("Tag",response.body().getRtmessage());

            }

            @Override
            public void onFailure(Call<DailyStockModel> call, Throwable t) {
                t.printStackTrace();
                Log.e("Tag", "Request failed: " + t.getMessage());
            }
        });

    }
    private void ReceiveMonthlyData(View root){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String Today = dateFormat.format(date);
        MonthStockApi stockApi= MonthServicey.getNowStockApi();
        Call<MonthStockModel> responseCall=stockApi.CallMonthStock(Today,from_fragment.getCode());

        responseCall.enqueue(new Callback<MonthStockModel>(){

            @Override
            public void onResponse(Call<MonthStockModel> call, Response<MonthStockModel> response) {
                Log.v("Tag",response.body().getStat());

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
