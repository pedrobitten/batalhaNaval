package View;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

class Arma {
    Rectangle2D shape;
    int length;
    Color color;
    int[][] offsets; // Deslocamentos para cada parte da arma

    Arma(Rectangle2D shape, int length, Color color, int[][] offsets) {
        this.shape = shape;
        this.length = length;
        this.color = color;
        this.offsets = offsets;
    }
}