/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.utils;

import agh.aq21gui.model.gld.processing.CellValue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author marcin
 */
public class Util {
	public static void isNull(Object obj, String name){
		if(obj==null){
			System.out.println(name+" is null!");
		}
	}
    
    public static <T> List<T> singleElemList(T item){
		List<T> list = new LinkedList<T>();
        list.add(item);
        return list;
    }

	public static List<String> strings(String... elem) {
		List<String> elems1 = new LinkedList<String>();
		elems1.addAll(Arrays.asList(elem));
		return elems1;
	}

	public static void collectionIntoArray(Iterable<CellValue> mesh_cells, List<CellValue> array) {
		Iterator<CellValue> iter = mesh_cells.iterator();
		while (iter.hasNext()) {
			CellValue val = iter.next();
			array.add(val);
		}
	}

	public static String streamToString(InputStream in) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		StringBuilder out = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			out.append(line).append("\n");
		}
		return out.toString();
	}

	public static void stringToStream(String in, OutputStream out) throws IOException {
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
		for (String line : in.split("\n")) {
			bw.write(line);
			bw.newLine();
		}
		bw.close();
	}
}
