package com.myrescribe.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.myrescribe.R;
import com.myrescribe.adapters.ShowHistoryListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OneFragment extends Fragment {
    private  ExpandableListView expListView;
    private List<String> listDataHeader;
    private ShowHistoryListAdapter listAdapter;
    private static final String COUNT = "column-count";
    private static final String VALUE = "VALUE";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private HashMap<String, List<String>> listDataChild;
    public OneFragment() {
        // Required empty public constructor
    }
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(COUNT);
        }
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_one, container, false);

        init(rootView);


        return rootView;
    }
    public static OneFragment createNewFragment(String dataString) {
        OneFragment fragment = new OneFragment();
        Bundle args = new Bundle();
        args.putString(VALUE, dataString);
     fragment.setArguments(args);
        return fragment;
    }

    private void init(View view) {

        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);
        prepareListData();
        listAdapter = new ShowHistoryListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        for(int i=0; i < listAdapter.getGroupCount(); i++) {
            expListView.expandGroup(i);
        }
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return true; // This way the expander cannot be collapsed
            }
        });
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }

}