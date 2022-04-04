package com.team28.qlnhathuoc.ui.bill.bill_create.bill_preview;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.team28.qlnhathuoc.databinding.ItemSpinnerPharmacyBinding;
import com.team28.qlnhathuoc.room.entity.NhaThuoc;
import com.team28.qlnhathuoc.utils.Helpers;

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
        @SuppressLint("ViewHolder")
        ItemSpinnerPharmacyBinding binding = ItemSpinnerPharmacyBinding.inflate(LayoutInflater.from(fragment.getContext()));
        NhaThuoc pharmacy = pharmacyList.get(i);
        binding.tvMaNT.setText(pharmacy.maNT);
        binding.tvTenNT.setText(pharmacy.tenNT);
        if (pharmacy.icon != null)
            binding.imgIcon.setImageBitmap(Helpers.bytesToBitmap(pharmacy.icon));
        return binding.getRoot();
    }
}
