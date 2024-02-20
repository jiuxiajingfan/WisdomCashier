package com.li.wisdomcashier.strategy.email;

import java.util.HashMap;
import java.util.Map;


public class EmailStrategyFactory {

    private EmailStrategyFactory(){}

    protected static final Map<Integer, AbstractEmailStrategy> STRATEGY_MAP = new HashMap<>();

    public static void register(Integer type, AbstractEmailStrategy strategy) {
        STRATEGY_MAP.put(type, strategy);
    }

    public static AbstractEmailStrategy getEmailStrategy(Integer type) {
        return STRATEGY_MAP.get(type);
    }

}
