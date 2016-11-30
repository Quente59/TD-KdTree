package kdtree;

import java.util.ArrayList;

public class KdTree<Point extends PointI>		// généricité contrainte
{
	/** A node in the KdTree
	 */
	public class KdNode 
	{
		KdNode child_left_, child_right_;
		Point pos_;
		int d_; 	/// dimension in which the cut occurs (x or y in 2d)
		
		KdNode(Point p, int d){		// point de départ
			this.pos_ = p;
			this.d_ = d;
			this.child_left_ = null;
			this.child_right_ = null;
		}

		KdNode(Point p, int d, KdNode l_child, KdNode r_child){
			this.pos_ = p;
			this.d_ = d;
			this.child_left_ = l_child;
			this.child_right_ = r_child;
		}
		
		/** 
		 * if strictly negative the query point is in the left tree
		 * TODO: equality is problematic if we want a truly balanced tree
		 */
		int dist1D(Point p) { 
			return p.get(d_) - pos_.get(d_);
		}
		
	}
	
	/////////////////
    /// Attributs ///
    /////////////////

	private final int dim_; /// dimension of space
	private int n_points_; /// number of points in the KdTree
	
	private KdNode root_; /// root node of the KdTree
	
	public Point getPointRoot(){
		return this.root_.pos_;
	}

    //////////////////
    /// Constructor///
    //////////////////

	/** Initialize an empty kd-tree
	 */
	KdTree(int dim) {
		this.dim_ = dim;
		this.root_ = null;
		this.n_points_ = 0;
	}

	/** Initialize the kd-tree from the input point set
	 *  The input dimension should match the one of the points
	 */
	KdTree(int dim, ArrayList<Point> points, int max_depth) {
		this.dim_ = dim;
		
		//balanced initialization
		this.root_ = buildTree(this.dim_,points,0,max_depth);			
		
	}
	
		
	private KdNode buildTree(int dim, ArrayList<Point> points, int depth, int max_depth){			// ne marche pour le moment que pour dim_ = 2
		// TERMINAISON : 
		// si points.size()==0 retourner null (sous-arbre vide)
		
		if(points.size()==0){
			return null;
		}
	
		// Calcul de la dimension de la coupe
		// la dimension de coupe alterne cycliquement entre chaque dimension
		
		int d = depth%dim;
		
		
		// TRAITEMENT SPECIAL pour le problème de la quantization
		// if depth == max_depth créer un noeud feuille comportant le barycentre des points restant
		
		if(depth == max_depth){				
					
			if(dim == 2){
			
				int baryx = 0;
				int baryy = 0;
				for(int i=0;i<points.size();i++){
					baryx += points.get(i).get(0);
					baryy += points.get(i).get(1);
				 
				}
			
				baryx = baryx / points.size();
				baryy = baryy / points.size();
			
				Point barycentre = (Point) new Point2i(baryx,baryy);		
			
				KdNode node = new KdNode(barycentre,d);			// noeud barycentre
				this.n_points_ += 1 ;
				return node;
				
			}
			if(dim == 3){
				
				int baryx = 0;
				int baryy = 0;
				int baryz = 0;
				for(int i=0;i<points.size();i++){
					baryx += points.get(i).get(0);
					baryy += points.get(i).get(1);
					baryz += points.get(i).get(2);
				 
				}
			
				baryx = baryx / points.size();
				baryy = baryy / points.size();
				baryz = baryz / points.size();
				
				Point barycentre = (Point) new Point3i(baryx,baryy,baryz);		
				
				KdNode node = new KdNode(barycentre,d);			// noeud barycentre
				this.n_points_ += 1 ;
				return node;
				
			}
		}

		// Trier le tableau de point en fonction de la dimension choisi
		// (cela permet d’obtenir la médiane et son indice)
		
		ArrayList<Point> aux = new ArrayList<Point>(0);
		for (int i=0;i<points.size();i++){
			aux.add(points.get(i));
		}
		
		
		// code pour trier les points
		ArrayList<Point> pointsSorted = new ArrayList<Point>(0);
		
		while (aux.size() > 0){
			
			int init=0;
			int min = (aux.get(init)).get(d);
			
			int indiceMin = init;
			for(int j=0;j<aux.size();j++){
				
				if ((aux.get(j)).get(d) < min){
					min = (aux.get(j)).get(d);
					
					indiceMin = j;
				}
			}
			pointsSorted.add(aux.get(indiceMin));
			aux.remove(indiceMin);
		}
		
		// Partager le tableau en deux tableaux (indice inférieur et supérieur à médiane)
		// left_points, right_points
		
		ArrayList<Point> left_points = new ArrayList<Point>(0);
		ArrayList<Point> right_points = new ArrayList<Point>(0);
		
		if (pointsSorted.size() == 2){
						
			left_points.add(pointsSorted.get(0));

			// Créer récursivement deux sous arbres
			KdNode left_child = buildTree(dim,left_points,depth+1,max_depth);
			KdNode right_child = buildTree(dim,right_points,depth+1,max_depth);
				 
			// Créer le nouveau noeud de profondeur depth et le retourner
			
			KdNode node = new KdNode(pointsSorted.get(1), d,left_child,right_child);
			
			this.n_points_ += 1 ;
			
			return node;
			
		}	
			
		if (pointsSorted.size() == 1){
			
			// Créer récursivement deux sous arbres
			KdNode left_child = buildTree(dim,left_points,depth+1,max_depth);
			KdNode right_child = buildTree(dim,right_points,depth+1,max_depth);
			
			// Créer le nouveau noeud de profondeur depth et le retourner
			KdNode node = new KdNode(pointsSorted.get(0),d,left_child,right_child);

			this.n_points_ += 1 ;
			
			return node;
		}
					
		int indiceMediane = pointsSorted.size()/2;			// indice du point médiant
		
		for(int i = 0;i<indiceMediane;i++){
			left_points.add(pointsSorted.get(i));
		}
		
		for(int i = indiceMediane+1;i<pointsSorted.size();i++){
			right_points.add(pointsSorted.get(i));
		}

		// Créer récursivement deux sous arbres
		KdNode left_child = buildTree(dim,left_points,depth+1,max_depth);
		KdNode right_child = buildTree(dim,right_points,depth+1,max_depth);
		// Créer le nouveau noeud de profondeur depth et le retourner
		
		KdNode node = new KdNode(pointsSorted.get(indiceMediane),d,left_child,right_child);
		
		this.n_points_ += 1 ;
		
		return node;
			
		
	}
	  
