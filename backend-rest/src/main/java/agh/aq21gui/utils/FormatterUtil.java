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
	
	public static void appendAll(StringBuilder builder, List<? extends Object> items){
		for(Object item : items){
			builder.append(item.toString());
		}
	}
	
	public static String terminate(StringBuilder builder){
		builder.append("}\n");
		return builder.toString();
	}
}
