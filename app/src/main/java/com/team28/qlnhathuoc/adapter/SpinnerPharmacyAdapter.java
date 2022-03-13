package com.team28.qlnhathuoc.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.team28.qlnhathuoc.databinding.ItemSpinnerPharmacyBinding;
import com.team28.qlnhathuoc.fragment.BillPreviewConfirmFragment;
import com.team28.qlnhathuoc.room.entity.NhaThuoc;

import java.util.List;

public class SpinnerPharmacyAdapter extends BaseAdapter {

    private List<NhaThuoc> pharmacyList;
    private BillPreviewConfirmFragment fragment;

    public SpinnerPharmacyAdapter(List<NhaThuoc> pharmacyList, BillPreviewConfirmFragment fragment) {
        this.pharmacyList = pharmacyList;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return pharmacyList == null ? 0 : pharmacyList.size();
    }

    @Override
    public NhaThuoc getItem(int i) {
        return pharmacyList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ItemSpinnerPharmacyBinding binding = ItemSpinnerPharmacyBinding.inflate(LayoutInflater.from(fragment.getContext()));
        NhaThuoc pharmacy = pharmacyList.get(i);
        binding.tvMaNT.setText(pharmacy.maNT);
        binding.tvTenNT.setText(pharmacy.tenNT);
        return binding.getRoot();
    }
}
