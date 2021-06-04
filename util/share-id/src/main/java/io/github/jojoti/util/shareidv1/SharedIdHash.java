package io.github.jojoti.util.shareidv1;

import com.google.common.base.Preconditions;
import com.google.common.io.BaseEncoding;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.hashids.Hashids;

/**
 * @author JoJo Wang
 */
public class SharedIdHash implements SharedId<Long, Long> {

    private final Hashids hashids;

    @Inject
    public SharedIdHash(@Assisted("hashIdSlat") String hashIdSlat) {
        this.hashids = new Hashids(hashIdSlat);
    }

    @Override
    public String encode(Long id) {
        return BaseEncoding.base64Url().encode(this.hashids.encode(id).getBytes());
    }

    @Override
    public Long decode(String encodeId) {
        String sharedId = new String(BaseEncoding.base64Url().decode(encodeId));
        long[] ids = this.hashids.decode(sharedId);
        Preconditions.checkArgument(ids != null && ids.length == 1);
        return ids[0];
    }

}
