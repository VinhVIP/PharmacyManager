package com.team28.qlnhathuoc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.team28.qlnhathuoc.databinding.ActivityMedicineFormBinding;
import com.team28.qlnhathuoc.room.entity.Thuoc;
import com.team28.qlnhathuoc.utils.Constants;
import com.team28.qlnhathuoc.utils.Helpers;
import com.team28.qlnhathuoc.viewmodel.MedicineViewModel;

public class MedicineFormActivity extends AppCompatActivity {

    private ActivityMedicineFormBinding binding;
    private MedicineViewModel viewModel;

    private boolean isEdit;
    private String oldMedicineId;

    private String[] dvt = {"Hộp", "Vỉ", "Viên", "Lọ", "Chai"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMedicineFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = new ViewModelProvider(this).get(MedicineViewModel.class);

        binding.btnSubmit.setOnClickListener(v -> {
            Thuoc medicine = getCurrentMedicine();

            if (medicine != null) {
                if (isEdit) {
                    viewModel.updateMedicine(medicine);
                    finish();
                } else {
                    if (viewModel.getMedicineById(medicine.maThuoc) != null) {
                        Helpers.showToast(this, "Mã thuốc đã tồn tại!");
                    } else {
                        viewModel.insertMedicine(medicine);
                        finish();
                    }
                }

            }
        });

        getIntentData();
        setupDVT();
    }

    private void getIntentData() {
        Intent intentData = getIntent();
        Bundle data = intentData.getBundleExtra(Constants.REQUEST_ACTION);
        if (data != null) {
            isEdit = true;

            oldMedicineId = data.getString(Constants.MANT);

            binding.edMaThuoc.setText(data.getString(Constants.MATHUOC));
            binding.edTenThuoc.setText(data.getString(Constants.TENTHUOC));
            binding.edDVT.setText(data.getString(Constants.DVT));
            binding.edDonGia.setText(Helpers.floatToString(data.getFloat(Constants.DONGIA, 0f)));

            binding.edMaThuoc.setEnabled(false);
            binding.btnSubmit.setText("Chỉnh sửa");
        }
    }

    private void setupDVT(){
        ArrayAdapter<String> dvtAdapter =new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dvt);
        binding.edDVT.setAdapter(dvtAdapter);
        binding.edDVT.setThreshold(0);
    }

    private Thuoc getCurrentMedicine() {
        if (binding.edMaThuoc.getText().toString().isEmpty()) {
            Helpers.showToast(this, "Mã thuốc không được bỏ trống!");
            return null;
        }
        if (binding.edTenThuoc.getText().toString().isEmpty()) {
            Helpers.showToast(this, "Tên thuốc không được bỏ trống!");
            return null;
        }
        if (binding.edDVT.getText().toString().isEmpty()) {
            Helpers.showToast(this, "Địa vị tính không được bỏ trống!");
            return null;
        }
        if (binding.edDonGia.getText().toString().isEmpty()) {
            Helpers.showToast(this, "Đơn giá không được bỏ trống!");
            return null;
        }

        return new Thuoc(binding.edMaThuoc.getText().toString().trim().toUpperCase(),
                binding.edTenThuoc.getText().toString().trim(),
                binding.edDVT.getText().toString().trim(),
                Float.parseFloat(binding.edDonGia.getText().toString()));
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