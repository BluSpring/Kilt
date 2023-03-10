/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.common.capabilities;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import xyz.bluspring.kilt.workarounds.CapabilityProviderWorkaround;

@ApiStatus.Internal // Modders should use ICapabilityProvider, this is for Forge
public interface ICapabilityProviderImpl<B extends ICapabilityProvider> extends ICapabilityProvider
{
    boolean areCapsCompatible(CapabilityProvider<B> other);
    boolean areCapsCompatible(@Nullable CapabilityDispatcher other);
    void invalidateCaps();
    void reviveCaps();

    default CapabilityProviderWorkaround<B> getWorkaround() {
        throw new IllegalStateException();
    }
}
