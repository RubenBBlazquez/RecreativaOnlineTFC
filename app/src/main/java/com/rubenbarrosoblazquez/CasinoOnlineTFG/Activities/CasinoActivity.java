package com.rubenbarrosoblazquez.CasinoOnlineTFG.Activities;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.material.navigation.NavigationView;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces.OnGetUserInformation;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces.OnAdsListener;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.JavaClass.User;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.model.FirebaseCloudFirestore;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.model.FirebaseMessagingModel;

import java.util.Arrays;

public class CasinoActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener, OnGetUserInformation, OnAdsListener, View.OnClickListener {

    private AppBarConfiguration mAppBarConfiguration;
    private User user;
    private MenuItem personalBalance;
    private TextView balanceDialog;
    private MenuItem actualBalanceProfile;
    private RewardedVideoAd mRewardedAd;
    private FirebaseCloudFirestore model;
    private FirebaseMessagingModel messagingModel;
    private BroadcastReceiver messageReceiver;


    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("informacion"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casino);

        model = new FirebaseCloudFirestore(getApplicationContext());

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.d("rewards", "fist init success");
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setBackgroundColor(getColor(R.color.colorbackgoundDrawer));

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_init, R.id.nav_ruleta, R.id.nav_tragaperras, R.id.nav_blackjact, R.id.nav_servicios, R.id.nav_profile)
                .setDrawerLayout(drawer)
                .build();

        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Bundle b = getIntent().getBundleExtra("bundle");

        this.user = (User) b.getSerializable("user");

        this.initElementsHeaderView(navigationView, this.user);

        new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("A5AC149BF65213880D4D353CECDCB424"));
        rewardedAd();
        loadRewardedVideoAd();

        Menu menu = navigationView.getMenu();
        personalBalance = menu.findItem(R.id.personalBalance);
        personalBalance.setTitle(user.getSaldo() + " €");

        MenuItem admin = menu.findItem(R.id.admin);

        if (user.getTipoUser() == 0) {
            admin.setVisible(false);
        } else {
            admin.setVisible(true);
        }


        messagingModel=new FirebaseMessagingModel(this);
        messagingModel.recuperarToken(user.getEmail());
        messagingModel.checkGooglePlayServices();

        messageReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                crearNotification(context,intent.getExtras());
            }
        };

    }


    private void initElementsHeaderView(NavigationView navigationView, User user) {
        View header_view = navigationView.getHeaderView(0);


        TextView nombre = header_view.findViewById(R.id.userNameHeader);
        nombre.setText(user.getName());

        TextView email = header_view.findViewById(R.id.userEmailHeader);
        email.setText(user.getEmail());

        Button logout = header_view.findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor preferences = getSharedPreferences(getString(R.string.prefs_login), Context.MODE_PRIVATE).edit();
                preferences.clear();
                preferences.apply();
                Intent i = new Intent(getApplicationContext(), LoginRegisterActivity.class);
                startActivity(i);
            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.casino, menu);
        MenuItem item = menu.findItem(R.id.profile_menu);
        item.setOnMenuItemClickListener(this);

        actualBalanceProfile = menu.findItem(R.id.saldoActualPerfil);
        actualBalanceProfile.setTitle(this.user.getSaldo() + " €");

        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_menu_profile, null);

        balanceDialog = v.findViewById(R.id.saldoActualPerfilDialogo);
        balanceDialog.setText(this.user.getSaldo() + " €");
        balanceDialog.setOnClickListener(this);

        TextView rewardedAdd = v.findViewById(R.id.ver_add_menu_item);
        rewardedAdd.setOnClickListener(this);

        TextView profile = v.findViewById(R.id.ProfileOptionsMenu);
        profile.setOnClickListener(this);

        builder.setView(v);
        builder.create();
        builder.show();

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ver_add_menu_item:
                Toast.makeText(this, "has pulsado ver anuncio", Toast.LENGTH_SHORT).show();
                showAd();
                break;
            case R.id.ProfileOptionsMenu:
                Toast.makeText(this, "has pulsado ir al perfil", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    @Override
    public void rewardedAd() {
        mRewardedAd = MobileAds.getRewardedVideoAdInstance(this);
        this.mRewardedAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {

            @Override
            public void onRewardedVideoAdLoaded() {

            }

            @Override
            public void onRewardedVideoAdOpened() {
                Log.d("rewards", "video abierto");
            }

            @Override
            public void onRewardedVideoStarted() {
                Log.d("rewards", "video empezado");
            }

            @Override
            public void onRewardedVideoAdClosed() {
                loadRewardedVideoAd();
                updateBalanceTexts();
                Log.d("rewards", "video cerrado");
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                //cuando el anuncio acaba, guardo en cloud firestore el saldo que tiene actualmente el usaurio, sacado al iniciar la aplicacion, y le actualizo con lo que tiene actualmente al ver el video
                Toast.makeText(CasinoActivity.this, "Has conseguido al ver el video " + (Float.valueOf(rewardItem.getAmount()) / 20), Toast.LENGTH_SHORT).show();
                user.setSaldo(user.getSaldo() + Float.valueOf(rewardItem.getAmount()) / 20);
                model.updateSaldo(user.getEmail(), user.getSaldo());

                //actualizo los textos de la aplicación donde aparece el saldo del usuario
                updateBalanceTexts();

            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                Log.d("rewards", "error al cargar el anuncio");
            }

            @Override
            public void onRewardedVideoCompleted() {
                Log.d("rewards", "Complete");
            }

        });
        this.loadRewardedVideoAd();

    }

    @Override
    public void loadRewardedVideoAd() {
        mRewardedAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());
    }

    @Override
    public void showAd() {
        if (this.mRewardedAd.isLoaded()) {
            mRewardedAd.show();
        }
    }


    @Override
    public User getUserInformation() {
        return this.user;
    }

    @Override
    public boolean UpdateUserInformation(User u) {
        return getFirestoreInstance().updateUser(u);
    }


    @Override
    public void updateBalanceTexts() {
        if (balanceDialog != null) {
            balanceDialog.setText(this.user.getSaldo() + " €");
        }
        personalBalance.setTitle(this.user.getSaldo() + " €");
        actualBalanceProfile.setTitle(this.user.getSaldo() + " €");
    }

    @Override
    public FirebaseCloudFirestore getFirestoreInstance() {
        return model;
    }


    @Override
    public void loadBannerAdView(AdView adView) {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public void crearNotification(Context context, Bundle b){
        NotificationCompat.Builder constructorNotif=new NotificationCompat.Builder(context,"micanal");
        constructorNotif.setSmallIcon(android.R.drawable.btn_star_big_on);

        //obtener una referencia del notificador
        NotificationManager notificationManager=(NotificationManager)context.getSystemService(context.getApplicationContext().NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel n=new NotificationChannel("micanal","Canal TFC",NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(n);
        }


        Intent resultadoIntent = new Intent(context, LoginRegisterActivity.class);
        TaskStackBuilder pila = TaskStackBuilder.create(context);
        pila.addParentStack(LoginRegisterActivity.class);

        // Añade el Intent que comienza la Actividad al inicio de la pila
        pila.addNextIntent(resultadoIntent);

        PendingIntent resultadoPendingIntent =
                pila.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        constructorNotif.setContentIntent(resultadoPendingIntent);

        NotificationCompat.InboxStyle expandible=new NotificationCompat.InboxStyle();
        expandible.setBigContentTitle(b.getString("titulo"));
        expandible.setSummaryText(b.getString("contenido"));
        constructorNotif.setStyle(expandible);

        notificationManager.notify(1,constructorNotif.build());


    }
}