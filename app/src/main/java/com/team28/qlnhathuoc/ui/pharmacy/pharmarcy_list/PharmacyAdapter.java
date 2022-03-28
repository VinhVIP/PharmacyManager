package com.team28.qlnhathuoc.ui.pharmacy.pharmarcy_list;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team28.qlnhathuoc.databinding.ItemPharmacyBinding;
import com.team28.qlnhathuoc.room.entity.NhaThuoc;

import java.util.ArrayList;
import java.util.List;

public class PharmacyAdapter extends RecyclerView.Adapter<PharmacyAdapter.ViewHolder> {

    private List<NhaThuoc> pharmacyList;
    private PharmacyFragment fragment;

    public PharmacyAdapter(PharmacyFragment fragment) {
        this.fragment = fragment;
        pharmacyList = new ArrayList<>();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setAdapter(List<NhaThuoc> pharmacyList) {
        this.pharmacyList = pharmacyList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemPharmacyBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(pharmacyList.get(position));
    }

    @Override
    public int getItemCount() {
        return pharmacyList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ItemPharmacyBinding itemBinding;

        public ViewHolder(@NonNull ItemPharmacyBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        public void bindData(NhaThuoc pharmacy) {
            itemBinding.tvMaNT.setText(pharmacy.maNT);
            itemBinding.tvTenNT.setText(pharmacy.tenNT);
            itemBinding.tvDiaChi.setText(pharmacy.diaChi);

            itemBinding.getRoot().setOnClickListener(v -> {
                fragment.goToEditPharmacy(pharmacy);
            });

        }
    }
}
