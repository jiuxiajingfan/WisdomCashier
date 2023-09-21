package com.li.WisdomCashier.strategy.email;

import java.util.HashMap;


public class EmailStrategyFactory {
    public static final HashMap<Integer,AbstractEmailStrategy> STRATEGY_MAP = new HashMap<>();

    public static void register(Integer type,AbstractEmailStrategy strategy) {
        STRATEGY_MAP.put(type,strategy);
    }

    public static AbstractEmailStrategy getEmailStrategy(Integer type){
       return STRATEGY_MAP.get(type);
    }

}
