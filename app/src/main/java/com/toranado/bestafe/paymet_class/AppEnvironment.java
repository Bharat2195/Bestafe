package com.toranado.bestafe.paymet_class;

import com.citrus.sdk.Environment;

/**
 * Created by vijay on 14/7/16.
 */
public enum AppEnvironment {
    SANDBOX {
        @Override
        public String getBillUrl() {
            return "http://bestafe.in/catalog/view/theme/kingstorepro/template/common/bill.php";
        }

        @Override
        public String getVanity() {
            return "69wy3vy3tl";
        }

        @Override
        public String getSignUpId() {
            return "69wy3vy3tl-signup";
        }

        @Override
        public String getSignUpSecret() {
            return "51029a02860885c2f1b46f0e49656b8e";
        }

        @Override
        public String getSignInId() {
            return "69wy3vy3tl-signin";
        }

        @Override
        public String getSignInSecret() {
            return "be9b6794e497a90b2e9fc66a7b867024";
        }

        @Override
        public Environment getEnvironment() {
            return Environment.SANDBOX;
        }
    },
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
