package ahadoo.com.collect.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ahadoo.com.collect.R;
import ahadoo.com.collect.adapter.LanguageDialogAdapter;
import ahadoo.com.collect.model.Survey;

public class LanguageDialogFragment extends android.support.v4.app.DialogFragment {

    private RecyclerView mRecyclerView;

    private RecyclerView.Adapter adapter;

    private List<String> languages;

    private Survey survey;

    private Context context;

    public static LanguageDialogFragment getInstance(Context context, List<String> languages, Survey survey){

        LanguageDialogFragment languageDialogFragment = new LanguageDialogFragment();

        languageDialogFragment.languages = languages;

        languageDialogFragment.survey = survey;

        languageDialogFragment.context = context;

        return languageDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.lang_menu_grid, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.lang_menu_recycler);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateView();

        return v;
    }

    public void updateView(){

        adapter = new LanguageDialogAdapter(this, languages, survey);

        mRecyclerView.setAdapter(adapter);
    }

}
