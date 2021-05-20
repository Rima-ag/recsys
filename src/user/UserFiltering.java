package user;

import RecommenderSystem.*;

public class UserFiltering extends RecommenderSystem {
    private double userModule;
    private double[][] itemMatrix;
    private double[] itemSimilarity;

    public UserFiltering(Integer userId, Integer movieId) {
        super(userId, movieId);
    }

    @Override
    public void getRatings() {
        readDataSet();
        normalizeMatrix();
        computeSimilarity();
        generateRating();
    }

    @Override
    protected void setMatrixValue(int user, int movie, int rating) {
        itemMatrix[user][movie] = rating;
        if (movie == movieId)
            movieRating[user] = rating;
    }

    @Override
    protected void initMatrix() {
        movieRating = new double[totalUsers];
        itemMatrix = new double[totalUsers][totalMovies];
    }

    private void normalizeMatrix() {
        double totalRatings = 0;
        double sum = 0;
        double avg;

        for (int r = 0; r < totalUsers; ++r) {
            sum = 0;
            totalRatings = 0;
            double[] user = itemMatrix[r];

            for (int movie = 0; movie < totalMovies; ++movie) {
                if (user[movie] != 0) {
                    sum += user[movie];
                    ++totalRatings;
                }
            }
            avg = sum / totalRatings;

            for (int movie = 0; movie < totalMovies; ++movie) {
                if (user[movie] != 0) {
                    user[movie] -= avg;
                }
            }
        }
        userRating = itemMatrix[userId];
    }

    private void computeSimilarity() {
        itemSimilarity = new double[totalUsers];

        for (int r = 0; r < totalUsers; ++r) {
            double dotProduct = 0;
            double normOfSecondItem = 0;
            if (r == userId)
                continue;
            if (movieRating[r] == 0)
                continue;
            double[] user = itemMatrix[r];
            for (int movie = 0; movie < totalMovies; ++movie) {
                if (userRating[movie] != 0 && user[movie] != 0) {
                    dotProduct += userRating[movie] * user[movie];
                    normOfSecondItem += user[movie] * user[movie];
                    userModule += userRating[movie] * userRating[movie];
                }
            }
            itemSimilarity[r] = dotProduct / (Math.sqrt(normOfSecondItem) * Math.sqrt(userModule));
        }
    }

    private void generateRating() {
        double sum = 0;
        double top = 0;
        for (int i = 0; i < itemSimilarity.length; ++i) {
            if (itemSimilarity[i] > 0) {
                top += movieRating[i] * itemSimilarity[i];
                sum += itemSimilarity[i];
            }
        }
        if (sum == 0) {
            System.out.println("No similar items found");
        } else
            System.out.println("Predicted rating for User-User Filtering : " +
                    top / sum);
    }

}
