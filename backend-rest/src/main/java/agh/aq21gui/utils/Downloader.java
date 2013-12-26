/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marcin
 */
public class Downloader {
	private String content="";
	
	public void download(String URL){
		try {
			BufferedInputStream bus = new BufferedInputStream(new URL(URL).openStream());
			content = Util.streamToString(bus);
		} catch (IOException ex) {
			Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
		}

	}
	
	public String getContent(){
		return content;
	}
	
	public static void main(String args[]){
		Downloader down = new Downloader();
		down.download("http://archive.ics.uci.edu/ml/machine-learning-databases/car/car.data");
		System.out.println(down.getContent());
	}
}
