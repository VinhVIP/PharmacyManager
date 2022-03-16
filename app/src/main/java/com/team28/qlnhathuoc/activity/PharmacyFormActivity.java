package com.team28.qlnhathuoc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.team28.qlnhathuoc.R;
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

    private void showDialogConfirmDelete(NhaThuoc pharmacy) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage(String.format("Bạn thực sự muốn xóa nhà thuốc %s ?", pharmacy.tenNT));
        builder.setCancelable(true);

        builder.setPositiveButton("Đồng ý", (arg0, arg1) -> {
            viewModel.deletePharmacy(pharmacy);
            Toast.makeText(this, "Xóa nhà thuốc thành công!", Toast.LENGTH_SHORT).show();
            finish();
        });
        builder.setNeutralButton("Hủy", (dialog, which) -> {
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            case R.id.menuDelete:
                NhaThuoc pharmacy = viewModel.getPharmacyById(oldPharmacyId);

                if (viewModel.canDeletePharmacy(pharmacy)) {
                    showDialogConfirmDelete(pharmacy);
                } else {
                    Toast.makeText(this, "Nhà thuốc đã có dữ liệu bán thuốc nên không thể xóa!", Toast.LENGTH_SHORT).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdit) getMenuInflater().inflate(R.menu.menu_form, menu);
        return super.onCreateOptionsMenu(menu);
    }
}