package com.team28.qlnhathuoc.ui.bill.bill_create.bill_preview;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team28.qlnhathuoc.databinding.ItemBillDetailPreviewBinding;
import com.team28.qlnhathuoc.room.entity.Thuoc;
import com.team28.qlnhathuoc.utils.Helpers;

import java.util.ArrayList;
import java.util.List;

public class BillPreviewAdapter extends RecyclerView.Adapter<BillPreviewAdapter.ViewHolder> {

    private List<Thuoc> medicines;
    private BillPreviewConfirmFragment fragment;

    public BillPreviewAdapter(BillPreviewConfirmFragment fragment) {
        this.fragment = fragment;
        medicines = new ArrayList<>();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setAdapter(List<Thuoc> medicines) {
        this.medicines = medicines;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemBillDetailPreviewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(medicines.get(position));
    }

    @Override
    public int getItemCount() {
        return medicines == null ? 0 : medicines.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemBillDetailPreviewBinding itemBinding;

        public ViewHolder(@NonNull ItemBillDetailPreviewBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        public void bindData(Thuoc medicine) {
            itemBinding.tvSoLuong.setText(medicine.soLuong + "");
            itemBinding.tvMaThuoc.setText(medicine.maThuoc);
            itemBinding.tvTenThuoc.setText(medicine.tenThuoc);
            itemBinding.tvDonGia.setText(Helpers.formatCurrency(medicine.donGia) + " đ / " + medicine.donViTinh);
            itemBinding.tvTotalMoney.setText(Helpers.formatCurrency(medicine.donGia * medicine.soLuong) + " đ");

            if (fragment != null) {
                itemBinding.getRoot().setOnClickListener(v -> fragment.changeMedicineAmount(medicine));
            }
        }
    }
}