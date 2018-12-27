package faceRecognition;

import java.util.ArrayList;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.stat.correlation.Covariance;

public class MatrixHandler {

    // Chaque image devra faire la meme taille en terme de pixels
    ArrayList<int[]>    images;
    ArrayList<double[]> reducedImages;
    RealMatrix          covarianceMatrix;
    RealMatrix          svdMatrix;

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
    public double[] calculateMean() {
        double[] mean = new double[this.images.get( 0 ).length];
        for ( int index = 0; index < this.images.get( 0 ).length; index++ ) {
            double pixelMean = 0.00000;
            for ( int[] image : this.images ) {
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
    public void calculateReducedMatrix( double[] mean ) {
        this.reducedImages = new ArrayList<double[]>();
        for ( int[] image : this.images ) {
            this.reducedImages.add( new double[this.images.get( 0 ).length] );
            for ( int index = 0; index < this.images.get( 0 ).length; index++ ) {
                this.reducedImages.get( this.reducedImages.size() - 1 )[index] = image[index] - mean[index];
            }
        }
    }

    // Pour calculer la matrice de covariance il est nécessaire de calculer la
    // matrice transposée de la matrice réduite recupérée grace à
    // calculateReducedMatrix
    public ArrayList<double[]> transposeMatrix() {
        ArrayList<double[]> transposed = new ArrayList<double[]>();
        int indexImage = 0;
        for ( int index = 0; index < this.reducedImages.get( 0 ).length; index++ ) {
            transposed.add( new double[this.reducedImages.size()] );
            for ( double[] image : this.reducedImages ) {
                transposed.get( transposed.size() - 1 )[indexImage] = image[index];
                indexImage++;
            }
            indexImage = 0;
        }
        return transposed;
    }

    // une methode pour multiplier deux matrices entre elles, elle renvoie les
    // arraylist sous forme de tableau de double en 2D pour faciliter la suite
    // du processus
    public double[][] multiplicar( ArrayList<double[]> A, ArrayList<double[]> B ) {

        int aRows = A.size();
        int aColumns = A.get( 0 ).length;
        int bRows = B.size();
        int bColumns = B.get( 0 ).length;

        if ( aColumns != bRows ) {
            throw new IllegalArgumentException( "A:Rows: " + aColumns + " did not match B:Columns " + bRows + "." );
        }

        double[][] C = new double[aRows][bColumns];
        for ( int i = 0; i < aRows; i++ ) {
            for ( int j = 0; j < bColumns; j++ ) {
                C[i][j] = 0.0000;
            }
        }

        for ( int i = 0; i < aRows; i++ ) { // aRow
            for ( int j = 0; j < bColumns; j++ ) { // bColumn
                for ( int k = 0; k < aColumns; k++ ) { // aColumn
                    C[i][j] += A.get( i )[k] * B.get( k )[j];
                }
            }
        }

        return C;
    }

    // On calcule la matrice de covariance via la matrice réduite multipliée par
    // la matrice
    // transposée.
    public void calculateCovariance() {
        ArrayList<double[]> transposed = this.transposeMatrix();
        double[][] multiplied = multiplicar( this.reducedImages, transposed );
        Covariance co = new Covariance( multiplied );
        RealMatrix covariance = co.getCovarianceMatrix();
        this.covarianceMatrix = covariance;
    }

    public void calculateSVD() {
        SingularValueDecomposition svd = new SingularValueDecomposition( this.covarianceMatrix );
        this.svdMatrix = svd.getU();
    }
}
