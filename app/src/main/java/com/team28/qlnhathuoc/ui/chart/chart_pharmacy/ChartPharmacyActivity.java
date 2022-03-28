package com.team28.qlnhathuoc.ui.chart.chart_pharmacy;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.team28.qlnhathuoc.databinding.ActivityChartPharmacyBinding;
import com.team28.qlnhathuoc.room.entity.NhaThuoc;
import com.team28.qlnhathuoc.utils.Helpers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChartPharmacyActivity extends AppCompatActivity {

    private ActivityChartPharmacyBinding binding;

    private ChartPharmacyViewModel viewModel;

    // Biểu đồ 4 cột ghép (4 quý)
    private static final int GROUPS = 4;

    private static final float BAR_SPACE = 0.05f;   // khoảng cách giữa các cột
    private static final float BAR_WIDTH = 0.2f;    // kích thước mỗi cột

    private BarChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChartPharmacyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        viewModel = new ViewModelProvider(this).get(ChartPharmacyViewModel.class);

        chart = binding.barChart;

        configureChartAppearance();

        createPharmacyChartData();

        chooseYear();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void chooseYear() {
        // Mặc định sẽ hiển thị dữ liệu của năm hiện tại
        viewModel.filter(Calendar.getInstance().get(Calendar.YEAR));

        binding.btnYearMinus.setOnClickListener(v -> {
            int year = Integer.parseInt(binding.tvYear.getText().toString());
            year--;
            binding.tvYear.setText(String.valueOf(year));
            viewModel.filter(year);
        });
        binding.btnYearPlus.setOnClickListener(v -> {
            int year = Integer.parseInt(binding.tvYear.getText().toString());
            year++;
            binding.tvYear.setText(String.valueOf(year));
            viewModel.filter(year);
        });
    }

    private void configureChartAppearance() {
        chart.setPinchZoom(false);
        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);

        chart.getDescription().setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(12);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setAxisMinimum(1f);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
//        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f);

        // format lại cách hiển thị các mốc giá trị
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int val = (int) value;

                if (val == 0) {
                    return "0";
                } else if (val < 1000000) {
                    return val / 1000 + "K";
                } else {
                    int m = val / 1000000;
                    int k = (val % 1000000) / 100000;
                    return String.format("%d.%dM", m, k);
                }
            }
        });


        chart.getAxisRight().setEnabled(false);

        chart.getXAxis().setAxisMinimum(0);
    }

    private void createPharmacyChartData() {
        viewModel.mapPharmacy.observe(this, map -> {
            ArrayList<String> labelsPharmacy = new ArrayList<>();

            ArrayList<BarEntry>[] values = new ArrayList[4];
            for (int i = 0; i < 4; i++)
                values[i] = new ArrayList<>();

            int pharmacyIndex = 0;
            for (NhaThuoc pharmacy : map.keySet()) {
                labelsPharmacy.add(pharmacy.tenNT);

                List<Float> moneyOfQuarter = map.get(pharmacy);
                for (int i = 0; i < 4; i++) {
                    values[i].add(new BarEntry(pharmacyIndex, moneyOfQuarter.get(i)));
                }
                pharmacyIndex++;
            }

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            String[] labelsQuarter = {"Quý 1", "Quý 2", "Quý 3", "Quý 4"};

            for (int i = 0; i < 4; i++) {
                BarDataSet set = new BarDataSet(values[i], labelsQuarter[i]);
                set.setColor(ColorTemplate.MATERIAL_COLORS[i]);
                dataSets.add(set);
            }

            BarData data = new BarData(dataSets);

            chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labelsPharmacy));
            chart.getXAxis().setAxisMaximum(labelsPharmacy.size());

            chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    int index = (int) e.getX();
                    String mess = Helpers.formatCurrency(e.getY()) + " đ";
                    Toast.makeText(ChartPharmacyActivity.this, mess, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected() {

                }
            });

            prepareChartData(data);

        });
    }

    private void prepareChartData(BarData data) {
        chart.setData(data);

        chart.getBarData().setBarWidth(BAR_WIDTH);

        float groupSpace = 1f - ((BAR_SPACE + BAR_WIDTH) * GROUPS);
        chart.groupBars(0, groupSpace, BAR_SPACE);

        chart.animateY(1500);
        chart.invalidate();
    }

}