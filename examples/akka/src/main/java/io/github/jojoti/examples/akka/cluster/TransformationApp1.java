package io.github.jojoti.examples.akka.cluster;

public class TransformationApp1 {

    public static void main(String[] args) {
        // starting 2 frontend nodes and 3 backend nodes
        TransformationBackendMain.main(new String[]{"2552"});
    }
}
