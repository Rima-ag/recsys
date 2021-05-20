package item;

import RecommenderSystem.*;

public class ItemFiltering extends RecommenderSystem {
    private double movieModule;
    private double[][] itemMatrix;
    private double[] itemSimilarity;

    public ItemFiltering(int userId, int movieId) {
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
        itemMatrix[movie][user] = rating;
        if (user == userId)
            userRating[movie] = rating;
    }

    @Override
    protected void initMatrix() {
        userRating = new double[totalMovies];
        itemMatrix = new double[totalMovies][totalUsers];
    }

    private void normalizeMatrix() {
        double totalRatings = 0;
        double sum = 0;
        double avg;

        for (int r = 0; r < totalMovies; ++r) {
            sum = 0;
            totalRatings = 0;
            double[] movie = itemMatrix[r];

            for (int user = 0; user < totalUsers; ++user) {
                if (movie[user] != 0) {
                    sum += movie[user];
                    ++totalRatings;
                }
            }
            avg = sum / totalRatings;

            for (int user = 0; user < totalUsers; ++user) {
                if (movie[user] != 0) {
                    movie[user] -= avg;
                }
            }
        }
        movieRating = itemMatrix[movieId];
    }

    private void computeSimilarity() {
        itemSimilarity = new double[totalMovies];

        for (int r = 0; r < totalMovies; ++r) {
            double dotProduct = 0;
            double normOfSecondItem = 0;
            if (r == movieId)
                continue;
            if (userRating[r] == 0)
                continue;
            double[] movie = itemMatrix[r];
            for (int user = 0; user < totalUsers; ++user) {
                if (movieRating[user] != 0 && movie[user] != 0) {
                    dotProduct += movieRating[user] * movie[user];
                    normOfSecondItem += movie[user] * movie[user];
                    movieModule += movieRating[user] * movieRating[user];
                }
            }
            itemSimilarity[r] = dotProduct / (Math.sqrt(normOfSecondItem) * Math.sqrt(movieModule));
        }
    }

    private void generateRating() {
        double sum = 0;
        double top = 0;
        for (int i = 0; i < itemSimilarity.length; ++i) {
            if (itemSimilarity[i] > 0) {
                top += userRating[i] * itemSimilarity[i];
                sum += itemSimilarity[i];
            }
        }
        if (sum == 0) {
            System.out.println("No similar items found");
        } else
            System.out.println("Predicted rating for Item-Item Filtering : " +
                    top / sum);
    }

}
