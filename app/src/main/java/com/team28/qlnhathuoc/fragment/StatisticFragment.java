package com.team28.qlnhathuoc.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.team28.qlnhathuoc.activity.ChartMedicineActivity;
import com.team28.qlnhathuoc.activity.ChartPharmacyActivity;
import com.team28.qlnhathuoc.databinding.FragmentStatisticBinding;

public class StatisticFragment extends Fragment {

    private FragmentStatisticBinding binding;

    public StatisticFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatisticBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnStatisticPharmacy.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ChartPharmacyActivity.class));
        });
        binding.btnStatisticMedicine.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ChartMedicineActivity.class));
        });
    }
}