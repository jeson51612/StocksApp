package com.example.recycle.ui.Recommand;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycle.data.api.Servicey;
import com.example.recycle.data.api.StockApi;
import com.example.recycle.data.model.StockModel;
import com.example.recycle.databinding.FragmentRecommandBinding;
import com.example.recycle.R;
import com.example.recycle.ui.Home.MyHomeAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecommandFragment extends Fragment {
    private FragmentRecommandBinding binding;
    private ArrayList<StockModel> mData=new ArrayList<>();;

    private RecyclerView recycler_view;

    private MyRecommandAdapter adapter;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRecommandBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        GetRetrofitResponse(root);

        return root;

    }
    private void GetRetrofitResponse(View root) {
        StockApi stockApi = Servicey.getStockApi();


        Call<List<StockModel>> responseCall=stockApi.CallStock();
        //是兩層巢狀json,所以要用List去排列矩陣行的List array

        responseCall.enqueue(new Callback<List<StockModel>>() {

            @Override
            public void onResponse(Call<List<StockModel>> call, Response<List<StockModel>> response) {
               if(response.code()==200){
                   int rmax=response.body().size();
                   int rmin=0;
                   for(int i=0;i<100;i++)
                   {
                       int int_chs_r=(int)(Math.random()*(rmax-rmin));
                       if(response.body().get(int_chs_r).getOpeningPrice().length()>1) {
                           StockModel r = response.body().get(int_chs_r);
                           mData.add(response.body().get(int_chs_r));
                       }
                   }

               }
                //連結元件
                recycler_view=(RecyclerView) root.findViewById(R.id.recommand_recycle_view);
                //設置RecyclerView為列表型態
                recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
                //設置隔線
                recycler_view.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));

                //將資料交給adapter
                adapter=new MyRecommandAdapter(mData);
                //設置adapter給recycler_view
                recycler_view.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<StockModel>> call, Throwable t) {
                Log.e("Tag", "Request failed: " + t.getMessage());
            }
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}
