package svd;

import RecommenderSystem.RecommenderSystem;
import org.apache.commons.math3.linear.*;

import java.io.File;
import java.util.Scanner;

public class SVD extends RecommenderSystem {
    private double[][] movieUserMatrix;
    private double[] userRating;
    private RealMatrix sigma;
    private RealMatrix V;
    private RealMatrix U;
    private RealMatrix qConcept;

    public SVD(double[] ratings) {
        this.userRating = ratings;
        this.fillMatrix();
        this.generateRating();
    }

    public void generateRating() {
        RealMatrix A = new Array2DRowRealMatrix(movieUserMatrix);
        SingularValueDecomposition svd = new SingularValueDecomposition(A);
        sigma = svd.getS();
        U = svd.getU();
        V = svd.getV();
        this.setDimension(svd.getSingularValues());
        RealMatrix q = new Array2DRowRealMatrix(userRating).transpose();
        qConcept = q.multiply(V); //pas terminer
    }

    private void fillArray(double[] array){
        for (int i = 0; i < array.length; ++i) {
            array[i] = 0;
        }
    }

    private void setDimension(double[] values) {
        double total = 0;
        double power = 0;
        double [] emptyColumn = new double[V.getColumnDimension()];
        double [] emptyRow = new double[V.getRowDimension()];
        this.fillArray(emptyRow);
        this.fillArray(emptyColumn);
        for (int i = 0; i < values.length; ++i) {
            total += values[i] * values[i];
        }
        for (int i = 0; i < values.length; ++i) {
            if (power / total > 0.9) {
                sigma.setEntry(i, i, 0);
                V.setColumn(i, emptyColumn);
                U.setRow(i, emptyRow);
            }
            power += values[i] * values[i];
        }
    }

    private void initMatrix() {
        movieUserMatrix = new double[totalUsers][totalMovies];
    }

    private void fillMatrix() {
        try {
            Integer lineNumber = 1;
            Scanner sc = new Scanner(new File(filename));
            while (sc.hasNextLine()) {
                if (lineNumber == 1) {
                    sc.nextLine();
                    ++lineNumber;
                    continue;
                } else if (lineNumber == 2) {
                    String[] line = sc.nextLine().split("-");
                    totalUsers = Integer.parseInt(line[0]);
                    totalMovies = Integer.parseInt(line[1]);
                    initMatrix();
                    ++lineNumber;
                    continue;
                }
                String[] line = sc.nextLine().split(",");
                if (line.length != 3)
                    throw new Exception("Wrong format");
                Integer user = Integer.parseInt(line[0]);
                Integer movie = Integer.parseInt(line[1]);
                Integer rating = Integer.parseInt(line[2]);
                movieUserMatrix[user][movie] = rating;
                ++lineNumber;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        double[] ratings = new double[6];
        ratings[0] = 5.0;
        ratings[1] = 0;
        ratings[2] = 0;
        ratings[3] = 0;
        ratings[4] = 0;
        ratings[5] = 0;
        SVD s = new SVD(ratings);
    }
}
