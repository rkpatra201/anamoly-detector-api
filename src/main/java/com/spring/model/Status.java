package com.spring.model;

public enum Status {
    NO_ANOMALY{
        @Override
        public String getCause() {
            return "Below the Upper Threshold Detector";
        }

        @Override
        public String getMessage() {
            return "Below threshold";
        }
    }, ANOMALY{
        @Override
        public String getCause() {
            return "Upper Bound Threshold Detector";
        }
        @Override
        public String getMessage() {
            return "Exceeds threshold";
        }
    }, NO_MODEL{
        @Override
        public String getCause() {
            return "";
        }

        @Override
        public String getMessage() {
            return "";
        }
    }, ERROR{
        @Override
        public String getCause() {
            return "errorCause";
        }

        @Override
        public String getMessage() {
            return "errorMessage";
        }
    };

    public abstract String getCause();
    public abstract String getMessage();

}
