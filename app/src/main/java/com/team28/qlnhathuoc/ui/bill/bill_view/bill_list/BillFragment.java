package com.team28.qlnhathuoc.ui.bill.bill_view.bill_list;

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

import com.team28.qlnhathuoc.adapter.BillAdapter;
import com.team28.qlnhathuoc.databinding.FragmentBillBinding;
import com.team28.qlnhathuoc.room.entity.CTBanLe;
import com.team28.qlnhathuoc.room.entity.Thuoc;
import com.team28.qlnhathuoc.room.entity.relations.HoaDonWithThuoc;
import com.team28.qlnhathuoc.ui.bill.bill_create.BillCreateActivity;
import com.team28.qlnhathuoc.ui.bill.bill_view.bill_detail.BillDetailActivity;
import com.team28.qlnhathuoc.utils.Constants;

public class BillFragment extends Fragment {

    private FragmentBillBinding binding;

    private BillAdapter adapter;

    private BillViewModel viewModel;

    public BillFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBillBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(BillViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new BillAdapter(this);
        binding.recyclerBill.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.recyclerBill.setAdapter(adapter);

        viewModel.billsList.observe(getActivity(), billsList -> {
            // Lấy từng hóa đơn ra, duyệt danh sách thuốc (số lượng, đơn giá) để tính tổng tiền của hóa đơn
            for (HoaDonWithThuoc bill : billsList) {
                int total = 0;
                for (Thuoc medicine : bill.thuocList) {
                    CTBanLe ctBanLe = viewModel.getCTBanLe(medicine.maThuoc, bill.hoaDon.soHD);

                    if (ctBanLe != null) {
                        // Lưu lại số lượng thuốc vào đối tượng medicine
                        medicine.soLuong = ctBanLe.soLuong;

                        total += medicine.donGia * medicine.soLuong;
                    }
                }

                bill.totalMoney = total;
            }

            // Cập nhật dữ liệu cho RecyclerView
            adapter.setAdapter(billsList);
        });

        // Mở form lập hóa đơn
        binding.fab.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), BillCreateActivity.class));
        });
    }

    /*
     * Đi đến màn hình xem chi tiết hóa đơn
     */
    public void goToBillDetail(HoaDonWithThuoc bill) {
        Intent intent = new Intent(getActivity(), BillDetailActivity.class);
        intent.putExtra(Constants.BILL_DETAIL, bill);
        startActivity(intent);
    }
}