package com.rubenbarrosoblazquez.CasinoOnlineTFG.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.User;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.model.FirebaseCloudFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppInformationActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.skip_information)
    Button skipInformation;


    @BindView(R.id.backToCasinoActivity)
    Button volver;

    private User userInfo;

    private Switch isSkip;

    private FirebaseCloudFirestore Db;

    private boolean setBack ;

    @BindView(R.id.skipAppInformation)
    LinearLayout skipLayout;

    @BindView(R.id.volverAppInformation)
    LinearLayout volverLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.Db = new FirebaseCloudFirestore(this);

        Bundle b = getIntent().getBundleExtra("bundle");
        userInfo = (User) b.getSerializable("user");
        setBack = (Boolean) b.getBoolean("setBack");

        setContentView(R.layout.activity_app_information);

        ButterKnife.bind(this);

        if (setBack){
            skipLayout.setVisibility(View.GONE);
            volverLayout.setVisibility(View.VISIBLE);
        }else{
            skipLayout.setVisibility(View.VISIBLE);
            volverLayout.setVisibility(View.GONE);
        }

        getSupportActionBar().hide();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_info_general, R.id.navigation_info_ruleta, R.id.navigation_info_slot_machine)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController((BottomNavigationView) findViewById(R.id.nav_view), navController);
    }

    @OnClick(R.id.skip_information)
    public void skipInformation(){

        dialogSkip();

    }

    @OnClick(R.id.backToCasinoActivity)
    public void volver(){
        launchIntentToCasinoActivity();
    }


    public void dialogSkip(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.dialog_skip_information, null);


        Button skip = v.findViewById(R.id.button_skip_dialog);
        skip.setOnClickListener(this);

        this.isSkip = v.findViewById(R.id.switchSkipInformation);

        builder.setView(v);
        builder.create();
        builder.show();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_skip_dialog:

                this.userInfo.setSkip(this.isSkip.isChecked());
                this.Db.updateUser(userInfo);

                launchIntentToCasinoActivity();

                break;
        }
    }

    public void launchIntentToCasinoActivity(){
        Intent i = new Intent(this, CasinoActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("user", this.userInfo);
        i.putExtra("bundle", b);
        startActivity(i);
        finish();
    }
}