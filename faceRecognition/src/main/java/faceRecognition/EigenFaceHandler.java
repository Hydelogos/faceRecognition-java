package faceRecognition;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.linear.RealMatrix;

public class EigenFaceHandler {
    // contient les noms des personnes et le svd g�n�r� pour chacun via
    // matrixhandler
    Map<String, RealMatrix> eigenfaceCollection;

    public EigenFaceHandler() {
        this.eigenfaceCollection = new HashMap<String, RealMatrix>();
    }

    // sert � ajouter une eigenface � la collection avec le nom de la personne,
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

    // sert � obtenir les poids qui serviront � la reconnaissance faciale �
    // partir d'une image d'entr�e qui aura �t� trait�e et une eigenface deja
    // enregistr�e
    public double[][] getWeight( RealMatrix input, RealMatrix eigenface ) {
        double[][] transposedEigenFace = this.transposeMatrix( eigenface.getData() );
        return this.multiplicar( input.getData(), transposedEigenFace );
    }

    // sert � savoir si les poids de deux images sont eloign�s ou non via un
    // calcul de distance euclidien
    public double getDistance( double[][] first, double[][] second ) {
        // retenir qu'une image ne fait qu'un seul array et donc un array
        // d'eigenface est equivalent � un seul systeme? verifier ca demain lors
        // des finitions
        return 0.0000;
    }
}
