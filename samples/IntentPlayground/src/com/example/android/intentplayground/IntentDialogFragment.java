/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.intentplayground;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * Shows a dialog with an activity name and a list of intent flags.
 */
public class IntentDialogFragment extends DialogFragment {
    private List<String> mFlags;
    private String mActivityName;
    private static final String ARGUMENT_ACTIVITY_NAME = "activityName";
    private static final String ARGUMENT_FLAGS = "flags";

    /**
     * Creates a new IntentDialogFragment to display the given flags.
     * @param activityName The name of the activity, also the title of the dialog.
     * @param flags The list of flags to be displayed.
     * @return A new IntentDialogFragment.
     */
    public static IntentDialogFragment newInstance(String activityName, List<String> flags) {
        IntentDialogFragment fragment = new IntentDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARGUMENT_ACTIVITY_NAME, activityName);
        args.putStringArrayList(ARGUMENT_FLAGS, new ArrayList<>(flags));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mFlags = args.getStringArrayList(ARGUMENT_FLAGS);
        mActivityName = args.getString(ARGUMENT_ACTIVITY_NAME);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle(mActivityName + getString(R.string.dialog_intent_flags));
        LinearLayout rootLayout = (LinearLayout) inflater
                .inflate(R.layout.fragment_intent_dialog, container, false /* attachToRoot */);
        ListView flagsListView = rootLayout.findViewById(R.id.flag_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.dialog_list_item, R.id.item, mFlags);
        flagsListView.setAdapter(adapter);
        rootLayout.findViewById(R.id.dialog_cancel).setOnClickListener(view -> {
            getDialog().dismiss();
        });
        Button copyFlagsButton = rootLayout.findViewById(R.id.copy_flags_button);
        if (mFlags.get(0).equals("None")) {
            copyFlagsButton.setEnabled(false);
        } else {
            copyFlagsButton.setOnClickListener(view -> {
                IntentBuilderView intentBuilderView = getActivity()
                        .findViewById(R.id.root_container)
                        .findViewWithTag(BaseActivity.BUILDER_VIEW);
                intentBuilderView.selectFlags(mFlags);
                getDialog().dismiss();
                ((ScrollView) getActivity().findViewById(R.id.scroll_container)).smoothScrollTo(0,
                        Float.valueOf(intentBuilderView.getY()).intValue());
            });
        }
        return rootLayout;
    }
}
