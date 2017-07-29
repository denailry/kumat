package com.example.user.kumat;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import java.util.Calendar;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.user.kumat.Adapter.IconAdapter;
import com.example.user.kumat.Database.AktivitasKeuanganDatabase;
import com.example.user.kumat.Database.PengeluaranOtomatisDatabase;
import com.example.user.kumat.Database.ProfilDatabase;
import com.example.user.kumat.Database.ProfilDatabase_Table;
import com.example.user.kumat.Database.QuickButtonDatabase;
import com.example.user.kumat.Fragment.AktivitasKeuanganFragment;
import com.example.user.kumat.Fragment.BukuHutangFragment;
import com.example.user.kumat.Fragment.KeuanganFragment;
import com.example.user.kumat.Fragment.KumatShopFragment;
import com.example.user.kumat.Fragment.PengeluaranOtomatisFragment;
import com.example.user.kumat.Fragment.QuickButtonFragment;
import com.example.user.kumat.Fragment.WishlistFragment;
import com.example.user.kumat.Listener.IconListener;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements IconListener {
    int day,month,year;

    CircleImageView fotoProfil;
    TextView namaProfil,emailProfil,koinProfil;

    NavigationView navigationView;
    DrawerLayout drawer;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    int hour,minute;

    RelativeLayout backgroundPopUp;
    boolean cekBackground = false;
    Button btnDate1,btnDate2;
    TextView txtCustom1,txtCustom2;

    RelativeLayout backgroundDeleteAktivitas;
    TextView txtDetailBarang,txtDetailHarga,txtTanggalAktivitas;
    Button btnDeleteAktivitas;

    RadioGroup radioGrupPeriode;
    int pilihanPeriode = 0;
    Button btnSubmitPeriode;
    RelativeLayout milihDate;
    int periodeSaved = R.id.radio_semua_data;
    int periode;
    int saved=0;

    private Calendar calendar;
    public int thisDay,thisMonth,thisYear;
    int DayPilihan1,MonthPilihan1,YearPilihan1,DayPilihan2,MonhtPilihan2,YearPilihan2;

    public String usernameAktif;

    Intent intent;

    Toolbar toolbar;

    RelativeLayout backgroundAddPhoto;
    RecyclerView rvIcon;
    ArrayList<Integer> listIcon = callIcon();

    RelativeLayout backgroundDeleteQuickButton;
    TextView txtDataQuickButtonDelete,txtNominalQuickButtonDelete;
    ImageView imgQuickButtonDelete;
    Button btnQuickButtonDelete;

    RelativeLayout backgroundDeleteOtomatis;
    TextView txtNamaOtomatis,txtNominalOtomatis,txtWaktuOtomatis;
    Button btnDeleteOtomatis;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_AKTIVITAS = "aktivitas";
    private static final String TAG_WISHLIST = "wishlist";
    private static final String TAG_QUICK = "quick";
    private static final String TAG_OTOMATIS = "otomatis";
    private static final String TAG_SHOP = "shop";
    private static final String TAG_HUTANG = "hutang";
    public static String CURRENT_TAG = TAG_HOME;

    View navHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("WAKTU", String.valueOf(IdGen.generateTimeId()));
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Kumat!");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mHandler= new Handler();

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        //load Database
        FlowManager.init(new FlowConfig.Builder(this).build());

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        backgroundPopUp = (RelativeLayout)findViewById(R.id.background_pop_up);
        backgroundPopUp.setVisibility(View.GONE);
        radioGrupPeriode = (RadioGroup)findViewById(R.id.radio_grup_periode);
        btnSubmitPeriode = (Button)findViewById(R.id.btn_submit_periode);
        btnDate1 = (Button)findViewById(R.id.btn_custom_1);
        btnDate2 = (Button)findViewById(R.id.btn_custom_2);
        txtCustom1 = (TextView)findViewById(R.id.txt_custom_1);
        txtCustom2 = (TextView)findViewById(R.id.txt_custom_2);
        milihDate = (RelativeLayout)findViewById(R.id.relbe);
        navHeader=navigationView.getHeaderView(0);
        fotoProfil = (CircleImageView)navHeader.findViewById(R.id.foto_profil);
        namaProfil = (TextView)navHeader.findViewById(R.id.txt_username);
        emailProfil = (TextView)navHeader.findViewById(R.id.txt_user_email);
        koinProfil = (TextView)navHeader.findViewById(R.id.jml_koin);
        backgroundDeleteAktivitas = (RelativeLayout)findViewById(R.id.back_delete_aktivitas);
        txtDetailBarang = (TextView)findViewById(R.id.txt_detail_delete);
        txtDetailHarga = (TextView)findViewById(R.id.txt_harga_delete);
        txtTanggalAktivitas = (TextView)findViewById(R.id.tanggal_delete);
        btnDeleteAktivitas = (Button)findViewById(R.id.btn_delete_detail);
        backgroundAddPhoto =(RelativeLayout)findViewById(R.id.background_pilih_icon_quick);
        rvIcon = (RecyclerView)findViewById(R.id.rv_icon);
        backgroundDeleteQuickButton = (RelativeLayout)findViewById(R.id.background_delete_quick);
        txtDataQuickButtonDelete = (TextView)findViewById(R.id.txt_nama_delete_quick);
        txtNominalQuickButtonDelete = (TextView)findViewById(R.id.txt_nominal_delete_quick);
        imgQuickButtonDelete = (ImageView)findViewById(R.id.img_icon_delete_quick);
        btnQuickButtonDelete = (Button)findViewById(R.id.btn_delete_quick);
        backgroundDeleteOtomatis = (RelativeLayout)findViewById(R.id.background_delete_otomatis);
        txtNamaOtomatis = (TextView)findViewById(R.id.txt_detail_delete_otomatis);
        txtNominalOtomatis = (TextView)findViewById(R.id.txt_harga_delete_otomatis);
        txtWaktuOtomatis = (TextView)findViewById(R.id.waktu_delete_otomatis);
        btnDeleteOtomatis = (Button)findViewById(R.id.btn_delete_detail_otomatis);

        backgroundDeleteOtomatis.setVisibility(View.GONE);
        backgroundDeleteOtomatis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundDeleteOtomatis.setVisibility(View.GONE);
                FragmentManager fm = getSupportFragmentManager();
                PengeluaranOtomatisFragment fragment = (PengeluaranOtomatisFragment)fm.findFragmentById(R.id.frame_container);
                fragment.notifyData();
            }
        });
        btnDeleteOtomatis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundDeleteOtomatis.setVisibility(View.GONE);
                FragmentManager fm = getSupportFragmentManager();
                PengeluaranOtomatisFragment fragment = (PengeluaranOtomatisFragment)fm.findFragmentById(R.id.frame_container);
                fragment.onTerimaDelete();
            }
        });

        backgroundDeleteQuickButton.setVisibility(View.GONE);
        backgroundDeleteQuickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundDeleteQuickButton.setVisibility(View.GONE);
            }
        });
        btnQuickButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundDeleteQuickButton.setVisibility(View.GONE);
                FragmentManager fm  = getSupportFragmentManager();
                QuickButtonFragment fragment = (QuickButtonFragment)fm.findFragmentById(R.id.frame_container);
                fragment.onTerimaDelete();
            }
        });

        backgroundAddPhoto.setVisibility(View.GONE);
        backgroundAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundAddPhoto.setVisibility(View.GONE);
            }
        });

        backgroundDeleteAktivitas.setVisibility(View.GONE);
        backgroundDeleteAktivitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundDeleteAktivitas.setVisibility(View.GONE);
                FragmentManager fm = getSupportFragmentManager();
                AktivitasKeuanganFragment fragment = (AktivitasKeuanganFragment)fm.findFragmentById(R.id.frame_container);
                fragment.notifyLagi();
            }
        });
        btnDeleteAktivitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                AktivitasKeuanganFragment fragment = (AktivitasKeuanganFragment)fm.findFragmentById(R.id.frame_container);
                fragment.notifyLagi();
                backgroundDeleteAktivitas.setVisibility(View.GONE);
                fragment.onTerimaDelete();
            }
        });

        navHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ProfilActivity.class);
                startActivity(intent);
            }
        });

        setUpHeader();


        btnDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(998);
            }
        });
        btnDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(997);
            }
        });

        radioGrupPeriode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId==R.id.radio_semua_data){
                    pilihanPeriode = 0;
                    milihDate.setVisibility(View.GONE);
                }else if (checkedId== R.id.radio_bulan){
                    pilihanPeriode= 1;
                    milihDate.setVisibility(View.GONE);
                }else if (checkedId==R.id.radio_hari){
                    pilihanPeriode = 2;
                    milihDate.setVisibility(View.GONE);
                }else if (checkedId==R.id.radio_custom){
                    pilihanPeriode=3;
                    milihDate.setVisibility(View.VISIBLE);
                }
                periode=checkedId;
            }
        });

        setHariini();


        btnSubmitPeriode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                AktivitasKeuanganFragment fragment = (AktivitasKeuanganFragment)fm.findFragmentById(R.id.frame_container);

                if (pilihanPeriode==3){
                    boolean cek = fragment.cekTanggal(DayPilihan1,MonthPilihan1,YearPilihan1,DayPilihan2,MonhtPilihan2,YearPilihan2);
                    Log.d("pilihperiode", "onClick: "+DayPilihan1+", "+MonthPilihan1+", "+YearPilihan1+", "+DayPilihan2+", "+MonhtPilihan2+", "+YearPilihan2);
                    if (!cek){
                        Toast.makeText(getApplicationContext(),"Rentang Tanggal Tidak Sesuai",Toast.LENGTH_SHORT).show();
                    }else{
                        fragment.setelahPilihPeriode(pilihanPeriode,DayPilihan1,MonthPilihan1,YearPilihan1,DayPilihan2,MonhtPilihan2,YearPilihan2);

                        periodeSaved=periode;
                        saved=pilihanPeriode;

                        backgroundPopUp.setVisibility(View.GONE);
                        cekBackground=false;
                    }

                }else{
                    fragment.setelahPilihPeriode(pilihanPeriode,DayPilihan1,MonthPilihan1,YearPilihan1,DayPilihan2,MonhtPilihan2,YearPilihan2);

                    periodeSaved=periode;

                    backgroundPopUp.setVisibility(View.GONE);
                    cekBackground=false;
                }

            }
        });

        navigationView.getMenu().getItem(6).setActionView(R.layout.tambahan_mode);


        backgroundPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundPopUp.setVisibility(View.GONE);
                cekBackground=false;
            }
        });


        setUpNavigationView();

        //buka fragment utama
        if (savedInstanceState==null){
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    public void setUpHeader() {

        List<ProfilDatabase> profilSearch = new Select()
                .from(ProfilDatabase.class)
                .where(ProfilDatabase_Table.id.is(1))
                .queryList();

        if (profilSearch.size()==0){
            usernameAktif = "camat";
            ProfilDatabase profil = new ProfilDatabase();
            profil.setId(1);
            profil.setUsername(usernameAktif);
            profil.setKoin(100);
            profil.setBukuHutang(false);
            profil.setEmail("kumat@email.com");
            profil.setXp(0);
            profil.save();

            setUpNama(usernameAktif);
            setUpJumlahKoin(100);
            setUpEmail(profil.getEmail());
        }else{
            ProfilDatabase profilDatabase = profilSearch.get(0);
            usernameAktif = profilDatabase.getUsername();
            setUpEmail(profilDatabase.getEmail());
            setUpNama(profilDatabase.getUsername());
            setUpJumlahKoin(profilDatabase.getKoin());
            if (profilDatabase.getIkon()!=null){
                setUpFoto(profilDatabase.getIkon());
            }

        }

    }

    private void setUpFoto(Bitmap bm){
        fotoProfil.setImageBitmap(bm);
    }
    private void setUpNama(String user){
        namaProfil.setText(user);
    }
    private void setUpEmail(String email){
        emailProfil.setText(email);
    }
    private void setUpJumlahKoin(int koin){
        koinProfil.setText(String.valueOf(koin));
    }

    private void setHariini() {
        calendar= new GregorianCalendar();
        thisYear = calendar.get(java.util.Calendar.YEAR);
        thisMonth = calendar.get(java.util.Calendar.MONTH);
        thisDay = calendar.get(java.util.Calendar.DAY_OF_MONTH);
        DayPilihan1=thisDay;
        DayPilihan2=thisDay;
        MonthPilihan1=thisMonth+1;
        MonhtPilihan2=thisMonth+1;
        YearPilihan1=thisYear;
        YearPilihan2=thisYear;

        if ((thisDay<10)&&(thisMonth+1<10)){
            txtCustom1.setText("0"+thisDay+" - 0"+(thisMonth+1)+" - "+thisYear);
        }else{
            if (thisDay<10){
                txtCustom1.setText("0"+thisDay+" - "+(thisMonth+1)+" - "+thisYear);
            }else if (thisMonth+1<10){
                txtCustom1.setText(thisDay+" - 0"+(thisMonth+1)+" - "+thisYear);
            }else {
                txtCustom1.setText(thisDay+" - "+(thisMonth+1)+" - "+thisYear);
            }
        }

        if ((thisDay<10)&&(thisMonth+1<10)){
            txtCustom2.setText("0"+thisDay+" - 0"+(thisMonth+1)+" - "+thisYear);
        }else{
            if (thisDay<10){
                txtCustom2.setText("0"+thisDay+" - "+(thisMonth+1)+" - "+thisYear);
            }else if (thisMonth+1<10){
                txtCustom2.setText(thisDay+" - 0"+(thisMonth+1)+" - "+thisYear);
            }else {
                txtCustom2.setText(thisDay+" - "+(thisMonth+1)+" - "+thisYear);
            }
        }
    }

    private Fragment getHomeFragment(){
        switch (navItemIndex){
            case 0:
                KeuanganFragment keuanganFragment = new KeuanganFragment();
                return keuanganFragment;
            case 1:
                AktivitasKeuanganFragment aktivitasKeuanganFragment = new AktivitasKeuanganFragment();
                return aktivitasKeuanganFragment;
            case 2:
                WishlistFragment wishlistFragment = new WishlistFragment();
                return wishlistFragment;
            case 3:
                QuickButtonFragment quickButtonFragment = new QuickButtonFragment();
                return  quickButtonFragment;
            case 4:
                PengeluaranOtomatisFragment pengeluaranOtomatisFragment = new PengeluaranOtomatisFragment();
                return pengeluaranOtomatisFragment;
            case 5:
                KumatShopFragment kumatShopFragment = new KumatShopFragment();
                return kumatShopFragment;
            case 6:
                BukuHutangFragment bukuHutangFragment = new BukuHutangFragment();
                return bukuHutangFragment;
            default:
                return new KeuanganFragment();
        }
    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home :
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_aktivitas :
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_AKTIVITAS;
                        radioGrupPeriode.clearCheck();
                        radioGrupPeriode.check(R.id.radio_semua_data);
                        setHariini();
                        milihDate.setVisibility(View.GONE);
                        pilihanPeriode=0;
                        saved=pilihanPeriode;
                        periodeSaved=R.id.radio_semua_data;
                        Log.d("radiocek", "onCreate: ");
                        break;
                    case R.id.nav_Wishlist:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_WISHLIST;
                        break;
                    case R.id.nav_quick :
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_QUICK;
                        break;
                    case R.id.nav_otomatis:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_OTOMATIS;
                        break;
                    case R.id.nav_shop:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_SHOP;
                        break;
                    case R.id.nav_hutang:
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_HUTANG;
                        break;
                    case R.id.nav_setting:
                        navItemIndex = 8;
                        intent = new Intent(getApplicationContext(),PengaturanActivity.class);
                        startActivity(intent);
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_help:
                        navItemIndex = 9;
                        intent = new Intent(getApplicationContext(),BantuanActivity.class);
                        startActivity(intent);
                        drawer.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                item.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        Log.d("sa", "hasil : "+navItemIndex+","+CURRENT_TAG);

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame_container, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();

    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
        Log.d("menu", "selectNavMenu");
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }else if (cekBackground){
            backgroundPopUp.setVisibility(View.GONE);
            cekBackground=false;
            return;
        }else  if (backgroundDeleteAktivitas.getVisibility()==View.VISIBLE){
            backgroundDeleteAktivitas.setVisibility(View.GONE);
            FragmentManager fm = getSupportFragmentManager();
            AktivitasKeuanganFragment fragment = (AktivitasKeuanganFragment)fm.findFragmentById(R.id.frame_container);
            fragment.notifyLagi();
            return;
        }else if (backgroundAddPhoto.getVisibility()==View.VISIBLE){
            backgroundAddPhoto.setVisibility(View.GONE);
            return;
        }else if (backgroundDeleteQuickButton.getVisibility()==View.VISIBLE){
            backgroundDeleteQuickButton.setVisibility(View.GONE);
            return;
        }else if (backgroundDeleteOtomatis.getVisibility()==View.VISIBLE){
            backgroundDeleteOtomatis.setVisibility(View.GONE);
            FragmentManager fm = getSupportFragmentManager();
            PengeluaranOtomatisFragment fragment = (PengeluaranOtomatisFragment)fm.findFragmentById(R.id.frame_container);
            fragment.notifyData();
        }

        if (navItemIndex == 2 && !WishlistFragment.isActive) {
            super.onBackPressed();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }


        super.onBackPressed();
    }


    public void showDate(int day,int month,int year){
        this.day=day;
        this.month=month;
        this.year=year;
        showDialog(999);
    }

    //prosedur ketika button perubahan tanggal dipanggil
    @Override
    protected Dialog onCreateDialog(int id) {

        // TODO Auto-generated method stub
        Log.d("showDate", "onClick1: "+day+","+month+","+year+","+id);
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }else if (id==998){
            return  new DatePickerDialog(this,
            myDateCustom1,thisYear,thisMonth,thisDay);
        }else if (id==997){
            return new DatePickerDialog(this,
                    myDateCustom2,thisYear,thisMonth,thisDay);
        }else if (id==995){
            return new TimePickerDialog(this,myTimePickerlistener,hour,minute,true);

        }
        return null;
    }



    public void setPointerMenu(){
        navigationView.setCheckedItem(R.id.nav_home);
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    year=arg1;
                    month=arg2;
                    day=arg3;

                    FragmentManager fm = getSupportFragmentManager();
                    KeuanganFragment fragment= (KeuanganFragment)fm.findFragmentById(R.id.frame_container);
                    fragment.refreshDate(day,month,year);
                }
            };
    private DatePickerDialog.OnDateSetListener myDateCustom1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            DayPilihan1 = dayOfMonth;
            MonthPilihan1 = month+1;
            YearPilihan1 = year;

            if ((dayOfMonth<10)&&(month+1<10)){
                txtCustom1.setText("0"+dayOfMonth+" - 0"+(month+1)+" - "+year);
            }else{
                if (dayOfMonth<10){
                    txtCustom1.setText("0"+dayOfMonth+" - "+(month+1)+" - "+year);
                }else if (month+1<10){
                    txtCustom1.setText(dayOfMonth+" - 0"+(month+1)+" - "+year);
                }else {
                    txtCustom1.setText(dayOfMonth+" - "+(month+1)+" - "+year);
                }
            }
        }
    };

    private DatePickerDialog.OnDateSetListener myDateCustom2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            DayPilihan2 = dayOfMonth;
            MonhtPilihan2 = month+1;
            YearPilihan2 = year;

            if ((dayOfMonth<10)&&(month+1<10)){
                txtCustom2.setText("0"+dayOfMonth+" - 0"+(month+1)+" - "+year);
            }else{
                if (dayOfMonth<10){
                    txtCustom2.setText("0"+dayOfMonth+" - "+(month+1)+" - "+year);
                }else if (month+1<10){
                    txtCustom2.setText(dayOfMonth+" - 0"+(month+1)+" - "+year);
                }else {
                    txtCustom2.setText(dayOfMonth+" - "+(month+1)+" - "+year);
                }
            }
        }
    };

    private TimePickerDialog.OnTimeSetListener myTimePickerlistener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            FragmentManager fm = getSupportFragmentManager();
            PengeluaranOtomatisFragment fragment = (PengeluaranOtomatisFragment)fm.findFragmentById(R.id.frame_container);


            fragment.setelahDapatJam(hourOfDay,minute);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        if (navItemIndex==1){
            getMenuInflater().inflate(R.menu.menu_aktivitas,menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int x = item.getItemId();

        if (x == R.id.menu_aktivitas){
            backgroundPopUp.setVisibility(View.VISIBLE);
            radioGrupPeriode.clearCheck();
            radioGrupPeriode.check(periodeSaved);
            pilihanPeriode = saved;
            if (periodeSaved!=R.id.radio_custom){
                milihDate.setVisibility(View.GONE);
            }
            cekBackground=true;
        }

        return super.onOptionsItemSelected(item);


    }

    @Override
    public void onResume(){
        super.onResume();

        setUpHeader();
        setHariini();
        setNotifHarian();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    public void removeD(int id){
        removeDialog(id);
    }


    public void showDialCuy(int id,int hour,int minute){
        this.hour=hour;
        this.minute=minute;
        showDialog(id);

    }

    public void setNotif(PengeluaranOtomatisDatabase pengeluaranOtomatis){
        Calendar calendar = new GregorianCalendar();

        int month = thisMonth;
        int year = thisYear;
        int thisHour = calendar.get(Calendar.HOUR_OF_DAY);
        int thisMinute = calendar.get(Calendar.MINUTE);

        // if (saat buat)>(tanggal di set) maka majukan bulan
        if (thisDay>pengeluaranOtomatis.getTanggal()){
            month=(month+1)%12;
            if (month==0){
                year++;
            }
        }else if (thisDay==pengeluaranOtomatis.getTanggal()){
            //kalo sama ya di cek jamnya
            if (thisHour>pengeluaranOtomatis.getJam()){
                month=(month+1)%12;
                if (month==0){
                    year++;
                }
            }else if (thisHour==pengeluaranOtomatis.getJam()){
                //kalo jam nya sama ya di cek menitnya
                if (thisMinute>=pengeluaranOtomatis.getMenit()){
                    month=(month+1)%12;
                    if (month==0){
                        year++;
                    }
                }
            }
        }


        //set tanggal awal
        calendar.set(Calendar.DAY_OF_MONTH,pengeluaranOtomatis.getTanggal());
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.YEAR,year);

        //set jam pada hari itu
        calendar.set(Calendar.HOUR_OF_DAY,pengeluaranOtomatis.getJam());
        calendar.set(Calendar.MINUTE,pengeluaranOtomatis.getMenit());
        calendar.set(Calendar.SECOND, 0 );

        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        intent.putExtra("id",pengeluaranOtomatis.getId());
        intent.putExtra("nama",pengeluaranOtomatis.getNamaBarang());
        intent.putExtra("harga",pengeluaranOtomatis.getHargaBarang());
        intent.putExtra("tanggal",pengeluaranOtomatis.getTanggal());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, pengeluaranOtomatis.getId(),intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) MainActivity.this.getSystemService(ALARM_SERVICE);

        //am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }


    public void deletNotif(PengeluaranOtomatisDatabase pengeluaranOtomatis){

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, pengeluaranOtomatis.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT );
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.cancel(sender);

    }


    public void munculHapusAktivitas(AktivitasKeuanganDatabase aktivitasKeuanganDatabase){
        backgroundDeleteAktivitas.setVisibility(View.VISIBLE);

        txtDetailBarang.setText(aktivitasKeuanganDatabase.getNamaBarang());
        txtDetailHarga.setText(editRupiah(String.valueOf(aktivitasKeuanganDatabase.getHargaBarang())));
        String tanggal = String.valueOf(aktivitasKeuanganDatabase.getTanggal());
        String tahun = String.valueOf(aktivitasKeuanganDatabase.getTahun());

        String bulan = "";
        switch (aktivitasKeuanganDatabase.getBulan()){
            case 1:
                bulan ="Januari";
                break;
            case 2:
                bulan = "Februari";
                break;
            case 3:
                bulan = "Maret";
                break;
            case 4:
                bulan = "April";
                break;
            case 5:
                bulan ="Mei";
                break;
            case 6:
                bulan = "Juni";
                break;
            case 7:
                bulan = "Juli";
                break;
            case 8:
                bulan = "Agustus";
                break;
            case 9:
                bulan = "September";
                break;
            case 10:
                bulan = "Oktober";
                break;
            case 11:
                bulan = "November";
                break;
            case 12:
                bulan = "Desember";
                break;
        }

        txtTanggalAktivitas.setText(tanggal+" "+bulan+" "+tahun);

    }

    public String editRupiah(String rupiah){
        String edtRuiah = rupiah;
        int length = edtRuiah.length();

        //100000

        while (length>3){
            Log.d("rupiah", "editRupiah: "+length+", "+edtRuiah);
            edtRuiah = edtRuiah.substring(0,(length-3))+"."+edtRuiah.substring((length-3),edtRuiah.length());
            length = length-3;
        }

        edtRuiah = "Rp " + edtRuiah + ",-";

        return edtRuiah;
    }

    public void setNotifHarian(){
        Calendar calendar = new GregorianCalendar();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        MainActivity.this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;

        ProfilDatabase profil = new Select()
                .from(ProfilDatabase.class)
                .where(ProfilDatabase_Table.Username.eq(this.usernameAktif))
                .querySingle();
        int updateId = IdGen.generateTimeId();
        if(profil != null) {
            if(profil.getUpdateId() == null || profil.getUpdateId() != updateId) {
                profil.setKoin(profil.getKoin() + 3);
                profil.setXp(profil.getXp() + 3);
                profil.setUpdateId(updateId);
                profil.save();
                showKoinDialog();
            }
        }

        Intent intent = new Intent(getApplicationContext(),BubleReceiver.class);
        intent.putExtra("width", width);
        intent.putExtra("height", height);
        intent.putExtra("timeId", updateId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, -1,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) MainActivity.this.getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+86400000, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void munculAddPhotoIcon(){

        backgroundAddPhoto.setVisibility(View.VISIBLE);

        IconAdapter iconAdapter;
        rvIcon.setHasFixedSize(true);
        rvIcon.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));

        iconAdapter = new IconAdapter(listIcon,this);

        rvIcon.setAdapter(iconAdapter);

    }

    private ArrayList<Integer> callIcon() {

        ArrayList<Integer> list = new ArrayList<>();

        list.add(R.drawable.icon_star);
        list.add(R.drawable.icon_hati);
        list.add(R.drawable.icon_academic);
        list.add(R.drawable.icon_gasoline);
        list.add(R.drawable.icon_transportasi_umum);
        list.add(R.drawable.icon_makan);
        list.add(R.drawable.icon_mie);
        list.add(R.drawable.icon_parkir);
        list.add(R.drawable.icon_pulsa);
        list.add(R.drawable.icon_snack);
        list.add(R.drawable.icon_stationery);

        return list;



    }

    @Override
    public void onClickIcon(int icon) {
        backgroundAddPhoto.setVisibility(View.GONE);
        FragmentManager fm = getSupportFragmentManager();
        QuickButtonFragment fragment = (QuickButtonFragment)fm.findFragmentById(R.id.frame_container);
        fragment.onIconClicked(icon);

    }

    public void munculHapusQuickButton(QuickButtonDatabase quick){

        backgroundDeleteQuickButton.setVisibility(View.VISIBLE);

        txtDataQuickButtonDelete.setText(quick.getNama());
        txtNominalQuickButtonDelete.setText(editRupiah(String.valueOf(quick.getHarga())));
        imgQuickButtonDelete.setImageResource(quick.getIcon());

    }

    public void munculHapusPengeluaranOtomatis(PengeluaranOtomatisDatabase pengeluaranOtomatisDatabase){
        backgroundDeleteOtomatis.setVisibility(View.VISIBLE);

        txtNamaOtomatis.setText(pengeluaranOtomatisDatabase.getNamaBarang());
        txtNominalOtomatis.setText(editRupiah(String.valueOf(pengeluaranOtomatisDatabase.getHargaBarang())));

        String jam, menit, tanggal;

        if (pengeluaranOtomatisDatabase.getJam()<10){
            jam = "0"+pengeluaranOtomatisDatabase.getJam();
        }else {
            jam = String.valueOf(pengeluaranOtomatisDatabase.getJam());
        }

        if (pengeluaranOtomatisDatabase.getMenit()<10){
            menit = "0"+pengeluaranOtomatisDatabase.getMenit();
        }else{
            menit = String.valueOf(pengeluaranOtomatisDatabase.getMenit());
        }

        if (pengeluaranOtomatisDatabase.getTanggal()<10){
            tanggal="0"+pengeluaranOtomatisDatabase.getTanggal();
        }else{
            tanggal=String.valueOf(pengeluaranOtomatisDatabase.getTanggal());
        }


        txtWaktuOtomatis.setText("Tanggal "+tanggal+" Jam "+jam+":"+menit);
    }

    public void showKoinDialog() {
        KoinGetDialog koinDialog = KoinGetDialog.newInstance(1);
        koinDialog.setListener(new KoinGetDialog.OnButtonClick() {
            @Override
            public void onClick() {
                android.app.FragmentManager fm = getFragmentManager();
                android.app.Fragment fragment = fm.findFragmentByTag("DIALOG");
                android.app.FragmentTransaction ft = fm.beginTransaction();
                ft.remove(fragment);
                ft.commit();
            }
        });
        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        koinDialog.show(ft, "DIALOG");
    }
}
