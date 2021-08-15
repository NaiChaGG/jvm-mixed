/*
 * Copyright 2021 JoJo Wang , homepage: https://github.com/jojoti/jvm-mixed.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.jojoti.examples.dagger;

import io.github.jojoti.examples.dagger.di.EventComponent;
import io.github.jojoti.examples.dagger.event.UserEventHandler;
import io.github.jojoti.examples.dagger.event.UserEventHandlerImpl;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 * @date 2020/5/19
 */
public class AppContext {

    private final Map<Long, UserEventHandler> context = new HashMap<>();

    @Inject
    EventComponent eventComponent;

    @Inject
    public AppContext() {

    }

    public void add(long uid, UserEventHandler userEventHandler) {
        var user1 = new UserEventHandlerImpl(1);
        eventComponent.inject(user1);
    }

}