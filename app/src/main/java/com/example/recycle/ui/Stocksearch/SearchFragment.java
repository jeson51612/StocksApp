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
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycle.R;
import com.example.recycle.data.api.Servicey;
import com.example.recycle.data.api.StockApi;
import com.example.recycle.data.model.StockModel;
import com.example.recycle.databinding.FragmentSearchBinding;
import com.example.recycle.ui.Home.MyHomeAdapter;
import com.example.recycle.ui.Recommand.MyRecommandAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private ArrayList<StockModel> mData=new ArrayList<>();
    private RecyclerView recycler_view;

    private ArrayList<StockModel> filteredData = new ArrayList<>(); // 用于存储过滤后的数据
    private ArrayList<StockModel> allData=new ArrayList<>();

    private MySearchAdapter adapter;

    private String previous_text;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        GetRetrofitResponse(root);
//        detaildata();
        SearchView searchView = root.findViewById(R.id.searchView);
        previous_text="";
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //按下enter時
//                filterData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()>=previous_text.length()) {
                    // 在文本搜尋時
                    if (newText.matches(".*[ㄅ-ㄩ].*")) {

                    } else {
                        filterData(newText);
                    }
                }
                else
                {
                    filteredData.clear();
                    for (StockModel item : allData) {

                        if (item.getCode().toLowerCase().contains(newText.toLowerCase())||item.getName().contains(newText)) {
                            filteredData.add(item);
                        }
                    }
                    // 更新适配器的数据
                    adapter.setData(filteredData);

                }
                previous_text=newText;
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
                if(mData.size()>allData.size()){

                    allData=new ArrayList<>(mData);
                }
//                detaildata();
                recycler_view.setAdapter(adapter);
                detaildata();
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
                if (item.getCode().toLowerCase().contains(query.toLowerCase())||item.getName().contains(query)) {
                    filteredData.add(item);
                }
            }
            // 更新适配器的数据
            adapter.setData(filteredData);
        }

    }
    private void detaildata(){
        if(adapter!=null){
            adapter.setOnItemClickListener(new MySearchAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    //需要設定彈出視窗為home...不然回不去home
                    NavOptions navOptions = new NavOptions.Builder()
                            .setPopUpTo(R.id.nav_search, false, false) // 三個參數分別是popUpToId, popUpToInclusive, popUpToSaveState
                            .setLaunchSingleTop(true)
                            .setRestoreState(true)
                            .build(); // 如果需要自定义导航选项，可以在这里设置 NavOptions
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main); // 帶入目前啟動的active與hostfragment
                    Bundle args = new Bundle();
                    ArrayList<String> argsList=new ArrayList<>();
                    argsList.add(mData.get(position).getCode());
                    argsList.add(mData.get(position).getName());
                    args.putStringArrayList("Code",argsList);
//
                    navController.navigate(R.id.nav_stockchart, args, navOptions);
                    mData.clear();
                }
            });}
    }


}
