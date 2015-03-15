package com.fabiendem.defib68.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fabiendem.defib68.R;
import com.fabiendem.defib68.utils.UiUtils;

/**
 * Created by Fabien on 07/03/15.
 */
public class TutorialFragment extends Fragment {
    private static final String PAGE_NUMBER_ARG = "PAGE_NUMBER_ARG";
    private static final String TEXT_RESOURCE_ID_ARG = "TEXT_RESOURCE_ID_ARG";

    private int mPageNumber;
    private int mTextResourceId;

    public static TutorialFragment newInstance(int pageNumber, int textResourceId) {
        TutorialFragment tutorialFragment = new TutorialFragment();
        Bundle args = new Bundle();
        args.putInt(PAGE_NUMBER_ARG, pageNumber);
        args.putInt(TEXT_RESOURCE_ID_ARG, textResourceId);
        tutorialFragment.setArguments(args);
        return tutorialFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(PAGE_NUMBER_ARG, 0);
        mTextResourceId = getArguments().getInt(TEXT_RESOURCE_ID_ARG);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);

        TextView[] textViewArray = new TextView[3];
        textViewArray[0] = (TextView) view.findViewById(R.id.tv_tip_3);
        textViewArray[1] = (TextView) view.findViewById(R.id.tv_tip_2);
        textViewArray[2] = (TextView) view.findViewById(R.id.tv_tip_1);

        textViewArray[mPageNumber].setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tip_24dp, 0, 0, 0);
        textViewArray[mPageNumber].setBackgroundResource(R.drawable.bkg_tip_active);
        textViewArray[mPageNumber].setPadding(UiUtils.dpToPx(getActivity(), 16), 0, UiUtils.dpToPx(getActivity(), 16), 0);

        return view;
    }
}
