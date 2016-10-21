/*
	 ______      __         __
	/ ____/_  __/ /__  ____/ /
 / / __/ / / / / _ \/ __ /
/ /_/ / /_/ / /  __/ /_/ /
\____/\__,_/_/\___/\__,_/

*/

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class TreeofPythagoras extends Frame {

	public String option = "";

	public static void main(String[] args) {
		if(args[0].equals("recursion")){
			System.out.println("Generating tree using recursion.");
		}else{
			System.out.println("Generating tree using a queue.");
		}
		new TreeofPythagoras(args[0]);
	}

	TreeofPythagoras(String option) {
		super("\"Tree of Pythagoras\"");

		this.option = option;
		// Add Window Listener
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// Default canvas size as instructed is 400 x 400
		setSize(400, 400);
		add("Center", new CvPythagoras());
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		show();
	}

	class CvPythagoras extends Canvas {

		int clickCounter = 0;

		int centerX, centerY;
		float pixelSize = 0, rWidth = 100.0F, rHeight = 100.0F, xP = 1000000, yP;
		int maxX = 0;
		int maxY = 0;
		float epsilon = 2;
		boolean ccw = false;
		Point2D.Float p1 = new Point2D.Float(0,0);
		Point2D.Float p2 = new Point2D.Float(0,0);
		Point2D.Float origin = new Point2D.Float(0,0);

		int iX(float x){return Math.round(x/pixelSize);}
		int iY(float y){return Math.round(maxY - y/pixelSize);}
		float fX(int x){return (float) (x * pixelSize);}
		float fY(int y) { return (float) ((maxY - y) * pixelSize); }


		CvPythagoras() {

			// constructor
			addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent evt) {
					// In the beginning we want to keep track of the first two
					// clicks
					// We want the first two clicks to be saved as device
					// coordinates
					if (clickCounter >= 0 && clickCounter < 2) {
						clickCounter++;

						// If the clickcounter is == 0, then that is the first
						// click
						if (clickCounter == 1) {

							p1.setLocation(fX(evt.getX()), fY(evt.getY()));

						} else if (clickCounter == 2) {

					   	p2.setLocation(fX(evt.getX()), fY(evt.getY()));

							// Check if orientation is ccw
							if(area2(p1, p2, origin) > 0){
								ccw = true;
							}else{
								ccw = false;
							}

						}
						repaint();
					}
				}
			});

		}

		public float area2(Point2D.Float a, Point2D.Float b, Point2D.Float c){
			return (a.x - c.x)*(b.y - c.y) - (a.y - c.y)*(b.x - c.x);
		}

		public float distance(Point2D.Float point1, Point2D.Float point2){
			 float xSquared = (float) Math.pow((point2.x - point1.x), 2);
			 float ySquared = (float) Math.pow((point2.y - point1.y), 2);
			 float distance = (float) Math.sqrt(xSquared + ySquared);
			 return distance;
		}

		private Random randomNumber = new Random();

		private Color getRandomColor() {
		    return new Color(randomNumber.nextFloat(),
		            randomNumber.nextFloat(), randomNumber.nextFloat());
		}


		public void makePentagonQueue(Graphics g, Point2D.Float P1, Point2D.Float P2){
			Queue<ArrayList<Point2D.Float>> pointQueue = new LinkedList<ArrayList<Point2D.Float>>();
			ArrayList<Point2D.Float> arr = new ArrayList<Point2D.Float>();
			arr.add(P1);
			arr.add(P2);
			pointQueue.add(arr);

			while(pointQueue.size() != 0){

				ArrayList<Point2D.Float> pointPair = pointQueue.remove();
				float dist = distance(pointPair.get(0), pointPair.get(1));
				Point2D.Float ccwRotation_i_hat = new Point2D.Float(0,1);
				Point2D.Float ccwRotation_j_hat = new Point2D.Float(-1,0);

				float vectorMagnitudeOfP2P1= (float) Math.sqrt(Math.pow((pointPair.get(0).x - pointPair.get(1).x), 2) + Math.pow((pointPair.get(0).y - pointPair.get(1).y), 2));
				Point2D.Float P2P1 = new Point2D.Float((pointPair.get(0).x - pointPair.get(1).x)/vectorMagnitudeOfP2P1,(pointPair.get(0).y - pointPair.get(1).y)/vectorMagnitudeOfP2P1);

				Point2D.Float temp1 = new Point2D.Float((P2P1.x * ccwRotation_i_hat.x), (P2P1.x * ccwRotation_i_hat.y));
				Point2D.Float temp2 = new Point2D.Float((P2P1.y * ccwRotation_j_hat.x), (P2P1.y * ccwRotation_j_hat.y));
				Point2D.Float temp3 = new Point2D.Float(dist * (temp1.x + temp2.x), dist * (temp1.y + temp2.y));


				Point2D.Float Q1 = new Point2D.Float((temp3.x + pointPair.get(0).x),(temp3.y + pointPair.get(0).y));
				//	Point2D.Float Q2 = new Point2D.Float((ccwRotation_Vector.x + P2.x),(ccwRotation_Vector.y + P2.y));
				Point2D.Float ccwRotation_i_hat_2 = new Point2D.Float(0,-1);
				Point2D.Float ccwRotation_j_hat_2  = new Point2D.Float(1,0);

				float vectorMagnitudeOfP1Q1= (float) Math.sqrt(Math.pow((pointPair.get(0).x - Q1.x), 2) + Math.pow((pointPair.get(0).y - Q1.y), 2));
				Point2D.Float P1Q1 = new Point2D.Float((pointPair.get(0).x - Q1.x)/vectorMagnitudeOfP1Q1,(pointPair.get(0).y - Q1.y)/vectorMagnitudeOfP1Q1);

				Point2D.Float temp4 = new Point2D.Float((P1Q1.x * ccwRotation_i_hat_2.x), (P1Q1.x * ccwRotation_i_hat_2.y));
				Point2D.Float temp5 = new Point2D.Float((P1Q1.y * ccwRotation_j_hat_2.x), (P1Q1.y * ccwRotation_j_hat_2.y));
				Point2D.Float temp6 = new Point2D.Float(dist * (temp4.x + temp5.x), dist * (temp4.y + temp5.y));

				Point2D.Float Q2 = new Point2D.Float((temp6.x + Q1.x),(temp6.y + Q1.y));

				g.drawLine(iX(pointPair.get(1).x), iY(pointPair.get(1).y), iX(pointPair.get(0).x), iY(pointPair.get(0).y));
				g.drawLine(iX(pointPair.get(0).x), iY(pointPair.get(0).y), iX(Q1.x), iY(Q1.y));
				g.drawLine(iX(Q1.x), iY(Q1.y), iX(Q2.x), iY(Q2.y));
				g.drawLine(iX(Q2.x), iY(Q2.y), iX(pointPair.get(1).x), iY(pointPair.get(1).y));

				g.setColor(getRandomColor());
				int[] xvalues = new int[4];
				int[] yvalues = new int[4];

				xvalues[0] =  iX(pointPair.get(0).x);
				xvalues[1] =  iX(Q1.x);
				xvalues[2] =  iX(Q2.x);
				xvalues[3] =  iX(pointPair.get(1).x);

				yvalues[0] =  iY(pointPair.get(0).y);
				yvalues[1] =  iY(Q1.y);
				yvalues[2] =  iY(Q2.y);
				yvalues[3] =  iY(pointPair.get(1).y);

				g.fillPolygon(xvalues, yvalues, 4);

				dist = distance(Q1, Q2);
				float distOfLeg = (dist * (float) Math.sqrt(2))/2;
				float vectorMagnitudeOfQ1Q2= (float) Math.sqrt(Math.pow((Q1.x - Q2.x), 2) + Math.pow((Q1.y - Q2.y), 2));
				Point2D.Float Q1Q2 = new Point2D.Float((Q1.x - Q2.x)/vectorMagnitudeOfQ1Q2,(Q1.y - Q2.y)/vectorMagnitudeOfQ1Q2);

				Point2D.Float ccwRotation_rotation_i = new Point2D.Float( (float) Math.cos(Math.toRadians(45)),(float) Math.sin(Math.toRadians(45)));
				Point2D.Float ccwRotation_rotation_j = new Point2D.Float((float) -Math.sin(Math.toRadians(45)),(float) Math.cos(Math.toRadians(45)));

				Point2D.Float temp7 = new Point2D.Float((Q1Q2.x * ccwRotation_rotation_i.x), (Q1Q2.x * ccwRotation_rotation_i.y));
				Point2D.Float temp8 = new Point2D.Float((Q1Q2.y * ccwRotation_rotation_j.x), (Q1Q2.y * ccwRotation_rotation_j.y));
				Point2D.Float temp9 = new Point2D.Float((distOfLeg) * (temp7.x + temp8.x), (distOfLeg) *  (temp7.y + temp8.y));

				Point2D.Float Q3 = new Point2D.Float((temp9.x + Q2.x), (temp9.y + Q2.y));
				g.drawLine(iX(Q1.x), iY(Q1.y), iX(Q3.x), iY(Q3.y));
				g.drawLine(iX(Q2.x), iY(Q2.y), iX(Q3.x), iY(Q3.y));

				g.setColor(getRandomColor());
				int[] xtriangle = new int[4];
				int[] ytriangle = new int[4];

				xtriangle[0] =  iX(Q1.x);
				xtriangle[1] =  iX(Q3.x);
				xtriangle[2] =  iX(Q2.x);

				ytriangle[0] =  iY(Q1.y);
				ytriangle[1] =  iY(Q3.y);
				ytriangle[2] =  iY(Q2.y);

				g.fillPolygon(xtriangle, ytriangle, 3);

				dist = distance(pointPair.get(0), pointPair.get(1));
				if(dist > 0.5){
					ArrayList<Point2D.Float> newPP1= new ArrayList<Point2D.Float>();
					ArrayList<Point2D.Float> newPP2= new ArrayList<Point2D.Float>();
					newPP1.add(Q3);
					newPP1.add(Q2);
					newPP2.add(Q1);
					newPP2.add(Q3);
					pointQueue.add(newPP1);
					pointQueue.add(newPP2);
				}
			}

		}

		public void makePentagon(Graphics g, Point2D.Float P1, Point2D.Float P2){

				if(distance(P1, P2) < 0.5){
					return;
				}

				// Solve for Q1 and Q3
				float dist = distance(P1, P2);
				Point2D.Float ccwRotation_i_hat = new Point2D.Float(0,1);
				Point2D.Float ccwRotation_j_hat = new Point2D.Float(-1,0);

				float vectorMagnitudeOfP2P1= (float) Math.sqrt(Math.pow((P1.x - P2.x), 2) + Math.pow((P1.y - P2.y), 2));
				Point2D.Float P2P1 = new Point2D.Float((P1.x - P2.x)/vectorMagnitudeOfP2P1,(P1.y - P2.y)/vectorMagnitudeOfP2P1);

				Point2D.Float temp1 = new Point2D.Float((P2P1.x * ccwRotation_i_hat.x), (P2P1.x * ccwRotation_i_hat.y));
				Point2D.Float temp2 = new Point2D.Float((P2P1.y * ccwRotation_j_hat.x), (P2P1.y * ccwRotation_j_hat.y));
				Point2D.Float temp3 = new Point2D.Float(dist * (temp1.x + temp2.x), dist * (temp1.y + temp2.y));


				Point2D.Float Q1 = new Point2D.Float((temp3.x + P1.x),(temp3.y + P1.y));
				//	Point2D.Float Q2 = new Point2D.Float((ccwRotation_Vector.x + P2.x),(ccwRotation_Vector.y + P2.y));
				Point2D.Float ccwRotation_i_hat_2 = new Point2D.Float(0,-1);
				Point2D.Float ccwRotation_j_hat_2  = new Point2D.Float(1,0);

				float vectorMagnitudeOfP1Q1= (float) Math.sqrt(Math.pow((P1.x - Q1.x), 2) + Math.pow((P1.y - Q1.y), 2));
				Point2D.Float P1Q1 = new Point2D.Float((P1.x - Q1.x)/vectorMagnitudeOfP1Q1,(P1.y - Q1.y)/vectorMagnitudeOfP1Q1);

				Point2D.Float temp4 = new Point2D.Float((P1Q1.x * ccwRotation_i_hat_2.x), (P1Q1.x * ccwRotation_i_hat_2.y));
				Point2D.Float temp5 = new Point2D.Float((P1Q1.y * ccwRotation_j_hat_2.x), (P1Q1.y * ccwRotation_j_hat_2.y));
				Point2D.Float temp6 = new Point2D.Float(dist * (temp4.x + temp5.x), dist * (temp4.y + temp5.y));

				Point2D.Float Q2 = new Point2D.Float((temp6.x + Q1.x),(temp6.y + Q1.y));

				g.drawLine(iX(P2.x), iY(P2.y), iX(P1.x), iY(P1.y));
				g.drawLine(iX(P1.x), iY(P1.y), iX(Q1.x), iY(Q1.y));
				g.drawLine(iX(Q1.x), iY(Q1.y), iX(Q2.x), iY(Q2.y));
				g.drawLine(iX(Q2.x), iY(Q2.y), iX(P2.x), iY(P2.y));


				// Fill
				g.setColor(getRandomColor());
				int[] xvalues = new int[4];
				int[] yvalues = new int[4];

				xvalues[0] =  iX(P1.x);
				xvalues[1] =  iX(Q1.x);
				xvalues[2] =  iX(Q2.x);
				xvalues[3] =  iX(P2.x);

				yvalues[0] =  iY(P1.y);
				yvalues[1] =  iY(Q1.y);
				yvalues[2] =  iY(Q2.y);
				yvalues[3] =  iY(P2.y);

				g.fillPolygon(xvalues, yvalues, 4);

				dist = distance(Q1, Q2);
				float distOfLeg = (dist * (float) Math.sqrt(2))/2;
				float vectorMagnitudeOfQ1Q2= (float) Math.sqrt(Math.pow((Q1.x - Q2.x), 2) + Math.pow((Q1.y - Q2.y), 2));
				Point2D.Float Q1Q2 = new Point2D.Float((Q1.x - Q2.x)/vectorMagnitudeOfQ1Q2,(Q1.y - Q2.y)/vectorMagnitudeOfQ1Q2);

				Point2D.Float ccwRotation_rotation_i = new Point2D.Float( (float) Math.cos(Math.toRadians(45)),(float) Math.sin(Math.toRadians(45)));
				Point2D.Float ccwRotation_rotation_j = new Point2D.Float((float) -Math.sin(Math.toRadians(45)),(float) Math.cos(Math.toRadians(45)));

				Point2D.Float temp7 = new Point2D.Float((Q1Q2.x * ccwRotation_rotation_i.x), (Q1Q2.x * ccwRotation_rotation_i.y));
				Point2D.Float temp8 = new Point2D.Float((Q1Q2.y * ccwRotation_rotation_j.x), (Q1Q2.y * ccwRotation_rotation_j.y));
				Point2D.Float temp9 = new Point2D.Float((distOfLeg) * (temp7.x + temp8.x), (distOfLeg) *  (temp7.y + temp8.y));

				Point2D.Float Q3 = new Point2D.Float((temp9.x + Q2.x), (temp9.y + Q2.y));
				g.drawLine(iX(Q1.x), iY(Q1.y), iX(Q3.x), iY(Q3.y));
				g.drawLine(iX(Q2.x), iY(Q2.y), iX(Q3.x), iY(Q3.y));

				g.setColor(getRandomColor());
				int[] xtriangle = new int[4];
				int[] ytriangle = new int[4];

				xtriangle[0] =  iX(Q1.x);
				xtriangle[1] =  iX(Q3.x);
				xtriangle[2] =  iX(Q2.x);

				ytriangle[0] =  iY(Q1.y);
				ytriangle[1] =  iY(Q3.y);
				ytriangle[2] =  iY(Q2.y);

				g.fillPolygon(xtriangle, ytriangle, 3);

				makePentagon(g, Q3, Q2);
				makePentagon(g, Q1, Q3);

		}

		void initgr()
    {  Dimension d = getSize();
       maxX = d.width - 1;
			 maxY = d.height - 1;
       pixelSize = Math.max(rWidth/maxX, rHeight/maxY);
    }

		public void paint(Graphics g) {
			initgr();
			if (clickCounter == 0)
				g.drawString("Choose your first point.", 10, 10);
			else if (clickCounter == 1) {
				g.drawString("Choose your second point.", 10, 10);
			}

			if(clickCounter == 2 && !ccw){
				g.drawString("NOT CCW. Try Point #2 Again.", 10, 10);
				clickCounter = 1;
			}
			if(clickCounter == 2 && ccw){
				System.out.println("Drawing");
				if(option.equals("recursion")){
					makePentagon(g, p1, p2);
				}else{
					makePentagonQueue(g, p1, p2);
				}
				clickCounter = 0;
			}

		}

	}

}
