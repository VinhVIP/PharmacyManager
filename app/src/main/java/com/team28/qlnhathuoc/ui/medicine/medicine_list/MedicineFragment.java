package com.team28.qlnhathuoc.ui.medicine.medicine_list;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.team28.qlnhathuoc.databinding.FragmentMedicineBinding;
import com.team28.qlnhathuoc.room.entity.Thuoc;
import com.team28.qlnhathuoc.ui.medicine.medicine_form.MedicineFormActivity;
import com.team28.qlnhathuoc.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class MedicineFragment extends Fragment {

    private FragmentMedicineBinding binding;

    private MedicineViewModel viewModel;

    private MedicineAdapter adapter;


    public MedicineFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMedicineBinding.inflate(getLayoutInflater(), container, false);
        viewModel = new ViewModelProvider(getActivity()).get(MedicineViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerMedicine.setLayoutManager(new LinearLayoutManager(this.getContext()));

        adapter = new MedicineAdapter(this);
        binding.recyclerMedicine.setAdapter(adapter);

        // Quan sát dữ liệu danh sách thuốc
        viewModel.medicineList.observe(getActivity(), medicineList -> {
            // Lọc danh sách theo từ khóa
            filterSearch(getKeyword());
        });

        // Di chuyển đến Form thuốc
        binding.fab.setOnClickListener(v -> {
            Intent intent = new Intent(this.getActivity(), MedicineFormActivity.class);
            startActivity(intent);
        });

        // Xóa từ khóa tìm kiếm
        binding.btnClear.setOnClickListener(v -> binding.edSearch.setText(""));


        searchMedicine();
    }

    /*
     * Bắt sự kiện EditText edSearch thay đổi nội dung
     * Cứ mỗi khi nội dung thay đổi thì ta lại tiến hành lọc lại danh sách thuốc được tìm kiếm theo từ khóa
     */
    private void searchMedicine() {
        binding.edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Hàm này được gọi sau khi text đã thay đổi
                // Tiến hành lọc kết quả ở đây
                filterSearch(getKeyword());
            }
        });
    }

    private void filterSearch(String keyword) {
        if (keyword.isEmpty()) {
            // Nếu không có từ khóa tìm kiếm
            // thì hiển thị toàn bộ danh sách thuốc
            adapter.setAdapter(viewModel.medicineList.getValue());
            return;
        }

        // Tiến hành lọc danh sách theo từ khóa, đưa danh sách vào 1 List mới là searchList
        List<Thuoc> searchList = new ArrayList<>();

        if (viewModel.medicineList.getValue() != null)
            for (Thuoc medicine : viewModel.medicineList.getValue()) {
                if (medicine.maThuoc.toLowerCase().contains(keyword) || medicine.tenThuoc.toLowerCase().contains(keyword)) {
                    searchList.add(medicine);
                }
            }

        // Cập nhật lại adapter cho RecyclerView
        adapter.setAdapter(searchList);
    }

    // Lấy từ khóa hiện tại
    private String getKeyword() {
        return binding.edSearch.getText().toString().trim().toLowerCase();
    }


    public void goToEditMedicine(Thuoc medicine) {
        Intent intent = new Intent(this.getActivity(), MedicineFormActivity.class);
        intent.putExtra(Constants.MEDICINE, medicine);

        startActivity(intent);
    }

}