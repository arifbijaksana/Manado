package com.haerul.manado.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.haerul.manado.R;
import com.haerul.manado.base.BaseActivity;
import com.haerul.manado.data.api.ConnectionServer;
import com.haerul.manado.data.db.repository.MasterRepository;
import com.haerul.manado.databinding.ActivityMainBaseBinding;
import com.haerul.manado.ui.home.HomeFragment;
import com.haerul.manado.utils.Util;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends BaseActivity<ActivityMainBaseBinding, MainViewModel> 
        implements BottomNavigationView.OnNavigationItemSelectedListener {
    
    @Inject
    MasterRepository repository;
    @Inject
    ConnectionServer server;
    MainViewModel viewModel;

    @SuppressLint("StaticFieldLeak")
    public static ActivityMainBaseBinding binding;
    public static final int INS_ID = 0x101;
    public static final int HOME = 0x102;
    public static final int PROFILE_ID = 0x104;
    public static final int GGN_ID = 0x105;
    public static final int C4A_ID = 0x106;
    public static BottomNavigationView bottomNavigationView;
    
    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main_base;
    }

    @Override
    public MainViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        viewModel = ViewModelProviders.of(this, new MainViewModel.ModelFactory(this, server, repository)).get(MainViewModel.class);
        
        bottomNavigationView = binding.bnMain;
        
        checkLogin();
        
        requestPermission();
        setupBottomNavMenu();
        
        binding.bnMain.setOnNavigationItemSelectedListener(this);
//        loadFragment(new HomeFragment(), "HOME");

        Util.startJobService(this, "job");
        // startService();
    }

    private void setupBottomNavMenu() {
        Menu menu = binding.bnMain.getMenu();
        menu.add(Menu.NONE, HOME, Menu.NONE, "HOME").setIcon(R.drawable.ic_home);
        
        int j = 0;
        /*for (UserRoles role : repository.getUserRoles(Util.getUserSID(this))) {
            GenericReferences ref = repository.getRefBySID(role.ur_role_sid);
            
            if (ref.ref_value == Constants.USER_C4A_WILAYAH || ref.ref_value == Constants.USER_C4A) {
                menu.add(Menu.NONE, C4A_ID, Menu.NONE, "C4A").setIcon(R.drawable.ic_list);
            }
            else if (ref.ref_value == Constants.USER_INSPEKSI) {
                menu.add(Menu.NONE, INS_ID, Menu.NONE, "INSPEKSI").setIcon(R.drawable.ic_list);
            }
            else if (ref.ref_value == Constants.USER_TL_RINTIS_VENDOR || ref.ref_value == Constants.USER_TL_RINTIS_YANTEK ||
                    ref.ref_value == Constants.USER_TL_HARDIST_VANDOR || ref.ref_value == Constants.USER_TL_HARDIST_YANTEK ||
                    ref.ref_value == Constants.USER_TL_HARDIST_PDKB || ref.ref_value == Constants.USER_TL_SPV_PEMBANGKIT ||
                    ref.ref_value == Constants.USER_TL_SPV_TRANS_ENERGI || ref.ref_value == Constants.USER_TL_SPV_ADM_UMUM
            ) {
                if (!menu.getItem(j).getTitle().toString().equals("TL-INSPEKSI")) {
                    menu.add(Menu.NONE, INS_ID, Menu.NONE, "TL-INSPEKSI").setIcon(R.drawable.ic_list);
                }
            }
            else if (ref.ref_value == Constants.USER_TL_GANGGUAN_YANTEK) {
                menu.add(Menu.NONE, GGN_ID, Menu.NONE, "TL-GANGGUAN").setIcon(R.drawable.ic_list);
            }
            
            invalidateOptionsMenu();
            j++;
        }*/

        menu.add(Menu.NONE, PROFILE_ID, Menu.NONE, "PROFILE").setIcon(R.drawable.ic_profile);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()){
            case HOME:
                fragment = new HomeFragment();
                break;
            case INS_ID:
        }
        setTag(menuItem.getTitle().toString());
        return changeFragment(fragment, getTag());
    }

    public boolean loadFragment(Fragment fragment, String tag) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, fragment, tag)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
            Log.w("TAG", tag);
            return true;
        }
        return false;
    }
    
    public boolean changeFragment(Fragment fragment, String tagFragmentName) {

        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        Fragment currentFragment = mFragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName);
        if (fragmentTemp == null) {
            fragmentTemp = fragment;
            fragmentTransaction.add(R.id.main_container, fragmentTemp, tagFragmentName);
        } else {
            fragmentTransaction.show(fragmentTemp);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNowAllowingStateLoss();
        
        return true;
    }

    private void requestPermission() {
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.CAMERA)
                .check();
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            TedPermission.with(MainActivity.this)
                    .setPermissionListener(permissionlistener)
                    .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setPermissions(
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.CAMERA)
                    .check();
        }
    };

    public static void navigateToMain(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }
    
    public static void setNavigationSelectedItemId(int id) {
        binding.bnMain.setSelectedItemId(id);
    }
    
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
    
}
