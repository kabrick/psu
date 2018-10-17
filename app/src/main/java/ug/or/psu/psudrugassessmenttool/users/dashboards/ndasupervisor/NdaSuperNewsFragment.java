package ug.or.psu.psudrugassessmenttool.users.dashboards.ndasupervisor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import ug.or.psu.psudrugassessmenttool.globalactivities.NewsViewActivity;
import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.adapters.NewsFeedAdapter;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.models.NewsFeed;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class NdaSuperNewsFragment extends Fragment implements NewsFeedAdapter.NewsFeedAdapterListener {

    private View view;
    private List<NewsFeed> newsList;
    private NewsFeedAdapter mAdapter;

    HelperFunctions helperFunctions;

    public NdaSuperNewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_nda_super_news, container, false);

        setHasOptionsMenu(true);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_nda_supervisor_news);
        newsList = new ArrayList<>();
        mAdapter = new NewsFeedAdapter(getContext(), newsList, this);

        helperFunctions = new HelperFunctions(getContext());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        fetchNews();

        return view;
    }

    private void fetchNews() {
        String url = helperFunctions.getIpAddress() + "get_news.php";

        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            //toast message about information not being found
                            helperFunctions.genericSnackbar("No news available", view);
                            return;
                        }

                        List<NewsFeed> items = new Gson().fromJson(response.toString(), new TypeToken<List<NewsFeed>>() {
                        }.getType());

                        newsList.clear();
                        newsList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json, so recursive call till successful
                fetchNews();
            }
        });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    /**
     * function to obtain selected news item and go to full view activity
     *
     * @param news news feed model for item clicked
     */
    @Override
    public void onNewsItemSelected(NewsFeed news) {
        Intent intent = new Intent(getContext(), NewsViewActivity.class);
        intent.putExtra("text", news.getText());
        intent.putExtra("title", news.getTitle());
        intent.putExtra("author", news.getAuthor());
        intent.putExtra("timestamp", news.getTimeStamp());
        intent.putExtra("image", news.getImage());
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_nda_supervisor_news, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nda_news_action_sign_out:
                //user sign out
                helperFunctions.signAdminUsersOut();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
