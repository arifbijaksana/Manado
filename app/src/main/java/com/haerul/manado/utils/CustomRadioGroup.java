package com.haerul.manado.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.haerul.manado.R;
import com.haerul.manado.data.db.repository.MasterRepository;
import com.haerul.manado.data.entity.GenericReferences;

import java.util.ArrayList;
import java.util.List;

public class CustomRadioGroup extends RadioGroup {
    
    public CustomRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public String getRadioGroupValueSID(MasterRepository repository, String cat) {
        RadioGroup rg = this;
        int radioButtonId = rg.getCheckedRadioButtonId();
        if (rg.getCheckedRadioButtonId() != -1) {
            List<GenericReferences> references = repository.getRefByCategory(cat);
            List<String> map  = new ArrayList<>();
            for (int i = 0; i < references.size(); i++){
                GenericReferences ref = references.get(i);
                map.add(ref.ref_sid);
            }
            return map.get(radioButtonId - 1);
        }
        return null;
    }

    public void setupRadioGroup(MasterRepository repository, String cat) {
        List<GenericReferences> references = repository.getRefByCategory(cat);
        LayoutParams layoutParams;
        for(int i = 0; i < references.size(); i++){
            GenericReferences reff = references.get(i);
            RadioGroup rg = this;
            RadioButton radioButton = new RadioButton(rg.getContext());
            radioButton.setText(reff.ref_name);
            radioButton.setId(reff.ref_value);
            radioButton.setButtonDrawable(android.R.color.transparent);
            
            radioButton.setBackground(getResources().getDrawable(R.drawable.rb_bg));
            
            switch (reff.ref_value) {
                case 1:
                    radioButton.setBackground(getResources().getDrawable(R.drawable.rb_bg_1));
                    radioButton.setTextColor(getResources().getColor(R.color.colorWaringDark));
                    break;
                case 2:
                    radioButton.setBackground(getResources().getDrawable(R.drawable.rb_bg_2));
                    radioButton.setTextColor(getResources().getColor(R.color.colorWarningExtraDark2));
                    break;
                case 3:
                    radioButton.setBackground(getResources().getDrawable(R.drawable.rb_bg_3));
                    radioButton.setTextColor(getResources().getColor(R.color.colorDangerExtraDark));
                    break;
                default:
                    radioButton.setBackground(getResources().getDrawable(R.drawable.rb_bg));
                    radioButton.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
            }
            
            int left= (int) getResources().getDimension(R.dimen._15dp);
            int top=(int) getResources().getDimension(R.dimen._10dp);
            int right=(int) getResources().getDimension(R.dimen._15dp);
            int bottom=(int) getResources().getDimension(R.dimen._10dp);
            radioButton.setPadding(left, top, right, bottom);
            radioButton.setTextSize(17f);
            
            layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i == 0) {
                layoutParams.setMargins(0, 0, 0, 0);
            } else {
                layoutParams.setMargins((int) getResources().getDimension(R.dimen._10dp), 0, 0, 0);
            }
            rg.addView(radioButton, layoutParams);
        }
    }

    public void setRadioGroupValue(MasterRepository repository, String cat, String value, boolean enabled) {
        List<GenericReferences> references = repository.getRefByCategory(cat);
        LayoutParams layoutParams;
        for(int i = 0; i < references.size(); i++){
            GenericReferences reff = references.get(i);
            RadioGroup rg = this;
            RadioButton radioButton = new RadioButton(rg.getContext());
            radioButton.setText(reff.ref_name);
            radioButton.setId(i);
            radioButton.setButtonDrawable(android.R.color.transparent);

            radioButton.setBackground(getResources().getDrawable(R.drawable.rb_bg));

            switch (reff.ref_value) {
                case 1:
                    radioButton.setBackground(getResources().getDrawable(R.drawable.rb_bg_1));
                    radioButton.setTextColor(getResources().getColor(R.color.colorWaringDark));
                    break;
                case 2:
                    radioButton.setBackground(getResources().getDrawable(R.drawable.rb_bg_2));
                    radioButton.setTextColor(getResources().getColor(R.color.colorWarningExtraDark2));
                    break;
                case 3:
                    radioButton.setBackground(getResources().getDrawable(R.drawable.rb_bg_3));
                    radioButton.setTextColor(getResources().getColor(R.color.colorDangerExtraDark));
                    break;
                default:
                    radioButton.setBackground(getResources().getDrawable(R.drawable.rb_bg));
                    radioButton.setTextColor(getResources().getColor(R.color.colorPrimaryExtraLight));
            }

            int left= (int) getResources().getDimension(R.dimen._15dp);
            int top=(int) getResources().getDimension(R.dimen._10dp);
            int right=(int) getResources().getDimension(R.dimen._15dp);
            int bottom=(int) getResources().getDimension(R.dimen._10dp);
            radioButton.setPadding(left, top, right, bottom);
            radioButton.setTextSize(17f);

            layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i == 0) {
                layoutParams.setMargins(0, 0, 0, 0);
            } else {
                layoutParams.setMargins((int) getResources().getDimension(R.dimen._10dp), 0, 0, 0);
            }
            rg.addView(radioButton, layoutParams);
            if (reff.ref_sid.equals(value)) {
                radioButton.setChecked(true);
            }
            radioButton.setEnabled(false);
        }
    }
}
