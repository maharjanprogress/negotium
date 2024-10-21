package com.example.negotium.ui.logout;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.negotium.LoginActivity;
import com.example.negotium.R;
import com.example.negotium.SharedPrefManager;

public class Logout extends Fragment {


    public static Logout newInstance() {
        return new Logout();
    }
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_logout, container, false);
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPrefManager.getInstance(getContext()).logout();
        startActivity(new Intent(getContext(), LoginActivity.class));
        // TODO: Use the ViewModel
    }

}