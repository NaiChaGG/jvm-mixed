package io.github.jojoti.examples.akka.cluster;

import java.io.Serializable;

public interface TransformationMessages {

    String BACKEND_REGISTRATION = "BackendRegistration";

    class TransformationJob implements Serializable {
        private final String text;

        public TransformationJob(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

    class TransformationResult implements Serializable {
        private final String text;

        public TransformationResult(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        @Override
        public String toString() {
            return "TransformationResult(" + text + ")";
        }
    }

    class JobFailed implements Serializable {
        private final String reason;
        private final TransformationJob job;

        public JobFailed(String reason, TransformationJob job) {
            this.reason = reason;
            this.job = job;
        }

        public String getReason() {
            return reason;
        }

        public TransformationJob getJob() {
            return job;
        }

        @Override
        public String toString() {
            return "JobFailed(" + reason + ")";
        }
    }

}
