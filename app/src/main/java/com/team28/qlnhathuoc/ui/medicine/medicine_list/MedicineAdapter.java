package com.team28.qlnhathuoc.ui.medicine.medicine_list;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team28.qlnhathuoc.databinding.ItemMedicineBinding;
import com.team28.qlnhathuoc.room.entity.Thuoc;
import com.team28.qlnhathuoc.utils.Helpers;

import java.util.ArrayList;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {

    private List<Thuoc> medicineList;
    private MedicineFragment fragment;

    public MedicineAdapter(MedicineFragment fragment) {
        this.fragment = fragment;
        medicineList = new ArrayList<>();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setAdapter(List<Thuoc> pharmacyList) {
        this.medicineList = pharmacyList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MedicineAdapter.ViewHolder(ItemMedicineBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(medicineList.get(position));
    }

    @Override
    public int getItemCount() {
        return medicineList == null ? 0 : medicineList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ItemMedicineBinding itemBinding;

        public ViewHolder(@NonNull ItemMedicineBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        public void bindData(Thuoc medicine) {
            itemBinding.tvMaThuoc.setText(medicine.maThuoc);
            itemBinding.tvTenThuoc.setText(medicine.tenThuoc);
            itemBinding.tvDonGia.setText(Helpers.formatCurrency(medicine.donGia) + " đ");
            itemBinding.tvDonViTinh.setText(medicine.donViTinh);


            itemBinding.getRoot().setOnClickListener(v -> {
                fragment.goToEditMedicine(medicine);
            });

        }
    }
}
