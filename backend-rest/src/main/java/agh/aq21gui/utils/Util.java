/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.utils;

import agh.aq21gui.model.gld.processing.CellValue;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.services.aq21.OutputParser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

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

    public static String negatedComparator(final String comparator) {
        String neg;
        if (comparator.equals("<")) {
            neg = ">=";
        } else if (comparator.equals(">")) {
            neg = "<=";
        } else if (comparator.equals(">=")) {
            neg = "<";
        } else if (comparator.equals("<=")) {
            neg = ">";
        } else if (comparator.equals("!=") || comparator.equals("<>")) {
            neg = "=";
        } else {
            neg = "!=";
        }
        return neg;
    }

    public static Output deepCopyOutput(Output in) {
        OutputParser parser = new OutputParser();
        return parser.parse(in.toString());
    }

    public static Input deepCopyInput(Input in) {
        OutputParser parser = new OutputParser();
        //System.out.print(attachLines(in.toString()));
        return parser.parse(in.toString());
    }
    
    public static String objectToJson(Object item) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
            return mapper.writeValueAsString(item);
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
            //Logger.getLogger(StatsAgregator.class.getName()).log(Level.SEVERE, null, ex);
        }
        //return "";
    }
    
    public static String attachLines(String in) {
        StringBuilder b = new StringBuilder();
        int i=0;
        for (String line : in.split("\n")) {
            b.append(++i);
            b.append("\t");
            b.append(line);
            b.append("\n");
        }
        return b.toString();
    }

    public static String loadFile(String filename) {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(filename);
            return Util.streamToString(stream);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
