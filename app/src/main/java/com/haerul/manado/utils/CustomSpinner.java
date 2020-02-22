package com.haerul.manado.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.haerul.manado.data.db.repository.MasterRepository;
import com.haerul.manado.data.entity.GenericReferences;

import java.util.ArrayList;
import java.util.List;

public class CustomSpinner extends Spinner {

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSpinnerValue(MasterRepository repository, String category, String value) {
        List<GenericReferences> references = repository.getRefByCategory(category);
        List<String> spinnerMap  = new ArrayList<>();
        spinnerMap.add("");
        for (int i = 0; i < references.size(); i++) {
            GenericReferences ref = references.get(i);
            spinnerMap.add(ref.ref_name);
        }
        ArrayAdapter<String> spnLegalAdapter = new ArrayAdapter<>(
                this.getContext(), android.R.layout.simple_spinner_dropdown_item,
                spinnerMap
        );
        this.setAdapter(spnLegalAdapter);
        if (!Util.isNullOrEmpty(value)) {
            this.setSelection(spnLegalAdapter.getPosition(repository.getRefBySID(value).ref_name));
        }
    }

    public void setSpinnerValue(MasterRepository repository, String category, String value, String where) {
        List<GenericReferences> references = repository.getRefByCategory(category, where);
        List<String> spinnerMap  = new ArrayList<>();
        spinnerMap.add("");
        for (int i = 0; i < references.size(); i++) {
            GenericReferences ref = references.get(i);
            spinnerMap.add(ref.ref_name);
        }
        ArrayAdapter<String> spnLegalAdapter = new ArrayAdapter<>(
                this.getContext(), android.R.layout.simple_spinner_dropdown_item,
                spinnerMap
        );
        this.setAdapter(spnLegalAdapter);
        if (!Util.isNullOrEmpty(value)) {
            this.setSelection(spnLegalAdapter.getPosition(repository.getRefBySID(value).ref_name));
        }
    }

    public void setSpinnerValueByParent(MasterRepository repository, String category, String value, String where) {
        List<GenericReferences> references = repository.getRefByCategoryAndParent(category, where);
        List<String> spinnerMap  = new ArrayList<>();
        spinnerMap.add("");
        for (int i = 0; i < references.size(); i++) {
            GenericReferences ref = references.get(i);
            spinnerMap.add(ref.ref_name);
        }
        ArrayAdapter<String> spnLegalAdapter = new ArrayAdapter<>(
                this.getContext(), android.R.layout.simple_spinner_dropdown_item,
                spinnerMap
        );
        this.setAdapter(spnLegalAdapter);
        if (!Util.isNullOrEmpty(value)) {
            this.setSelection(spnLegalAdapter.getPosition(repository.getRefBySID(value).ref_name));
        }
    }

    public String getSpinnerValueName(MasterRepository repository, String category) {
        int position = this.getSelectedItemPosition();
        List<GenericReferences> references = repository.getRefByCategory(category);
        List<String> spinnerMap  = new ArrayList<>();
        spinnerMap.add("");
        for (int i = 0; i < references.size(); i++) {
            GenericReferences ref = references.get(i);
            spinnerMap.add(ref.ref_name);
        }
        return spinnerMap.get(position);
    }

    public String getSpinnerValueSID(MasterRepository repository, String category) {
        int position = this.getSelectedItemPosition();
        List<GenericReferences> references = repository.getRefByCategory(category);
        List<String> spinnerMap  = new ArrayList<>();
        spinnerMap.add("");
        for (int i = 0; i < references.size(); i++) {
            GenericReferences ref = references.get(i);
            spinnerMap.add(ref.ref_sid);
        }
        return spinnerMap.get(position);
    }

    public String getSpinnerValueSID(MasterRepository repository, String category, String where) {
        int position = this.getSelectedItemPosition();
        List<GenericReferences> references = repository.getRefByCategory(category, where);
        List<String> spinnerMap  = new ArrayList<>();
        spinnerMap.add("");
        for (int i = 0; i < references.size(); i++) {
            GenericReferences ref = references.get(i);
            spinnerMap.add(ref.ref_sid);
        }
        return spinnerMap.get(position);
    }

    public String getSpinnerValueSIDByParent(MasterRepository repository, String category, String where) {
        int position = this.getSelectedItemPosition();
        List<GenericReferences> references = repository.getRefByCategoryAndParent(category, where);
        List<String> spinnerMap  = new ArrayList<>();
        spinnerMap.add("");
        for (int i = 0; i < references.size(); i++) {
            GenericReferences ref = references.get(i);
            spinnerMap.add(ref.ref_sid);
        }
        return spinnerMap.get(position);
    }

    public GenericReferences getSpinnerValue(MasterRepository repository, String category) {
        int position = this.getSelectedItemPosition();
        List<GenericReferences> references = repository.getRefByCategory(category);
        List<GenericReferences> spinnerMap  = new ArrayList<>();
        spinnerMap.add(null);
        for (int i = 0; i < references.size(); i++) {
            GenericReferences ref = references.get(i);
            spinnerMap.add(ref);
        }
        return spinnerMap.get(position);
    }


    public void setupSpinnerGeneric(MasterRepository repository, String category) {
        List<GenericReferences> references = repository.getRefByCategory(category);
        List<String> spinnerMap  = new ArrayList<>();
        spinnerMap.add("");
        for (int i = 0; i < references.size(); i++) {
            GenericReferences ref = references.get(i);
            spinnerMap.add(ref.ref_name);
        }
        ArrayAdapter<String> spnLegalAdapter = new ArrayAdapter<>(
                this.getContext(), android.R.layout.simple_spinner_dropdown_item,
                spinnerMap
        );
        this.setAdapter(spnLegalAdapter);
    }

    public void setupSpinnerGenericByParent(MasterRepository repository, String category, String parent) {
        List<GenericReferences> references = repository.getRefByCategoryAndParent(category, parent);
        List<String> spinnerMap  = new ArrayList<>();
        spinnerMap.add("");
        for (int i = 0; i < references.size(); i++) {
            GenericReferences ref = references.get(i);
            spinnerMap.add(ref.ref_name);
        }
        ArrayAdapter<String> spnLegalAdapter = new ArrayAdapter<>(
                this.getContext(), android.R.layout.simple_spinner_dropdown_item,
                spinnerMap
        );
        this.setAdapter(spnLegalAdapter);
    }

    public void setupSpinnerGeneric(MasterRepository repository, String category, String where) {
        List<GenericReferences> references = repository.getRefByCategory(category, where);
        List<String> spinnerMap  = new ArrayList<>();
        spinnerMap.add("");
        for (int i = 0; i < references.size(); i++) {
            GenericReferences ref = references.get(i);
            spinnerMap.add(ref.ref_name);
        }
        ArrayAdapter<String> spnLegalAdapter = new ArrayAdapter<>(
                this.getContext(), android.R.layout.simple_spinner_dropdown_item,
                spinnerMap
        );
        this.setAdapter(spnLegalAdapter);
    }
    
}
