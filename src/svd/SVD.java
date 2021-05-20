package svd;

import recommenderSystem.RecommenderSystem;
import org.apache.commons.math3.linear.*;

public class SVD extends RecommenderSystem {
    private double[][] movieUserMatrix;
    private RealMatrix sigma;
    private RealMatrix V;
    private RealMatrix U;
    private RealMatrix qConcept;

    public SVD(double[] ratings) {
        this.userRating = ratings;
    }

    @Override
    public void printRatings() {
        this.readDataSet();
        this.generateRating();
        //ratings should be printed when work is done here
    }

    @Override
    protected void setMatrixValue(int user, int movie, int rating) {
        movieUserMatrix[user][movie] = rating;
    }

    @Override
    protected void initMatrix() {
        movieUserMatrix = new double[totalUsers][totalMovies];
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

    private void fillArray(double[] array) {
        for (int i = 0; i < array.length; ++i) {
            array[i] = 0;
        }
    }

    private void setDimension(double[] values) {
        double total = 0;
        double power = 0;
        double[] emptyColumn = new double[V.getColumnDimension()];
        double[] emptyRow = new double[V.getRowDimension()];
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
}
