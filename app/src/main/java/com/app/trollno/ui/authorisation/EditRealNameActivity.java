package com.app.trollno.ui.authorisation;

import androidx.databinding.DataBindingUtil;

import com.app.trollno.R;
import com.app.trollno.databinding.ActivityEditRealNameBinding;
import com.app.trollno.ui.base.BaseActivity;
import com.app.trollno.utils.SnackBarMessageCustom;
import com.app.trollno.utils.networking.authorisation.GetUserDataForUpdateRealName;
import com.app.trollno.utils.networking.authorisation.UpdateShowName;

public class EditRealNameActivity extends BaseActivity {
    private ActivityEditRealNameBinding binding;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_edit_real_name;
    }

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, getLayoutID());

        getInitialData();
        buttonClickListeners();
    }

    private void getInitialData() {
        new Thread(() -> {
            GetUserDataForUpdateRealName.getUserDataForUpdateRealName(this, prefUtils, binding);
        }).start();
    }

    private void buttonClickListeners() {
        binding.updateButton.setOnClickListener(v -> {
            hideKeyBoard();
            if (binding.edtRealName.toString() != null || binding.edtRealName.toString().length() > 0) {
                String name = binding.edtRealName.getText().toString();
                new Thread(() -> {
                    UpdateShowName.updateShowName(this, prefUtils, name, binding.layout);
                }).start();
            } else {
                SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation(binding.layout, getString(R.string.msg_enter_name));
            }
        });
    }
}