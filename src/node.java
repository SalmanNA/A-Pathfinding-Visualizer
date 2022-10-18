import java.util.ArrayList;

public class node {
	private double sCost;
	private double eCost;
	private double fCost;
	private int x;
	private int y;
	private node parent;
	private ArrayList<node> neighbors;
	
	public node(double sCost, double eCost, int x, int y,node parent) {
		this.setsCost(sCost);
		this.seteCost(eCost);
		this.setX(x);
		this.setY(y);
		this.setParent(parent);
		fCost = sCost + eCost; 
		neighbors = new  ArrayList<node>();
	}
	public void setNeighbors(node[] d) {
		for(int i = 0; i<d.length; i++) {
			neighbors.add(d[i]);
		}
	}
	public node[] getNeighbors() {
		return neighbors.toArray(new node[0]);
	}
	public double getfCost() {
		return fCost;
	}
	public void setfCost(double fCost) {
		this.fCost = fCost;
	}
	public double geteCost() {
		return eCost;
	}
	public void seteCost(double eCost) {
		this.eCost = eCost;
		fCost = sCost + eCost;
	}
	public double getsCost() {
		return sCost;
	}
	public void setsCost(double sCost) {
		this.sCost = sCost;
		fCost = sCost + eCost;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public node getParent() {
		return parent;
	}
	public void setParent(node parent) {
		this.parent = parent;
	}
}
