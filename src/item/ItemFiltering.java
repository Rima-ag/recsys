package item;

import java.io.File;
import java.util.Scanner;

public class ItemFiltering {

    private String filename = "data";

    private int totalUsers = 5;
    private int totalMovies = 5;

    private int userId;
    private int[] userRating;

    private int movieId;
    private double movieModule;
    private int[] movieRating;

    private int[][] itemMatrix;

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
        userRating = new int[totalMovies];
        itemMatrix = new int[totalMovies][totalUsers];
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
            userRating[r] = movie[userId];
        }
        movieRating = itemMatrix[movieId];
    }

    private void computeSimilarity(){
        itemSimilarity = new double[totalMovies];
        for(int i = 0; i < totalUsers; ++i){
            movieModule += movieRating[i];
        }
        movieModule = Math.sqrt(movieModule);
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
            itemSimilarity[r] = dotProduct / (Math.sqrt(normOfSecondItem) * movieModule);
        }
    }

    private void generateRating(){
        int imax1 = 0;
        double vmax1 = itemSimilarity[0];
        int imax2 = 1;
        double vmax2 = itemSimilarity[1];
        for(int i = 0; i < itemSimilarity.length; ++i){
            if(i != imax1 && i != imax2){
                if(vmax1 < itemSimilarity[i]){
                    imax2 = imax1;
                    vmax2 = vmax1;
                    imax1 = i;
                    vmax1 = itemSimilarity[i];
                }else if(vmax2 < itemSimilarity[i]){
                    imax2 = i;
                    vmax2 = itemSimilarity[i];
                }
            }
        }
        System.out.println("Predicted rating : " +
                (userRating[imax1] * itemSimilarity[imax1] + userRating[imax2] * itemSimilarity[imax2])
                        / (itemSimilarity[imax1] + itemSimilarity[imax2]));
    }


    public static void main(String[] args){
        ItemFiltering filter = new ItemFiltering(0,1);
    }

}
