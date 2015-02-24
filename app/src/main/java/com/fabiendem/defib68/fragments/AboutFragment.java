package com.fabiendem.defib68.fragments;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.avast.android.dialogs.core.BaseDialogFragment;
import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.iface.IPositiveButtonDialogListener;
import com.fabiendem.defib68.R;

/**
 * Created by Fabien on 15/11/14.
 */
public class AboutFragment extends SimpleDialogFragment {

    public static String TAG = "AboutFragment";

    public static void show(FragmentActivity activity) {
        new AboutFragment().show(activity.getSupportFragmentManager(), TAG);
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
