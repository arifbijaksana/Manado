package com.haerul.manado.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.haerul.manado.R;
import com.haerul.manado.base.BaseFragment;
import com.haerul.manado.data.api.ConnectionServer;
import com.haerul.manado.data.db.repository.MasterRepository;
import com.haerul.manado.databinding.FragmentHomeBinding;
import com.haerul.manado.ui.MainActivity;
import com.haerul.manado.ui.login.LoginActivity;
import com.haerul.manado.utils.Constants;
import com.haerul.manado.utils.Util;

import javax.inject.Inject;

public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> {

    @Inject
    ConnectionServer server;
    @Inject
    MasterRepository repository;

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    
    @Override
    public int getBindingVariable() {
        return com.haerul.manado.BR._all;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public HomeViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = getViewDataBinding();
        viewModel = ViewModelProviders.of(this, new HomeViewModel.ModelFactory(getBaseActivity(), server, repository)).get(HomeViewModel.class);
        binding.name.setText(Util.getStringPreference(getBaseActivity(), Constants.USER_NAME));
        binding.banner.setImageBitmap(Util.setImageBase64(Util.getStringPreference(getBaseActivity(), Constants.USER_BANNER)));

        if (repository.getRefBySID(Util.getStringPreference(getBaseActivity(), Constants.USER_UNIT)) != null) {
           /* GenericReferences ulp = repository.getRefBySID(Util.getStringPreference(getBaseActivity(), Constants.USER_UNIT));
            if (ulp.ref_value == 2) {
                binding.titleGrafik1.setText("GRAFIK INSPEKSI / TL PER UP3");
                binding.titleGrafik2.setText("GRAFIK C4A / TL PER UP3");
                binding.titleGrafik3.setText("GRAFIK GANGGUAN / TL PER UP3");
                setupGrafikInsWil("up3");
                setupGrafikC4AWil("up3");
                setupGrafikGgnWil("up3");
            } else {
                setupGrafikIns(ulp.parent_sid);
                setupGrafikC4A(ulp.parent_sid);
                setupGrafikGgn(ulp.parent_sid);
            }*/
        } else {
            Util.stopJobService(getBaseActivity(), "job");
            Util.putPreference(getBaseActivity(), Constants.IS_LOGIN, false);
            LoginActivity.navigateToLogin(getBaseActivity());
            Snackbar.make(binding.getRoot(), "Logging out!", Snackbar.LENGTH_SHORT).show();
            getBaseActivity().finish();
            System.exit(0);
        }
        
        
        
        binding.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MainActivity.navigateToMain(getBaseActivity());
            }
        });
        
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.bottomNavigationView.setClickable(true);
            }
        },1000);
    }
    
}
