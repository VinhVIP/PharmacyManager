package com.team28.qlnhathuoc.ui.bill.bill_view.bill_detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.team28.qlnhathuoc.adapter.BillDetailAdapter;
import com.team28.qlnhathuoc.databinding.ActivityBillDetailBinding;
import com.team28.qlnhathuoc.room.entity.relations.HoaDonWithThuoc;
import com.team28.qlnhathuoc.utils.Constants;
import com.team28.qlnhathuoc.utils.Helpers;

public class BillDetailActivity extends AppCompatActivity {

    private ActivityBillDetailBinding binding;

    private BillDetailAdapter adapter;

    private HoaDonWithThuoc bill;


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
        bill = (HoaDonWithThuoc) data.getSerializableExtra(Constants.BILL_DETAIL);

        binding.tvsoHD.setText(bill.hoaDon.soHD);
        binding.tvTenNT.setText(bill.pharmacy.maNT + " - " + bill.pharmacy.tenNT);
        binding.tvNgayHD.setText(Helpers.getStringDate(bill.hoaDon.ngayHD));
        binding.tvTotalMoney.setText(Helpers.formatCurrency(bill.totalMoney) + " đ");
    }

    private void setupRecyclerView() {
        adapter = new BillDetailAdapter();
        binding.recycler.setAdapter(adapter);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));

        adapter.setAdapter(bill.thuocList);
    }
}