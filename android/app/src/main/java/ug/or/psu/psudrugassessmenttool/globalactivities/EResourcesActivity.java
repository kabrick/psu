package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;

public class EResourcesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eresources);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // Get ListView object from xml
        ListView listView = findViewById(R.id.eresources_list);

        // Defined Array values to show in ListView
        String[] values = new String[] { "NDA",
                "PSU",
                "Clinical Pharmacy",
                "Industrial Pharmacy",
                "Community Pharmacy",
                "Regulatory Pharmacy",
                "Pharmaceutical Chemicals",
                "Pharmacognosy"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, position, l) -> {

            switch (position){
                case 0:
                    Intent intent = new Intent(EResourcesActivity.this, EResourcesViewActivity.class);
                    intent.putExtra("category", 0);
                    startActivity(intent);
                    break;
                case 1:
                    Intent intent1 = new Intent(EResourcesActivity.this, EResourcesViewActivity.class);
                    intent1.putExtra("category", 1);
                    startActivity(intent1);
                    break;
                case 2:
                    Intent intent2 = new Intent(EResourcesActivity.this, EResourcesViewActivity.class);
                    intent2.putExtra("category", 2);
                    startActivity(intent2);
                    break;
                case 3:
                    Intent intent3 = new Intent(EResourcesActivity.this, EResourcesViewActivity.class);
                    intent3.putExtra("category", 3);
                    startActivity(intent3);
                    break;
                case 4:
                    Intent intent4 = new Intent(EResourcesActivity.this, EResourcesViewActivity.class);
                    intent4.putExtra("category", 4);
                    startActivity(intent4);
                    break;
                case 5:
                    Intent intent5 = new Intent(EResourcesActivity.this, EResourcesViewActivity.class);
                    intent5.putExtra("category", 5);
                    startActivity(intent5);
                    break;
                case 6:
                    Intent intent6 = new Intent(EResourcesActivity.this, EResourcesViewActivity.class);
                    intent6.putExtra("category", 6);
                    startActivity(intent6);
                    break;
                case 7:
                    Intent intent7 = new Intent(EResourcesActivity.this, EResourcesViewActivity.class);
                    intent7.putExtra("category", 7);
                    startActivity(intent7);
                    break;
                default:
                    //
                    break;
            }

        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
