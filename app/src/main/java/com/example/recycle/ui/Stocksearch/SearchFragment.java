package com.example.recycle.ui.Stocksearch;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycle.R;
import com.example.recycle.data.api.Servicey;
import com.example.recycle.data.api.StockApi;
import com.example.recycle.data.model.StockModel;
import com.example.recycle.databinding.FragmentSearchBinding;
import com.example.recycle.ui.Recommand.MyRecommandAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private ArrayList<StockModel> mData=new ArrayList<>();;
    private RecyclerView recycler_view;

    private ArrayList<StockModel> filteredData = new ArrayList<>(); // 用于存储过滤后的数据


    private MySearchAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        GetRetrofitResponse(root);
        SearchView searchView = root.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //按下enter時
//                filterData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 在文本搜尋時
                filterData(newText);
                return true;
            }
        });
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
                    for(int i=0;i<rmax;i++)
                    {
                        if(response.body().get(i).getOpeningPrice().length()>1) {
                            StockModel r = response.body().get(i);
                            mData.add(response.body().get(i));
                        }
                    }

                }
                //連結元件
                recycler_view=(RecyclerView) root.findViewById(R.id.search_recycle_view);
                //設置RecyclerView為列表型態
                recycler_view.setLayoutManager(new GridLayoutManager(getActivity(),2));
                //設置隔線
                recycler_view.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
                recycler_view.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.HORIZONTAL));

                //將資料交給adapter
                adapter=new MySearchAdapter(mData);
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

    // 根据搜索文本过滤数据
    private void filterData(String query) {
        if(mData.isEmpty())
            return;
        filteredData.clear(); // 清空已过滤的数据

        if (query.isEmpty()) {
            // 如果搜索文本为空，则显示所有数据
            filteredData.addAll(mData);
        } else {
            // 否则，比较搜索文本与股票名称
            for (StockModel item : mData) {
                if (item.getCode().toLowerCase().contains(query.toLowerCase())||item.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredData.add(item);
                }
            }
            // 更新适配器的数据
            adapter.setData(filteredData);
        }

    }


}
