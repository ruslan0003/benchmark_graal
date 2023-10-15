package com.mycompany.app;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Source;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class App 
{
    static int ITERATIONS = 40;

    public static final String SCRIPT = "var arr = [];\n" +
            "var result;\n" +
            "for (var i = 0; i < 1000000; i++) {\n" +
            "result = Math.sqrt(i); \n" +
            "arr.push(result);\n" +
            "}\n";

    public static void BenchmarkGraalScriptEngine(ScriptEngineManager manager) throws ScriptException {
        try {
            // Запускаем Graal.js через ScriptEngine
            ScriptEngine graalEngine = manager.getEngineByName("graal.js");
            long graalStartTime;
            long graalEndTime;
            long graalDuration;
            double graalSeconds;

            for (int i = 1; i < ITERATIONS; i++) {
                graalStartTime = System.nanoTime();
                graalEngine.eval(SCRIPT);
                graalEndTime = System.nanoTime();
                graalDuration = graalEndTime - graalStartTime;
                graalSeconds = (double) graalDuration / 1000000000;
                System.out.println("Итерация №" + i + ". Время выполнения Graal.js через ScriptEngine: " + String.format("%.9f", graalSeconds) + " секунд");
            }
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }

    public static void BenchmarkGraalContext() {
        try {
            // Запускаем Graal.js через Context
            long graalCtxStartTime;
            long graalCtxEndTime;
            long graalCtxDuration;
            double graalCtxSeconds;
            Source source = Source.create("js", SCRIPT);
            Engine engine = Engine.create();
            Context ctx = Context.newBuilder().engine(engine).build();
            for (int i = 1; i < ITERATIONS; i++) {
                graalCtxStartTime = System.nanoTime();
                ctx.eval(source);
                graalCtxEndTime = System.nanoTime();
                graalCtxDuration = graalCtxEndTime - graalCtxStartTime;
                graalCtxSeconds = (double) graalCtxDuration / 1000000000;
                System.out.println("Итерация №" + i + ". Время выполнения Graal.js через Context: " + String.format("%.9f", graalCtxSeconds) + " секунд");
            }
            ctx.close();
            engine.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void BenchmarkNashorn(ScriptEngineManager manager) throws ScriptException {
        try {
            // Запускаем Nashorn
            ScriptEngine nashornEngine = manager.getEngineByName("nashorn");
            long nashornStartTime;
            long nashornEndTime;
            long nashornDuration;
            double nashornSeconds;

            for (int i = 1; i < ITERATIONS; i++) {
                nashornStartTime = System.nanoTime();
                nashornEngine.eval(SCRIPT);
                nashornEndTime = System.nanoTime();
                nashornDuration = nashornEndTime - nashornStartTime;
                nashornSeconds = (double) nashornDuration / 1000000000;
                System.out.println("Итерация №" + i + ". Время выполнения Nashorn: " + String.format("%.9f", nashornSeconds) + " секунд");
            }
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }

    public static void main( String[] args ) throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        BenchmarkGraalScriptEngine(manager);
        BenchmarkGraalContext();
        BenchmarkNashorn(manager);
    }
}
