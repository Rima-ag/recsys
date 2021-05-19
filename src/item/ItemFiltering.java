package item;

import java.io.File;
import java.util.Scanner;

public class ItemFiltering {

    private String filename = "";

    private int totalUsers = 5;
    private int totalMovies = 5;

    private int userId;
    private int[] userRating;

    private int movieId;
    private int movieModule;
    private int[] movieRating;
    private double movieNorm;

    private int[][] itemMatrix;

    private double[] itemSimilarity;

    public ItemFiltering(Integer userId, Integer movieId){
        itemMatrix = new int[totalMovies][totalUsers];

        this.userId = userId;
        this.movieId = movieId;

        fillMatrix();
        normalizeMatrix();
        computeSimilarity();
        generateRating();
    }

    private void initMatrix(){
        itemMatrix = new int[totalMovies][totalUsers];
    }

    private void fillMatrix(){
        try {
            Integer lineNumber = 1;
            Scanner sc = new Scanner(new File(filename));
            while (sc.hasNextLine()) {
                if(lineNumber == 1) {
                    continue;
                }else if(lineNumber == 2) {
                    String[] line = sc.nextLine().split("-");
                    totalUsers = Integer.parseInt(line[0]);
                    totalMovies = Integer.parseInt(line[1]);
                    initMatrix();
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
                System.out.println(sc.nextLine());
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

            int[] movie = itemMatrix[r];

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
        for(int i = 0; i < totalUsers; ++i){
            movieNorm += movieRating[i];
        }
        movieNorm = Math.sqrt(movieNorm);
        double dotProduct = 0;
        double normOfSecondItem = 0;
        for(int r = 0; r < totalMovies; ++r){
            if(r == movieId)
                continue;
            if(userRating[r] == 0)
                continue;
            int[] movie = itemMatrix[r];
            for(int user = 0; user < totalUsers; ++user){
                if(movieRating[user] != 0 && movie[user] != 0){
                    dotProduct += movieRating[user] * movie[user];
                    normOfSecondItem += movie[user] * movie[user];
                }
            }
            itemSimilarity[r] = dotProduct / (Math.sqrt(normOfSecondItem) * movieNorm);
        }
    }

    private void generateRating(){

    }


    public static void main(String[] args){

    }

}
