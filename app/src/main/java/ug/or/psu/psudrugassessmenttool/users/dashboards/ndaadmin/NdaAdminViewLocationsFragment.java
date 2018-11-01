package ug.or.psu.psudrugassessmenttool.users.dashboards.ndaadmin;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.globalactivities.PharmaciesMapActivity;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class NdaAdminViewLocationsFragment extends Fragment {

    View view;
    TextView pharmacist_number, assigned_pharmacists, pharmacies_number, verified_pharmacies;
    Button view_pharmacy_map;
    HelperFunctions helperFunctions;

    public NdaAdminViewLocationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_nda_admin_view_locations, container, false);

        pharmacist_number = view.findViewById(R.id.pharmacist_number);
        assigned_pharmacists = view.findViewById(R.id.assigned_pharmacists);
        pharmacies_number = view.findViewById(R.id.pharmacies_number);
        verified_pharmacies = view.findViewById(R.id.verified_pharmacies);

        view_pharmacy_map = view.findViewById(R.id.view_pharmacy_map);

        view_pharmacy_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PharmaciesMapActivity.class);
                Objects.requireNonNull(getContext()).startActivity(intent);
            }
        });

        helperFunctions = new HelperFunctions(getContext());

        fetch_stats();

        return view;
    }

    public void fetch_stats(){
        String network_address = helperFunctions.getIpAddress()
                + "get_locations_stats.php";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            pharmacist_number.setText(response.getString("pharmacist_number"));
                            assigned_pharmacists.setText(response.getString("assigned_pharmacists"));
                            pharmacies_number.setText(response.getString("pharmacies_number"));
                            verified_pharmacies.setText(response.getString("verified_pharmacies"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                fetch_stats();
            }
        });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

}
