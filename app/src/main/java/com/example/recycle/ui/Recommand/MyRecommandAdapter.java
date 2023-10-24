package com.example.recycle.ui.Recommand;

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
import retrofit2.Callback;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;

public class MyRecommandAdapter extends RecyclerView.Adapter<MyRecommandAdapter.ViewHolder>{

    private List<StockModel> mData;

    public MyRecommandAdapter(List<StockModel> data){
        mData=data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //連結項目佈局檔list_item
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recommand_stock_listitem,parent,false);
        return new MyRecommandAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyRecommandAdapter.ViewHolder holder, int position) {
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
            topic_txt=(TextView) itemview.findViewById(R.id.recommand_topic);
            value_txt=(TextView) itemview.findViewById(R.id.recommand_value);
            high_txt=(TextView) itemview.findViewById(R.id.recommand_open_value);
            low_txt=(TextView) itemview.findViewById(R.id.recommand_close_value);

            //點擊項目
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    //顯示目前價格
                    NowStockApi stockApi = NowServicey.getNowStockApi();
                    Log.v("url","tse_"+mData.get(getBindingAdapterPosition()).getCode()+".tw");
                    Call<DailyStockModel> responseCall=stockApi.CallNowStock("tse_"+mData.get(getBindingAdapterPosition()).getCode()+".tw");
                    responseCall.enqueue(new Callback<DailyStockModel>(){

                        @Override
                        public void onResponse(Call<DailyStockModel> call, Response<DailyStockModel> response) {
                            String jump_message="當前盤中成交價"+response.body().getMsgArray().get(0).getZ()
                                    +"\n開盤價格"+response.body().getMsgArray().get(0).getO()
                                    +"\n最高價格"+response.body().getMsgArray().get(0).getH()
                                    +"\n最低價格"+response.body().getMsgArray().get(0).getL()
                                    +"\n昨日收盤價格"+response.body().getMsgArray().get(0).getY();
                            AlertDialog.Builder alertbox = new AlertDialog.Builder(view.getRootView().getContext());
                            alertbox.setMessage(jump_message);
                            alertbox.setTitle(response.body().getMsgArray().get(0).getNf());

                            alertbox.setNeutralButton("OK",
                                    new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface arg0,
                                                            int arg1) {

                                        }
                                    });
                            alertbox.show();

//                            Log.v("Tag",response.body().getMsgArray().get(0).getY());

                        }

                        @Override
                        public void onFailure(Call<DailyStockModel> call, Throwable t) {
                            t.printStackTrace();
                            Log.e("Tag", "Request failed: " + t.getMessage());
                        }
                    });

//                    Toast.makeText(view.getContext(),"click"+getBindingAdapterPosition(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
