package item;

import java.io.File;
import java.util.Scanner;

public class ItemFiltering {

    private String filename = "data";

    private int totalUsers = 5;
    private int totalMovies = 5;

    private int userId;
    private double[] userRating;

    private int movieId;
    private double movieModule;
    private double[] movieRating;

    private double[][] itemMatrix;

    private double[] itemSimilarity;

    public ItemFiltering(Integer userId, Integer movieId){

        this.userId = userId;
        this.movieId = movieId;

        fillMatrix();
        normalizeMatrix();
        computeSimilarity();
        generateRating();
    }

    private void initMatrix(){
        userRating = new double[totalMovies];
        itemMatrix = new double[totalMovies][totalUsers];
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
                itemMatrix[movie][user]= rating;
                if(user == userId)
                    userRating[movie] = rating;
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

        for(int r = 0; r < totalMovies; ++r){
            sum = 0;
            totalRatings = 0;
            double[] movie = itemMatrix[r];

            for(int user = 0; user < totalUsers; ++user){
                if(movie[user]!= 0){
                    sum += movie[user];
                    ++totalRatings;
                }
            }
            avg = sum / totalRatings;

            for(int user = 0; user < totalUsers; ++user){
                if(movie[user]!= 0){
                    movie[user] -= avg;
                }
            }
        }
        movieRating = itemMatrix[movieId];
    }

    private void computeSimilarity(){
        itemSimilarity = new double[totalMovies];

        for(int r = 0; r < totalMovies; ++r){
            double dotProduct = 0;
            double normOfSecondItem = 0;
            if(r == movieId)
                continue;
            if(userRating[r] == 0)
                continue;
            double[] movie = itemMatrix[r];
            for(int user = 0; user < totalUsers; ++user){
                if(movieRating[user] != 0 && movie[user] != 0){
                    dotProduct += movieRating[user] * movie[user];
                    normOfSecondItem += movie[user] * movie[user];
                    movieModule += movieRating[user] * movieRating[user];
                }
            }
            itemSimilarity[r] = dotProduct / (Math.sqrt(normOfSecondItem) * Math.sqrt(movieModule));
        }
    }

    private void generateRating(){
        double sum = 0;
        double top = 0;
        for(int i = 0; i < itemSimilarity.length; ++i){
            if(itemSimilarity[i] > 0){
                top += userRating[i] * itemSimilarity[i];
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
        ItemFiltering filter = new ItemFiltering(5,2);
    }

}
