package com.example.maganaactivity6;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maganaactivity6.api.ApiUtilities;
import com.example.maganaactivity6.api.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView text_total_confirmed, text_total_active, text_total_recovered, text_total_deaths, text_total_tests;
    TextView text_today_confirmed, text_today_recovered, text_today_deaths;
    TextView text_date;
    PieChart pieChart;

    private List<CountryData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();

        text_total_confirmed = findViewById(R.id.txtview_totalconfirmed);
        text_total_active = findViewById(R.id.txtview_totalactive);
        text_total_recovered = findViewById(R.id.txtview_totalrecovered);
        text_total_deaths = findViewById(R.id.txtview_totaldeaths);
        text_total_tests = findViewById(R.id.textview_totaltests);
        text_today_confirmed = findViewById(R.id.txtview_todayconfirmed);
        text_today_recovered = findViewById(R.id.txtview_todayrecovered);
        text_today_deaths = findViewById(R.id.txtview_todaydeaths);
        text_date = findViewById(R.id.txtview_date);
        pieChart = findViewById(R.id.piechart);


        ApiUtilities.getApiInterface().getCountryData()
                .enqueue(new Callback<List<CountryData>>() {
                    @Override
                    public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                        list.addAll(response.body());

                        for (int i = 0; i<list.size(); i++){
                            if (list.get(i).getCountry().equals("Philippines")){
                                int text_confirm = Integer.parseInt(list.get(i).getCases());
                                int text_active = Integer.parseInt(list.get(i).getActive());
                                int text_recovered = Integer.parseInt(list.get(i).getRecovered());
                                int text_death = Integer.parseInt(list.get(i).getDeaths());

                                int today_confirm = Integer.parseInt(list.get(i).getTodayCases());
                                int today_tests = Integer.parseInt(list.get(i).getTests());
                                int today_recovered = Integer.parseInt(list.get(i).getTodayRecovered());
                                int today_death = Integer.parseInt(list.get(i).getTodayDeaths());

                                text_total_confirmed.setText(NumberFormat.getInstance().format(text_confirm));
                                text_total_active.setText(NumberFormat.getInstance().format(text_active));
                                text_total_recovered.setText(NumberFormat.getInstance().format(text_recovered));
                                text_total_deaths.setText(NumberFormat.getInstance().format(text_death));

                                text_today_confirmed.setText(NumberFormat.getInstance().format(today_confirm));
                                text_total_tests.setText(NumberFormat.getInstance().format(today_tests));
                                text_today_recovered.setText(NumberFormat.getInstance().format(today_recovered));
                                text_today_deaths.setText(NumberFormat.getInstance().format(today_death));

                                setText(list.get(i).getUpdated());

                                pieChart.addPieSlice(new PieModel("Confirm",text_confirm, Color.parseColor("#00FD19")));
                                pieChart.addPieSlice(new PieModel("Active",text_active, Color.parseColor("#FF00E5")));
                                pieChart.addPieSlice(new PieModel("Recovered",text_recovered, Color.parseColor("#FAD308")));
                                pieChart.addPieSlice(new PieModel("Death",text_death, Color.parseColor("#970505")));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CountryData>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Error Acquiring Data", Toast.LENGTH_SHORT).show();
                    }

                    private void setText(String updated) {
                    DateFormat format = new SimpleDateFormat("MMM dd, yyyy");

                    long milliseconds = Long.parseLong(updated);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(milliseconds);

                    text_date.setText("Updated at "+format.format(calendar.getTime()));
                    }
                });
    }
}