package com.book.ireader.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.book.ireader.R;
import com.book.ireader.model.local.ReadSettingManager;
import com.book.ireader.ui.base.BaseActivity;

/**
 * Created by newbiechen on 17-6-6.
 * 阅读界面的更多设置
 */

public class MoreSettingActivity extends BaseActivity {
    RelativeLayout mRlVolume;
    SwitchCompat mScVolume;
    RelativeLayout mRlFullScreen;
    SwitchCompat mScFullScreen;
    RelativeLayout mRlConvertType;
    Spinner mScConvertType;
    private ReadSettingManager mSettingManager;
    private boolean isVolumeTurnPage;
    private boolean isFullScreen;
    private int convertType;

    @Override
    protected int getContentId() {
        return R.layout.activity_more_setting;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mSettingManager = ReadSettingManager.getInstance();
        isVolumeTurnPage = mSettingManager.isVolumeTurnPage();
        isFullScreen = mSettingManager.isFullScreen();
        convertType = mSettingManager.getConvertType();
    }

    @Override
    protected void setUpToolbar(Toolbar toolbar) {
        super.setUpToolbar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.nb_read_setting_title));
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mRlVolume = findViewById(R.id.more_setting_rl_volume);
        mScVolume = findViewById(R.id.more_setting_sc_volume);
        mRlFullScreen = findViewById(R.id.more_setting_rl_full_screen);
        mScFullScreen = findViewById(R.id.more_setting_sc_full_screen);
        mScConvertType = findViewById(R.id.more_setting_sc_convert_type);
        initSwitchStatus();
    }

    private void initSwitchStatus() {
        mScVolume.setChecked(isVolumeTurnPage);
        mScFullScreen.setChecked(isFullScreen);
    }

    @Override
    protected void initClick() {
        super.initClick();
        mRlVolume.setOnClickListener(
                (v) -> {
                    if (isVolumeTurnPage) {
                        isVolumeTurnPage = false;
                    } else {
                        isVolumeTurnPage = true;
                    }
                    mScVolume.setChecked(isVolumeTurnPage);
                    mSettingManager.setVolumeTurnPage(isVolumeTurnPage);
                }
        );

        mRlFullScreen.setOnClickListener(
                (v) -> {
                    if (isFullScreen) {
                        isFullScreen = false;
                    } else {
                        isFullScreen = true;
                    }
                    mScFullScreen.setChecked(isFullScreen);
                    mSettingManager.setFullScreen(isFullScreen);
                }
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.conversion_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mScConvertType.setAdapter(adapter);

        // initSwitchStatus() be called earlier than onCreate(), so setSelection() won't work
        mScConvertType.setSelection(convertType);

        mScConvertType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSettingManager.setConvertType(position);
                convertType = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
