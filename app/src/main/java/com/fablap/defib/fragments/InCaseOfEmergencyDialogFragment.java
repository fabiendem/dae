package com.fablap.defib.fragments;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.avast.android.dialogs.core.BaseDialogFragment;
import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.iface.IPositiveButtonDialogListener;
import com.fablap.defib.R;

/**
 * Created by fabien on 30/03/2015.
 */
public class InCaseOfEmergencyDialogFragment extends SimpleDialogFragment {
    public static String TAG = "InCaseOfEmergencyFragment";

    public static void show(FragmentActivity activity) {
        new InCaseOfEmergencyDialogFragment().show(activity.getSupportFragmentManager(), TAG);
    }

    @Override
    public BaseDialogFragment.Builder build(BaseDialogFragment.Builder builder) {

        builder.setTitle(getString(R.string.emerg_title));
        builder.setView(LayoutInflater.from(getActivity()).inflate(R.layout.fragment_in_case_emergency, null));
        builder.setPositiveButton(getString(R.string.close), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (IPositiveButtonDialogListener listener : getPositiveButtonDialogListeners()) {
                    listener.onPositiveButtonClicked(mRequestCode);
                }
                dismiss();
            }
        });

        return builder;
    }

}
