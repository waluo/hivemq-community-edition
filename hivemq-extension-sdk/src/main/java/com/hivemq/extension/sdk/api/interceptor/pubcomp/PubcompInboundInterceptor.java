/*
 * Copyright 2019 dc-square GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hivemq.extension.sdk.api.interceptor.pubcomp;

import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.async.TimeoutFallback;
import com.hivemq.extension.sdk.api.interceptor.Interceptor;
import com.hivemq.extension.sdk.api.interceptor.pubcomp.parameter.PubcompInboundInput;
import com.hivemq.extension.sdk.api.interceptor.pubcomp.parameter.PubcompInboundOutput;
import com.hivemq.extension.sdk.api.packets.pubcomp.ModifiablePubcompPacket;

import java.time.Duration;

/**
 * Interface for the inbound PUBCOMP interception.
 * <p>
 * Interceptors are always called by the same Thread for all messages from the same client.
 * <p>
 * If the same instance is shared between multiple clients it can be called in different Threads and must therefore be
 * thread-safe.
 * <p>
 * When the method {@link #onInboundPubcomp(PubcompInboundInput, PubcompInboundOutput)} throws an exception or a call
 * to {@link PubcompInboundOutput#async(Duration)} times out with {@link TimeoutFallback#FAILURE}, HiveMQ will ignore
 * this interceptor and will:
 * <ol>
 *    <li>Log the exception</li>
 *    <li>Revert the changes to the {@link ModifiablePubcompPacket} made by the interceptor</li>
 *    <li>Call the next {@link PubcompInboundInterceptor} or send the PUBCOMP to the server if no interceptor is left</li>
 * </ol>
 *
 * @author Yannick Weber
 */
@FunctionalInterface
public interface PubcompInboundInterceptor extends Interceptor {

    /**
     * When a {@link PubcompInboundInterceptor} is set through any extension, this method gets called for every inbound
     * PUBCOMP packet from any MQTT client.
     *
     * @param pubcompInboundInput  The {@link PubcompInboundInput} parameter.
     * @param pubcompInboundOutput The {@link PubcompInboundOutput} parameter.
     */
    void onInboundPubcomp(
            @NotNull PubcompInboundInput pubcompInboundInput,
            @NotNull PubcompInboundOutput pubcompInboundOutput);
}
