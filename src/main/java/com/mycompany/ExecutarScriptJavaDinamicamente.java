/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany;

/**
 *
 * @author cesar
 */
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Locale;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class ExecutarScriptJavaDinamicamente {

    private static String diretorioDosScriptsJava = System.getProperty("user.dir") + File.separator + "script";

    public static void main(String[] args) {

        //no paramater
        Class noparams[] = {};

        //String parameter
        Class[] paramString = new Class[1];
        paramString[0] = String.class;

        //int parameter
        Class[] paramInt = new Class[1];
        paramInt[0] = Integer.TYPE;

        //int parameter
        Class[] paramConnection = new Class[1];
        paramConnection[0] = Connection.class;

        String nomeDoScriptJavaExterno = "ScriptJavaTeste";

        try {

            compilarScriptJava(Arrays.asList(obterScriptJava(nomeDoScriptJavaExterno)));

            Class cls = obterClasseJava(nomeDoScriptJavaExterno);

            Object obj = cls.newInstance();

            Connection con = new ConexaoMySQL().get();

            //call the printIt method
            Method method = cls.getDeclaredMethod("printIt", paramConnection);
            method.invoke(obj, con);
            if (con != null) {
                con.close();
            }

            //call the printItString method, pass a String param 
            method = cls.getDeclaredMethod("printItString", paramString);
            method.invoke(obj, new String("mkyong"));

            //call the printItInt method, pass a int param
            method = cls.getDeclaredMethod("printItInt", paramInt);
            method.invoke(obj, 123);

            //call the setCounter method, pass a int param
            method = cls.getDeclaredMethod("setCounter", paramInt);
            method.invoke(obj, 999);

            //call the printCounter method
            method = cls.getDeclaredMethod("printCounter", noparams);
            method.invoke(obj, null);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Class obterClasseJava(String className) throws ClassNotFoundException, MalformedURLException {
        File file = new File(diretorioDosScriptsJava);

        // Convert File to a URL
        URL url = file.toURL(); // file:/classes/demo
        URL[] urls = new URL[]{url};

        // Create a new class loader with the directory
        ClassLoader loader = new URLClassLoader(urls);

        // Load in the class; Class.childclass should be located in        
        Class thisClass = loader.loadClass(className);

        return thisClass;
    }

    /**
     * compile your files by JavaCompiler
     */
    public static void compilarScriptJava(Iterable<? extends JavaFileObject> files) {
        //get system compiler:
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        // for compilation diagnostic message processing on compilation WARNING/ERROR
        MyDiagnosticListener c = new MyDiagnosticListener();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(c,
                Locale.ENGLISH,
                null);
        //specify classes output folder
        Iterable options = Arrays.asList("-d", diretorioDosScriptsJava);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager,
                c, options, null,
                files);
        Boolean result = task.call();
        if (result == true) {
            System.out.println("Script Compilado");
        }
    }

    public static class MyDiagnosticListener implements DiagnosticListener<JavaFileObject> {

        public void report(Diagnostic<? extends JavaFileObject> diagnostic) {

            System.out.println("Line Number->" + diagnostic.getLineNumber());
            System.out.println("code->" + diagnostic.getCode());
            System.out.println("Message->"
                    + diagnostic.getMessage(Locale.ENGLISH));
            System.out.println("Source->" + diagnostic.getSource());
            System.out.println(" ");
        }
    }

    private static JavaFileObject obterScriptJava(String className) throws IOException {

        String contents = new String(Files.readAllBytes(new File(diretorioDosScriptsJava, className + ".java").toPath()));

        JavaFileObject so = null;
        try {
            so = new InMemoryJavaFileObject(className, contents.toString());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return so;
    }

    public static class InMemoryJavaFileObject extends SimpleJavaFileObject {

        private String contents = null;

        public InMemoryJavaFileObject(String className, String contents) throws Exception {
            super(URI.create("string:///" + className.replace('.', '/')
                    + JavaFileObject.Kind.SOURCE.extension), JavaFileObject.Kind.SOURCE);
            this.contents = contents;
        }

        public CharSequence getCharContent(boolean ignoreEncodingErrors)
                throws IOException {
            return contents;
        }
    }
}
