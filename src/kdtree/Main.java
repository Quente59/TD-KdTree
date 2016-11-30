package kdtree;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Main 
{
	public static void main(String[] args)
    {
        System.out.println("Entrer le nom de l'image à charger :");
        String filename = new Scanner(System.in).nextLine();
        
        try{
            File pathToFile = new File(filename);
            BufferedImage img = ImageIO.read(pathToFile);

            int imgHeight = img.getHeight();
            int imgWidth  = img.getWidth();
            BufferedImage res_img = new BufferedImage(imgWidth, imgHeight, img.getType());
            //BufferedImage id_img = new BufferedImage(imgWidth, imgHeight, img.getType());

/////////////////////////////////////////////////////////////////
//Creation de la palette
/////////////////////////////////////////////////////////////////
            
            ArrayList<Point3i> pxColors = new ArrayList<Point3i>(imgHeight*imgWidth);
            
            for (int y = 0; y < imgHeight; y++) {
                for (int x = 0; x < imgWidth; x++) {
                    int Color = img.getRGB(x,y);
                    int R = (Color >> 16) & 0xff;
                    int G = (Color >> 8) & 0xff;
                    int B = Color & 0xff;
                    
                    Point3i colorPoint = new Point3i(R,G,B);
                    pxColors.add(colorPoint);		// Creation d'un tableau comprenant les couleurs de chaque pixel de l'image
                    
                    /*int resR = R, resG = G, resB = B;

                    int cRes = 0xff000000 | (resR << 16)
                                          | (resG << 8)
                                          | resB;
                    res_img.setRGB(x,y,cRes);*/
                    
                }
            }
            int max_depth = 4;		//permet de choisir le nombre de couleur (2^max_depth = nb de couleur de la futur nouvelle image)
           
            //génération d'un arbre qui comportera les couleurs de notre palette sur ses feuilles
            // la max_depth donne le nombre de couleurs présentes dans la palette : 2^max_depth=16 dans notre cas
            KdTree<Point3i> tree = new KdTree<Point3i>(3,pxColors,max_depth);	
            
            ArrayList<Point3i> palette_colors = new ArrayList<Point3i>(1<<max_depth);
            tree.getPointsFromLeaf(palette_colors);
            
/////////////////////////////////////////////////////////////////
//Choix de la couleur à utiliser pour chaque pixel de l'image d'origine    
/////////////////////////////////////////////////////////////////

            //creation de points contenant également la position dans la palette
            ArrayList<RefColor> tmp_palette = new ArrayList<RefColor>(1<<max_depth);
            int i=0;
            for(Point3i p : palette_colors){
            	tmp_palette.add(new RefColor(p,i));
            	++i;
            }
            
            //construction d'un arbre avec comme noeuds les couleurs de la palette et ses identifiants
            KdTree<RefColor> paletteTree = new KdTree<RefColor>(3,tmp_palette,Integer.MAX_VALUE);
            
            //utilisation de paletteTree pour remplir un tableau contenant l'indice de couleur à utiliser pour chaque pixel
            //utilisation de la fonction getNN() pour chaque pixel de l'image
            int v_id[] = new int[imgHeight*imgWidth];
            
            ArrayList<RefColor> pxColorsId = new ArrayList<RefColor>(imgHeight*imgWidth);

            
            //ajout d'un id -1 (valeur sans importance) dans le seul but de travailler avec les mêmes objets pour la fonction getNN()
            for(Point3i p : pxColors){
            	pxColorsId.add(new RefColor(p,-1));
            }
           
            for(int j=0;j<imgHeight*imgWidth;j++){
            	v_id[j] = (paletteTree.getNN(pxColorsId.get(j))).getId();		//remplissage du tableau d'indice de couleur
            }
            
/////////////////////////////////////////////////////////////////
//Sauvegarde des résultats sous forme visualisable 
/////////////////////////////////////////////////////////////////  
            
            for (int y = 0; y < imgHeight; y++) {
                for (int x = 0; x < imgWidth; x++) {

                    int id = v_id[y*imgWidth+x]; // identifiant de la nouvelle couleur pour le pixel concerné
                    Point3i color = palette_colors.get(id);  // obtention de la couleur correspondante à l'id

                    // sauvegarde dans une image

                    int R = color.get(0);
                    int G = color.get(1);
                    int B = color.get(2);

                    int cRes = 0xff000000 | (R << 16)
                                          | (G << 8)
                                          | B;
                    res_img.setRGB(x,y,cRes);
                }
            }
            
/////////////////////////////////////////////////////////////////
//Sauvegarde des couleurs de la palette dans une image (by Cedric Zanni)
/////////////////////////////////////////////////////////////////             
            
         // Save the palette as an image (each color of the palette is represented by a block
         // of 8x8 pixels
         BufferedImage palette_img = new BufferedImage(palette_colors.size()*80, 80, img.getType());
         for (int j = 0; j < palette_colors.size(); j++) {
             int R = palette_colors.get(j).get(0);
             int G = palette_colors.get(j).get(1);
             int B = palette_colors.get(j).get(2);
             int cRes = 0xff000000 | (R << 16)
                                   | (G << 8)
                                   | B;

             for (int y = 0; y < 80; y++) {
                 for (int x = 0; x < 80; x++) {
                     palette_img.setRGB(x+j*80,y,cRes);
                  }
             }
         }
            
/////////////////////////////////////////////////////////////////
//Ecriture des images
/////////////////////////////////////////////////////////////////     
        ImageIO.write(palette_img, "jpg", new File("PaletteColor.jpg"));
        //ImageIO.write(id_img, "jpg", new File("ResId.jpg"));
        ImageIO.write(res_img, "jpg", new File("ResColor.jpg"));
/////////////////////////////////////////////////////////////////
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
      
    }
}
