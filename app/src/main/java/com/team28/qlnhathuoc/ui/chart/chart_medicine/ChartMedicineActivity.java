package com.team28.qlnhathuoc.ui.chart.chart_medicine;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.team28.qlnhathuoc.databinding.ActivityChartMedicineBinding;
import com.team28.qlnhathuoc.room.entity.CTBanLe;
import com.team28.qlnhathuoc.room.entity.HoaDon;
import com.team28.qlnhathuoc.room.entity.relations.ThuocWithHoaDon;
import com.team28.qlnhathuoc.utils.Helpers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChartMedicineActivity extends AppCompatActivity {

    private ActivityChartMedicineBinding binding;

    private ChartMedicineViewModel viewModel;

    private BarChart barChart;

    private List<ThuocWithHoaDon> medicineList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChartMedicineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        barChart = binding.barChart;

        viewModel = new ViewModelProvider(this).get(ChartMedicineViewModel.class);

        viewModel.medicineBillList.observe(this, medicineList -> {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            binding.tvYear.setText(String.valueOf(year));
            filterYear(year);
        });

        binding.btnYearMinus.setOnClickListener(v -> {
            if (binding.swType.isChecked()) return;

            int year = Integer.parseInt(binding.tvYear.getText().toString());
            filterYear(--year);
            binding.tvYear.setText(String.valueOf(year));
        });

        binding.btnYearPlus.setOnClickListener(v -> {
            if (binding.swType.isChecked()) return;

            int year = Integer.parseInt(binding.tvYear.getText().toString());
            filterYear(++year);
            binding.tvYear.setText(String.valueOf(year));
        });

        binding.swType.setOnCheckedChangeListener((compoundButton, selected) -> {
            if (selected) {
                binding.tvYear.setText("Tất cả");
                filterAll();
            } else {
                int year = Calendar.getInstance().get(Calendar.YEAR);
                binding.tvYear.setText(String.valueOf(year));
                filterYear(year);
            }
        });

        configureChart();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void filterYear(int year) {
        medicineList = new ArrayList<>();

        for (ThuocWithHoaDon medicine : viewModel.medicineBillList.getValue()) {
            int total = 0;
            for (HoaDon hoaDon : medicine.hoaDonList) {
                if (Helpers.isDateOfYear(hoaDon.ngayHD, year)) {
                    CTBanLe ctBanLe = viewModel.getCTBanLe(medicine.thuoc.maThuoc, hoaDon.soHD);
                    if (ctBanLe != null) {
                        total += ctBanLe.soLuong;
                    }
                }
            }
            medicine.total = total;
            medicineList.add(medicine);
        }

        showBarChart();
    }

    private void filterAll() {
        medicineList = new ArrayList<>();

        for (ThuocWithHoaDon medicine : viewModel.medicineBillList.getValue()) {
            int total = 0;
            for (HoaDon hoaDon : medicine.hoaDonList) {
                CTBanLe ctBanLe = viewModel.getCTBanLe(medicine.thuoc.maThuoc, hoaDon.soHD);
                if (ctBanLe != null) {
                    total += ctBanLe.soLuong;
                }
            }
            medicine.total = total;
            medicineList.add(medicine);
        }

        showBarChart();
    }

    private void configureChart() {
        barChart.getDescription().setEnabled(false);

        barChart.getXAxis().setDrawGridLines(false);
        // scaling can now only be done on x- and y-axis separately
        barChart.setPinchZoom(false);

        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);

        barChart.getAxisLeft().setDrawGridLines(true);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(true);
        barChart.getXAxis().setDrawGridLines(false);
        // add a nice and smooth animation
        barChart.animateY(1500);

        barChart.getAxisLeft().setValueFormatter(new ValueFormatter() {
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

        barChart.getLegend().setEnabled(false);

        barChart.getAxisRight().setDrawLabels(false);
        barChart.getAxisLeft().setDrawLabels(true);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.getXAxis().setEnabled(true);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.setDrawValueAboveBar(true);
        barChart.invalidate();
    }

    private void showBarChart() {
        ArrayList<BarEntry> entries = new ArrayList<>();

        int index = 0;
        for (ThuocWithHoaDon medicine : medicineList) {
            entries.add(new BarEntry(index++, medicine.total * medicine.thuoc.donGia));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Title");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(dataSet);

        barChart.setData(data);

        barChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                int index = (int) value;
                return medicineList.get(index).thuoc.maThuoc;
            }
        });

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int index = (int) e.getX();
                String mess = medicineList.get(index).thuoc.tenThuoc + " : " + Helpers.formatCurrency(e.getY()) + " đ";
                Toast.makeText(ChartMedicineActivity.this, mess, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        barChart.animateY(1000);
        barChart.invalidate();
    }
}