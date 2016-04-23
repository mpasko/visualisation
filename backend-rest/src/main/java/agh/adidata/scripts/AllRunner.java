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
        HRCOnly.hrcOnly();
        PropertyRecognAll.fromAll();
        PropertyRecognUp.fromReceipe();
    }
}
