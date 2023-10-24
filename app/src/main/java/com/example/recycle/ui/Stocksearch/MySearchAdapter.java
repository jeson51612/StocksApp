package com.example.recycle.ui.Stocksearch;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.recycle.R;
import com.example.recycle.data.api.NowServicey;
import com.example.recycle.data.api.NowStockApi;
import com.example.recycle.data.model.DailyStockModel;
import com.example.recycle.data.model.StockModel;
import com.example.recycle.ui.Recommand.MyRecommandAdapter;

import retrofit2.Callback;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;
public class MySearchAdapter extends RecyclerView.Adapter<MySearchAdapter.ViewHolder>{
    private List<StockModel> mData;

    public MySearchAdapter(List<StockModel> data){
        mData=data;
    }

    @NonNull
    @Override
    public MySearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //連結項目佈局檔list_item
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_stock_listitem,parent,false);
        return new MySearchAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MySearchAdapter.ViewHolder holder, int position) {
        //設置txtItem要顯示的內容
        if(Float.parseFloat(mData.get(position).getChange())>0) {
            holder.topic_txt.setTextColor(Color.RED);
            holder.value_txt.setTextColor(Color.RED);
            holder.high_txt.setTextColor(Color.RED);
            holder.low_txt.setTextColor(Color.RED);
        }
        else {
            holder.topic_txt.setTextColor(0xff00aa00);
            holder.value_txt.setTextColor(0xff00aa00);
            holder.high_txt.setTextColor(0xff00aa00);
            holder.low_txt.setTextColor(0xff00aa00);
        }
        holder.topic_txt.setText(mData.get(position).getCode());
        holder.value_txt.setText(mData.get(position).getName() + " " + mData.get(position).getClosingPrice());
        holder.high_txt.setText(String.valueOf(mData.get(position).getChange()));
        holder.low_txt.setText(String.format("%.2f", 100 * Float.parseFloat(mData.get(position).getChange()) / Float.parseFloat(mData.get(position).getClosingPrice())) + "%");
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //宣告元件
        private TextView topic_txt;
        private TextView value_txt;
        private TextView high_txt;
        private TextView low_txt;




        ViewHolder(View itemview){
            super(itemview);
            topic_txt=(TextView) itemview.findViewById(R.id.searchlist_topic);
            value_txt=(TextView) itemview.findViewById(R.id.searchlist_value);
            high_txt=(TextView) itemview.findViewById(R.id.searchlist_open_value);
            low_txt=(TextView) itemview.findViewById(R.id.searchlist_close_value);

            //點擊項目
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    //顯示目前價格

                }
            });
        }
    }
    // 添加 setData 方法，用于更新数据
    public void setData(List<StockModel> data) {
        mData.clear(); // 清空当前数据
        mData.addAll(data); // 添加新数据
        notifyDataSetChanged(); // 通知适配器数据已更改
    }
}
