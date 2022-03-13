package com.team28.qlnhathuoc.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team28.qlnhathuoc.databinding.ItemMedicineBinding;
import com.team28.qlnhathuoc.fragment.MedicineFragment;
import com.team28.qlnhathuoc.room.entity.relations.ThuocWithHoaDon;
import com.team28.qlnhathuoc.utils.Helpers;

import java.util.ArrayList;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {

    private List<ThuocWithHoaDon> medicineList;
    private MedicineFragment fragment;

    public MedicineAdapter(MedicineFragment fragment) {
        this.fragment = fragment;
        medicineList = new ArrayList<>();
    }

    public MedicineAdapter(List<ThuocWithHoaDon> medicineList) {
        this.medicineList = medicineList;
    }

    public void setAdapter(List<ThuocWithHoaDon> pharmacyList) {
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
        return medicineList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ItemMedicineBinding itemBinding;

        public ViewHolder(@NonNull ItemMedicineBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        public void bindData(ThuocWithHoaDon medicine) {
            itemBinding.tvMaThuoc.setText(medicine.thuoc.maThuoc);
            itemBinding.tvTenThuoc.setText(medicine.thuoc.tenThuoc);
            itemBinding.tvDonGia.setText(Helpers.formatCurrency(medicine.thuoc.donGia) + " đ");
            itemBinding.tvDonViTinh.setText(medicine.thuoc.donViTinh);

            float totalMoney = medicine.total * medicine.thuoc.donGia;
            itemBinding.tvTotal.setText(medicine.total + " " + medicine.thuoc.donViTinh + " -> " + Helpers.formatCurrency(totalMoney) + " đ");

            itemBinding.getRoot().setOnClickListener(v -> {
                fragment.goToEditMedicine(medicine.thuoc);
            });

            itemBinding.getRoot().setOnLongClickListener(v -> {
//                fragment.showDialogDeletePharmacy(medicine);
                return false;
            });
        }
    }
}
