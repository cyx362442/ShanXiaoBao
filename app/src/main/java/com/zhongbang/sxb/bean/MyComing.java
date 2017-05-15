package com.zhongbang.sxb.bean;

/**
 * Created by Administrator on 2017-05-15.
 */

public class MyComing {

    /**
     * data : {"totalAlipay":"0.00","totalEarnings":"0.00","totalIncoming":"0.00","totalMoney":"2.45","totalProfit":"2.45","totalRemaining":"0.00","totalUnionpay":"0.00","totalWxpay":"0.00","unsettledAlipay":"0.00","unsettledProfit":"2.45","unsettledUnionpay":"0.00","unsettledWxpay":"0.00"}
     * status : 1
     */

    private DataBean data;
    private String status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class DataBean {
        /**
         * totalAlipay : 0.00
         * totalEarnings : 0.00
         * totalIncoming : 0.00
         * totalMoney : 2.45
         * totalProfit : 2.45
         * totalRemaining : 0.00
         * totalUnionpay : 0.00
         * totalWxpay : 0.00
         * unsettledAlipay : 0.00
         * unsettledProfit : 2.45
         * unsettledUnionpay : 0.00
         * unsettledWxpay : 0.00
         */

        private String totalAlipay;
        private String totalEarnings;
        private String totalIncoming;
        private String totalMoney;
        private String totalProfit;
        private String totalRemaining;
        private String totalUnionpay;
        private String totalWxpay;
        private String unsettledAlipay;
        private String unsettledProfit;
        private String unsettledUnionpay;
        private String unsettledWxpay;

        public String getTotalAlipay() {
            return totalAlipay;
        }

        public void setTotalAlipay(String totalAlipay) {
            this.totalAlipay = totalAlipay;
        }

        public String getTotalEarnings() {
            return totalEarnings;
        }

        public void setTotalEarnings(String totalEarnings) {
            this.totalEarnings = totalEarnings;
        }

        public String getTotalIncoming() {
            return totalIncoming;
        }

        public void setTotalIncoming(String totalIncoming) {
            this.totalIncoming = totalIncoming;
        }

        public String getTotalMoney() {
            return totalMoney;
        }

        public void setTotalMoney(String totalMoney) {
            this.totalMoney = totalMoney;
        }

        public String getTotalProfit() {
            return totalProfit;
        }

        public void setTotalProfit(String totalProfit) {
            this.totalProfit = totalProfit;
        }

        public String getTotalRemaining() {
            return totalRemaining;
        }

        public void setTotalRemaining(String totalRemaining) {
            this.totalRemaining = totalRemaining;
        }

        public String getTotalUnionpay() {
            return totalUnionpay;
        }

        public void setTotalUnionpay(String totalUnionpay) {
            this.totalUnionpay = totalUnionpay;
        }

        public String getTotalWxpay() {
            return totalWxpay;
        }

        public void setTotalWxpay(String totalWxpay) {
            this.totalWxpay = totalWxpay;
        }

        public String getUnsettledAlipay() {
            return unsettledAlipay;
        }

        public void setUnsettledAlipay(String unsettledAlipay) {
            this.unsettledAlipay = unsettledAlipay;
        }

        public String getUnsettledProfit() {
            return unsettledProfit;
        }

        public void setUnsettledProfit(String unsettledProfit) {
            this.unsettledProfit = unsettledProfit;
        }

        public String getUnsettledUnionpay() {
            return unsettledUnionpay;
        }

        public void setUnsettledUnionpay(String unsettledUnionpay) {
            this.unsettledUnionpay = unsettledUnionpay;
        }

        public String getUnsettledWxpay() {
            return unsettledWxpay;
        }

        public void setUnsettledWxpay(String unsettledWxpay) {
            this.unsettledWxpay = unsettledWxpay;
        }
    }
}
