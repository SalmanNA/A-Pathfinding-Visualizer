import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Timer;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

class Map extends JPanel implements MouseListener, MouseMotionListener,ActionListener,MouseWheelListener, KeyListener {
	    int CSIZE = 20;
		int cells = 1000/CSIZE;
		int type = 0;
		int x = 0;
		int y = 0;
		int start = 0;
		int startx;
		int starty;
		int pointx;
		int pointy;
		int pointx2;
		int pointy2;
		int endx;
		int count = 0;
		int endy;
		int end  = 0;
		int click = 0;
		boolean wall = false;
		JButton run = new JButton("Start");
		//JButton mazeGen = new JButton(("Generate Maze"));
		public Map() {
			this.setFocusable(true);
			addMouseListener(this);
			addMouseMotionListener(this);
			run.addActionListener(this);
		//	mazeGen.addActionListener(this);
			this.add(run);
	//		this.add(mazeGen);
			addMouseWheelListener(this);
			this.addKeyListener(this);
			repaint();
			
		}
		int[][] recs = new int[1000/CSIZE][1000/CSIZE];
		

		int dCheck = 0;
		boolean done = false;
		ArrayList<node> open = new ArrayList<node>();
		ArrayList<node> closed = new ArrayList<node>();
		ArrayList<node> walls = new ArrayList<node>();
		ArrayList<Integer> wallX = new ArrayList<Integer>();
		ArrayList<Integer> wallY = new ArrayList<Integer>();
		ArrayList<Integer> x1 = new ArrayList<Integer>();
		ArrayList<Integer> y1 = new ArrayList<Integer>();
		node current = null;
		ArrayList<node> nodes = new ArrayList<node>();
		public void paintComponent(Graphics g) {
			double low= 9999999;
			node lowest = null;
			int index = 0;
			if(type == 0) {			
				super.paintComponent(g);
				for(int y = 0; y < cells ; y++) {
					for(int x = 0; x < cells; x++) {
						g.setColor(Color.BLACK);
						g.drawRect(x*CSIZE,y*CSIZE,CSIZE,CSIZE);
					}
				}
				
				if(current != null) {
					for(node var : open) {
						g.setColor(Color.green);
						g.fillRect((var.getX())*CSIZE,(var.getY())*CSIZE,CSIZE,CSIZE);
					}
					for(node var : closed) {
						g.setColor(Color.red);
						g.fillRect((var.getX())*CSIZE,(var.getY())*CSIZE,CSIZE,CSIZE);
					}
					for(int i = 0; i < wallX.size();i++) {
						g.setColor(Color.BLUE);
						g.fillRect(wallX.get(i)*CSIZE,wallY.get(i)*CSIZE,CSIZE,CSIZE);
					}
					for(int i = 0;i<x1.size();i++) {
						g.setColor(Color.ORANGE);
						g.fillRect(x1.get(i)*CSIZE,y1.get(i)*CSIZE,CSIZE,CSIZE);
					}
					g.setColor(Color.GRAY);
					g.fillRect(startx*CSIZE,starty*CSIZE,CSIZE,CSIZE);
					g.setColor(Color.BLACK);
					g.fillRect(endx*CSIZE,endy*CSIZE,CSIZE,CSIZE);
					
				}
			}else if(type == -1) {
				MazeGenerator maze = new  MazeGenerator(cells,cells);
				int[][] make = maze.getMaze();
				for (int row = 0; row < make.length; row++) {
					for (int col = 0; col < cells; col+=3) {
						if((make[col][row] & 1) ==0) {
							g.setColor(Color.BLUE);
							g.fillRect(col*CSIZE,row*CSIZE,CSIZE,CSIZE);
							walls.add(new node(0.0,0.0, col, row,null));
						}
						
					}
					for (int col = 0; col < cells; col+=2) {
						if((make[col][row] & 8) ==0) {
							g.setColor(Color.BLUE);
							g.fillRect(row*CSIZE,col*CSIZE,CSIZE,CSIZE);
							walls.add(new node(0.0,0.0, row, col,null));
						}
						
					}
				}
			}
			switch (type) {
			case 1:
				if(done) {
					return;
				}
				boolean add = true;
				g.setColor(Color.BLUE);
				g.fillRect(x*CSIZE,y*CSIZE,CSIZE,CSIZE); 
				if(count == 0) {
					walls.add(new node(0.0,0.0, x, y,null));
					wallX.add(x);
					wallY.add(y);
				}
				count++;
				for(node var: walls) {
					if(var.getX()==x&&var.getY()==y) {
						add = false;
						break;
					}
				}
				if(add) {
					walls.add(new node(0.0,0.0, x, y,null));
					wallX.add(x);
					wallY.add(y);
				}
				System.out.println(walls.size());
				break;
			case 2:
				g.setColor(Color.GRAY);
				g.fillRect(x*CSIZE,y*CSIZE,CSIZE,CSIZE);
				break;
			case 3:
				g.setColor(Color.BLACK);
				g.fillRect(x*CSIZE,y*CSIZE,CSIZE,CSIZE);
				type = 1;
				break;
			case 4:
				if(done) {
					return;
				}
//					g.setColor(Color.GREEN);
//					g.fillRect((startx)*CSIZE,(starty)*CSIZE,CSIZE,CSIZE);
					node[] neighbors = new node[4];
//					open.add(new node(Math.sqrt(Math.pow((Math.abs((pointx)-startx)),2) + Math.pow((Math.abs((pointy-1)-starty)),2)) , Math.sqrt(Math.pow((Math.abs((pointx)-endx)),2) + Math.pow((Math.abs((pointy-1)-endy)),2)), pointx, pointy-1));
					closed.add(current);
//					neighbors[0] = new node(current.getsCost()+1,Math.sqrt(Math.pow(Math.abs(1+current.getX()-endx),2)+Math.pow(Math.abs(current.getY()-endy),2)), pointx+1, pointy, current);
//					neighbors[1] = new node(current.getsCost()+1,Math.sqrt(Math.pow(Math.abs(current.getX()-endx-1),2)+Math.pow(Math.abs(current.getY()-endy),2)), pointx-1, pointy, current);
//					neighbors[2] = new node(current.getsCost()+1,Math.sqrt(Math.pow(Math.abs(current.getX()-endx),2)+Math.pow(Math.abs(1+current.getY()-endy),2)), pointx, pointy+1, current);
//					neighbors[3] = new node(current.getsCost()+1,Math.sqrt(Math.pow(Math.abs(current.getX()-endx),2)+Math.pow(Math.abs(current.getY()-endy-1),2)), pointx, pointy-1, current);

					
					neighbors[0] = new node(current.getsCost()+1,Math.abs(1+current.getX()-endx)+Math.abs(current.getY()-endy), pointx+1, pointy, current);
					neighbors[1] = new node(current.getsCost()+1,Math.abs(current.getX()-endx-1)+Math.abs(current.getY()-endy), pointx-1, pointy, current);
					neighbors[2] = new node(current.getsCost()+1,Math.abs(current.getX()-endx)+Math.abs(1+current.getY()-endy), pointx, pointy+1, current);
					neighbors[3] = new node(current.getsCost()+1,Math.abs(current.getX()-endx)+Math.abs(current.getY()-endy-1), pointx, pointy-1, current);
					
					open.add(neighbors[0]);
					open.add(neighbors[1]);
					open.add(neighbors[2]);
					open.add(neighbors[3]);
//					open.add(neighbors[4]);
//					open.add(neighbors[5]);
//					open.add(neighbors[6]);
//					open.add(neighbors[7]);
					
					for(int i = 0;i<open.size();i++) {
						for(int j = i+1;j<open.size();j++) {
							if(open.get(i).getX()==open.get(j).getX() && open.get(i).getY()==open.get(j).getY()) {
								open.remove(j);
							}
						}
					}
					for(int i = 0;i<closed.size();i++) {
						for(int j = 0;j<open.size();j++) {
							if(open.get(j).getX()==closed.get(i).getX() && open.get(j).getY()==closed.get(i).getY()) {
								open.remove(j);
							}
						}
					}
//					if(open.contains(neighbors[4])) {
//						System.out.println("WORK");
//					}else {
//						open.add(neighbors[4]);						
//					}if(open.contains(neighbors[5])) {
//						System.out.println("WORK");
//					}else {
//						open.add(neighbors[5]);						
//					}if(open.contains(neighbors[6])) {
//						System.out.println("WORK");
//					}else {
//						open.add(neighbors[6]);						
//					}if(open.contains(neighbors[7])) {
//						System.out.println("WORK");
//					}else {
//						open.add(neighbors[7]);						
//					}
					
					System.out.println("OPEN "+open.size());
					if(!done) {
						current = findLow(open.toArray(new node[0]));						
					}
					current.setNeighbors(neighbors);
					for(int i = 0; i<current.getNeighbors().length;i++) {
						g.setColor(Color.GREEN);
						g.fillRect((current.getNeighbors()[i].getX())*CSIZE,(current.getNeighbors()[i].getY())*CSIZE,CSIZE,CSIZE);
					}
					if(!done) {
						for(node var : closed) {
							g.setColor(Color.red);
							g.fillRect((var.getX())*CSIZE,(var.getY())*CSIZE,CSIZE,CSIZE);
						}						
					}
//					current = findLow(current.getNeighbors());
					pointx = current.getX();
					pointy = current.getY();
					
					for(int i = 0;i<walls.size();i++) {
						for(int j = 0;j<nodes.size();j++) {
							if(nodes.get(j).getX()==walls.get(i).getX()&&nodes.get(j).getY()==walls.get(i).getY()) {
								nodes.remove(j);
							}
						}
					}
					for(int i = 0; i < wallX.size();i++) {
						g.setColor(Color.BLUE);
						g.fillRect(wallX.get(i)*CSIZE,wallY.get(i)*CSIZE,CSIZE,CSIZE);
					}
					g.setColor(Color.GRAY);
					g.fillRect(startx*CSIZE,starty*CSIZE,CSIZE,CSIZE);
					
					
					
					if((pointx == endx && pointy == endy)) {
						for(int i = 0; i<nodes.size();i++) {
							g.setColor(Color.GRAY);
							g.fillRect((nodes.get(i).getX())*CSIZE,(nodes.get(i).getY())*CSIZE,CSIZE,CSIZE);
						}
//						g.setColor(Color.GREEN);
//						g.fillRect(startx*CSIZE,starty*CSIZE,CSIZE,CSIZE);
						g.setColor(Color.RED);
						g.fillRect(endx*CSIZE,endy*CSIZE,CSIZE,CSIZE);
						while (current.getParent()!=null) {
							x1.add(current.getParent().getX());
							y1.add(current.getParent().getY());
							current = current.getParent();
						}
						for(int i = 0; i < wallX.size();i++) {
							g.setColor(Color.BLUE);
							g.fillRect(wallX.get(i)*CSIZE,wallY.get(i)*CSIZE,CSIZE,CSIZE);
						}
						for(int i = 0;i<x1.size();i++) {
							g.setColor(Color.ORANGE);
							g.fillRect(x1.get(i)*CSIZE,y1.get(i)*CSIZE,CSIZE,CSIZE);
						}
						g.setColor(Color.GRAY);
						g.fillRect(startx*CSIZE,starty*CSIZE,CSIZE,CSIZE);
						g.setColor(Color.BLACK);
						g.fillRect(endx*CSIZE,endy*CSIZE,CSIZE,CSIZE);
						done = true;
						type = 0;
						System.out.println("DONE");
					}
					
				}
				}
		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void keyPressed(KeyEvent e) {
			System.out.println("SPACE");
			int key = e.getKeyCode();
			if(key == KeyEvent.VK_ESCAPE&&type !=4) {
				System.out.println("SPACE");
				open.clear();
				closed.clear();
				walls.clear();
				wallX.clear();
				wallY.clear();
				x1.clear();
				y1.clear();
				type = 0;
				x = 0;
				click = 0;
				y = 0;
				start = 0;
				end = 0;
				type = 0;
				current = null;
				done = false;
				repaint();
			}
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
		if((start == 1 && end == 1)&& type != 4) {
			
			type = 1;
			x=e.getX()/CSIZE;
			y=e.getY()/CSIZE;
			repaint();
		}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			x=e.getX()/CSIZE;
			y=e.getY()/CSIZE;
			if(start == 0) {
				type = 2;
				startx = x;
				starty = y;
				start++;
				repaint();	
			}else if(end == 0) {
				type = 3;
				endx = x;
				endy = y;
				end++;
				current = new node(0 , Math.abs(startx-endx)+Math.abs(starty-endy), startx, starty, null);
				repaint();			
			}
			
			
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if(start == 0  || done) {
				
				if(e.getWheelRotation()>0) {
					type = 0;
					CSIZE+=1;
					cells = 1000/CSIZE;
					repaint();
					System.out.println(cells);
				}else if(e.getWheelRotation()<0&&CSIZE>5){
					type = 0;
					CSIZE-=1;
					cells = 1000/CSIZE;
					repaint();
					System.out.println(cells);
				}
				
			}
			
		}
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void actionPerformed(ActionEvent e) {
		//	if (e.getSource()==mazeGen)
	      //   {
				if(type == -1) {
					type = 0;
					repaint();
				}else {
					type = -1;
					repaint();					
				}
	      //   }else {
	        	 
	        	 if(!done) {
	        		 
	        		 if(click == 0) {
	        			 pointx = startx;
	        			 pointy = starty;				
	        		 }
	        		 type = 4;
	        		 click++;
	        		 
	        		 new SwingWorker() {
	        			 @Override
	        			 protected Object doInBackground() throws Exception {
	        				 while(!done) {
	        					 repaint();
	        					 try{Thread.sleep(1);} catch(Exception ex){}
	        				 }
	        				 return null;
	        			 }
	        		 }.execute();
	        	 }
	        // }
			this.requestFocus();
		}
		public node findLow(node[] list) {
			double low = 99999999;
			int index = 0;
			int countt = 0;
			ArrayList<node> dupe = new ArrayList<node>();
			for(int i = 0; i<list.length;i++) {
				boolean cont = false;
				if(list[i].getfCost()<low) {
					for(node var : walls) {
						if(var.getX()==list[i].getX()&&var.getY()==list[i].getY()) {
							cont = true;
							wall = true;
						}
					}
					if(cont) {
						continue;
					}
					low = list[i].getfCost();
					index = i;
				}
			}
			for(int i = 0; i<list.length;i++) {
				if(low==list[i].getfCost()) {
					dupe.add(list[i]);
					countt++;
				}
			}
//			if(!wall) {
//				System.out.println("EEEEEEEEEE");
//				return findLowE(list);
//			}
			System.out.println("FFFFFFFFF");
			return list[index];
		}
		public node findLowE(node[] list) {
			double low = 99999999;
			int index = 0;
			for(int i = 0; i<list.length;i++) {
				boolean cont = false;
				if(list[i].geteCost()<low) {
					for(node var : walls) {
						if(var.getX()==list[i].getX()&&var.getY()==list[i].getY()) {
							cont = true;
						}
					}
					for(node var: closed) {
						if(var.getX()==list[i].getX()&&var.getY()==list[i].getY()) {
							cont = true;
						}
					}
					if(cont) {
						continue;
					}
					low = list[i].geteCost();
					index = i;
				}
			}
			return list[index];
		}

	}
