package com.team28.qlnhathuoc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.team28.qlnhathuoc.adapter.BillDetailAdapter;
import com.team28.qlnhathuoc.databinding.ActivityBillDetailBinding;
import com.team28.qlnhathuoc.model.BillObj;
import com.team28.qlnhathuoc.model.MedicineObj;
import com.team28.qlnhathuoc.room.entity.Thuoc;
import com.team28.qlnhathuoc.utils.Constants;
import com.team28.qlnhathuoc.utils.Helpers;

import java.util.ArrayList;
import java.util.List;

public class BillDetailActivity extends AppCompatActivity {

    private ActivityBillDetailBinding binding;

    private BillDetailAdapter adapter;

    private BillObj billObj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBillDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getIntentData();
        setupRecyclerView();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getIntentData() {
        Intent data = getIntent();
        billObj = (BillObj) data.getSerializableExtra(Constants.BILL_DETAIL);

        binding.tvsoHD.setText(billObj.soHD);
        binding.tvTenNT.setText(billObj.pharmacyObj.maNT + " - " + billObj.pharmacyObj.tenNT);
        binding.tvNgayHD.setText(Helpers.getStringDate(billObj.ngayHD));
        binding.tvTotalMoney.setText(Helpers.formatCurrency(billObj.total) + " đ");
    }

    private void setupRecyclerView() {
        adapter = new BillDetailAdapter();
        binding.recycler.setAdapter(adapter);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));

        List<Thuoc> medicines = new ArrayList<>();
        for (MedicineObj medicineObj : billObj.medicineObjList) {
            medicines.add(medicineObj.toThuoc());
        }
        adapter.setAdapter(medicines);
    }
}