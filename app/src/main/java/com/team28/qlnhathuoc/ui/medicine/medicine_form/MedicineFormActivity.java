package com.team28.qlnhathuoc.ui.medicine.medicine_form;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.team28.qlnhathuoc.R;
import com.team28.qlnhathuoc.databinding.ActivityMedicineFormBinding;
import com.team28.qlnhathuoc.room.entity.Thuoc;
import com.team28.qlnhathuoc.utils.Constants;
import com.team28.qlnhathuoc.utils.FileUtil;
import com.team28.qlnhathuoc.utils.Helpers;

import java.io.File;

public class MedicineFormActivity extends AppCompatActivity {

    private ActivityMedicineFormBinding binding;
    private MedicineFormViewModel viewModel;

    // Thuốc muốn cập nhật / xóa
    // Nếu Form được mở là Form thêm thì oldMedicine = null
    private Thuoc oldMedicine = null;
    private Bitmap iconBitmap = null;


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

        onLoadImageIcon();

        getIntentData();
        setupDVT();
    }

    private void onLoadImageIcon() {
        // Chọn icon từ thư viện
        binding.btnChooseIcon.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");      // Lọc chỉ lấy hình ảnh
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                startActivityForResult(Intent.createChooser(intent, "Chọn ảnh để làm icon"), Constants.REQUEST_PICK_FILE);
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(this, "Vui lòng cài đặt File Manager", Toast.LENGTH_SHORT).show();
            }

        });

        // Tải ảnh từ internet để hiển thị
        binding.btnLoadImage.setOnClickListener(v -> {
            // Lấy link ảnh được điền từ EditText
            String link = binding.edPathIcon.getText().toString().trim();

            // Cần phải kiểm tra xem link ảnh có đúng hay không
            Picasso.with(this).load(link).resize(100, 100).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    // Load ảnh thành công
                    binding.imgIconPreview.setImageBitmap(bitmap);
                    iconBitmap = bitmap;
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    // Load ảnh thất bại
                    Toast.makeText(MedicineFormActivity.this, "Đường dẫn ảnh không hợp lệ!", Toast.LENGTH_SHORT).show();
                    iconBitmap = null;
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        });

        // Clear ảnh
        binding.btnClearImage.setOnClickListener(v -> {
            iconBitmap = null;
            binding.imgIconPreview.setImageResource(R.drawable.medicine);
        });


        // Chụp ảnh để làm icon
        binding.btnTakePhoto.setOnClickListener(v -> {
            // Kiểm tra quyền CAMERA đã được cấp chưa
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // Nếu chưa thì yêu cầu cấp quyền
                requestPermissions(new String[]{Manifest.permission.CAMERA}, Constants.REQUEST_PERMISSION_CAMERA);
            } else {
                // Nếu đã cấp quyền thì mở app camera
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, Constants.REQUEST_TAKE_PHOTO);
            }
        });
    }

    /*
     * Xử lý kết quả cấp quyền (permission) tại đây
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.REQUEST_PERMISSION_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, Constants.REQUEST_TAKE_PHOTO);
            } else {
                Toast.makeText(this, "Chưa cấp quyền sử dụng camera", Toast.LENGTH_LONG).show();
            }
        }
    }

    /*
     * Xử lý kết quả chọn ảnh từ thư viện, chụp ảnh từ camera tại đây
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_PICK_FILE) {
            // Nếu request là chọn ảnh từ thư viện

            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();

                File file = FileUtil.from(this, uri);
                Picasso.with(this).load(file).resize(100, 100).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        binding.imgIconPreview.setImageBitmap(bitmap);
                        iconBitmap = bitmap;
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }
        } else if (requestCode == Constants.REQUEST_TAKE_PHOTO) {
            // request là chụp ảnh từ camera

            if (resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");

                // resize ảnh về kích thước 100x100 để tối ưu bộ nhớ
                iconBitmap = Helpers.getResizedBitmap(photo, 100, 100);

                binding.imgIconPreview.setImageBitmap(iconBitmap);
            }
        }
    }


    private void getIntentData() {
        Intent intentData = getIntent();

        if (intentData.hasExtra(Constants.MEDICINE)) {
            oldMedicine = (Thuoc) intentData.getSerializableExtra(Constants.MEDICINE);

            binding.edMaThuoc.setText(oldMedicine.maThuoc);
            binding.edTenThuoc.setText(oldMedicine.tenThuoc);
            binding.edDVT.setText(oldMedicine.donViTinh);
            binding.edDonGia.setText(Helpers.floatToString(oldMedicine.donGia));

            if (oldMedicine.icon != null) {
                binding.imgIconPreview.setImageBitmap(Helpers.bytesToBitmap(oldMedicine.icon));
            }

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
                Float.parseFloat(binding.edDonGia.getText().toString()),
                Helpers.bitmapToBytes(iconBitmap));
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