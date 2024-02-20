package com.li.wisdomcashier.strategy.pay;

import java.util.HashMap;
import java.util.Map;

public class PayStrategyFactory {

    private PayStrategyFactory(){}

    protected static final Map<Integer, AbstractPayStrategy> STRATEGY_MAP = new HashMap<>();

    public static void register(Integer type, AbstractPayStrategy strategy) {
        STRATEGY_MAP.put(type, strategy);
    }

    public static AbstractPayStrategy getEmailStrategy(Integer type) {
        return STRATEGY_MAP.get(type);
    }
}
