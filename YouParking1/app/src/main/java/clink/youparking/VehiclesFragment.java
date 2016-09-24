package clink.youparking;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VehiclesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VehiclesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VehiclesFragment extends Fragment implements AsyncResponse {

    Spinner smake, smodel, syear, scolor;
    String MakeTxt, ModelTxt;
    public String AssetJSONFile(String filename, Context context) throws IOException {
        //AssetManager manager = context.getAssets();
        InputStream file = getActivity().getAssets().open(filename);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();
        String json = new String(formArray, "UTF-8");
        return json;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public VehiclesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VehiclesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VehiclesFragment newInstance(String param1, String param2) {
        VehiclesFragment fragment = new VehiclesFragment();
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        smake = (Spinner)getView().findViewById(R.id.sMake);
        smodel = (Spinner)getView().findViewById(R.id.sModel);
        syear = (Spinner)getView().findViewById(R.id.sYear);

        //ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
        try {
            JSONObject jsonobject = new JSONObject(AssetJSONFile("json/vehicles.json", getActivity()));
            //JSONObject jsonobject = new JSONObject(jsonLocation);
            JSONArray jarray = (JSONArray) jsonobject.getJSONArray("makes");
            ArrayList<String> strArr = new ArrayList<>();
            strArr.add("Please Select a Make");
            for(int i=0;i<jarray.length();i++)
            {
                JSONObject jb =(JSONObject) jarray.get(i);
                String m = jb.getString("make");
                strArr.add(m);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_dropdown_item, strArr);
            smake.setAdapter(adapter);

            smake.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    MakeTxt = selectedItem;
                    picksMake(selectedItem);
                } // to close the onItemSelected
                public void onNothingSelected(AdapterView<?> parent)
                {

                }
            });

            smodel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    ModelTxt = selectedItem;
                    picksModel(selectedItem);
                } // to close the onItemSelected
                public void onNothingSelected(AdapterView<?> parent)
                {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void picksMake(String str)
    {
        try {
            JSONObject jsonobject = new JSONObject(AssetJSONFile("json/vehicles.json", getActivity()));
            //JSONObject jsonobject = new JSONObject(jsonLocation);
            JSONArray jarray = (JSONArray) jsonobject.getJSONArray("makes");
            int index = 0;
            for(int i=0;i<jarray.length();i++)
            {
                JSONObject jb =(JSONObject) jarray.get(i);
                String m = jb.getString("make");
                if(m.equals(str)) //change to option chosen in the make dropdown
                {
                    index = i;
                }
                //JSONArray jarray2 = (JSONArray) jb.getJSONArray("models");
            }
            JSONObject jb =(JSONObject) jarray.get(index);
            JSONArray jarray2 = (JSONArray) jb.getJSONArray("models");
            ArrayList<String> strArr = new ArrayList<>();
            strArr.add("Please Select a Model");
            for(int j=0;j<jarray2.length();j++)
            {
                JSONObject jb2 =(JSONObject) jarray2.get(j);
                String model = jb2.getString("model");
                strArr.add(model);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item, strArr);
                smodel.setAdapter(adapter);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void picksModel(String str)
    {
        try {
            JSONObject jsonobject = new JSONObject(AssetJSONFile("json/vehicles.json", getActivity()));
            //JSONObject jsonobject = new JSONObject(jsonLocation);
            JSONArray jarray = (JSONArray) jsonobject.getJSONArray("makes");
            int index = 0;
            for(int i=0;i<jarray.length();i++)
            {
                JSONObject jb =(JSONObject) jarray.get(i);
                String m = jb.getString("make");
                if(m.equals(MakeTxt)) //change to option chosen in the make dropdown
                {
                    index = i;
                }
            }
            JSONObject jb =(JSONObject) jarray.get(index);
            JSONArray jarray2 = (JSONArray) jb.getJSONArray("models");
            for(int i=0;i<jarray2.length();i++)
            {
                JSONObject jb2 =(JSONObject) jarray2.get(i);
                String m = jb2.getString("model");
                if(m.equals(ModelTxt)) //change to option chosen in the make dropdown
                {
                    index = i;
                }
            }
            JSONObject jb3 =(JSONObject) jarray2.get(index);
            JSONArray jarray3 = (JSONArray) jb3.getJSONArray("years");
            ArrayList<String> strArr = new ArrayList<>();
            strArr.add("Please Select a Year");
            for(int j=0;j<jarray3.length();j++)
            {
                jb3 =(JSONObject) jarray3.get(j);
                String model = jb3.getInt("year") + "";
                strArr.add(model);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item, strArr);
                syear.setAdapter(adapter);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_vehicle_registration, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void processFinish(String output) throws JSONException {
        if (output.contains("success")) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
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
