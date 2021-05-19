package user;

import java.io.File;
import java.util.Scanner;

public class UserFiltering {

    private String filename = "data";

    private int totalUsers = 5;
    private int totalMovies = 5;

    private int userId;
    private double userModule;
    private double[] userRating;

    private int movieId;
    private double[] movieRating;

    private double[][] itemMatrix;

    private double[] itemSimilarity;

    public UserFiltering(Integer userId, Integer movieId){

        this.userId = userId;
        this.movieId = movieId;

        fillMatrix();
        normalizeMatrix();
        computeSimilarity();
        generateRating();
    }

    private void initMatrix(){
        movieRating = new double[totalUsers];
        itemMatrix = new double[totalUsers][totalMovies];
    }

    private void fillMatrix(){
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
                itemMatrix[user][movie]= rating;
                if(movie == movieId)
                    movieRating[user] = rating;
                ++lineNumber;
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void normalizeMatrix(){
        double totalRatings = 0;
        double sum = 0;
        double avg;

        for(int r = 0; r < totalUsers; ++r){
            sum = 0;
            totalRatings = 0;
            double[] user = itemMatrix[r];

            for(int movie = 0; movie < totalMovies; ++movie){
                if(user[movie]!= 0){
                    sum += user[movie];
                    ++totalRatings;
                }
            }
            avg = sum / totalRatings;

            for(int movie = 0; movie < totalMovies; ++movie){
                if(user[movie]!= 0){
                    user[movie] -= avg;
                }
            }
        }
        userRating = itemMatrix[userId];
    }

    private void computeSimilarity(){
        itemSimilarity = new double[totalUsers];

        for(int r = 0; r < totalUsers; ++r){
            double dotProduct = 0;
            double normOfSecondItem = 0;
            if(r == userId)
                continue;
            if(movieRating[r] == 0)
                continue;
            double[] user = itemMatrix[r];
            for(int movie = 0; movie < totalMovies; ++movie){
                if(userRating[movie] != 0 && user[movie] != 0){
                    dotProduct += userRating[movie] * user[movie];
                    normOfSecondItem += user[movie] * user[movie];
                    userModule += userRating[movie] * userRating[movie];
                }
            }
            itemSimilarity[r] = dotProduct / (Math.sqrt(normOfSecondItem) * Math.sqrt(userModule));
        }
    }

    private void generateRating(){
        double sum = 0;
        double top = 0;
        for(int i = 0; i < itemSimilarity.length; ++i){
            if(itemSimilarity[i] > 0){
                top += movieRating[i] * itemSimilarity[i];
                sum += itemSimilarity[i];
            }
        }
        if(sum == 0){
            System.out.println("No similar items found");
        }else
        System.out.println("Predicted rating : " +
                top/sum);
    }


    public static void main(String[] args){
        UserFiltering filter = new UserFiltering(11,2);
    }

}
