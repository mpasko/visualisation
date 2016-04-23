/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.utils;

import java.util.List;

/**
 *
 * @author marcin
 */
public class FormatterUtil {
	
	public static StringBuilder begin(String label){
		StringBuilder builder = new StringBuilder();
		builder.append(label).append("\n{\n");
		return builder;
	}
	
	public static StringBuilder begin(String label, String name){
		StringBuilder builder = new StringBuilder();
		builder.append(label).append(" ");
		builder.append(name);
		builder.append("\n{\n");
		return builder;
	}
	
	public static void intent(StringBuilder builder, int intent){
		for(int i = 0; i<intent; ++i){
			builder.append("  ");
		}
	}
	
	public static void appendAll(StringBuilder builder, List<? extends Object> items, int pade){
		for(Object item : items){
			intent(builder,pade);
			builder.append(item.toString());
		}
	}
	
	public static String terminate(StringBuilder builder){
		builder.append("}\n");
		return builder.toString();
	}

    public static String alignString(int desiredLength, String textToFill, final String text) {
        String prefix = "";
        for (int i = 0; i < desiredLength - text.length(); ++i) {
            prefix = prefix.concat(textToFill);
        }
        return prefix.concat(text);
    }
    
    public static String alignStringForward(int desiredLength, String textToFill, final String text) {
        String prefix = text;
        for (int i = 0; i < desiredLength - text.length(); ++i) {
            prefix = prefix.concat(textToFill);
        }
        return prefix;
    }

    public static String makeFilenameFromText(String caption) {
        String lc = caption.toLowerCase();
        String spaces = lc.replaceAll(" ", "-").replaceAll("_", "-");
        String diacritic = spaces.replaceAll("\u017C", "z");
        diacritic = diacritic.replaceAll("\u015B", "s");
        diacritic = diacritic.replaceAll("\u0107", "c");
        diacritic = diacritic.replaceAll("\u0142", "l");
        diacritic = diacritic.replaceAll("\u00F3", "o");
        return diacritic;
    }
}