	/////////////////
	/// Accessors ///
	/////////////////

	int dimension() { return dim_; }

	int nb_points() { return n_points_; }

	void getPointsFromLeaf(ArrayList<Point> points) {
		getPointsFromLeaf(root_, points);
	}

	 
	///////////////
	/// Mutator ///
	///////////////

	/** Insert a new point in the KdTree.
	 */
	void insert(Point p) {
		n_points_ += 1;
		
		if(root_==null) 
			root_ = new KdNode(p, 0);
		
		KdNode node = getParent(p);
		if(node.dist1D(p)<0) {
			assert(node.child_left_==null);
			node.child_left_ = new KdNode(p, (node.d_+1)%dim_);
		} else {
			assert(node.child_right_==null);
			node.child_right_ = new KdNode(p, (node.d_+1)%dim_);
		}
	}
	void delete(Point p) {
		assert(false);
	}

	///////////////////////
	/// Query Functions ///
	///////////////////////

	/** Return the node that would be the parent of p if it has to be inserted in the tree
	 */
	KdNode getParent(Point p) {
		assert(p!=null);
		
		KdNode next = root_, node = null;

		while (next != null) {
			node = next;
			if ( node.dist1D(p) < 0 ){
				next = node.child_left_;
			} else {
				next = node.child_right_;
			}
		}
		
		return node;
	}
	
	/** Check if p is a point registered in the tree
	 */
	boolean contains(Point p) {
        return contains(root_, p);
	}

	/** Get the nearest neighbor of point p
	 */
    public Point getNN(Point p)
    {
    	assert(root_!=null);
        return getNN(root_, p, root_.pos_);
    }

	///////////////////////
	/// Helper Function ///
	///////////////////////

    /** Add the points in the leaf nodes of the subrre defined by root 'node'
     * to the array 'point'
     */
	private void getPointsFromLeaf(KdNode node, ArrayList<Point> points)
	{
		if(node.child_left_==null && node.child_right_==null) {
			points.add(node.pos_);
		} else {
		    if(node.child_left_!=null)
		    	getPointsFromLeaf(node.child_left_, points);
		    if(node.child_right_!=null)
		    	getPointsFromLeaf(node.child_right_, points);
		}
	 }
	
	/** Search for a better solution than the candidate in the subtree with root 'node'
	 *  if no better solution is found, return candidate
	 */
	 private Point getNN(KdNode node, Point point, Point candidate)
	 {
	    if ( point.sqrDist(node.pos_) <  point.sqrDist(candidate)) 
	    	candidate = node.pos_;

	    int dist_1D = node.dist1D(point);
	    KdNode n1, n2;
	    if( dist_1D < 0 ) {
	    	n1 = node.child_left_;
	    
	    	n2 = node.child_right_;
	    } else {
	    	// start by the right node
	    	n1 = node.child_right_;
	    	n2 = node.child_left_;
	    }
	    
	    if(n1!=null)
	    	candidate = getNN(n1, point, candidate);

	    if(n2!=null && dist_1D*dist_1D < point.sqrDist(candidate)) 
	    	candidate = getNN(n2, point, candidate);
		 
		 return candidate;
	 }
	 
	private boolean contains(KdNode node, Point p) {
        if (node == null) return false;
        if (p.equals(node.pos_)) return true;

        //TODO : assume the "property" is strictly verified
        if (node.dist1D(p)<0)
            return contains(node.child_left_, p);
        else
            return contains(node.child_right_, p);
	}
	
}


