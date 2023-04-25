package com.test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class App {
    static Boolean[] array1 = { true, true, true, false,
            true, true, true, true,
            true, false, true, false,
            true, false, false, true,
            true, true, true, true, true,
            true, true
    };

    static Boolean[] array2 = { true, true, true, false,
            true, true, true, true,
            true, false, true, false,
            true, false, false, true,
            true, true, true, true, true,
            true
    };

    public static void main(String[] args) {

        String result = testPlayerCode();

        System.out.println(result);
    }

    public static String testPlayerCode() {
        try {
            OutputStream os = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(os);
            PrintStream old = System.out;
            System.setOut(ps);

            // Compile the code
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            int compilationResult = compiler.run(null, null, null, "src/main/java/com/test/Counter.java");
            if (compilationResult != 0) {
                // Compilation failed
                System.setOut(old);
                ps.flush();
                String compilationErrorMessage = "Compilation failed with error code1: " + compilationResult;
                System.out.println(compilationErrorMessage);
                return compilationErrorMessage;
            }
            // Load the Counter class
            ClassLoader classLoader = App.class.getClassLoader();
            Class<?> cls = classLoader.loadClass("com.test.Counter");
            Object obj = cls.getDeclaredConstructor().newInstance();
            Method method = cls.getDeclaredMethod("countSheeps", Boolean[].class);

            // Invoke the method
            method.invoke(obj, (Object) array1);
            String result = Integer.toString((int) method.invoke(obj, (Object) array1));
            String result2 = Integer.toString((int) method.invoke(obj, (Object) array2));

            boolean test1 = result.contains("18");
            boolean test2 = result2.contains("17");

            System.setOut(old);
            ps.flush(); // Flush the PrintStream to ensure all captured output is written to the
                        // OutputStream
            System.out.println(os.toString());
            return test1 && test2 ? "Test passed" : "Test failed";
        } catch (Exception e) {
            e.printStackTrace();
            String error = e.getMessage();
            return error;
        }
    }
}
