package com.team28.qlnhathuoc.utils;

import android.os.AsyncTask;

import com.team28.qlnhathuoc.room.dao.BillDao;
import com.team28.qlnhathuoc.room.dao.CTBanLeDao;
import com.team28.qlnhathuoc.room.dao.PharmacyDao;
import com.team28.qlnhathuoc.room.entity.CTBanLe;
import com.team28.qlnhathuoc.room.entity.NhaThuoc;

public class Task {
    public static class GetCTBanLeTask extends AsyncTask<String, Void, CTBanLe> {
        private final CTBanLeDao dao;

        public GetCTBanLeTask(CTBanLeDao dao) {
            this.dao = dao;
        }

        @Override
        protected CTBanLe doInBackground(String... strings) {
            return dao.getCTBanLe(strings[0], strings[1]);
        }
    }

    public static class GetBillsSizeTask extends AsyncTask<Void, Void, Integer> {
        private final BillDao dao;

        public GetBillsSizeTask(BillDao dao) {
            this.dao = dao;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return dao.getBillsSize();
        }
    }

    public static class GetPharmacyByIdTask extends AsyncTask<String, Void, NhaThuoc> {
        private final PharmacyDao dao;

        public GetPharmacyByIdTask(PharmacyDao dao) {
            this.dao = dao;
        }

        @Override
        protected NhaThuoc doInBackground(String... strings) {
            return dao.getPharmacyById(strings[0]);
        }
    }

    public static class GetPharmacyByNameTask extends AsyncTask<String, Void, NhaThuoc> {
        private final PharmacyDao dao;

        public GetPharmacyByNameTask(PharmacyDao dao) {
            this.dao = dao;
        }

        @Override
        protected NhaThuoc doInBackground(String... strings) {
            return dao.getPharmacyByName(strings[0]);
        }
    }
}
