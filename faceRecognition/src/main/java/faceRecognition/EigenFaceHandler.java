package faceRecognition;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.linear.RealMatrix;

public class EigenFaceHandler {
    // contient les noms des personnes et le svd généré pour chacun via
    // matrixhandler
    Map<String, RealMatrix> eigenfaceCollection;

    public EigenFaceHandler() {
        this.eigenfaceCollection = new HashMap<String, RealMatrix>();
    }

    // sert à ajouter une eigenface à la collection avec le nom de la personne,
    // l'eigenface s'obtient via la classe matrixhandler
    public void addEigenface( String name, RealMatrix eigenface ) {
        this.eigenfaceCollection.put( name, eigenface );
    }

    // pour transposer une matrice
    public double[][] transposeMatrix( double[][] matrice ) {
        double[][] transposed = new double[matrice[0].length][matrice.length];
        int indexImage = 0;
        for ( int index = 0; index < matrice[0].length; index++ ) {
            for ( double[] array : matrice ) {
                transposed[index][indexImage] = array[index];
                indexImage++;
            }
            indexImage = 0;
        }
        return transposed;
    }

    // pour multiplier deux matrices ensemble
    public double[][] multiplicar( double[][] A, double[][] B ) {

        int aRows = A.length;
        int aColumns = A[0].length;
        int bRows = B.length;
        int bColumns = B[0].length;

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
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }

        return C;
    }

    // sert à obtenir les poids qui serviront à la reconnaissance faciale à
    // partir d'une image d'entrée qui aura été traitée et une eigenface deja
    // enregistrée
    public double[][] getWeight( RealMatrix input, RealMatrix eigenface ) {
        double[][] transposedEigenFace = this.transposeMatrix( eigenface.getData() );
        return this.multiplicar( input.getData(), transposedEigenFace );
    }

    // il faut normaliser l'eigenface pour pouvoir l'utiliser sans disparité
    public double[][] normalizeEigenface( double[][] eigenface ) {
        double min = eigenface[0][0];
        double max = eigenface[0][0];
        for ( double[] face : eigenface ) {
            for ( double pixel : face ) {
                if ( pixel > max ) {
                    max = pixel;
                }
                if ( pixel < min ) {
                    min = pixel;
                }
            }
        }
        double[][] normalized = new double[eigenface.length][eigenface[0].length];
        int index = 0;
        int index2 = 0;
        for ( double[] face : eigenface ) {

            for ( double pixel : face ) {
                normalized[index][index2] = 255 * ( pixel - min ) / ( max - min );
                index2++;
            }
            index++;
        }
        return normalized;
    }

    // sert à savoir si les poids de deux images sont eloignés ou non via un
    // calcul de distance euclidien
    public double getDistance( double[][] first, double[][] second, double[][] eigenface ) {
        int pixelCount = first.length * first[0].length;
        int eigenCount = eigenface.length * eigenface[0].length;
        int index = 0;
        int index2 = 0;
        double total = 0;
        for ( double[] row : first ) {

            for ( double pixel : row ) {
                total += Math.pow( ( pixel - second[index][index2] ) / pixelCount, 2 );
                index2++;
            }
            index++;
        }
        double step2 = Math.sqrt( total );
        double step3 = step2 / Math.sqrt( eigenCount );
        return step3;
    }
}
