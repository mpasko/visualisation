/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.adidata.scripts;

/**
 *
 * @author marcin
 */
public class AllRunner {
    public static void main(String[] args) {
        runAll(false);
    }

    public static void runAll(boolean humanize) {
        //HRCOnly.hrcOnly(humanize);
        PropertyRecognAll.fromAll(humanize);
        PropertyRecognUp.fromReceipe(humanize);
    }
}
