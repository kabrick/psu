package ug.or.psu.psudrugassessmenttool.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.globalactivities.EditPharmacyInformationActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.EditYourPharmacies;
import ug.or.psu.psudrugassessmenttool.globalactivities.ViewPharmacyLocationActivity;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.models.Pharmacies;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class EditYourPharmaciesAdapter extends RecyclerView.Adapter<EditYourPharmaciesAdapter.MyViewHolder>  {

    private List<Pharmacies> pharmaciesList;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView pharmacy_name;
        Button edit_your_pharmacies, remove_your_pharmacies;

        MyViewHolder(View view) {
            super(view);

            // Text views
            pharmacy_name = view.findViewById(R.id.pharmacy_name_your_pharmacies);

            // Buttons
            edit_your_pharmacies = view.findViewById(R.id.edit_your_pharmacies);
            remove_your_pharmacies = view.findViewById(R.id.remove_your_pharmacies);

            edit_your_pharmacies.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // get the patient id
                    int position = getAdapterPosition();
                    Pharmacies pharmacy = pharmaciesList.get(position);
                    final String id = pharmacy.getId();
                    final String name = pharmacy.getName();

                    final HelperFunctions helperFunctions = new HelperFunctions(context);

                    String[] mStringArray = {"Edit pharmacy information", "Edit pharmacy location"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Choose your action");

                    builder.setItems(mStringArray, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (i == 0){
                                Intent intent = new Intent(context, EditPharmacyInformationActivity.class);
                                intent.putExtra("id", id);
                                context.startActivity(intent);
                            } else if (i == 1){
                                helperFunctions.genericProgressBar("Retrieving details...");
                                //fetch the coordinates for the pharmacy
                                String network_address = helperFunctions.getIpAddress()
                                        + "get_pharmacy_coordinates.php?id=" + id;

                                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    //dismiss dialog
                                                    helperFunctions.stopProgressBar();

                                                    Intent intent = new Intent(context, ViewPharmacyLocationActivity.class);
                                                    intent.putExtra("pharmacy_id", id);
                                                    intent.putExtra("pharmacy_name", name);
                                                    intent.putExtra("latitude", Double.parseDouble(response.getString("latitude")));
                                                    intent.putExtra("longitude", Double.parseDouble(response.getString("longitude")));
                                                    context.startActivity(intent);

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        //
                                    }
                                });

                                VolleySingleton.getInstance(context).addToRequestQueue(request);
                            }
                        }
                    });

                    // create and show the alert dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            remove_your_pharmacies.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // get the patient id
                    int position = getAdapterPosition();

                    Pharmacies pharmacy = pharmaciesList.get(position);

                    final String id = pharmacy.getId();

                    final String confirm_text = "Remove " + pharmacy.getName() + " from your pharmacies?";

                    final HelperFunctions helperFunctions = new HelperFunctions(context);

                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText(confirm_text)
                            .setConfirmText("Yes")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    helperFunctions.genericProgressBar("Removing pharmacy..");
                                    String network_address = helperFunctions.getIpAddress() + "remove_pharmacy.php?id=" + id;

                                    // Request a string response from the provided URL
                                    StringRequest request = new StringRequest(network_address,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    helperFunctions.stopProgressBar();

                                                    if(response.equals("1")){
                                                        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                                                .setTitleText("Success!")
                                                                .setContentText("Pharmacy removed")
                                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                    @Override
                                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                        Intent delete_intent = new Intent(context, EditYourPharmacies.class);
                                                                        context.startActivity(delete_intent);
                                                                    }
                                                                })
                                                                .show();
                                                    }

                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            //
                                        }
                                    });

                                    //add to request queue in singleton class
                                    VolleySingleton.getInstance(context).addToRequestQueue(request);
                                }
                            })
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                }
                            })
                            .show();
                }
            });
        }
    }

    public EditYourPharmaciesAdapter(Context context, List<Pharmacies> pharmaciesList) {
        this.pharmaciesList = pharmaciesList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_edit_your_pharmacies, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Pharmacies pharmacy = pharmaciesList.get(position);
        holder.pharmacy_name.setText(pharmacy.getName());
    }

    @Override
    public int getItemCount() {
        return pharmaciesList.size();
    }
}