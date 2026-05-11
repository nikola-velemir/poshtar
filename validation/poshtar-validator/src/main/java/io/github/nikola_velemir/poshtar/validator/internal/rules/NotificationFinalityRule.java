package io.github.nikola_velemir.poshtar.validator.internal.rules;

import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;

import java.util.Set;

public class NotificationFinalityRule extends FinalityRule{
    @Override
    protected String getViolationMessage() {
        String FINALITY_VIOLATED_MESSAGE = "PoshtaR: Finality Violated! Notification '%s' must be final or a record!";
        return FINALITY_VIOLATED_MESSAGE;
    }

    @Override
    protected Set<String> getFQNs(ProcessorContext ctx) {
        System.out.println(ctx.getKnownNotifications());
        return ctx.getKnownNotifications();
    }
}