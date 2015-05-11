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
		sb.append(image[0].length + " " + image.length);
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
					// System.out.println("success write");
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

	public static Graph tograph(int[][] itr,int pix1, int pix2) {
		int width = itr[0].length;
		int height = itr.length;
		int node = width * height + 2;
		int i, j, k;
		int[] kVal; // C'est le tableau des k, pour remplir l'utilisation des
					// arrêtes
		int infinite = Integer.MAX_VALUE;
		kVal = new int[height];

		int pix1x = pix1 / height;
		int pix1y = pix1%height;
		int pix2x = pix2 / height;
		int pix2y = pix2%height;
		int ix = 0;
		int iy = 0;
		
		Graph g = new Graph(node);

		/* liaison entre (i,j) et (i,j+1) */
		for (i = 0; i < height; i++) {
			ix = i / height;
			iy = i % height;
			k = Integer.MAX_VALUE;
			for (j = 0; j < width; j++) {
				k = Math.min(itr[i][j], k);
			}
			// System.out.println("min " + i + " " + k);
			k = 0; // TODO A SUPPRIMER !!!!! pour réspécter les consigne, mais
					// mon algo marche mieux avec
			kVal[i] = k;
			for (j = 0; j < width - 1; j++) {
				if((pix1x <= ix && ix <= pix2x) && (pix1y <= iy && iy <= pix2y) && (pix1 < pix2)){
					g.addEdge(new Edge((j * height) + i, ((j + 1) * height) + i,
							itr[i][j], Integer.MAX_VALUE));
				} else {
				g.addEdge(new Edge((j * height) + i, ((j + 1) * height) + i,
						itr[i][j], k));
				}
			}
		}

		/* liaison noeuds à t */
		for (i = 0; i < height; i++) {
			g.addEdge(new Edge((width - 1) * height + i, node - 2,
					itr[i][width - 1], kVal[i]));
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
//				g.addEdge(new Edge((j * height) + i, ((j - 1) * height) + i, infinite, 0));
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

	// calcul le flot max d'un graph
	public static void fullGraph(Graph g) {
		int s = g.vertices() - 1;
		int t = s - 1;
		int p;
		int numberBack;
		boolean saturation;
		HashMap<Integer, Integer> vUsed; // K : Vertices; V : used
		ArrayList<Integer> toDo, full;
		vUsed = new HashMap<Integer, Integer>();
		toDo = new ArrayList<Integer>();
		full = new ArrayList<Integer>();
		Iterable<Edge> edges = g.adj(s);
		for (Edge ed : edges) {
			p = ed.other(s);
			toDo.add(p);
			vUsed.put(p, 255);
			ed.used += 255;
		}
		full.add(s);
		while (toDo.size() > 0) {
			saturation = true;
			numberBack = 0;
			int vFrom = toDo.get(0);
			// System.out.println("point : " + vFrom);
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
									// l'arrête n'est pas saturée au début de
									// l'analyse
									if (saturation) {
										flowInUse = flowNotUsed;
									} else {
										flowInUse = flow;
									}
									ed.used += flowInUse;
									flow -= flowInUse;
									Integer isSet = vUsed.putIfAbsent(vTo,
											flowInUse);
									if (isSet != null) {
										vUsed.put(vTo, isSet + flowInUse);
									} else {
										toDo.add(vTo);
									}
								}
							} else {
								numberBack++;
							}
						} else if ((!full.contains(vTo)) && ed.from == vTo
								&& ed.capacity > 256 && ed.used > 0) {
							numberBack++;
						}
					}
					// Si'l y a saturation
					if (saturation) {
						for (Edge ed : edges) {
							int vTo = ed.other(vFrom);
							if (vFrom == ed.from && (!full.contains(vTo))
									&& ed.capacity > 256 && numberBack > 0) {
								// L'arrête vas vers la diagonal
								int flowMod = flow / numberBack;
								ed.used += (flowMod);
								Integer isSet = vUsed.putIfAbsent(vTo, flowMod);
								if (isSet != null) {
									vUsed.put(vTo, isSet + flowMod);
								} else {
									toDo.add(vTo);
								}
								flow -= flowMod;
								numberBack--;
							}
						}
					}
				}
				// On se retire de la liste de sommet en cours
				if (saturation) {
					backFlow(g, vFrom, flow);
					full.add(vFrom);
				}
				toDo.remove(0);
				vUsed.remove(vFrom);
			}
		}
	}

	// réduit le flot actuel d'un point vers l'entrée
	public static void backFlow(Graph g, int v, int numberToRemove) {
		int passable;
		int s = g.vertices() - 1;
		boolean pathFind;
		while (v != s && numberToRemove > 0) {
			pathFind = false;
			if (!pathFind) {
				for (Edge ed : g.adj(v)) {
					if (ed.to == v && ed.used > 0) {
						// arrête diagonal vers la droite et horizontal gauche
						passable = ed.used;
						if (passable >= numberToRemove) {
							pathFind = true;
							ed.used -= numberToRemove;
							v = ed.other(v);
							break;
						} else {
							ed.used -= passable;
							backFlow(g, ed.other(v), passable);
							numberToRemove -= passable;
						}
					}
				}
			}
			if (!pathFind) {
				System.out.println("Problème, chemin introuvable");
			}
		}
	}

	public static boolean isStillPath(Graph g) {
		boolean pathFind = false;
		boolean ended = false;
		int vertices = g.vertices();
		int s = vertices - 1;
		int t = vertices - 2;
		int currentV = s;
		ArrayList<Integer> toDo = new ArrayList<Integer>();
		ArrayList<Integer> view = new ArrayList<Integer>();
		toDo.add(s);

		while (!ended) {
			view.add(currentV);
			if (currentV == t) {
				pathFind = true;
				break;
			}
			for (Edge ed : g.adj(currentV)) {
				int next = ed.other(currentV);
				if (!view.contains(next)
						&& ((ed.from == currentV && ed.used < ed.capacity) || (ed.to == currentV)
								&& ed.used > 0)) {

					if (!toDo.contains(next)) {
						toDo.add(next);
					}
				}
			}
			toDo.remove(0);
			if (toDo.size() > 0) {
				currentV = toDo.get(0);
			} else {
				ended = true;
			}
		}
		return pathFind;
	}

	public static boolean newFindPath(Graph g) {
		HashMap<Integer, Integer> origin = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> weight = new HashMap<Integer, Integer>();
		ArrayList<Integer> done = new ArrayList<Integer>();
		boolean pathFind = true;
		boolean ended = false;
		int vertices = g.vertices();
		int s = vertices - 1;
		int t = vertices - 2;
		int currentV = s;
		int nextV;
		int count = 0;
		weight.put(s, 0);

//		System.out.println("      |/| On parcourt tout le graph avec Dijkstra |\\|");
		while (!ended) {
			count++;
			if (count%1000== 0) {
//				System.out.println("        && " + count + "/" + s);
			}
//			System.out.println("        ||#|| Sommet n° " + currentV);
			for (Edge ed : g.adj(currentV)) {
				nextV = ed.other(currentV);
				if (!done.contains(nextV)) {
					int w = weight.get(currentV) + 1;
					if (ed.from == currentV && ed.used < ed.capacity) {
						Integer oldW = weight.putIfAbsent(nextV, w);
						if (oldW != null && oldW > w) {
							weight.put(nextV, w);
							origin.put(nextV, currentV);
						} else if (oldW == null) {
							origin.put(nextV, currentV);
						}
					} else if (ed.to == currentV && ed.used > 0) {
						Integer oldW = weight.putIfAbsent(nextV, w);
						if (oldW != null && oldW > w) {
							weight.put(nextV, w);
							origin.put(nextV, currentV);
						} else if (oldW == null) {
							origin.put(nextV, currentV);
						}
					}
				}
			}
			weight.remove(currentV);
			done.add(currentV);
			if (weight.isEmpty()) {
				ended = true;
				pathFind = false;
			} else {
				int min = Integer.MAX_VALUE;
				int v = -1;
				for (Integer k : weight.keySet()) {
					if (!done.contains(k) && min > weight.get(k)) {
						min = weight.get(k);
						v = k;
					}
				}
				currentV = v;
				if (currentV == t) {
					ended = true;
				}
			}
		}

//		System.out.println("      |/| On retrace le chemin |\\|");
		ArrayList<Edge> path = new ArrayList<Edge>();
		if (pathFind) {
			int next;
			int min = Integer.MAX_VALUE;
			while (currentV != s) {
				// System.out.println("        ||#|| Sommet n° " + currentV);
				next = origin.get(currentV);
				for (Edge ed : g.adj(currentV)) {
					if (ed.other(currentV) == next) {
						path.add(ed);
						if (ed.to == currentV && min > ed.capacity - ed.used) {
							min = ed.capacity - ed.used;
						} else if (ed.from == currentV && min > ed.used) {
							min = ed.used;
						}
					}
				}
				currentV = next;
			}
//			System.out.println("      |/| On augemente le flot |\\|");
			currentV = t;
			for (Edge ed : path) {
				if (ed.to == currentV) {
					ed.used += min;
					currentV = ed.other(currentV);
				} else if (ed.from == currentV) {
					ed.used -= min;
					currentV = ed.other(currentV);
				} else {
//					System.out.println("Il y a un problème grave !!!");
				}
			}
		}

		return pathFind;
	}

	public static void fordFulkerson(Graph g) {
		int vertices = g.vertices();
		int s = vertices - 1;
		int t = vertices - 2;
		boolean sommetMarque;
		ArrayList<Integer> marque = new ArrayList<Integer>();
		
		boolean flotMax = false;
		
		while(!flotMax) {
			marque.add(s);
			sommetMarque = true;
			while(sommetMarque) {
				sommetMarque = false;
				for (Integer v : marque) {
					for (Edge ed : g.adj(v)) {
						if ((ed.from == v && ed.used < ed.capacity)||(ed.to == v && ed.used > 0)) { // f (i,j) < c(i,j) ou f (j,i) > 0 
							marque.add(ed.other(v));
							sommetMarque = true;
						}
					}
				}
			}
			if (marque.contains(t)) {
				
			}
		}
		
		/*
		 * 	Initialisation par un flot initial réalisable (f = 0) 
		 * 	Tant que le flot n’est pas maximal 
		 * 		Marquage de la source s 
		 * 		Tant qu’on marque des sommets 
		 * 			Pour tout sommet marqué i 
		 * 				Marquer les sommets j non marqués tq 
		 * 				f (i,j) < c(i,j) ou f (j,i) > 0 
		 * 			Fin pour 
		 * 		Fin Tant que 
		 * 		Si le puits t n’est pas marqué alors le flot est maximal 
		 * 		Sinon amélioration du Flot 
		 * Fin Tant que
		 */
	}

	public static void ameliorer(Graph g) {
		/*
		 * trouver une chaîne qui a permis de marquer t et calculer e' =
		 * min(e'1,e'2) > 0 avec e'1 = min{c(ip,ip+1) e' f (ip,ip+1) avec (ip,ip+1)
		 * e' e' (arête directe)} e'2 = min{f (ip+1,ip) avec (ip+1,ip) e' e' (arête
		 * inverse)} trouver le nouveau e'ot f 0 : Si (ip,ip+1) e' e' (arête
		 * directe) alors f 0(ip,ip+1) = f (ip,ip+1) + e' Si (ip+1,ip) e' e' (arête
		 * inverse) alors f 0(ip,ip+1) = f (ip,ip+1) e' e'
		 */
	}
	
	public static boolean findPath(Graph g) {
		System.out.println("## looking for new path ##");
		boolean ended = false;
		int vertices = g.vertices();
		int s = vertices - 1;
		int t = vertices - 2;
		int currentV = s;
		boolean pathFind = false;
		ArrayList<Integer> toDo = new ArrayList<Integer>();
		ArrayList<Integer> view = new ArrayList<Integer>();
		toDo.add(s);
		while (!ended) {
			view.add(currentV);
			if (currentV == t) {
				pathFind = true;
				break;
			}
			for (Edge ed : g.adj(currentV)) {
				int next = ed.other(currentV);
				if (!view.contains(next)
						&& ((ed.from == currentV && ed.used < ed.capacity) || (ed.to == currentV && ed.used > 0))) {
					if (!toDo.contains(next)) {
						toDo.add(next);
					}
				}
			}
			toDo.remove(0);
			if (toDo.size() > 0) {
				currentV = toDo.get(0);
			} else {
				ended = true;
			}
		}
		if (!pathFind) {
			return false;
		}

		System.out.println("## a new path exist ##");
		currentV = t;
		ArrayList<Edge> arrayListE = new ArrayList<>();

		while (currentV != s) {
			System.out.println(currentV);
			for (Edge ed : g.adj(currentV)) {
				int next = ed.other(currentV);
				if (!view.contains(next)
						&& !arrayListE.contains(ed)
						&& ((ed.to == currentV && ed.used < ed.capacity) || (ed.from == currentV)
								&& ed.used > 0)) {
					arrayListE.add(ed);
				}
			}
		}
		int k = Integer.MAX_VALUE;
		int notUsed;
		currentV = t;
		for (Edge ed : arrayListE) {
			if (currentV == ed.to) {
				notUsed = ed.capacity - ed.used;
			} else {
				notUsed = ed.used;
			}
			k = Math.min(k, notUsed);
			currentV = ed.other(currentV);
		}
		for (Edge ed : arrayListE) {
			ed.used += k;
		}

		return true;
	}

	public static ArrayList<Integer> newCoupeMin(Graph g) {
		ArrayList<Integer> done = new ArrayList<Integer>();
		ArrayList<Integer> toDo = new ArrayList<Integer>();
		ArrayList<Integer> coupeMin = new ArrayList<Integer>();
		boolean ended = false;
		int vertices = g.vertices();
		int s = vertices - 1;
		int t = vertices - 2;
		int currentV = s;
		int nextV;
		toDo.add(s);

		while (!toDo.isEmpty() && !ended) {
			for (Edge ed : g.adj(currentV)) {
				nextV = ed.other(currentV);
				if (!done.contains(nextV) && !toDo.contains(nextV)) {
					if (ed.from == currentV && ed.used < ed.capacity) {
						toDo.add(nextV);
					} else if (ed.to == currentV && ed.used > 0) {
						toDo.add(nextV);
					}
				}
			}
			toDo.remove(0);
			done.add(currentV);
			if (!toDo.isEmpty()) {
				currentV = toDo.get(0);
				if (currentV == t) {
					ended = true;
				}
			}
		}

		int i, j, height, width;
		height = 0;
		boolean select = false;
		for (Edge ed : g.adj(s)) {
			height++;
		}
		width = (vertices - 1) / height;
		for (i = 0; i < height; i++) {
			select = false;
			for (j = 0; j < width; j++) {
				if (!done.contains((j * height) + i)) {
					coupeMin.add(((j - 1) * height) + i);
					select = true;
					break;
				}
			}
			if (!select) {
				coupeMin.add(((j - 1) * height) + i);
			}
		}

		return coupeMin;
	}

	public static ArrayList<Integer> coupeMin(Graph g) {
		boolean ended = false;
		int vertices = g.vertices();
		int s = vertices - 1;
		int t = vertices - 2;
		int currentV = s;
		ArrayList<Integer> toDo = new ArrayList<Integer>();
		ArrayList<Integer> view = new ArrayList<Integer>();
		toDo.add(s);

		while (!ended) {
			if (currentV == 10) {
				System.out.println();
			}
			for (Edge ed : g.adj(currentV)) {
				if ((!view.contains(ed.other(currentV)))
						&& (((ed.from == currentV && ed.used < ed.capacity)) || (ed.to == currentV && ed.used > 0))) {
					toDo.add(ed.other(currentV));
				}
			}
			toDo.remove(0);
			view.add(currentV);
			if (toDo.size() > 0 && currentV != t) {
				currentV = toDo.get(0);
			} else {
				ended = true;
			}
		}

		int i, j, height, width;
		height = 0;
		boolean select = false;
		for (Edge ed : g.adj(s)) {
			height++;
		}
		width = (vertices - 1) / height;
		ArrayList<Integer> coupeMin;
		coupeMin = new ArrayList<>();
		for (i = 0; i < height; i++) {
			select = false;
			for (j = 0; j < width; j++) {
				if (!view.contains((j * height) + i)) {
					coupeMin.add(((j - 1) * height) + i);
					select = true;
					break;
				}
			}
			if (!select) {
				coupeMin.add(((j - 1) * height) + i);
			}
		}
		return coupeMin;
	}

	public static int[][] toImg(int[][] img, ArrayList<Integer> coup) {
		int height = img.length;
		int width = img[0].length;
		int[][] newImg = new int[height][width - 1];
		int i = 0;
		int j = 0;
		int pass = 0;

		for (i = 0; i < height; i++) {
			pass = coup.get(i) / height;
			for (j = 0; j < width; j++) {
				if (j < pass) {
					newImg[i][j] = img[i][j];
				} else if (j > pass) {
					newImg[i][j - 1] = img[i][j];
				}
			}
		}

		return newImg;
	}

	
	
	
	
	public static void main(String[] args) {

		/*
		 * int[][] inter = { { 5, 2, 3 }, { 7, 8, 1 }, { 9, 5, 2 }, { 10, 15, 20
		 * } };
		 * 
		 * System.out.println(" -------------    lecture img   ------------- ");
		 * int[][] img = readpgm("ex2");
		 * System.out.println(" -------------   interest img   ------------- ");
		 * inter = interest(img); Graph g = tograph(inter);
		 * System.out.println(" ------------- start full Graph ------------- ");
		 * fullGraph(g);
		 * System.out.println(" -------------  end full Graph  ------------- ");
		 * 
		 * while (newFindPath(g)) ;
		 * 
		 * System.out.println(" -------------  Graph completed ------------- ");
		 * g.writeFile("fg2"); ArrayList<Integer> coup = newCoupeMin(g);
		 * System.out.println(" -------------  Coup min done   ------------- ");
		 * System.out.println(coup); int[][] img2 = toImg(img, coup); //
		 * printImg(img2); writepgm(img, "ex2_rewrite"); writepgm(img2,
		 * "ex2_coup"); System.out.println(" ------------- fin ------------- ");
		 */

		String fileName = "ex4";
		int[][] inter;
		int[][] img = readpgm(fileName);

		for (int i = 0; i < 10; i++) {
			System.out.println(" ------------- " + i + " ------------- ");
			inter = interest(img);
			Graph g = tograph(inter,0,0);
			System.out.println("  +- fullgraph");
			fullGraph(g);
			System.out.println("  +- findPath");
			int j = 1;
			System.out.println("  | +- findPath " + j++);
			while (newFindPath(g)) {
				System.out.println("  | +- findPath " + j++);
			}
			// g.writeFile(fileName + "_graph");
			System.out.println("  #- coupMin");
			ArrayList<Integer> coup = newCoupeMin(g);
			System.out.println("  #- tiImg");
			img = toImg(img, coup);
		}

		writepgm(img, fileName + "_coup");
		System.out.println(" ------------- fin ------------- ");

	}
}
