package modelisation;

class Edge
{
   final int from;
   final int to;
   final int capacity;
   int used;
   Edge(int x, int y, int capacity, int used)
	 {
		this.from = x;
		this.to = y;
		this.capacity = capacity;
		this.used = used;
	 }
   
   
   final int other(int v)
	 {
		if (this.from == v) return this.to; else return this.from;
	 }
   
   
}
