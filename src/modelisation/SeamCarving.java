package modelisation;

import java.util.ArrayList;
import java.io.*;
import java.util.*;

public class SeamCarving {

	public static int[][] readpgm(String fn) {
		try {
			InputStream f = ClassLoader.getSystemClassLoader()
					.getResourceAsStream(fn + ".pgm");
			if (f == null) {
				System.out.println("PAS DE FICHIER TROUVEE !!!!!!!");
			}
			InputStreamReader isr = new InputStreamReader(f);
			BufferedReader d = new BufferedReader(isr);
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
		sb.append(image.length + " " + image[0].length);
		/* separateur */
		sb.append("\n");
		/* blanc */
		sb.append("255");
		/* separateur */
		sb.append("\n");
		int i;
		int j;

		for (i = 0; i < image.length; i++) {
			for (j = 0; j < image[i].length; j++) {
				sb.append(image[i][j] + " ");
			}
			sb.append("\n");
		}

		try {
			/* création d'un fichier */
			java.io.File file = new File(filename + ".pgm");
			file.createNewFile();
			try {
				/* on ouvre le fichier à l'écriture */
				java.io.FileOutputStream fileFlux = new java.io.FileOutputStream(
						file);
				java.io.FileWriter fw = new FileWriter(file);
				fw.write(sb.toString());
				try {
					/* fermeture de l'écriture */
					fileFlux.close();
					fw.close();
					System.out.println("success");
				} catch (IOException t) {
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

	public static void printImg(int[][] image) {
		int i;
		int j;
		for (i = 0; i < image.length; i++) {
			for (j = 0; j < image[i].length; j++) {
				if (image[i][j] < 10) {
					System.out.print(" ");
				}
				System.out.print(image[i][j] + " ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		System.out.println("bonjour1");
		new SeamCarving();
		System.out.println("bonjour2");
		// int[][] image =
		// readpgm("C:/Users/erwan/workspace/modelisation/ex1.pgm");
		System.out.println("bonjour3");
		int[][] img = { { 10, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 },
				{ 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7 },
				{ 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7 },
				{ 10, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 7 },
				{ 10, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 },
				{ 10, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7 },
				{ 10, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 },
				{ 10, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 7 },
				{ 10, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7 },
				{ 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7 },
				{ 10, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6 },
				{ 10, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 7 },
				{ 10, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 },
				{ 10, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 },
				{ 10, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 7 },
				{ 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7 },
				{ 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7 },
				{ 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7 },
				{ 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10 } };
		writepgm(img, "nouveau");
		printImg(img);
		System.out.println(" ------------- interet ------------- ");
		int[][] img1p5 = interest(img);
		printImg(img1p5);
		int[][] img2 = readpgm("test.pgm");
		printImg(img2);
		
		System.out.println(" ------------- interet ------------- ");
		//
		// for (i = 0; i < interet.length; i++) {
		// for (j = 0; j < interet[i].length; j++) {
		// System.out.print(interet[i][j]+" ");
		// }
		// System.out.print("\n");
		// }
	}
}
