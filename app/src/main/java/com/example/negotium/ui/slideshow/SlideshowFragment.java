package com.example.negotium.ui.slideshow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.negotium.BoughtProduct;
import com.example.negotium.BuyList;
import com.example.negotium.BuylistAdapter;
import com.example.negotium.BuylistListener;
import com.example.negotium.Constants;
import com.example.negotium.offlineThings.OfflineHome;
import com.example.negotium.R;
import com.example.negotium.RateProduct;
import com.example.negotium.RequestHandler;
import com.example.negotium.SharedPrefManager;
import com.example.negotium.databinding.FragmentSlideshowBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlideshowFragment extends Fragment implements BuylistListener {
    RecyclerView recyclerView;
    Integer userid;
    BuylistAdapter buylistAdapter;
    List<BuyList> buyLists;

    private FragmentSlideshowBinding binding;
    private ProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        SlideshowViewModel slideshowViewModel =
//                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textSlideshow;
//        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userid = SharedPrefManager.getInstance(getContext()).getUserid();
        progressDialog = new ProgressDialog(getContext());
        buyLists = new ArrayList<>();
        recyclerView = view.findViewById(R.id.buylistRecycle);


        progressDialog.setMessage("showing products...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Constants.URL_BUYLIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();

                        try {
                            JSONObject obj = new JSONObject(s);
                            if(!obj.getBoolean("error")){
                                JSONArray jsonArray = (JSONArray)obj.getJSONArray("productid");
                                JSONArray jsonArray1 = (JSONArray)obj.getJSONArray("product_name");
                                JSONArray jsonArray2 = (JSONArray)obj.getJSONArray("pic");
                                JSONArray jsonArray3 = (JSONArray)obj.getJSONArray("israted");
                                JSONArray jsonArray4 = (JSONArray)obj.getJSONArray("quantity");
                                JSONArray jsonArray5 = (JSONArray)obj.getJSONArray("totalvalue");
                                JSONArray jsonArray6 = (JSONArray)obj.getJSONArray("buydate");
                                JSONArray jsonArray7 = (JSONArray)obj.getJSONArray("totaldays");
                                String total = obj.getString("finalvalue");
                                if(jsonArray != null){
                                    buyLists = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        BuyList the = new BuyList();
                                        the.id = jsonArray.getString(i);
                                        the.productname = jsonArray1.getString(i);
                                        the.pic = jsonArray2.getString(i);
                                        the.israted = jsonArray3.getString(i);
                                        the.quantity = jsonArray4.getString(i);
                                        the.totalvalue = jsonArray5.getString(i);
                                        the.buydate = jsonArray6.getString(i);
                                        the.totaldays = jsonArray7.getString(i);
                                        the.isbought = "0";
                                        buyLists.add(the);

//                                        productid.add(jsonArray.getString(i));
//                                        product_name.add(jsonArray1.getString(i));
//                                        category.add(jsonArray2.getString(i));
//                                        subcate.add(jsonArray3.getString(i));
//                                        price.add(jsonArray4.getString(i));
//                                        pic.add(jsonArray5.getString(i));
//                                        description.add(jsonArray6.getString(i));
//                                        producer.add(jsonArray7.getString(i));
                                    }
                                    binding.totalcostforbuy.setText("Price To Be Paid In The Time Of Delivery Is "+total);
                                    progressDialog.dismiss();
                                    send(buyLists);
                                }progressDialog.dismiss();
                            }else{
                                Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), OfflineHome.class);
                startActivity(intent);
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id",String.valueOf(userid));
                params.put("bought","0");
//                params.put("password",password);
                return params;
            }
        };
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);

        binding.boughtproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BoughtProduct.class);
                startActivity(intent);
            }
        });
        binding.rateproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RateProduct.class);
                startActivity(intent);
            }
        });
        binding.cancelbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<BuyList> selectedbuylist = buylistAdapter.getSelectedbuylist();
                StringBuilder buyid = new StringBuilder();
                for (int i = 0; i < selectedbuylist.size(); i++) {
                    if(i==0){
                        buyid.append(selectedbuylist.get(i).id);
                    }else{
                        buyid.append(",").append(selectedbuylist.get(i).id);
                    }
                }
                String buy = buyid.toString();
                progressDialog.setMessage("Removing selected wishlist...");
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST, Constants.URL_DELETEBUY,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {

                                try {
                                    JSONObject obj = new JSONObject(s);
                                    Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getContext(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                ){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("productid",buy);
                        params.put("id", String.valueOf(userid));
//                params.put("password",password);
                        return params;
                    }
                };
                RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
                progressDialog.dismiss();
                reload();

            }
        });

    }

    private void reload() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getFragmentManager().beginTransaction().detach(this).commitNow();
            getFragmentManager().beginTransaction().attach(this).commitNow();
        } else {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    private void send(List<BuyList> buyLists) {
        buylistAdapter = new BuylistAdapter(buyLists,this,getContext());
        recyclerView.setAdapter(buylistAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onbuylistAction(Boolean isSelected) {
        if(isSelected){
            binding.cancelbuy.setVisibility(View.VISIBLE);
        }else{
            binding.cancelbuy.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(int position) {

    }
}