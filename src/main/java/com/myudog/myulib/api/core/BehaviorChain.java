package com.myudog.myulib.api.core;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

public class BehaviorChain {

    private static final Logger LOGGER = Logger.getLogger(BehaviorChain.class.getName());
    private final List<ReversibleAction> actions = new ArrayList<>();

    public BehaviorChain add(ReversibleAction action) {
        this.actions.add(action);
        return this;
    }

    public BehaviorChain add(ThrowingRunnable execute, Runnable rollback) {
        this.actions.add(ReversibleAction.of(execute, rollback));
        return this;
    }

    public boolean execute() {
        int executed = 0;
        try {
            for (ReversibleAction action : actions) {
                action.execute();
                executed++;
            }
            return true;
        }
        catch (Exception e) {
            LOGGER.warning("BehaviorRegistry execution failed at action " + executed + ": " + e.toString());
            rollback(executed);
            return false;
        }

    }

    public void rollback() {
        rollback(actions.size());
    }


    private void rollback(int executed) {
        ListIterator<ReversibleAction> iterator = actions.listIterator(executed);
        while (iterator.hasPrevious()) {
            ReversibleAction action = iterator.previous();
            try {
                action.rollback();
            }
            catch (Exception e) {
                LOGGER.warning("BehaviorRegistry rollback failed at action " + executed + ": " + e.toString());
            }
        }
        LOGGER.warning("BehaviorRegistry execution completed at action " + executed);
    }


}
