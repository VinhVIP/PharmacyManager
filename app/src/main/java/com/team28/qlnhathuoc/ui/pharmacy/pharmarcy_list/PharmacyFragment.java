package com.team28.qlnhathuoc.ui.pharmacy.pharmarcy_list;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.team28.qlnhathuoc.databinding.FragmentPharmacyBinding;
import com.team28.qlnhathuoc.room.entity.NhaThuoc;
import com.team28.qlnhathuoc.ui.pharmacy.pharmacy_form.PharmacyFormActivity;
import com.team28.qlnhathuoc.utils.Constants;

public class PharmacyFragment extends Fragment {

    private FragmentPharmacyBinding binding;

    private PharmacyViewModel viewModel;

    public PharmacyFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPharmacyBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(PharmacyViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set RecyclerView scroll theo chiều dọc
        binding.recyclerPharmacy.setLayoutManager(new LinearLayoutManager(this.getContext()));

        PharmacyAdapter adapter = new PharmacyAdapter(this);
        binding.recyclerPharmacy.setAdapter(adapter);

        // Quan sát sự thay đổi dữ liệu danh sách nhà thuốc
        // Cập nhật lên RecyclerView
        viewModel.pharmacyList.observe(getActivity(), nhaThuocs -> adapter.setAdapter(nhaThuocs));

        // Di chuyển đếm form thêm/sửa/xóa thuốc
        binding.fab.setOnClickListener(v -> {
            Intent intent = new Intent(this.getActivity(), PharmacyFormActivity.class);
            startActivity(intent);
        });
    }

    public void goToEditPharmacy(NhaThuoc pharmacy) {
        Intent intent = new Intent(this.getActivity(), PharmacyFormActivity.class);

        intent.putExtra(Constants.PHARMACY, pharmacy);

        startActivity(intent);
    }

}