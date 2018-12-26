package faceRecognition;

import java.util.ArrayList;

public class MatrixHandler {

    ArrayList<int[]> images;

    public MatrixHandler() {
        this.images = new ArrayList<int[]>();
    }

    public void addImage( int[] image ) {
        this.images.add( image );
    }

}
