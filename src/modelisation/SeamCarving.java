package modelisation;

import java.io.*;
import java.util.*;

public class SeamCarving {

	public static int[][] readpgm(String fn) {
		try {
			InputStream f = new FileInputStream(fn + ".pgm");
			InputStreamReader isr = new InputStreamReader(f);
			BufferedReader d = new BufferedReader(isr);
			d.readLine();
			String line = d.readLine();
			while (line.startsWith("#")) {
				line = d.readLine();
			}
			Scanner s = new Scanner(line);
			int height = s.nextInt();
			int width = s.nextInt();
			line = d.readLine();
			s = new Scanner(line);
			s.nextInt();
			int[][] im = new int[width][height];
			s = new Scanner(d);
			int count = 0;
			while (count < width * height) {
				im[count / height][count % height] = s.nextInt();
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
			/* crÃ©ation d'un fichier */
			java.io.File file = new File(filename + ".pgm");
			file.createNewFile();
			try {
				/* on ouvre le fichier Ã  l'Ã©criture */
				java.io.FileOutputStream fileFlux = new java.io.FileOutputStream(
						file);
				java.io.FileWriter fw = new FileWriter(file);
				fw.write(sb.toString());
				try {
					/* fermeture de l'Ã©criture */
					fileFlux.close();
					fw.close();
					System.out.println("success write");
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
		int[][] tmp = new int[image.length][image[0].length];
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
				if (image[i][j] < 100) {
					System.out.print(" ");
				}
				System.out.print(image[i][j] + " ");
			}
			System.out.println();
		}
	}

	public static Graph tograph(int[][] itr) {
		int width = itr[0].length;
		int height = itr.length;
		int node = width * height + 2;
		int i, j, k;
		int[] kVal;	// C'est le tableau des k, pour remplir l'utilisation des arrêtes
		int infinite = Integer.MAX_VALUE;
		kVal = new int[height];
		
		Graph g = new Graph(node);

		/* liaison entre (i,j) et (i,j+1) */
		for (i = 0; i < height; i++) {
			k = Integer.MAX_VALUE;
			for (j=0; j<width; j++) {
				k = Math.min(itr[i][j], k);
			}
			System.out.println("min " + i + " " + k);
			k = 0; // TODO A SUPPRIMER !!!!!
			kVal[i]=k;
			for (j = 0; j < width - 1; j++) {
				g.addEdge(new Edge((j * height) + i, ((j + 1) * height) + i, itr[i][j],
						k));
			}
		}

		/* liaison noeuds à t */
		for (i = 0; i < height; i++) {
			g.addEdge(new Edge((width - 1) * height + i, node - 2, itr[i][width - 1], kVal[i]));
		}

		/* liaison s à noeuds */
		for (i = 0; i < height; i++) {
			g.addEdge(new Edge(node - 1, i, infinite, kVal[i]));
		}

		/*
		 * 
		 */
		for (i = 0; i < height; i++) {
			for (j = 1; j < width; j++) {
//				g.addEdge(new Edge((j * height) + i, ((j - 1) * height) + i,
//						infinite, 0));
				if (i != 0) {
					g.addEdge(new Edge((j * height) + i, ((j - 1) * height) + i
							- 1, infinite, 0));
				}
				if (i != height - 1) {
					g.addEdge(new Edge((j * height) + i, ((j - 1) * height) + i
							+ 1, infinite, 0));
				}
			}
		}

		return g;
	}

	// fonction de test de Raph
	public static void fullGraph(Graph g) {
		int s = g.vertices()-1;
		int t = s-1;
		int p;
		int numberBack;
		boolean saturation;
		HashMap<Integer, Integer> vUsed;	// K : Vertices; V : used
		ArrayList<Integer> toDo, full;
		vUsed = new HashMap<Integer, Integer>();
		toDo = new ArrayList<Integer>();
		full = new ArrayList<Integer>();
		Iterable<Edge> edges = g.adj(s);
		for (Edge ed : edges) {
			p = ed.other(s);
			toDo.add(p);
			vUsed.put(p, 255);
			ed.used = 255;
		}
		full.add(s);
		while (toDo.size() > 0) {
			saturation = true;
			numberBack = 0;
			int vFrom = toDo.get(0);
			if (vFrom == t) { // Point d'arrivée
				toDo.remove(0);
				vUsed.remove(vFrom);
			} else {
				edges = g.adj(vFrom);
				int flow = vUsed.get(vFrom);
				if (flow > 0) {
					for (Edge ed : edges) {
						int vTo = ed.other(vFrom);
						if ((!full.contains(vTo)) && ed.from == vFrom) {
							if (ed.capacity < 256) {
								saturation = false;
								// Arrête qui vas vers la droite
								int flowNotUsed = ed.capacity - ed.used;
								int flowInUse;
								if (flow >= flowNotUsed) {
									saturation = true;
								}
								if (flowNotUsed > 0) {
									// l'arrête n'est pas saturée au début de l'analyse
									if (saturation) {
										flowInUse = flowNotUsed;
										//ed.used += flowNotUsed;
										//flow -= flowNotUsed;
									} else {
										flowInUse = flow;
										//ed.used += flow;
										//flow = 0;
									}
									ed.used += flowInUse;
									flow -= flowInUse;
									Integer isSet = vUsed.putIfAbsent(vTo, flowInUse);
									if (isSet != null) {
										vUsed.put(vTo, isSet + flowInUse);
									} else {
										toDo.add(vTo);	// TODO à vérifier
									}
								}
							} else {
								numberBack++;
								// TODO à vérifier 
								// arrête qui ne vas pas vers l'avant
								// Je crois qu'il ne faut rien faire de plus
							}
						}
					}
					// Si'l y a saturation
					if (saturation) {
						for (Edge ed : edges) {
							int vTo = ed.other(vFrom);
							if (!full.contains(vTo)) {
								if (ed.capacity > 256 && numberBack > 0) {
									// L'arrête vas vers l'arriere
									int flowMod = flow/numberBack;
									ed.used+=(flowMod);
									Integer isSet = vUsed.putIfAbsent(vTo, flowMod);
									if (isSet != null) {
										vUsed.put(vTo, isSet + flowMod);
									} else {
										toDo.add(vTo);	// TODO à vérifier bis
									}
									flow -= flowMod;
									numberBack--;
								}
							}
						}
					}
				}
				// On se retire de la liste de sommet en cours
				if (saturation) {
					freeLine(g, vFrom, flow);
					full.add(vFrom);
				}
				toDo.remove(0);
				vUsed.remove(vFrom);
			}
		}
	}
	
	// autre fonction de test de Raph
	public static void freeLine(Graph g, int v, int numberToRemove) {
		int vTo = v;
		int s = g.vertices()-1;
		while (vTo != s) {
			for(Edge ed : g.adj(vTo)) {
				if ((ed.to == vTo && ed.capacity < 255) || ed.from == s) {
					ed.used -= numberToRemove;
					vTo = ed.from;
					break;
				}
			}
		}
	}
	

	public static void maxFlow(Graph g) {
		
	}
	
	public static void main(String[] args) {
		int[][] inter = { 
				{ 5, 2, 3 },
				{ 7, 8, 1 },
				{ 9, 5, 2 },
				{ 10, 15, 20 }};
		//		System.out.println(" ------------- ecriture img 1 ------------- ");
//		writepgm(img, "nouveau");
//		printImg(img);
//		System.out.println(" ------------- interet img 1------------- ");
//		int[][] img1p5 = interest(img);
//		printImg(img1p5);
//		System.out.println(" ------------- lecture img 2 ------------- ");
//		int[][] img2 = readpgm("nouveau");
//		printImg(img2);
//		System.out.println(" ------------- lecture img 3 ------------- ");
//		int[][] img3 = readpgm("test");
//		System.out.println(" ------------- print img ------------- ");
//		printImg(img);
//		int[][] inter = interest(img);
		System.out.println(" ------------- print interest ------------- ");
		printImg(inter);
		Graph g = tograph(inter);
		g.writeFile("test_img.dot");
		System.out.println(" ------------- start full Graph ------------- ");
		fullGraph(g);
		System.out.println(" ------------- end full Graph ------------- ");
		g.writeFile("test_img_full.dot");
		System.out.println(" ------------- fin ------------- ");
	}
}