/*
 
					if(open.contains(new node(Math.sqrt(Math.pow((Math.abs((pointx+1)-startx)),2) + Math.pow((Math.abs((pointy)-starty)),2)) , Math.sqrt(Math.pow((Math.abs((pointx+1)-endx)),2) + Math.pow((Math.abs((pointy)-endy)),2)), pointx, pointy-1))) {
						break;
					}else {						
						open.add(new node(Math.sqrt(Math.pow((Math.abs((pointx+1)-startx)),2) + Math.pow((Math.abs((pointy)-starty)),2)) , Math.sqrt(Math.pow((Math.abs((pointx+1)-endx)),2) + Math.pow((Math.abs((pointy)-endy)),2)), pointx, pointy-1));
					}
					
					if(open.contains(new node(Math.sqrt(Math.pow((Math.abs((pointx-1)-startx)),2) + Math.pow((Math.abs((pointy)-starty)),2)) , Math.sqrt(Math.pow((Math.abs((pointx+1)-endx)),2) + Math.pow((Math.abs((pointy)-endy)),2)), pointx, pointy-1))) {
						break;
					}else {						
						open.add(new node(Math.sqrt(Math.pow((Math.abs((pointx-1)-startx)),2) + Math.pow((Math.abs((pointy)-starty)),2)) , Math.sqrt(Math.pow((Math.abs((pointx-1)-endx)),2) + Math.pow((Math.abs((pointy)-endy)),2)), pointx, pointy-1));
						
					}
					
					if(open.contains(new node(Math.sqrt(Math.pow((Math.abs((pointx)-startx)),2) + Math.pow((Math.abs((pointy+1)-starty)),2)) , Math.sqrt(Math.pow((Math.abs((pointx)-endx)),2) + Math.pow((Math.abs((pointy+1)-endy)),2)), pointx, pointy-1))) {
						break;
					}else {						
						open.add(new node(Math.sqrt(Math.pow((Math.abs((pointx)-startx)),2) + Math.pow((Math.abs((pointy+1)-starty)),2)) , Math.sqrt(Math.pow((Math.abs((pointx)-endx)),2) + Math.pow((Math.abs((pointy+1)-endy)),2)), pointx, pointy-1));
						
					}
					
					if(open.contains(new node(Math.sqrt(Math.pow((Math.abs((pointx)-startx)),2) + Math.pow((Math.abs((pointy-1)-starty)),2)) , Math.sqrt(Math.pow((Math.abs((pointx)-endx)),2) + Math.pow((Math.abs((pointy-1)-endy)),2)), pointx, pointy-1))) {
						break;
					}else {						
						open.add(new node(Math.sqrt(Math.pow((Math.abs((pointx)-startx)),2) + Math.pow((Math.abs((pointy-1)-starty)),2)) , Math.sqrt(Math.pow((Math.abs((pointx)-endx)),2) + Math.pow((Math.abs((pointy-1)-endy)),2)), pointx, pointy-1));
						
					}	
*/