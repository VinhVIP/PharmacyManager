package com.team28.qlnhathuoc.ui.medicine.medicine_form;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.team28.qlnhathuoc.R;
import com.team28.qlnhathuoc.databinding.ActivityMedicineFormBinding;
import com.team28.qlnhathuoc.room.entity.Thuoc;
import com.team28.qlnhathuoc.utils.Constants;
import com.team28.qlnhathuoc.utils.Helpers;

public class MedicineFormActivity extends AppCompatActivity {

    private ActivityMedicineFormBinding binding;
    private MedicineFormViewModel viewModel;

    // Thuốc muốn cập nhật / xóa
    // Nếu Form được mở là Form thêm thì oldMedicine = null
    private Thuoc oldMedicine = null;

    private String[] dvt = {"Hộp", "Vỉ", "Viên", "Lọ", "Chai", "Kit"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMedicineFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = new ViewModelProvider(this).get(MedicineFormViewModel.class);

        binding.btnSubmit.setOnClickListener(v -> {
            Thuoc medicine = getCurrentMedicine();

            if (medicine != null) {
                if (isEdit()) {
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

        if (intentData.hasExtra(Constants.MEDICINE)) {
            oldMedicine = (Thuoc) intentData.getSerializableExtra(Constants.MEDICINE);

            binding.edMaThuoc.setText(oldMedicine.maThuoc);
            binding.edTenThuoc.setText(oldMedicine.tenThuoc);
            binding.edDVT.setText(oldMedicine.donViTinh);
            binding.edDonGia.setText(Helpers.floatToString(oldMedicine.donGia));

            binding.edMaThuoc.setEnabled(false);
            binding.btnSubmit.setText("Chỉnh sửa");
        }
    }

    // Kiểm tra form là form thêm hay là form chỉnh sửa
    private boolean isEdit() {
        return oldMedicine != null;
    }

    // Setup danh sách đơn vị tính cho custom EditText dvt
    private void setupDVT() {
        ArrayAdapter<String> dvtAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dvt);
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

    /*
     * Hiển thị dialog xác nhận xóa
     */
    public void showDialogDeleteMedicine(Thuoc medicine) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa");

        builder.setMessage(String.format("Bạn thực sự muốn xóa thuốc %s ?", medicine.tenThuoc));
        builder.setCancelable(true);

        builder.setPositiveButton("Đồng ý", (arg0, arg1) -> {
            viewModel.deleteMedicine(medicine);
            Toast.makeText(this, "Xóa thuốc thành công!", Toast.LENGTH_SHORT).show();
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
                super.onBackPressed();
                break;
            case R.id.menuDelete:
                // Kiểm tra thuốc đã được bán chưa
                // Nếu chưa thì có thể xóa
                if (viewModel.canDeleteMedicine(oldMedicine)) {
                    showDialogDeleteMedicine(oldMedicine);
                } else {
                    Toast.makeText(this, "Thuốc đã từng được bán nên không thể xóa!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Nếu là Form Chỉnh sửa thì mới hiện menu Xóa thuốc
        if (isEdit()) getMenuInflater().inflate(R.menu.menu_form, menu);
        return super.onCreateOptionsMenu(menu);
    }
}