import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
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
		int wallI = 0;
		boolean wall = false;
		JButton run = new JButton("Start");
		JButton mazeGen = new JButton(("Generate Maze"));
		JSlider speed = new JSlider(JSlider.HORIZONTAL,0,30,15);
		JLabel slider = new JLabel("Change Speed of Algorithm");
		Color myC = new Color(0, 0,0,100);
		BoxLayout gl = new BoxLayout(this, 1);
		public Map() {
			slider.setBackground(Color.BLACK);
			this.setFocusable(true);
			addMouseListener(this);
			addMouseMotionListener(this);
			run.addActionListener(this);
			mazeGen.addActionListener(this);
			this.add(run);
			this.add(mazeGen);
			addMouseWheelListener(this);
			this.addKeyListener(this);
			repaint();
			slider.setForeground(Color.white);
			speed.setBackground(myC);
			speed.setOpaque(false);
			this.add(speed);
			this.add(slider);
			
		}
		int[][] recs = new int[1000/CSIZE][1000/CSIZE];
		

		int dCheck = 0;
		boolean done = false;
		int counter = 0;
		ArrayList<node> open = new ArrayList<node>();
		ArrayList<node> closed = new ArrayList<node>();
		ArrayList<node> walls = new ArrayList<node>();
		ArrayList<Integer> wallX = new ArrayList<Integer>();
		ArrayList<Integer> wallY = new ArrayList<Integer>();
		ArrayList<Integer> x1 = new ArrayList<Integer>();
		ArrayList<Integer> y1 = new ArrayList<Integer>();
		ArrayList<node> mapMaker = new ArrayList<node>();
		node current = null;
		ArrayList<node> nodes = new ArrayList<node>();
		ArrayList<Point> map = new ArrayList<Point>();
		ArrayList<Point> maze = new ArrayList<Point>();
		public void paintComponent(Graphics g) {
			double low= 9999999;
			node lowest = null;
			int index = 0;
			if(type == 0) {			
				super.paintComponent(g);
				g.setColor(myC);
				g.fillRect(200, 0, 580, 50);
				for(int y = 0; y < cells ; y++) {
					for(int x = 0; x < cells; x++) {
						g.setColor(Color.BLACK);
						g.drawRect(x*CSIZE,y*CSIZE,CSIZE,CSIZE);
						
						
					}
				}
				
				
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
				if(current != null) {
					g.setColor(Color.GRAY);
					g.fillRect(startx*CSIZE,starty*CSIZE,CSIZE,CSIZE);
					g.setColor(Color.BLACK);
					g.fillRect(endx*CSIZE,endy*CSIZE,CSIZE,CSIZE);
					
				}
			}else if(type == -1) {
				
				map.clear();
				mapMaker.clear();
				maze.clear();
				wallX.clear();
				wallY.clear();
				walls.clear();
				node head = new node(0.0,0.0,0,0,null);
				int visited = 0;
				int x = 0;
				int y = 0;
				int direction;
				mapMaker.add(head);
				node current;
				node newN;
				current = head;
				map.add(new Point(0,0));
				int loop = 0;
				while(visited<((cells/2)*(cells/2))-1) {
					direction = (int) (Math.random()*4)+1;
					switch(direction) {
					case 1:
						y-=2;
						if((x<0 || y<0)||(x>cells-1 || y>cells-1)) {
							y+=2;
							break;
						}
						
						if(map.contains(new Point(x,y))) {
							y+=2;
							loop++;
							if(loop>25) {		
								x = current.getParent().getX();
								y = current.getParent().getY();
								current = current.getParent();
								loop = 0;
							}
							break;
						}
						mapMaker.add(new node(0.0,0.0,x,y,current));
						newN = new node(0.0,0.0,x,y,current);
						current = newN;
						map.add(new Point(x,y));
						maze.add(new Point(x,y));
						maze.add(new Point(x,y+1));
						visited++;
						System.out.println(x+" "+y);
						System.out.println(visited);
						break;
					case 2: 
						x+=2;
						if((x<0 || y<0)||(x>cells-1 || y>cells-1)) {
							x-=2;
							break;
						}
						if(map.contains(new Point(x,y))) {
							x-=2;
							loop++;
							if(loop>25) {		
								x = current.getParent().getX();
								y = current.getParent().getY();
								current = current.getParent();
								loop = 0;
							}
							break;
						}
						mapMaker.add(new node(0.0,0.0,x,y,current));
						newN = new node(0.0,0.0,x,y,current);
						current = newN;
						map.add(new Point(x,y));
						maze.add(new Point(x,y));
						maze.add(new Point(x-1,y));
						visited++;
						System.out.println(x+" "+y);
						System.out.println(visited);
						break;
					case 3:
						y+=2;
						if((x<0 || y<0)||(x>cells-1 || y>cells-1)) {
							y-=2;
							break;
						}
						if(map.contains(new Point(x,y))) {
							y-=2;
							loop++;
							if(loop>25) {		
								x = current.getParent().getX();
								y = current.getParent().getY();
								current = current.getParent();
								loop = 0;
							}
							break;
						}
						mapMaker.add(new node(0.0,0.0,x,y,current));
						newN = new node(0.0,0.0,x,y,current);
						current = newN;
						map.add(new Point(x,y));
						maze.add(new Point(x,y));
						maze.add(new Point(x,y-1));
						visited++;
						System.out.println(x+" "+y);
						System.out.println(visited);
						break;
					case 4:
						x-=2; 
						if((x<0 || y<0)||(x>cells-1 || y>cells-1)) {
							x+=2;
							break;
						}
						if(map.contains(new Point(x,y))) {
							x+=2;
							loop++;
							if(loop>25) {		
								x = current.getParent().getX();
								y = current.getParent().getY();
								current = current.getParent();
								loop = 0;
							}
							break;
						}
						mapMaker.add(new node(0.0,0.0,x,y,current));
						newN = new node(0.0,0.0,x,y,current);
						current = newN;
						map.add(new Point(x,y));
						maze.add(new Point(x,y));
						maze.add(new Point(x+1,y));
						visited++;
						System.out.println(x+" "+y);
						System.out.println(visited);
						break;
					}
				}
				
				maze.add(new Point(0,0));
				for(int row = 0; row < cells;row++) {
					for(int col = 0; col<cells;col++) {
						if(maze.contains(new Point(col,row))){
							continue;
						}else {
							wallX.add(col);
							wallY.add(row);	
							walls.add(new node(999999999.0,999999999.0,col,row,null));
							
						}
					}
				}
				for(int i = 0;i<cells;i++) {
					wallX.add(i);
					wallY.add(cells);
					walls.add(new node(999999999.0,999999999.0,i,cells,null));
				}
				for(int i = 0;i<cells;i++) {
					wallX.add(cells);
					wallY.add(i);
					walls.add(new node(999999999.0,999999999.0,cells,i,null));
				}
				for(int i = 0;i<cells;i++) {
					wallX.add(i);
					wallY.add(-1);
					walls.add(new node(999999999.0,999999999.0,i,-1,null));
				}
				for(int i = 0;i<cells;i++) {
					wallX.add(-1);
					wallY.add(i);
					walls.add(new node(999999999.0,999999999.0,-1,i,null));
				}
				
				type = 0;
				repaint();

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
					walls.add(new node(999999999.0,999999999.0, x, y,null));
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
					walls.add(new node(999999999.0,999999999.0, x, y,null));
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
					node[] neighbors = new node[4];
					closed.add(current);

					
					neighbors[0] = new node(current.getsCost()+1,Math.abs(1+current.getX()-endx)+Math.abs(current.getY()-endy), pointx+1, pointy, current);
					neighbors[1] = new node(current.getsCost()+1,Math.abs(current.getX()-endx-1)+Math.abs(current.getY()-endy), pointx-1, pointy, current);
					neighbors[2] = new node(current.getsCost()+1,Math.abs(current.getX()-endx)+Math.abs(1+current.getY()-endy), pointx, pointy+1, current);
					neighbors[3] = new node(current.getsCost()+1,Math.abs(current.getX()-endx)+Math.abs(current.getY()-endy-1), pointx, pointy-1, current);
					
					open.add(neighbors[0]);
					open.add(neighbors[1]);
					open.add(neighbors[2]);
					open.add(neighbors[3]);
					
					//remove diag
					for(int i = 0;i<open.size();i++) {
						for(int j = i+1;j<open.size();j++) {
							if(open.get(i).getX()==open.get(j).getX() && open.get(i).getY()==open.get(j).getY()) {
								open.remove(j);
							}
						}
					}
					//remove straight
					for(int i = 0;i<closed.size();i++) {
						for(int j = 0;j<open.size();j++) {
							if(open.get(j).getX()==closed.get(i).getX() && open.get(j).getY()==closed.get(i).getY()) {
								open.remove(j);
							}
						}
					}
					for(int i = 0;i<open.size();i++) {
						if(open.get(i).getX()<0||open.get(i).getY()<0) {
							open.remove(i);
						}
					}
					for(int i = 0;i<walls.size();i++) {
						for(int j = 0;j<open.size();j++) {
							if(open.get(j).getX()==walls.get(i).getX() && open.get(j).getY()==walls.get(i).getY()) {
								open.remove(j);
							}
						}
					}
					for(int i = 0; i < open.size();i++) {
						if((open.get(i).getX()>cells)||(open.get(i).getY()>cells)) {
							open.remove(i);
						}
					}
					if(open.size()==0) {
						done = true;
						type = 5;
					}
					System.out.println("OPEN "+open.size());
					if(!done) {
						current = aStarPath(open.toArray(new node[0]));
						
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
					if(cells%2==1) {
						cells++;
					}
					System.out.println(cells);
				}else if(e.getWheelRotation()<0&&CSIZE>2){
					type = 0;
					CSIZE-=1;
					cells = 1000/CSIZE;
					repaint();
					if(cells%2==1) {
						cells++;
					}
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
			if (e.getSource()==mazeGen)
	         {
				type = -1;
				repaint();
				
	         }else {
	        	 
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
	        					 try{Thread.sleep(speed.getValue());} catch(Exception ex){}
	        				 }
	        				 return null;
	        			 }
	        		 }.execute();
	        	 }
	         }
			this.requestFocus();
		}
		
		
		public node aStarPath(node[] list) {
			double low = 99999999;
			int index = 0;
			for(int i = 0; i<list.length;i++) {
				if(list[i].getfCost()<low) {
					low = list[i].getfCost();
					index = i;
				}
			}
			
			return list[index];
		}
		
		public void updateWall(Graphics g) {
			for(int i = 0; i < wallX.size();i++) {
				g.setColor(Color.BLUE);
				g.fillRect(wallX.get(i)*CSIZE,wallY.get(i)*CSIZE,CSIZE,CSIZE);
				
				
			}
			
		}

	}
