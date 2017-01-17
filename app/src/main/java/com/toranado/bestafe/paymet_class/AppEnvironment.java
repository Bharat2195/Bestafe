package com.toranado.bestafe.paymet_class;

import com.citrus.sdk.Environment;

/**
 * Created by vijay on 14/7/16.
 */
public enum AppEnvironment {
//    SANDBOX {
//        @Override
//        public String getBillUrl() {
//            return "http://bestafe.in/catalog/view/theme/kingstorepro/template/common/bill.php";
//        }
//
//        @Override
//        public String getVanity() {
//            return "nativeSDK";
//        }
//
//        @Override
//        public String getSignUpId() {
//            return "9hh5re3r5q-signup";
//        }
//
//        @Override
//        public String getSignUpSecret() {
//            return "3be4d7bf59c109e76a3619a33c1da9a8";
//        }
//
//        @Override
//        public String getSignInId() {
//            return "9hh5re3r5q-signin";
//        }
//
//        @Override
//        public String getSignInSecret() {
//            return "ffcfaaf6e6e78c2f654791d9d6cb7f09";
//        }
//
//        @Override
//        public Environment getEnvironment() {
//            return Environment.SANDBOX;
//        }
//    },
    PRODUCTION {
        @Override
        public String getBillUrl() {
            return "http://bestafe.in/catalog/view/theme/kingstorepro/template/common/bill.php";
        }

        @Override
        public String getVanity() {
            return "8fdman6cdo";
        }

        @Override
        public String getSignUpId() {
            return "8fdman6cdo-signup";
        }

        @Override
        public String getSignUpSecret() {
            return "b6f68f9f5843d2bdfe7cdece8fbb87b9";
        }

        @Override
        public String getSignInId() {
            return "8fdman6cdo-signin";
        }

        @Override
        public String getSignInSecret() {
            return "ab07bff6ebc83a6a09bb5264f1fdca9b";
        }

        @Override
        public Environment getEnvironment() {
            return Environment.PRODUCTION;
        }
    };

    public abstract String getBillUrl();

    public abstract String getVanity();

    public abstract String getSignUpId();

    public abstract String getSignUpSecret();

    public abstract String getSignInId();

    public abstract String getSignInSecret();

    public abstract Environment getEnvironment();
}
