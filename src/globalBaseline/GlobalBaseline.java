package globalBaseline;

import recommenderSystem.RecommenderSystem;

public class GlobalBaseline extends RecommenderSystem {
    private int sumOfRatings = 0;
    private int totalNumberOfRatings = 0;
    private double userToBaseline;
    private double movieToBaseline;
    private double globalMean;

    public GlobalBaseline(int userId, int movieId) {
        super(userId, movieId);
    }

    @Override
    public void printRatings() {
        readDataSet();
        computeMean();
        predict();
    }

    @Override
    protected void setMatrixValue(int user, int movie, int rating) {
        if (user == userId)
            userRating[movie] = rating;
        if (movie == movieId)
            movieRating[user] = rating;
        if (rating != 0) {
            sumOfRatings += rating;
            ++totalNumberOfRatings;
        }
    }

    @Override
    protected void initMatrix() {
        userRating = new double[totalMovies];
        movieRating = new double[totalUsers];
    }

    private void computeMean() {
        globalMean = sumOfRatings / (double) totalNumberOfRatings;

        int count = 0;
        for (int i = 0; i < totalUsers; ++i) {
            if (movieRating[i] != 0) {
                movieToBaseline += movieRating[i];
                ++count;
            }
        }
        movieToBaseline = (movieToBaseline / count) - globalMean;

        count = 0;
        for (int i = 0; i < totalMovies; ++i) {
            if (userRating[i] != 0) {
                userToBaseline += userRating[i];
                ++count;
            }
        }
        userToBaseline = (userToBaseline / count) - globalMean;

    }

    private void predict() {
        System.out.println("Predicted rating for Global BaseLine: " + (globalMean + userToBaseline + movieToBaseline));
    }
}

