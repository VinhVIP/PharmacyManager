package com.team28.qlnhathuoc.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.team28.qlnhathuoc.activity.PharmacyFormActivity;
import com.team28.qlnhathuoc.adapter.PharmacyAdapter;
import com.team28.qlnhathuoc.databinding.FragmentPharmacyBinding;
import com.team28.qlnhathuoc.room.entity.NhaThuoc;
import com.team28.qlnhathuoc.utils.Constants;
import com.team28.qlnhathuoc.utils.Helpers;
import com.team28.qlnhathuoc.viewmodel.PharmacyViewModel;

public class PharmacyFragment extends Fragment {

    private FragmentPharmacyBinding binding;

    private PharmacyViewModel viewModel;

    public PharmacyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPharmacyBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(PharmacyViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerPharmacy.setLayoutManager(new LinearLayoutManager(this.getContext()));

        PharmacyAdapter adapter = new PharmacyAdapter(this);
        binding.recyclerPharmacy.setAdapter(adapter);

        viewModel.pharmacyList.observe(getActivity(), nhaThuocs -> adapter.setAdapter(nhaThuocs));

        binding.fab.setOnClickListener(v -> {
            Intent intent = new Intent(this.getActivity(), PharmacyFormActivity.class);
            startActivity(intent);
        });
    }

    public void goToEditPharmacy(NhaThuoc pharmacy) {
        Intent intent = new Intent(this.getActivity(), PharmacyFormActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString(Constants.MANT, pharmacy.maNT);
        bundle.putString(Constants.TENNT, pharmacy.tenNT);
        bundle.putString(Constants.DIACHI, pharmacy.diaChi);

        intent.putExtra(Constants.REQUEST_ACTION, bundle);
        startActivity(intent);
    }

    public void showDialogDeletePharmacy(NhaThuoc pharmacy) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Xác nhận xóa");
//        alertDialogBuilder.setIcon(R.drawable.question);
        builder.setMessage(String.format("Bạn thực sự muốn xóa nhà thuốc %s ?", pharmacy.tenNT));
        builder.setCancelable(true);

        builder.setPositiveButton("Đồng ý", (arg0, arg1) -> {
            if (viewModel.canDeletePharmacy(pharmacy)) {
                viewModel.deletePharmacy(pharmacy);
            } else {
                Helpers.showToast(this.getContext(), "Nhà thuốc đã có dữ liệu nên không thể xóa!");
            }
        });
        builder.setNeutralButton("Hủy", (dialog, which) -> {
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}