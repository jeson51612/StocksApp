package com.example.recycle.ui.Home;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycle.MainActivity;
import com.example.recycle.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;

public class MyHomeAdapter extends RecyclerView.Adapter<MyHomeAdapter.ViewHolder> {
    private List<String> mData;
    private final List<Integer> drawables = Arrays.asList(
            R.drawable.bank1,
            R.drawable.bank2,
            R.drawable.money1,
            R.drawable.money2);

    public MyHomeAdapter(List<String> data){
        mData=data;
    }
    //set on ListenClicker
    private OnItemClickListener itemClickListener;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }
    //


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //連結項目佈局檔list_item
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //設置txtItem要顯示的內容
        holder.txtItem.setText(mData.get(position));
        holder.imageView.setImageDrawable(ResourcesCompat.getDrawable(holder.imageView.getResources(),
                drawables.get(position),
                null));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    //建立ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder{
        //宣告元件
        private TextView txtItem;

        private ImageView imageView;




        ViewHolder(View itemview){
            super(itemview);
            txtItem=(TextView) itemview.findViewById(R.id.txtItem);
            imageView=(ImageView) itemview.findViewById(R.id.listimage);

            //點擊項目
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
//                    // 使用NavController导航到目的地

                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(getBindingAdapterPosition());
                    }
                }
            });
        }
    }


}
