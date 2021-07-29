package io.github.trapspring;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public class Debugs {

    private boolean debug;

    Debugs(boolean debug, boolean trace) {
        if (debug) {
            this.debug = true;
            return;
        }
        if (trace) {
            this.debug = true;
        }
    }

    public boolean isDebug() {
        return debug;
    }

}
