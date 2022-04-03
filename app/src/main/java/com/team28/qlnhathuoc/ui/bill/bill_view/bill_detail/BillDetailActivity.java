package com.team28.qlnhathuoc.ui.bill.bill_view.bill_detail;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.team28.qlnhathuoc.R;
import com.team28.qlnhathuoc.databinding.ActivityBillDetailBinding;
import com.team28.qlnhathuoc.room.entity.relations.HoaDonWithThuoc;
import com.team28.qlnhathuoc.utils.Constants;
import com.team28.qlnhathuoc.utils.Helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bill_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_pdf:
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Nếu chưa thì yêu cầu cấp quyền
                    requestPermissions(new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    }, Constants.REQUEST_PERMISSION_EXTERNAL_STORAGE);
                } else {
                    exportBillPDF();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.REQUEST_PERMISSION_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exportBillPDF();
            } else {
                Toast.makeText(this, "Chưa cấp quyền truy cập bộ nhớ", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void exportBillPDF() {
        int pageHeight = 600;
        int pageWidth = 300;

        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        canvas.drawText("Hóa đơn ndsnvjpsdi vpdnvps", 50, 50, paint);
        canvas.drawText("Hóa đơn dsssssssssssssssssss vpdnvps", 10, 10, paint);

        pdfDocument.finishPage(page);

        File file = getApplicationContext().getExternalFilesDir(bill.hoaDon.soHD + ".pdf");
        if (file.exists()) file.delete();

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(this, file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pdfDocument.close();
        }
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