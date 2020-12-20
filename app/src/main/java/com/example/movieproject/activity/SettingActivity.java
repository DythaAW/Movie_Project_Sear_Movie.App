package com.example.movieproject.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.movieproject.R;
import com.example.movieproject.preference.AppPreference;
import com.example.movieproject.reminder.DailyReminder;
import com.example.movieproject.reminder.UpComingTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {

    //Setting notification
    @BindView(R.id.switch_daily)
    Switch dailySwitch;
    @BindView(R.id.switch_upcoming)
    Switch upcomingSwitch;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.daily_notif)
    LinearLayout dailyNotif;
    @BindView(R.id.upcoming_notif)
    LinearLayout upcomingNotif;
    @BindView(R.id.setting_local)
    LinearLayout settingLocal;

    private UpComingTask mUpComingTask;
    private DailyReminder dailyReminder;
    private boolean isUpcoming, isDaily;
    private AppPreference appPreference;

    Button btn_logout;
    SharedPreferences sharedpreferences;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dailyReminder = new DailyReminder();
        appPreference = new AppPreference(this);

        setEnableDisableNotif();

        btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    void setEnableDisableNotif(){
        if (appPreference.isDaily()){
            dailySwitch.setChecked(true);
        }

        else {
            dailySwitch.setChecked(false);
        }

        if (appPreference.isUpcoming()){
            upcomingSwitch.setChecked(true);
        }

        else {
            upcomingSwitch.setChecked(false);
        }
    }

    @OnClick({R.id.switch_daily,R.id.switch_upcoming,R.id.setting_local})
    public void onClick(View view){
        switch (view.getId()){

            //Notif harian
            case R.id.switch_daily:
                isDaily = dailySwitch.isChecked();
                if (isDaily){
                    dailySwitch.setEnabled(true);
                    appPreference.setDaily(isDaily);
                    dailyReminder.setRepeatingAlarm(this, DailyReminder.TYPE_REPEATING,
                            "17:41", getString(R.string.message_notif_daily));
                }

                else {
                    dailySwitch.setChecked(false);
                    appPreference.setDaily(isDaily);
                    dailyReminder.cancelAlarm(this, DailyReminder.TYPE_REPEATING);
                }
                break;

            //Notif Kalo ada film baru
            case R.id.switch_upcoming:
                mUpComingTask = new UpComingTask(this);
                isUpcoming = upcomingSwitch.isChecked();
                if (isUpcoming){
                    upcomingSwitch.setEnabled(true);
                    appPreference.setUpcoming(isUpcoming);

                    mUpComingTask.createPeriodicTask();
                    Toast.makeText(this, R.string.message_setup_upcoming, Toast.LENGTH_SHORT).show();
                }else {
                    upcomingSwitch.setChecked(false);
                    appPreference.setUpcoming(isUpcoming);
                    mUpComingTask.cancelPeriodicTask();
                    Toast.makeText(this, R.string.message_cancel_upcoming, Toast.LENGTH_SHORT).show();
                }
                break;

            //Ganti bahasa
            case R.id.setting_local:
                Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(mIntent);
                break;
        }
    }

    private void logout() {
        sharedpreferences = this.getSharedPreferences(
                LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(LoginActivity.session_status, false);
        editor.putString(LoginActivity.TAG_ID, null);
        editor.putString(LoginActivity.TAG_EMAIL, null);
        editor.putString(LoginActivity.TAG_NAME, null);
        editor.putString(LoginActivity.TAG_PHONE, null);
        editor.commit();

        intent = new Intent(this, LoginActivity.class);
        this.finish();
        startActivity(intent);
    }

}
