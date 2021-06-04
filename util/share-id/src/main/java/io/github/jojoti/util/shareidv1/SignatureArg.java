package io.github.jojoti.util.shareidv1;

/**
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public interface SignatureArg {

    SignatureArgHash argsSign(String str, long id, long ttl);

    SignatureArgHash argsSign(String str, String id, long ttl);

    interface SignatureArgHash {
        String sha256();
    }

}
