package kdtree;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.Test;

public class KdTreeTest {

	ArrayList<Point2i> grid() {															
		ArrayList<Point2i> v_tree_points = new ArrayList<Point2i>();
		for(int i=0; i<100; ++i) {
			for(int j=0; j<100; ++j) {
				v_tree_points.add(new Point2i(10*i,10*j));
			}
		}
		return v_tree_points;								// futurs points de l'arbre
	}
	
	@Test
	public void testNearestNeighbor() {

		// Init vector of points

		ArrayList<Point2i> v_tree_points = grid();
		System.out.println("v_tree_points=" + v_tree_points);
		
		// Create kd-tree
		
		KdTree<Point2i> tree = new KdTree<Point2i>(2, v_tree_points,Integer.MAX_VALUE);		// arbre et ses points
		
		// Create a vector of query Point
		ArrayList<Point2i> v_query_points = new ArrayList<Point2i>();		// points à tester
		v_query_points.add(new Point2i(15,15));
		v_query_points.add(new Point2i(100,100));
		v_query_points.add(new Point2i(200,200));
		v_query_points.add(new Point2i(25,150));
		v_query_points.add(new Point2i(25,55));
		v_query_points.add(new Point2i(33,25));
		
		// Compare result of linear search with kdtree search

		for (Point2i p : v_query_points) {

			//TODO: move that in linear search class
			float l_min = Float.MAX_VALUE;
	        Point2i point = new Point2i(100,100);
			for (Point2i pi: v_tree_points) {
	        	float sqr_dist = pi.sqrDist(p);
	        	
	        	if(sqr_dist < l_min) {
	        		l_min = sqr_dist;
	        		point = pi;
	        	}
	        }
			
			/*System.out.println("linear_point=" + point);
			System.out.println("x=" + point.get(0));
			System.out.println("y=" + point.get(1));*/
			
	        Point2i np = tree.getNN(p);
	        float t_min = p.sqrDist(np);
	       
	       /* System.out.println("tree_point=" + np);
			System.out.println("x=" + np.get(0));
			System.out.println("y=" + np.get(1));
		    
			System.out.println("t=" + t_min);
			System.out.println("l=" + l_min);*/
			
			assertTrue(t_min==l_min);
		   
		}
	}
	
	@Test
	public void testNumberOfPoints() {

		// Init vector of points

		ArrayList<Point2i> v_tree_points = new ArrayList<Point2i>();
		
		v_tree_points.add(new Point2i(0,0));
		v_tree_points.add(new Point2i(60,60));
		v_tree_points.add(new Point2i(100,100));
		v_tree_points.add(new Point2i(200,25));
		v_tree_points.add(new Point2i(25,250));
			
		// Create kd-tree
		
		KdTree<Point2i> tree = new KdTree<Point2i>(2, v_tree_points,Integer.MAX_VALUE);		
		
		assertTrue(tree.nb_points() == 5);
		assertFalse(tree.nb_points() !=5);
		
	}
	
	
	
	
	@Test
	public void testNearestNeighbor3D() {

		// Init vector of points

		ArrayList<Point3i> v_tree_points = new ArrayList<Point3i>();
		
		v_tree_points.add(new Point3i(0,0,0));
		v_tree_points.add(new Point3i(60,60,60));
		v_tree_points.add(new Point3i(100,100,100));
		v_tree_points.add(new Point3i(200,25,150));
		v_tree_points.add(new Point3i(25,250,80));
		
		// Create kd-tree
		
		KdTree<Point3i> tree = new KdTree<Point3i>(2, v_tree_points,Integer.MAX_VALUE);		// arbre et ses points
		
		// Create a vector of query Point
		ArrayList<Point3i> v_query_points = new ArrayList<Point3i>();		// points à tester
		v_query_points.add(new Point3i(15,15,15));
		v_query_points.add(new Point3i(100,100,100));
		v_query_points.add(new Point3i(200,200,200));
		v_query_points.add(new Point3i(25,150,95));
		v_query_points.add(new Point3i(25,55,170));
		v_query_points.add(new Point3i(33,25,10));
		
		// Compare result of linear search with kdtree search

		for (Point3i p : v_query_points) {

			//TODO: move that in linear search class
			float l_min = Float.MAX_VALUE;
	        Point3i point = new Point3i(0,0,0);
			for (Point3i pi: v_tree_points) {
	        	float sqr_dist = pi.sqrDist(p);
	        	
	        	if(sqr_dist < l_min) {
	        		l_min = sqr_dist;
	        		point = pi;
	        	}
	        }
			
	        Point3i np = tree.getNN(p);
	        float t_min = p.sqrDist(np);
	       
	        assertTrue(t_min==l_min);
	        
		   
		}
	}
	
	@Test
	public void testZeroPoint() {

		// Init vector of points

		ArrayList<Point2i> v_tree_points = new ArrayList<Point2i>();
		
		// Create kd-tree
		
		KdTree<Point2i> tree = new KdTree<Point2i>(2, v_tree_points,Integer.MAX_VALUE);		
		
		assertTrue(tree.nb_points() == 0);
		
	}
	
	@Test
	public void testMaxDepth() {
		
		
		// Init vector of points

		ArrayList<Point3i> v_tree_points = new ArrayList<Point3i>();
		
		v_tree_points.add(new Point3i(0,0,0));
		v_tree_points.add(new Point3i(60,60,60));
		v_tree_points.add(new Point3i(100,100,100));
		v_tree_points.add(new Point3i(200,25,150));
		
		// Create kd-tree
		
		KdTree<Point3i> tree = new KdTree<Point3i>(3, v_tree_points,0);		// arbre et ses points
		
		
		Point3i point = (Point3i)tree.getPointRoot();
		
		
		Point3i barycentreTree = new Point3i(point.get(0),point.get(1),point.get(2));
		
		
		int baryx = 0;
		int baryy = 0;
		int baryz = 0;
		
		for (int i=0;i<v_tree_points.size();i++){
			
			baryx += v_tree_points.get(i).get(0);
			baryy += v_tree_points.get(i).get(1);
			baryz += v_tree_points.get(i).get(2);
			 
			}
		baryx = baryx / v_tree_points.size();
		baryy = baryy / v_tree_points.size();
		baryz = baryz / v_tree_points.size();
		
		Point3i barycentreTheo = new Point3i(baryx,baryy,baryz);
		
		assertTrue(barycentreTree.get(0) == barycentreTheo.get(0));
		assertTrue(barycentreTree.get(1) == barycentreTheo.get(1));
		assertTrue(barycentreTree.get(2) == barycentreTheo.get(2));
		
		
	}
	
}
