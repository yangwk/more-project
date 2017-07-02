package com.github.yangwk.more.demo.javase.file;

import java.io.File;

public class TraverseDirectory {
	
	public void traverse(File root){
		if(root == null){
			return ;
		}
		File[] children = root.listFiles();
		if(children == null){
			return ;
		}
		for(File file : children){
			System.out.println(file.getName());
			traverse(file);
		}
	}

	public static void main(String[] args) {
		File root = new File("F:/yangwk/temp");
		
		TraverseDirectory traDirec = new TraverseDirectory();
		traDirec.traverse(root);

	}

}
