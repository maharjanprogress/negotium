package com.example.negotium.ui.gallery;

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
import com.example.negotium.Constants;
import com.example.negotium.offlineThings.OfflineHome;
import com.example.negotium.R;
import com.example.negotium.RequestHandler;
import com.example.negotium.SharedPrefManager;
import com.example.negotium.Wishlist;
import com.example.negotium.WishlistAdapter;
import com.example.negotium.WishlistListener;
import com.example.negotium.databinding.FragmentGalleryBinding;
import com.example.negotium.productlook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryFragment extends Fragment implements WishlistListener {

    RecyclerView recyclerView;
    Integer userid;
    WishlistAdapter wishlistAdapter;
    List<Wishlist> wishlists;

    private FragmentGalleryBinding binding;

    private ProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        GalleryViewModel galleryViewModel =
//                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textGallery;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userid = SharedPrefManager.getInstance(getContext()).getUserid();


        progressDialog = new ProgressDialog(getContext());


        wishlists = new ArrayList<>();

//        Wishlist the = new Wishlist();
//        the.name = "lol";
//        the.pic = "kohinoor_basmati_rice.jpg";
//        wishlists.add(the);
//
//        Wishlist thee = new Wishlist();
//        thee.name = "loll";
//        thee.pic = "kelloggs_oats_1kg.jpg";
//        wishlists.add(thee);
//
//        Wishlist thew = new Wishlist();
//        thew.name = "lowwwwww";
//        thew.pic = "Current-Hot-n-Spicy-Noodles.jpg";
//        wishlists.add(thew);
//        for (int i = 0; i < 20; i++) {
//            Wishlist the = new Wishlist();
//            the.name = String.valueOf(userid);
//            the.pic = "Current-Hot-n-Spicy-Noodles.jpg";
//            wishlists.add(the);
//
//        }
        recyclerView = view.findViewById(R.id.wishlistRecycle);


        progressDialog.setMessage("showing products...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Constants.URL_WISHLIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            JSONObject obj = new JSONObject(s);
                            if(!obj.getBoolean("error")){
                                JSONArray jsonArray = (JSONArray)obj.getJSONArray("productid");
                                JSONArray jsonArray1 = (JSONArray)obj.getJSONArray("product_name");
                                JSONArray jsonArray2 = (JSONArray)obj.getJSONArray("pic");
                                JSONArray jsonArray3 = (JSONArray)obj.getJSONArray("producer");
                                JSONArray jsonArray4 = (JSONArray)obj.getJSONArray("quantity");
                                JSONArray jsonArray5 = (JSONArray)obj.getJSONArray("total_value");
                                JSONArray jsonArray6 = (JSONArray)obj.getJSONArray("price");
                                if(jsonArray != null){
                                    wishlists = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        Wishlist the = new Wishlist();
                                        the.id = jsonArray.getString(i);
                                        the.name = jsonArray1.getString(i);
                                        the.pic = jsonArray2.getString(i);
                                        the.producer = jsonArray3.getString(i);
                                        the.quantity = jsonArray4.getString(i);
                                        the.total_value = jsonArray5.getString(i);
                                        wishlists.add(the);

//                                        productid.add(jsonArray.getString(i));
//                                        product_name.add(jsonArray1.getString(i));
//                                        category.add(jsonArray2.getString(i));
//                                        subcate.add(jsonArray3.getString(i));
//                                        price.add(jsonArray4.getString(i));
//                                        pic.add(jsonArray5.getString(i));
//                                        description.add(jsonArray6.getString(i));
//                                        producer.add(jsonArray7.getString(i));
                                    }
                                    progressDialog.dismiss();
                                    send(wishlists);
                                }}else{
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
//                params.put("password",password);
                return params;
            }
        };
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);







        binding.buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Wishlist> selectedWishlist = wishlistAdapter.getSelectedwishlist();
                StringBuilder wishid = new StringBuilder();
                StringBuilder wishquantity = new StringBuilder();
                for (int i = 0; i < selectedWishlist.size(); i++) {
                    if(i==0){
                        wishid.append(selectedWishlist.get(i).id);
                        wishquantity.append(selectedWishlist.get(i).quantity);
                    }else{
                        wishid.append(",").append(selectedWishlist.get(i).id);
                        wishquantity.append(",").append(selectedWishlist.get(i).quantity);
                    }

                }
                String wish = wishid.toString();
                String wquantity = wishquantity.toString();
                progressDialog.setMessage("Removing selected wishlist...");
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST, Constants.URL_GETWISH,
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
                        params.put("productid",wish);
                        params.put("id", String.valueOf(userid));
                        params.put("quantity", wquantity);
//                params.put("password",password);
                        return params;
                    }
                };
                RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
                progressDialog.dismiss();
                reload();
            }
        });
        binding.cartcancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Wishlist> selectedWishlist = wishlistAdapter.getSelectedwishlist();
                StringBuilder wishid = new StringBuilder();
//                wishid.append("(");
                for (int i = 0; i < selectedWishlist.size(); i++) {
                    if(i==0){
                        wishid.append(selectedWishlist.get(i).id);
                    }else{
                        wishid.append(",").append(selectedWishlist.get(i).id);
                    }

                }
//                wishid.append(")");
                String wish = wishid.toString();
                progressDialog.setMessage("Removing selected wishlist...");
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST, Constants.URL_DELETEWISH,
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
                        params.put("productid",wish);
                        params.put("id", String.valueOf(userid));
//                params.put("password",password);
                        return params;
                    }
                };
                RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
                progressDialog.dismiss();
                reload();

//                Intent intent = new Intent(getContext(), MainActivity.class);
//                startActivity(intent);
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

    private void send(List<Wishlist> wishlists) {
        wishlistAdapter = new WishlistAdapter(wishlists,this,getContext());
        recyclerView.setAdapter(wishlistAdapter);
    }

    @Override
    public void onwishlistAction(Boolean isSelected) {
        if(isSelected){
            binding.buyButton.setVisibility(View.VISIBLE);
            binding.cartcancelbutton.setVisibility(View.VISIBLE);
        }else{
            binding.buyButton.setVisibility(View.GONE);
            binding.cartcancelbutton.setVisibility(View.GONE);
        }

    }

    @Override
    public void onItemSelect(int position) {
        String id = wishlists.get(position).id;
        Intent intent = new Intent(getContext(), productlook.class);
        intent.putExtra("id",id);
        startActivity(intent);
        Toast.makeText(getContext(), id, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}