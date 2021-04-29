package com.qihoo360.replugin.sample.host;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * author:CliffLeopard
 * date:4/26/21
 * time:2:36 PM
 * email:precipiceleopard@gmail.com
 * link:
 */
public class DemoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_layout,container,false);
    }
}
