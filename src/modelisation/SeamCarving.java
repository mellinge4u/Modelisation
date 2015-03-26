package modelisation;

import java.util.ArrayList;
import java.io.*;
import java.util.*;

public class SeamCarving {

	public static int[][] readpgm(String fn) {
		try {
			InputStream f = ClassLoader.getSystemClassLoader()
					.getResourceAsStream(fn);
			BufferedReader d = new BufferedReader(new InputStreamReader(f));
			String magic = d.readLine();
			String line = d.readLine();
			while (line.startsWith("#")) {
				line = d.readLine();
			}
			Scanner s = new Scanner(line);
			int width = s.nextInt();
			int height = s.nextInt();
			line = d.readLine();
			s = new Scanner(line);
			int maxVal = s.nextInt();
			int[][] im = new int[height][width];
			s = new Scanner(d);
			int count = 0;
			while (count < height * width) {
				im[count / width][count % width] = s.nextInt();
				count++;
			}
			return im;
		}

		catch (Throwable t) {
			t.printStackTrace(System.err);
			return null;
		}
	}

	/*
	 * Fonction qui un fichier pgm
	 */
	public static void writepgm(int[][] image, String filename) {
		
		StringBuilder sb = new StringBuilder();
		/* format du fichier */
		sb.append("P2");
		/* separateur */
		sb.append("\n");
		/* ligne + cologne */
		sb.append(image.length+" "+image[0].length);
		/* separateur */
		sb.append("\n");
		/* blanc */
		sb.append("255");
		/* separateur */
		sb.append("\n");
		int i;
		int j;
		
		for(i = 0 ; i < image.length ; i++){
			for(j = 0; j < image[i].length; j++){
				sb.append(image[i][j]+" ");
			}
			sb.append("\n");
		}
		
		try {
			/* cr�ation d'un fichier */
			java.io.File file = new File(filename+".pgm");
			file.createNewFile();
			try {
				/* on ouvre le fichier � l'�criture */
				java.io.FileOutputStream fileFlux = new java.io.FileOutputStream(
						file);
				java.io.FileWriter fw = new FileWriter(file);
				fw.write(sb.toString());
				try {
					/* fermeture de l'�criture */
					fileFlux.close();  
					fw.close();
					System.out.println("success");
				}
				catch (IOException t) {
					t.printStackTrace(System.err);
				}
			} catch (IOException t) {
				t.printStackTrace(System.err);
			}
		} catch (IOException t) {
			t.printStackTrace(System.err);

		}
		
	}

	public static int[][] interest(int[][] image) {
		int i;
		int j;
		int[][] tmp = image;
		for (i = 0; i < image.length; i++) {
			for (j = 0; j < image[i].length; j++) {
				if (j == 0) {
					tmp[i][j] = Math.abs(image[i][j] - image[i][j + 1]);
				} else if (j == image[i].length - 1) {
					tmp[i][j] = Math.abs(image[i][j] - image[i][j - 1]);
				} else {
					tmp[i][j] = Math.abs(image[i][j]
							- ((image[i][j - 1] + image[i][j + 1]) / 2));
				}
			}
		}
		return tmp;

	}

	public static void main(String[] args) {
		System.out.println("bonjour1");
		new SeamCarving();
		System.out.println("bonjour2");
		int[][] image = readpgm("C:/Users/erwan/workspace/modelisation/ex1.pgm");
		System.out.println("bonjour3");
		writepgm(image,"nouveau");
		int[][] interet = interest(image);
		System.out.println("bonjour");
	}
}