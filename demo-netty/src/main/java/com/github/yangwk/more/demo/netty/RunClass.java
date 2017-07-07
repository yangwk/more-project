package com.github.yangwk.more.demo.netty;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

//org.apache.hadoop.util.RunJar
public class RunClass {

	public void run(Class<?> mainClass, String[] args) throws Throwable {
		Method main = mainClass.getMethod("main", new Class[] { Array.newInstance(String.class, 0).getClass() });
		try {
			main.invoke(null, new Object[] { args });
		} catch (InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	public void run(String mainClassName, String[] args) throws Throwable {
		Class<?> mainClass = Class.forName(mainClassName);
		run(mainClass,args);
	}

	public void fromMain(String[] args) throws Throwable {
		final String usage = "mainClassName args...";

	    if (args.length < 1) {
	      System.err.println(usage);
	      System.exit(-1);
	    }
	    
		int firstArg = 0;
		String mainClassName = args[firstArg++];
		String[] newArgs = Arrays.asList(args).subList(firstArg, args.length).toArray(new String[0]);
		
		run(mainClassName, newArgs);
	}
	
	public static void runWithCatch(Class<?> mainClass, String[] args){
		try {
			RunClass rc = new RunClass();
			rc.run(mainClass, args);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
