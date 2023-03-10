/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.items;

import net.minecraftforge.common.capabilities.*;

@Deprecated(forRemoval = true, since = "1.19.2")
public class CapabilityItemHandler
{
    /**
     * @deprecated Create your own reference using {@link CapabilityManager#get(CapabilityToken)}, or use {@link ForgeCapabilities#ITEM_HANDLER}.
     */
    @Deprecated(forRemoval = true, since = "1.19.2")
    public static final Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = ForgeCapabilities.ITEM_HANDLER;

    public static void register(RegisterCapabilitiesEvent event){}

}
