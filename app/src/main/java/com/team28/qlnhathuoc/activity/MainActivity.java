package com.team28.qlnhathuoc.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.team28.qlnhathuoc.R;
import com.team28.qlnhathuoc.adapter.ViewPagerAdapter;
import com.team28.qlnhathuoc.databinding.ActivityMainBinding;
import com.team28.qlnhathuoc.fragment.BillFragment;
import com.team28.qlnhathuoc.fragment.MedicineFragment;
import com.team28.qlnhathuoc.fragment.PharmacyFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupViewPager();
        setupBottomNav();
//        MyDao myDao = MyDatabase.getInstance(this).myDao();
//
//        myDao.insertNhaThuoc(new NhaThuoc("NT001", "Nhà thuôc Quang Vinh", "TPHCM"));
//        myDao.insertNhaThuoc(new NhaThuoc("NT002", "Nhà thuôc Hoàng Long", "TPHCM"));
//
//        List<NhaThuoc> list = myDao.nhaThuocList();
//        Log.d("VINH", list.size() + "");
    }

    private void setupViewPager() {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(this);
        pagerAdapter.addFragment(new PharmacyFragment());
        pagerAdapter.addFragment(new MedicineFragment());
        pagerAdapter.addFragment(new BillFragment());

        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.bottomNav.setSelectedItemId(menus[position]);
            }
        });
    }

    int[] menus = {R.id.navNhaThuoc, R.id.navThuoc, R.id.navHoaDon};

    private void setupBottomNav() {
        binding.bottomNav.setOnItemSelectedListener(item -> {
            for (int i = 0; i < menus.length; i++) {
                if (menus[i] == item.getItemId()) {
                    binding.viewPager.setCurrentItem(i);
                    break;
                }
            }
            return true;
        });
    }
}