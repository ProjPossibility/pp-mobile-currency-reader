package harriscornerdetector;


import java.awt.*;
import java.awt.image.*;
import java.applet.*;
import java.net.*;
import java.io.*;
//import java.lang.Math;
import java.util.*;
 
 // additional stuff for swing
import java.awt.event.*;
import javax.swing.*;
//import javax.swing.JApplet;
import javax.imageio.*;

public class gaussianFilter {

		double[] input;
		double[] output;
		float[] template;
		int progress;
		double sigma;
		int templateSize;
		int width;
		int height;

		public void gaussianFilter() {
			progress=0;
		}
		
		public void init(double[] original, int sigmaIn, int tempSize, int widthIn, int heightIn) {
			if((tempSize%2)==0) templateSize=tempSize-1;
			sigma=(double)sigmaIn;
			templateSize=tempSize;
			width=widthIn;
			height=heightIn;
			input = new double[width*height];
			output = new double[width*height];
			template = new float[templateSize*templateSize];
			input=original;
		}
		public void init(double[] original, double sigmaIn, int tempSize, int widthIn, int heightIn) {
			if((tempSize%2)==0) templateSize=tempSize-1;
			sigma=sigmaIn;
			templateSize=tempSize;
			width=widthIn;
			height=heightIn;
			input = new double[width*height];
			output = new double[width*height];
			template = new float[templateSize*templateSize];
			input=original;
		}
		public void generateTemplate() {
			float center=(templateSize-1)/2;

			float total=0;

			for(int x = 0; x < templateSize; x++) {
				for(int y = 0; y < templateSize; y++) {
					template[x*templateSize+y] = (float)(1/(float)(2*Math.PI*sigma*sigma))*(float)Math.exp((float)(-((x-center)*(x-center)+(y-center)*(y-center))/(2*sigma*sigma)));
					total+=template[x*templateSize+y];
				}
			}
			for(int x = 0; x < templateSize; x++) {
				for(int y = 0; y < templateSize; y++) {
					template[x*templateSize+y] = template[x*templateSize+y]/total;
				}
			}
		}
		public double[] process() {
			double sum;
			progress=0;
			

			for(int x=(templateSize-1)/2; x<width-(templateSize+1)/2;x++) {
				progress++;
				for(int y=(templateSize-1)/2; y<height-(templateSize+1)/2;y++) {
					sum=0;
					for(int x1=0;x1<templateSize;x1++) {
						for(int y1=0;y1<templateSize;y1++) {
							int x2 = (x-(templateSize-1)/2+x1);
							int y2 = (y-(templateSize-1)/2+y1);
							double value = (input[y2*width+x2]) * (template[y1*templateSize+x1]);
							sum += value;
						}
					}
					//System.out.println("setting val " + sum);
					output[(y-(templateSize-1)/2)*(width)+(x-(templateSize-1)/2)] = sum;
				}
			}
			progress=width;

			return output;
		}

		public int getProgress() {
			return progress;
		}


	}
