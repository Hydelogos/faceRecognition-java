package faceRecognition;

import java.util.ArrayList;

public class MatrixHandler {

    // Chaque image devra faire la meme taille en terme de pixels
    ArrayList<int[]>   images;
    ArrayList<float[]> reducedImages;

    public MatrixHandler() {
        this.images = new ArrayList<int[]>();
    }

    // Nous devons une image en vecteur afin de la manipuler librement
    // Cela signifie transformer tous les arrays de pixels contenus dans l'image
    // En un seul array contenant chaque pixel de l'image (comme si elle ne
    // faisait qu'un pixel de haut)
    public void imageToVector( ArrayList<int[]> image ) {
        int index = 0;
        int[] vector = new int[image.size() * image.get( 0 ).length];
        for ( int[] loop : image ) {
            for ( int pixel : loop ) {
                vector[index] = pixel;
                index++;
            }
        }
        // On appelle directement la méthode pour enregistrer le vecteur dans la
        // liste de vecteurs qui serviront aux calculs
        this.addVector( vector );
    }

    // Enregistre simplement un vecteur d'image dans la liste des vecteurs qui
    // serviront aux calculs
    public void addVector( int[] image ) {
        this.images.add( image );
    }

    // On calcule la moyenne des pixels de chaque image selon leur emplacement
    // dans le tableau. Par exemple (image1Pixel1 + image2Pixel1)/nombreImage
    // Puis on enregistre chaque moyenne dans un tableau faisant la taille exact
    // d'un vecteur d'image
    public float[] calculateMean() {
        float[] mean = new float[this.images.get( 0 ).length];
        for ( int index = 0; index < this.images.get( 0 ).length; index++ ) {
            float pixelMean = 0;
            for ( int[] image : images ) {
                pixelMean += image[index];
            }
            pixelMean /= this.images.get( 0 ).length;
            mean[index] = pixelMean;
        }
        return mean;
    }

    // On recentre chaque pixel selon l'origine en lui déduisant la moyenne
    // calculée precedemment, chaque pixel devra etre deduit de la moyenne a la
    // meme position dans le tableau
    public void calculateReducedMatrix( float[] mean ) {
        this.reducedImages = new ArrayList<float[]>();
        for ( int[] image : images ) {
            this.reducedImages.add( new float[this.images.get( 0 ).length] );
            for ( int index = 0; index < this.images.get( 0 ).length; index++ ) {
                this.reducedImages.get( this.reducedImages.size() - 1 )[index] = image[index] - mean[index];
            }
        }
    }

    // Pour calculer la matrice de covariance il est nécessaire de calculer la
    // matrice transposée de la matrice réduite recupérée grace à
    // calculateReducedMatrix
    public void transposeMatrix() {

    }

    // On calcule la matrice de covariance via la matrice réduite et la matrice
    // transposée
    public void calculateCovariance() {

    }
}
