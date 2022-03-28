package com.team28.qlnhathuoc.ui.bill.bill_create;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.team28.qlnhathuoc.R;
import com.team28.qlnhathuoc.databinding.ActivityBillCreateBinding;
import com.team28.qlnhathuoc.ui.bill.bill_create.bill_choose.BillChooseMedicineFragment;
import com.team28.qlnhathuoc.ui.bill.bill_create.bill_preview.BillPreviewConfirmFragment;

public class BillCreateActivity extends AppCompatActivity {

    private ActivityBillCreateBinding binding;

    private BillCreateViewModel viewModel;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBillCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = new ViewModelProvider(this).get(BillCreateViewModel.class);

        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.fragmentChooseMedicine, BillChooseMedicineFragment.class, null)
                .setReorderingAllowed(true)
                .commit();

        if (viewModel.isShowPreviewBill) {
            clearAllFragmentBackStack();
            previewBill();
        }
    }

    public void previewBill() {
        fragmentManager.beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                )
                .replace(R.id.fragmentChooseMedicine, BillPreviewConfirmFragment.class, null)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();

    }

    private void clearAllFragmentBackStack() {
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}