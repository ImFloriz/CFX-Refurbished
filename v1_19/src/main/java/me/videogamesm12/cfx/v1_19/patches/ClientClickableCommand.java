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

package me.videogamesm12.cfx.v1_19.patches;

import me.videogamesm12.cfx.CFX;
import me.videogamesm12.cfx.management.PatchMeta;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * <h1>ClientClickableCommand</h1>
 * <p>Patches an exploit caused by Mojang not adding a prompt before executing a command.</p>
 * <p>This patch is for version 1.19.</p>
 */
@Mixin(Screen.class)
@PatchMeta(minVersion = 759, maxVersion = 759)
public class ClientClickableCommand
{
    @Inject(method = "handleTextClick",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;sendCommand(Ljava/lang/String;)V",
                    shift = At.Shift.BEFORE),
            cancellable = true)
    public void handleClickableCommand(Style style, CallbackInfoReturnable<Boolean> cir)
    {
        switch (CFX.getConfig().getTextPatches().getClickEvent().getCommandClickClientMode())
        {
            case NOTIFY ->
            {
                if (style == null || style.getClickEvent() == null)
                {
                    return;
                }

                final ClickEvent clickEvent = style.getClickEvent();

                cir.setReturnValue(true);

                MinecraftClient.getInstance().setScreen(new ConfirmScreen(
                        (bool) ->
                        {
                            if (bool)
                            {
                                MinecraftClient.getInstance().player.sendCommand(clickEvent.getValue().substring(1));
                            }

                            MinecraftClient.getInstance().setScreen(null);
                        },
                        Text.translatable("cfx.prompt.run_command"),
                        Text.literal(clickEvent.getValue())
                ));

            }
            case DO_NOTHING -> cir.setReturnValue(true);
            default ->
            {
                // Do nothing
            }
        }
    }

    @Inject(method = "handleTextClick",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;sendChatMessage(Ljava/lang/String;)V",
                    shift = At.Shift.BEFORE),
            cancellable = true)
    public void handleClickableChat(Style style, CallbackInfoReturnable<Boolean> cir)
    {
        switch (CFX.getConfig().getTextPatches().getClickEvent().getCommandClickClientMode())
        {
            case NOTIFY ->
            {
                if (style == null || style.getClickEvent() == null)
                {
                    return;
                }

                final ClickEvent clickEvent = style.getClickEvent();

                cir.setReturnValue(true);

                MinecraftClient.getInstance().setScreen(new ConfirmScreen(
                        (bool) ->
                        {
                            if (bool)
                            {
                                MinecraftClient.getInstance().player.sendChatMessage(clickEvent.getValue());
                            }

                            MinecraftClient.getInstance().setScreen(null);
                        },
                        Text.translatable("cfx.prompt.run_command"),
                        Text.literal(clickEvent.getValue())
                ));

            }
            case DO_NOTHING -> cir.setReturnValue(true);
            default ->
            {
                // Do nothing
            }
        }
    }
}
