package com.team28.qlnhathuoc.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team28.qlnhathuoc.databinding.ItemMedicineBillBinding;
import com.team28.qlnhathuoc.ui.bill.bill_create.BillChooseMedicineFragment;
import com.team28.qlnhathuoc.room.entity.Thuoc;
import com.team28.qlnhathuoc.utils.Helpers;

import java.util.ArrayList;
import java.util.List;

public class MedicineBillAdapter extends RecyclerView.Adapter<MedicineBillAdapter.ViewHolder> {

    private List<Thuoc> medicineList;

    private BillChooseMedicineFragment fragment;


    public MedicineBillAdapter(BillChooseMedicineFragment fragment) {
        this.fragment = fragment;
        medicineList = new ArrayList<>();
    }

    public MedicineBillAdapter(BillChooseMedicineFragment fragment, List<Thuoc> medicineList) {
        this.fragment = fragment;
        this.medicineList = medicineList;
    }

    public void setAdapter(List<Thuoc> medicineList) {
        this.medicineList = medicineList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemMedicineBillBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(medicineList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return medicineList == null ? 0 : medicineList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemMedicineBillBinding itemBinding;

        public ViewHolder(@NonNull ItemMedicineBillBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        public void bindData(Thuoc medicine, int position) {
            itemBinding.tvMaThuoc.setText(medicine.maThuoc);
            itemBinding.tvDonViTinh.setText(medicine.donViTinh);
            itemBinding.tvTenThuoc.setText(medicine.tenThuoc);
            itemBinding.tvDonGia.setText(Helpers.formatCurrency(medicine.donGia));
            itemBinding.tvNumber.setText(medicine.soLuong + "");

            itemBinding.btnMinus.setOnClickListener(v -> fragment.minus(medicine, position));
            itemBinding.btnPlus.setOnClickListener(v -> fragment.plus(medicine, position));

            itemBinding.getRoot().setOnClickListener(v -> fragment.plus(medicine, position));
        }
    }
}
