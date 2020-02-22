package com.haerul.manado.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.haerul.manado.ui.MainActivity;

import dagger.android.support.AndroidSupportInjection;


public abstract class BaseFragment<T extends ViewDataBinding, V extends BaseViewModel> extends Fragment {

    private BaseActivity activity;
    private View rootView;
    private T binding;
    private V viewModel;
    public String title;

    public abstract int getBindingVariable();

    public abstract @LayoutRes int getLayoutId();

    public abstract V getViewModel();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.activity = activity;
            activity.onFragmentAttached();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        performDependencyInjection();
        super.onCreate(savedInstanceState);
        viewModel = getViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        rootView = binding.getRoot();
        return rootView;
    }

    @Override
    public void onDetach() {
        activity = null;
        super.onDetach();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setVariable(getBindingVariable(), viewModel);
        binding.executePendingBindings();
    }

    public BaseActivity getBaseActivity() {
        return activity;
    }

    public T getViewDataBinding() {
        return binding;
    }

    public void hideKeyboard() {
        if (activity != null) {
            activity.hideKeyboard();
        }
    }

    public boolean isNetworkConnected() {
        return activity != null && activity.isNetworkConnected();
    }

    public void openActivityOnTokenExpire() {
        if (activity != null) {
            activity.openActivityOnTokenExpire();
        }
    }

    private void performDependencyInjection() {
        AndroidSupportInjection.inject(this);
    }

    public interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }

    public void setFragmentTitle(String title) {
        this.title = title;
    }
    
    public void onResume(){
        super.onResume();
        // Set title bar
        if (title != null) {
            ((MainActivity) getBaseActivity()).setActionBarTitle(title);
        }

    }
}
