package com.jmc.blindtest.core;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Utils {

	public static List<String> unfoldPath(String start) {
		List<String> unfolded = new ArrayList<>(); 
		File f = new File(start);

		if(f.isFile()) unfolded.add(start);
		if(f.isDirectory()) {
			Stream.of(f.listFiles())
			.forEach(child -> unfolded.addAll(unfoldPath(child.getAbsolutePath())));
		}

		return unfolded;
	}

	public static String getFileContent(String filename) {
		try {
			filename.replaceAll(Pattern.quote(File.separator), Pattern.quote("\\/"));
			return new String(Files.readAllBytes(Paths.get(filename)), "UTF-8");
		} catch(IOException e) {
			System.err.println("Error while reading " + filename);
			e.printStackTrace();
			return "";
		}
	}

	public static void writeToFile(String filename, String content) {
		try {
			Files.write(Paths.get(filename), Stream.of(content.split(System.lineSeparator())).collect(Collectors.toList()), StandardCharsets.UTF_8);
		} catch(IOException e) {
			System.err.println("Error while writing " + filename);
			System.err.println(e.getMessage());
		}
	}

	public static String getFileExtension(File file) {
		String name = file.getName();
		try {
			return name.substring(name.lastIndexOf(".") + 1);
		} catch (IndexOutOfBoundsException e) {
			return "";
		}
	}
}
