package com.example.surmeet.statistics;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScatterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScatterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScatterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    ScatterChart scatterChart;
    ArrayList<String> s=new ArrayList<>();
    int c=0;

    public ScatterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScatterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScatterFragment newInstance(String param1, String param2) {
        ScatterFragment fragment = new ScatterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bar, container, false);
        scatterChart=(ScatterChart)view.findViewById(R.id.scattergraph);

        Bundle bundle = getArguments();

        if (bundle != null)
            s=bundle.getStringArrayList("list");

        ArrayList<Entry> scatterEntries=new ArrayList<Entry>();
        final ScatterDataSet scatterDataSet=new ScatterDataSet(scatterEntries,"Dates");
        scatterEntries.add(new Entry(Integer.parseInt(s.get(c++)),0));


        final ArrayList<String> theDates=new ArrayList<>();
        theDates.add("Full Time Male");
        theDates.add("Full Time Female");
        theDates.add("Para Contract Male");
        theDates.add("Para Contract Female");

        ScatterData scatterData=new ScatterData(theDates,scatterDataSet);
        scatterDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        Log.i("arrayList",s.toString());
        Log.i("theDates",theDates.toString());


//        YAxis yAxis=scatterChart.getAxis(YAxis.AxisDependency.LEFT);
  //      yAxis.setAxisMinValue(0);

        scatterChart.setData(scatterData);
        scatterChart.setEnabled(true);
        scatterChart.setDragEnabled(true);
        scatterChart.setScaleEnabled(true);

        Log.i("BLAH","inside  on create  of fragment");

        try
        {
            Log.i("BLAH","inside try catch of fragment");

            for(int i=0;i<3;i++)
            {
                scatterDataSet.addEntry(new Entry(Integer.parseInt(s.get(c++)),i+1));
            }

            scatterChart.notifyDataSetChanged();
            scatterChart.invalidate();
            scatterChart.setVisibility(View.VISIBLE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

  /*  @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
*/
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
