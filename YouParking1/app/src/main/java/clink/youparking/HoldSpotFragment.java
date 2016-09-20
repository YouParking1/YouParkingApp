package clink.youparking;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HoldSpotFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HoldSpotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HoldSpotFragment extends Fragment implements AsyncResponse {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView coords;
    double mLat, mLong;
    EditText comments;
    Spinner tickets;
    Button holdBtn;

    private OnFragmentInteractionListener mListener;

    public HoldSpotFragment() {
        // Required empty public constructor
    }



    OnHoldPressed onHoldPressed;
    public interface OnHoldPressed {
        // Add another int paramter
        public void onButtonPressed(int position);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HoldSpotFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HoldSpotFragment newInstance(String param1, String param2) {
        HoldSpotFragment fragment = new HoldSpotFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hold_spot, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        coords = (TextView) getView().findViewById(R.id.showCoords);
        if (User.myLocation != null)
            coords.setText("COORDINATES ARE " + Double.toString(User.myLocation.latitude) + " " + Double.toString(User.myLocation.longitude));

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        super.onViewCreated(view, savedInstanceState);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onHold() {

    }

//    public void onHold(View view) {
//        comments = (EditText)getView().findViewById(R.id.comments);
//        tickets = (Spinner)getView().findViewById(R.id.spinner1);
//        String comment = comments.getText().toString();
//        String ticket = tickets.getSelectedItem().toString();
//        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
//        backgroundWorker.delegate = this;
//        backgroundWorker.execute("hold", ticket, "1", Double.toString(mLat), Double.toString(mLong), comment);
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onHoldPressed = (OnHoldPressed) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() +
            " must implement OnHoldPressed");
        }
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

    @Override
    public void processFinish(String output) {

    }
}
