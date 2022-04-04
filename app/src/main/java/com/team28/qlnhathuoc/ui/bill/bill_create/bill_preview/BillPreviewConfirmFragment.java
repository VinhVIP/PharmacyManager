package com.team28.qlnhathuoc.ui.bill.bill_create.bill_preview;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.team28.qlnhathuoc.databinding.DialogSendBillBinding;
import com.team28.qlnhathuoc.databinding.FragmentBillPreviewConfirmBinding;
import com.team28.qlnhathuoc.room.entity.Thuoc;
import com.team28.qlnhathuoc.ui.bill.bill_create.BillCreateViewModel;
import com.team28.qlnhathuoc.utils.Helpers;

import java.util.List;
import java.util.stream.Collectors;


public class BillPreviewConfirmFragment extends Fragment {

    private FragmentBillPreviewConfirmBinding binding;

    private BillCreateViewModel viewModel;

    private BillPreviewAdapter adapter;
    private SpinnerPharmacyAdapter spinnerAdapter;

    public BillPreviewConfirmFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBillPreviewConfirmBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(BillCreateViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
        setupSpinner();
    }

    private void setupRecyclerView() {
        adapter = new BillPreviewAdapter(this);
        binding.recycler.setAdapter(adapter);
        binding.recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.btnConfirmBill.setOnClickListener(v -> {
            if (viewModel.getTotalMoney().getValue() == 0f) {
                Toast.makeText(getContext(), "Vui lòng chọn thuốc muốn mua!", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.insertBill();
                Toast.makeText(getContext(), "Lập hóa đơn thành công", Toast.LENGTH_SHORT).show();
                showDialogSendMail();
            }
        });

        viewModel.getTotalMoney().observe(getActivity(), totalMoney -> {
            binding.tvTotalMoney.setText(Helpers.formatCurrency(totalMoney) + " đ");

        });

        viewModel.medicinesChoose.observe(getActivity(), medicinesChooseList -> {
            adapter.setAdapter(medicinesChooseList
                    .stream().filter(medicine -> medicine.soLuong > 0)
                    .collect(Collectors.toList()));
        });
    }

    private void setupSpinner() {
        viewModel.pharmacyList.observe(getActivity(), pharmacyList -> {
            spinnerAdapter = new SpinnerPharmacyAdapter(pharmacyList, this);
            binding.spinnerNT.setAdapter(spinnerAdapter);
        });

        binding.spinnerNT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                viewModel.pharmacyChoose = spinnerAdapter.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void changeMedicineAmount(Thuoc medicine) {
        BottomSheetChangeAmountFragment fragment = new BottomSheetChangeAmountFragment(medicine, this);
        fragment.show(getActivity().getSupportFragmentManager(), "change_amount");
    }

    public void minusAmount(Thuoc medicine) {
        viewModel.minus(medicine);
        adapter.setAdapter(viewModel.medicinesChoose.getValue()
                .stream().filter(m -> m.soLuong > 0)
                .collect(Collectors.toList()));
    }

    public void plusAmount(Thuoc medicine) {
        viewModel.plus(medicine);
        adapter.setAdapter(viewModel.medicinesChoose.getValue()
                .stream().filter(m -> m.soLuong > 0)
                .collect(Collectors.toList()));
    }

    private void showDialogSendMail() {
        Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(false);
        DialogSendBillBinding dialogBinding = DialogSendBillBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialogBinding.btnDialogCancel.setOnClickListener(v -> {
            dialog.dismiss();
            getActivity().finish();
        });

        dialogBinding.btnDialogOK.setOnClickListener(v -> {
            String email = dialogBinding.edEmail.getText().toString().trim();
            if (Helpers.isValidEmail(email)) {
                sendEmail(email);
            } else {
                Toast.makeText(getContext(), "Định dạng email không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void sendEmail(String email) {
        String[] TO = {email};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.setPackage("com.google.android.gm");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hóa đơn mua hàng");

        List<Thuoc> list = viewModel.medicinesChoose.getValue();
        StringBuilder builder = new StringBuilder("Danh sách thuốc:\n\n");
        for (Thuoc medicine : list) {
            if (medicine.soLuong > 0) {
                builder.append(medicine.soLuong).append(" ").append(medicine.donViTinh).append(" : ");
                builder.append(medicine.tenThuoc).append(" - ");
                builder.append(Helpers.formatCurrency(medicine.donGia * medicine.soLuong)).append(" đ");
                builder.append("\n");
            }
        }

        builder.append("\nTổng cộng: ")
                .append(Helpers.formatCurrency(viewModel.getTotalMoney().getValue()))
                .append(" đ");

        emailIntent.putExtra(Intent.EXTRA_TEXT, builder.toString());

        try {
            startActivity(Intent.createChooser(emailIntent, "Gửi hóa đơn cho khách ahngf"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this.getContext(), "Không có ứng dụng gửi mail nào có sẵn!", Toast.LENGTH_SHORT).show();
        }
    }
}