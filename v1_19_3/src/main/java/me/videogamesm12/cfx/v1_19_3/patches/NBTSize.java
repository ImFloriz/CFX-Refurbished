/*
 * Copyright (c) 2023 Video
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.videogamesm12.cfx.v1_19_3.patches;

import me.videogamesm12.cfx.CFX;
import me.videogamesm12.cfx.management.PatchMeta;
import net.minecraft.nbt.NbtTagSizeTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * <h1>NBTSize</h1>
 * <p>Patches an exploit caused by a cap in NBT size.</p>
 * <p>This patch is for 1.19.4 - 1.20.1.</p>
 * <p>This patch does not take effect if you are running DeviousMod.</p>
 */
@Mixin(NbtTagSizeTracker.class)
@PatchMeta(minVersion = 761, maxVersion = 763, conflictingMods = "deviousmod") // 1.19.3 to 1.20.1
public class NBTSize
{
    @Inject(method = "add",
            at = @At(value = "INVOKE",
                    target = "Ljava/lang/RuntimeException;<init>(Ljava/lang/String;)V",
                    shift = At.Shift.BEFORE),
            cancellable = true)
    public void disableNbtSizeRestrictions(long bits, CallbackInfo ci)
    {
        if (!CFX.getConfig().getNbtPatches().isSizeLimitEnabled())
        {
            ci.cancel();
        }
    }
}
