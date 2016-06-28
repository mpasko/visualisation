/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.adidata.scripts;

import agh.aq21gui.utils.Util;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author marcin
 */
public class ScriptsConfiguration {
    
    public static String read(String parameter) {
        String file = Util.loadFile("resources/output_paths.txt");
        List<String> lines = Arrays.asList(file.split("\n|\r"));
        for (String line : lines) {
            if (line.contains(parameter)) {
                return line.split("=")[1];
            }
        }
        //throw new RuntimeException(String.format("Parameter %s not found!", parameter));
        return "";
    }
    
    public static String getParam(String parameter) {
        Map<String,String> defaults = new HashMap<String, String>();
        defaults.put("results_path", "./results/");
        defaults.put("latex_tables", "./results/");
        String fromFile = read(parameter);
        if (!fromFile.isEmpty()) {
            return fromFile;
        } else {
            return defaults.get(parameter);
        }
    }
}
