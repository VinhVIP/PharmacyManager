package com.team28.qlnhathuoc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.team28.qlnhathuoc.databinding.ActivityPharmacyFormBinding;
import com.team28.qlnhathuoc.room.entity.NhaThuoc;
import com.team28.qlnhathuoc.utils.Constants;
import com.team28.qlnhathuoc.utils.Helpers;
import com.team28.qlnhathuoc.viewmodel.PharmacyViewModel;

public class PharmacyFormActivity extends AppCompatActivity {

    private ActivityPharmacyFormBinding binding;
    private PharmacyViewModel viewModel;

    private boolean isEdit;
    private String oldPharmacyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPharmacyFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = new ViewModelProvider(this).get(PharmacyViewModel.class);

        binding.btnSubmit.setOnClickListener(v -> {
            NhaThuoc pharmacy = getCurrentPharmacy();

            if (pharmacy != null) {
                if (isEdit) {
                    NhaThuoc find = viewModel.getPharmacyByName(pharmacy.tenNT);

                    if (find != null && !find.maNT.equalsIgnoreCase(oldPharmacyId)) {
                        Helpers.showToast(this, "Tên nhà thuốc đã tồn tại!");
                    } else {
                        viewModel.updatePharmacy(pharmacy);
                        finish();
                    }
                } else {
                    if (viewModel.getPharmacyById(pharmacy.maNT) != null) {
                        Helpers.showToast(this, "Mã nhà thuốc đã tồn tại!");
                    } else if (viewModel.getPharmacyByName(pharmacy.tenNT) != null) {
                        Helpers.showToast(this, "Tên nhà thuốc đã tồn tại!");
                    } else {
                        viewModel.insertPharmacy(pharmacy);
                        finish();
                    }
                }

            }
        });

        getIntentData();
    }

    private void getIntentData() {
        Intent intentData = getIntent();
        Bundle data = intentData.getBundleExtra(Constants.REQUEST_ACTION);
        if (data != null) {
            isEdit = true;

            oldPharmacyId = data.getString(Constants.MANT);

            binding.edMaNT.setText(data.getString(Constants.MANT));
            binding.edTenNT.setText(data.getString(Constants.TENNT));
            binding.edDiaChi.setText(data.getString(Constants.DIACHI));

            binding.edMaNT.setEnabled(false);
            binding.btnSubmit.setText("Chỉnh sửa");
        }

    }

    private NhaThuoc getCurrentPharmacy() {
        if (binding.edMaNT.getText().toString().isEmpty()) {
            Helpers.showToast(this, "Mã nhà thuốc không được bỏ trống!");
            return null;
        }
        if (binding.edTenNT.getText().toString().isEmpty()) {
            Helpers.showToast(this, "Tên nhà thuốc không được bỏ trống!");
            return null;
        }
        if (binding.edDiaChi.getText().toString().isEmpty()) {
            Helpers.showToast(this, "Địa chỉ nhà thuốc không được bỏ trống!");
            return null;
        }

        return new NhaThuoc(binding.edMaNT.getText().toString().trim().toUpperCase(),
                binding.edTenNT.getText().toString().trim(),
                binding.edDiaChi.getText().toString().trim());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}