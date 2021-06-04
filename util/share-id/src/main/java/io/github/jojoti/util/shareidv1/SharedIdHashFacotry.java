package io.github.jojoti.util.shareidv1;

import com.google.inject.assistedinject.Assisted;

public interface SharedIdHashFacotry {

    SharedId<Long, Long> create(@Assisted("hashIdSlat") String hashIdSlat);

}
