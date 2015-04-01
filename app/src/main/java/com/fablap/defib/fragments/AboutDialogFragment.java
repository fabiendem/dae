package com.fablap.defib.fragments;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.avast.android.dialogs.core.BaseDialogFragment;
import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.iface.IPositiveButtonDialogListener;
import com.fablap.defib.R;

/**
 * Created by Fabien on 15/11/14.
 */
public class AboutDialogFragment extends SimpleDialogFragment {

    public static final String TAG = "AboutFragment";

    public static void show(FragmentActivity activity) {
        new AboutDialogFragment().show(activity.getSupportFragmentManager(), TAG);
    }

    @Override
    public BaseDialogFragment.Builder build(BaseDialogFragment.Builder builder) {

        builder.setTitle(getString(R.string.about));
        builder.setView(LayoutInflater.from(getActivity()).inflate(R.layout.fragment_about, null));
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
