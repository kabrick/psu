package ug.or.psu.psudrugassessmenttool.globalfragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.adapters.NewsFeedAdapter;
import ug.or.psu.psudrugassessmenttool.globalactivities.CreateNewsActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.EditYourNewsActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.NewsViewActivity;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.models.NewsFeed;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class NewsFragment extends Fragment implements NewsFeedAdapter.NewsFeedAdapterListener {

    private View view;
    private List<NewsFeed> newsList;
    private NewsFeedAdapter mAdapter;
    private HelperFunctions helperFunctions;
    private ShimmerFrameLayout news_shimmer_view_container;

    private FloatingActionButton fab;
    private RecyclerView recyclerView;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_news, container, false);

        setHasOptionsMenu(true);

        recyclerView = view.findViewById(R.id.recycler_view_news);
        newsList = new ArrayList<>();
        mAdapter = new NewsFeedAdapter(getContext(), newsList, this);

        fab = view.findViewById(R.id.add_news_fab);
        news_shimmer_view_container = view.findViewById(R.id.news_shimmer_view_container);

        fab.setOnClickListener(view -> Objects.requireNonNull(getContext()).startActivity(new Intent(getContext(), CreateNewsActivity.class)));

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
                response -> {

                    //news has been got so stop progress bar
                    news_shimmer_view_container.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

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
                }, error -> {
                    // error in getting json, so recursive call till successful
                    fetchNews();
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
        intent.putExtra("source", news.getSource());
        intent.putExtra("id", news.getId());
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_news, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_view_own_news) {
            Intent intent_own_news = new Intent(getContext(), EditYourNewsActivity.class);
            Objects.requireNonNull(getContext()).startActivity(intent_own_news);
        }

        return super.onOptionsItemSelected(item);
    }

}
