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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.recycle.R;
import com.example.recycle.databinding.FragmentAutochsBinding;
import com.example.recycle.databinding.StockPlotChartBinding;
import com.example.recycle.ui.AutoChs.AutoChsFragment;

public class StockChartfragment extends Fragment {
    private StockPlotChartBinding binding;
    private TextView txtItem;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = StockPlotChartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //接收母視窗的股票代碼
        ReceiveStock(root);
        //網路爬蟲獲取資料到Stockmodel

        //建立畫圖方塊
        RelativeLayout relativeLayout = (RelativeLayout) root.findViewById(R.id.rect);
        relativeLayout.addView(new StockChartfragment.Rectangle(getActivity()));
        return root;
    }
    private void ReceiveStock(View root) {
        Bundle args = getArguments();
        String code=args.getString("Code");
        Log.v("Tag","Pass code is:"+code);
        txtItem=root.findViewById(R.id.Stockcode);
        txtItem.setText(code);
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
