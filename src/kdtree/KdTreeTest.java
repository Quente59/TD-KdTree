package kdtree;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.Test;

public class KdTreeTest {

	ArrayList<Point2i> grid() {															
		ArrayList<Point2i> v_tree_points = new ArrayList<Point2i>();
		for(int i=0; i<10; ++i) {
			for(int j=0; j<10; ++j) {
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
		v_query_points.add(new Point2i(29,11));
		//v_query_points.add(new Point2i(100,100));
		/*v_query_points.add(new Point2i(200,200));
		v_query_points.add(new Point2i(25,150));
		v_query_points.add(new Point2i(25,55));
		v_query_points.add(new Point2i(33,25));*/
		
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
			
			System.out.println("linear_point=" + point);
			System.out.println("x=" + point.get(0));
			System.out.println("y=" + point.get(1));
			
	        Point2i np = tree.getNN(p);
	        float t_min = p.sqrDist(np);
	       
	        System.out.println("tree_point=" + np);
			System.out.println("x=" + np.get(0));
			System.out.println("y=" + np.get(1));
		    
			System.out.println(t_min);
			System.out.println(l_min);
			
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
	public void testZeroPoint() {

		// Init vector of points

		ArrayList<Point2i> v_tree_points = new ArrayList<Point2i>();
		
		// Create kd-tree
		
		KdTree<Point2i> tree = new KdTree<Point2i>(2, v_tree_points,Integer.MAX_VALUE);		
		
		assertTrue(tree.nb_points() == 0);
		
	}
	
}
