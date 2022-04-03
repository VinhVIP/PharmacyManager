package com.team28.qlnhathuoc.ui.pharmacy.pharmacy_form;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.team28.qlnhathuoc.R;
import com.team28.qlnhathuoc.databinding.ActivityPharmacyFormBinding;
import com.team28.qlnhathuoc.room.entity.NhaThuoc;
import com.team28.qlnhathuoc.utils.Constants;
import com.team28.qlnhathuoc.utils.FileUtil;
import com.team28.qlnhathuoc.utils.Helpers;

import java.io.File;

public class PharmacyFormActivity extends AppCompatActivity {

    private ActivityPharmacyFormBinding binding;
    private PharmacyFormViewModel viewModel;

    private NhaThuoc oldPharmacy = null;    // lưu đối tương nhà thuốc muốn chỉnh sửa
    private Bitmap iconBitmap = null;       // Lưu hình ảnh icon của nhà thuốc


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPharmacyFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = new ViewModelProvider(this).get(PharmacyFormViewModel.class);

        onEvents();

        getIntentData();
    }

    private void onEvents() {
        onSubmit();
        onLoadImageIcon();
    }

    private void onSubmit() {
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
    }

    private void onLoadImageIcon() {
        binding.btnPickIcon.setOnClickListener(v -> {
            try {
                // Cách mới
                fileChooser.launch("image/*");
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(this, "Vui lòng cài đặt File Manager", Toast.LENGTH_SHORT).show();
            }

        });

        // Tải hình ảnh từ internet
        binding.btnLoadImage.setOnClickListener(v -> {
            String link = binding.edPathIcon.getText().toString().trim();

            // Kiểm tra link ảnh có đúng hay không
            Picasso.with(this).load(link).resize(100, 100).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    // Load thành công
                    binding.imgIconPreview.setImageBitmap(bitmap);
                    iconBitmap = bitmap;
                    binding.layoutPreview.setVisibility(View.VISIBLE);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    // Load thất bại
                    Toast.makeText(PharmacyFormActivity.this, "Đường dẫn ảnh không hợp lệ!", Toast.LENGTH_SHORT).show();
                    iconBitmap = null;
                    binding.layoutPreview.setVisibility(View.GONE);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
            binding.layoutPreview.setVisibility(View.VISIBLE);
        });

        // Clear ảnh
        binding.btnClearImage.setOnClickListener(v -> {
            iconBitmap = null;
            binding.layoutPreview.setVisibility(View.GONE);
        });
    }

    /*
     * Cách mới
     * Nhận hình ảnh (dạng Uri) từ thư viện
     */
    ActivityResultLauncher<String> fileChooser = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Chuyển Uri về dạng File
                    File file = FileUtil.from(PharmacyFormActivity.this, uri);

                    if (file != null) {
                        // Load ảnh từ file
                        Picasso.with(PharmacyFormActivity.this).load(file).resize(100, 100).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                binding.imgIconPreview.setImageBitmap(bitmap);
                                iconBitmap = bitmap;
                                binding.layoutPreview.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                                Toast.makeText(PharmacyFormActivity.this, "Đường dẫn ảnh không hợp lệ!", Toast.LENGTH_SHORT).show();
                                iconBitmap = null;
                                binding.layoutPreview.setVisibility(View.GONE);
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
                        binding.layoutPreview.setVisibility(View.VISIBLE);
                    }
                }
            });

    private void getIntentData() {
        Intent intentData = getIntent();

        if (intentData.hasExtra(Constants.PHARMACY)) {
            oldPharmacy = (NhaThuoc) intentData.getSerializableExtra(Constants.PHARMACY);

            binding.edMaNT.setText(oldPharmacy.maNT);
            binding.edTenNT.setText(oldPharmacy.tenNT);
            binding.edDiaChi.setText(oldPharmacy.diaChi);

            if (oldPharmacy.icon != null) {
                iconBitmap = Helpers.bytesToBitmap(oldPharmacy.icon);
                binding.imgIconPreview.setImageBitmap(iconBitmap);
                binding.layoutPreview.setVisibility(View.VISIBLE);
            }

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

        NhaThuoc pharmacy = new NhaThuoc(binding.edMaNT.getText().toString().trim().toUpperCase(),
                binding.edTenNT.getText().toString().trim(),
                binding.edDiaChi.getText().toString().trim(), null);

        if (iconBitmap != null) {
            pharmacy.icon = Helpers.bitmapToBytes(iconBitmap);
        }

        return pharmacy;
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