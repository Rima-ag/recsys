package recommenderSystem;

import globalBaseline.GlobalBaseline;
import item.ItemFiltering;
import svd.SVD;
import user.UserFiltering;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class RecommenderSystem {
    protected static String filename = "data";
    protected int totalUsers;
    protected int totalMovies;
    protected int movieId;
    protected int userId;
    protected double[] userRating;
    protected double[] movieRating;

    public RecommenderSystem() {
    }

    public RecommenderSystem(int userId, int movieId) {
        this.userId = userId;
        this.movieId = movieId;
    }

    protected abstract void setMatrixValue(int user, int movie, int rating);

    protected abstract void initMatrix();

    public abstract void printRatings();

    protected void readDataSet() {
        try {
            int lineNumber = 1;
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

                    this.initMatrix();
                    ++lineNumber;
                    continue;
                }
                String[] line = sc.nextLine().split(",");
                if (line.length != 3)
                    throw new Exception("Wrong format");
                int user = Integer.parseInt(line[0]);
                int movie = Integer.parseInt(line[1]);
                int rating = Integer.parseInt(line[2]);
                this.setMatrixValue(user, movie, rating);
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
        List<RecommenderSystem> recommenderSystemList = new ArrayList<RecommenderSystem>();
        recommenderSystemList.add(new GlobalBaseline(2, 2));
        recommenderSystemList.add(new UserFiltering(11, 2));
        recommenderSystemList.add(new ItemFiltering(5, 2));
        recommenderSystemList.add(new SVD(ratings));
        for (RecommenderSystem e: recommenderSystemList) {
            e.printRatings();
        }
    }
}
