package com.team28.qlnhathuoc.ui.pharmacy.pharmacy_form;

import android.annotation.SuppressLint;
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

public class PharmacyFormActivity extends AppCompatActivity {

    private ActivityPharmacyFormBinding binding;
    private PharmacyFormViewModel viewModel;

    private NhaThuoc oldPharmacy = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPharmacyFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = new ViewModelProvider(this).get(PharmacyFormViewModel.class);

        binding.btnSubmit.setOnClickListener(v -> {
            NhaThuoc pharmacy = getCurrentPharmacy();

            if (pharmacy != null) {
                if (isEdit()) {
                    // Kiểm tra xem có nhà thuốc nào bị trùng tên hay không
                    NhaThuoc find = viewModel.getPharmacyByName(pharmacy.tenNT);

                    if (find != null && !find.maNT.equalsIgnoreCase(oldPharmacy.maNT)) {
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

        if (intentData.hasExtra(Constants.PHARMACY)) {
            oldPharmacy = (NhaThuoc) intentData.getSerializableExtra(Constants.PHARMACY);

            binding.edMaNT.setText(oldPharmacy.maNT);
            binding.edTenNT.setText(oldPharmacy.tenNT);
            binding.edDiaChi.setText(oldPharmacy.diaChi);

            binding.edMaNT.setEnabled(false);
            binding.btnSubmit.setText("Chỉnh sửa");
        }
    }

    /*
     * Kiểm tra Form hiện tại là Form thêm hay là Form chỉnh sửa
     */
    private boolean isEdit() {
        return oldPharmacy != null;
    }

    /*
     * Lấy đối tượng nhà thuốc hiện tại dựa trên các dữ liệu đã nhập ở EditText
     * Nếu lỗi -> hiển thị Toast thông báo
     */
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

    /*
     * Hiển thị Dialog xác nhận xóa
     */
    private void showDialogConfirmDelete(NhaThuoc pharmacy) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage(String.format("Bạn thực sự muốn xóa nhà thuốc %s ?", pharmacy.tenNT));
        builder.setCancelable(true);

        builder.setPositiveButton("Đồng ý", (arg0, arg1) -> {
            viewModel.deletePharmacy(pharmacy);
            Toast.makeText(this, "Xóa nhà thuốc thành công!", Toast.LENGTH_SHORT).show();

            // Xóa xong thì đóng activity form, quay về màn hình dánh sách nhà thuốc
            finish();
        });
        builder.setNeutralButton("Hủy", (dialog, which) -> {
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
            case R.id.menuDelete:
                NhaThuoc pharmacy = viewModel.getPharmacyById(oldPharmacy.maNT);

                // Kiểm tra nhà thuốc đã có dữ liệu bán thuốc hay chưa
                // Nếu chưa có thì xóa được
                if (viewModel.canDeletePharmacy(pharmacy)) {
                    showDialogConfirmDelete(pharmacy);
                } else {
                    Toast.makeText(this, "Nhà thuốc đã có dữ liệu bán thuốc nên không thể xóa!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Nếu Form là Form chỉnh sửa thì mới hiển thị menu "Xóa nhà thuốc"
        if (isEdit()) getMenuInflater().inflate(R.menu.menu_form, menu);

        return super.onCreateOptionsMenu(menu);
    }
}