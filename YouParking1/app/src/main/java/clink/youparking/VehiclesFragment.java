package clink.youparking;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VehiclesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VehiclesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VehiclesFragment extends Fragment implements AsyncResponse {

    TextView vehicleMakeText, vehicleModelText, vehicleYearText, vehicleColorText;
    ImageView vehicleImage;
    private RequestHandler requestHandler;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vehicles, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity());
        backgroundWorker.delegate = this;
        backgroundWorker.execute("getVehicles");
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


//    private void getImage(int id) {
//        class GetImage extends AsyncTask<String,Void,Bitmap> {
//            ProgressDialog loading;
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                loading = ProgressDialog.show(getActivity(), "Uploading...", null,true,true);
//            }
//
//            @Override
//            protected void onPostExecute(Bitmap b) {
//                super.onPostExecute(b);
//                loading.dismiss();
//                vehicleImage.setImageBitmap(b);
//            }
//
//            @Override
//            protected Bitmap doInBackground(String... params) {
//                String id = params[0];
//                String add = "http://www.troyparking.com/getImage.php?id="+id;
//
//                System.out.println("Link with id:" + add);
//
//                URL url = null;
//                Bitmap image = null;
//                try {
//                    url = new URL(add);
//                    image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return image;
//            }
//        }
//
//        GetImage gi = new GetImage();
//        gi.execute(Integer.toString(id));
//    }



    @Override
    public void processFinish(String output) throws JSONException {

        JSONArray jsonArray = new JSONArray(output);

        if (!User.vehicles.isEmpty()) {
            User.vehicles.clear();
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            User.vehicles.add(new Vehicles (jsonObject.getInt("id"), jsonObject.getString("Make"),
            jsonObject.getString("Model"), jsonObject.getInt("Year"), jsonObject.getString("Color")));
        }

        LinearLayout linearLayout = (LinearLayout) getActivity().findViewById(R.id.populate_vehicles);

        if (User.vehicles.size() > 0) {
            for (int i = 0; i < User.vehicles.size(); i++) {

                User.id = User.vehicles.get(i).getId();

                Bundle bundle = new Bundle();
                bundle.putInt("VEHICLEID", User.vehicles.get(i).getId());
                bundle.putString("MAKE", User.vehicles.get(i).getMake());
                bundle.putString("MODEL", User.vehicles.get(i).getModel());
                bundle.putInt("YEAR", User.vehicles.get(i).getYear());
                bundle.putString("COLOR", User.vehicles.get(i).getColor());
                bundle.putInt("ID", i);

//                getImage(User.vehicles.get(i).getId());

                System.out.println("VehicleID: " + User.vehicles.get(i).getId());
                System.out.println("Make: " + User.vehicles.get(i).getMake());
                System.out.println("Model: " + User.vehicles.get(i).getModel());
                System.out.println("Year: " + User.vehicles.get(i).getYear());
                System.out.println("Color: " + User.vehicles.get(i).getColor());
                System.out.println("ID: " + i);
                System.out.println("Size of Array: " + User.vehicles.size());

                Fragment fragment = new DynamicVehicleFragment();
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().add(linearLayout.getId(), fragment).commit();
            }
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
