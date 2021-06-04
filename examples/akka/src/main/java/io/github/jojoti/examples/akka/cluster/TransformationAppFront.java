package io.github.jojoti.examples.akka.cluster;

public class TransformationAppFront {

    public static void main(String[] args) {
        // starting 2 frontend nodes and 3 backend nodes
        TransformationFrontendMain.main(new String[]{"2554"});
    }
}
