package com.cs639.pacexchange;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.preference.DialogPreference;

public class ChangePasswordPreference extends DialogPreference {
    EditText first;
    EditText second;
    EditText third;

    public ChangePasswordPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected View onCreateDialogView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.three_edit_texts, null);
        first = v.findViewById(R.id.first);
        second = v.findViewById(R.id.second);
        third = v.findViewById(R.id.third);
        return v;
    }
}
