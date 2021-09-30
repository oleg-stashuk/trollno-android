package com.app.trollno.ui.fragment.base;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.trollno.R;
import com.app.trollno.utils.data.PrefUtils;

import static android.content.Context.MODE_PRIVATE;

public abstract class BaseFragment extends Fragment {

    protected PrefUtils prefUtils;
    protected Context context;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getActivity().getApplicationContext();
        prefUtils = new PrefUtils(getActivity().getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE));

        initView();
    }

    protected abstract void initView();
}
