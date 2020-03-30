package com.optum.challenge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class GetRandomFiles {

	public static GetRandomFiles fileGen = new GetRandomFiles();
    private Set<String> files;

	private GetRandomFiles() {
		// private constructor
	}

	public Set<String> getFiles(int noOfFiles, int low, int high) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = low; i < high; i++) {
			list.add(new Integer(i));
		}
		Collections.shuffle(list);
		files = list.stream().map(s -> String.valueOf("file" + s)).collect(Collectors.toCollection(HashSet::new));
		return files;
	}
}
