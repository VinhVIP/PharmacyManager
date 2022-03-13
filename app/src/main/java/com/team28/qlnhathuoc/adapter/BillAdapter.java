package com.team28.qlnhathuoc.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team28.qlnhathuoc.databinding.ItemBillBinding;
import com.team28.qlnhathuoc.fragment.BillFragment;
import com.team28.qlnhathuoc.room.entity.relations.HoaDonWithThuoc;
import com.team28.qlnhathuoc.utils.Helpers;

import java.util.ArrayList;
import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {

    private List<HoaDonWithThuoc> billsList;
    private BillFragment fragment;

    public BillAdapter(BillFragment fragment) {
        this.fragment = fragment;
        billsList = new ArrayList<>();
    }

    public BillAdapter(List<HoaDonWithThuoc> billsList) {
        this.billsList = billsList;
    }

    public void setAdapter(List<HoaDonWithThuoc> pharmacyList) {
        this.billsList = pharmacyList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemBillBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(billsList.get(position));
    }

    @Override
    public int getItemCount() {
        return billsList == null ? 0 : billsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ItemBillBinding itemBinding;

        public ViewHolder(@NonNull ItemBillBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        public void bindData(HoaDonWithThuoc bill) {
            itemBinding.tvSoHD.setText(bill.hoaDon.soHD);
            itemBinding.tvTenNT.setText(bill.pharmacy.tenNT);
            itemBinding.tvNgayHD.setText(Helpers.getStringDate(bill.hoaDon.ngayHD));
            itemBinding.tvTotalMoney.setText(Helpers.formatCurrency(bill.totalMoney) + " đ");

            itemBinding.getRoot().setOnClickListener(v -> {
                fragment.goToBillDetail(bill);
            });

            itemBinding.getRoot().setOnLongClickListener(v -> {
//                fragment.showDialogDeletePharmacy(medicine);
                return false;
            });
        }
    }
}
