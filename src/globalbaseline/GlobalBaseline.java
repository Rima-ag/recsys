package globalbaseline;

import java.io.File;
import java.util.Scanner;

public class GlobalBaseline {

    private int totalMovies;
    private int totalUsers;
    private int movieId;
    private int userId;
    private double[] userRating;
    private double[] movieRating;

    private int sumOfRatings = 0;
    private int totalNumberOfRatings = 0;

    private double userToBaseline;
    private double movieToBaseline;
    private double globalMean;

    public GlobalBaseline(int userId, int movieId){
        this.userId = userId;
        this.movieId = movieId;

        fillRatings();
        computeMean();
        predict();
    }

    private void initMatrix(){
        userRating = new double[totalMovies];
        movieRating = new double[totalUsers];
    }

    private void fillRatings(){
        try {

            Integer lineNumber = 1;
            Scanner sc = new Scanner(new File("data"));
            while (sc.hasNextLine()) {
                if(lineNumber == 1) {
                    sc.nextLine();
                    ++lineNumber;
                    continue;
                }else if(lineNumber == 2) {
                    String[] line = sc.nextLine().split("-");
                    totalUsers = Integer.parseInt(line[0]);
                    totalMovies = Integer.parseInt(line[1]);

                    initMatrix();
                    ++lineNumber;
                    continue;
                }
                String[] line = sc.nextLine().split(",");
                if(line.length != 3)
                    throw new Exception("Wrong format");
                Integer user = Integer.parseInt(line[0]);
                Integer movie = Integer.parseInt(line[1]);
                Integer rating = Integer.parseInt(line[2]);

                if(user == userId)
                    userRating[movie] = rating;
                if(movie == movieId)
                    movieRating[user] = rating;
                if(rating != 0) {
                    sumOfRatings += rating;
                    ++totalNumberOfRatings;
                }
                ++lineNumber;
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void computeMean(){
        globalMean = sumOfRatings / (double) totalNumberOfRatings;

        int count = 0;
        for(int i = 0; i < totalUsers; ++i){
            if(movieRating[i] != 0){
                movieToBaseline += movieRating[i];
                ++count;
            }
        }
        movieToBaseline = (movieToBaseline / count) - globalMean;


        count = 0;
        for(int i = 0; i < totalMovies; ++i){
            if(userRating[i] != 0){
                userToBaseline += userRating[i];
                ++count;
            }
        }
        userToBaseline = (userToBaseline / count) - globalMean;

    }

    private void predict(){
        System.out.println("Prediction: " + (globalMean + userToBaseline + movieToBaseline));
    }

    public static void main(String[] args){
        GlobalBaseline filter = new GlobalBaseline(2,2);
    }

}
