package io.ib67.util;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import lombok.SneakyThrows;

import javax.script.ScriptEngine;

public class Eval {
    static final ScriptEngine scriptEngine = new NashornScriptEngineFactory().getScriptEngine(cl -> false);
    @SneakyThrows
    public static double eval(String expr){
        double result = ((Number)scriptEngine.eval("function rand(minNum,maxNum){ \n" +
                "    switch(arguments.length){ \n" +
                "        case 1: \n" +
                "            return parseInt(Math.random()*minNum+1,10); \n" +
                "        break; \n" +
                "        case 2: \n" +
                "            return parseInt(Math.random()*(maxNum-minNum+1)+minNum,10); \n" +
                "        break; \n" +
                "            default: \n" +
                "                return 0; \n" +
                "            break; \n" +
                "    } \n" +
                "} " +
                ""+expr)).doubleValue();
        return result;
    }
}
