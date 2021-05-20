package recommenderSystem;

public abstract class CollaborativeFiltering extends  RecommenderSystem {

    @Override
    public void printRatings() {
        readDataSet();
        normalizeMatrix();
        computeSimilarity();
        generateRating();
    }

    public CollaborativeFiltering(int userId, int movieId){
        super(userId,movieId);
    }

    protected abstract void computeSimilarity();

    protected abstract void normalizeMatrix();

    protected abstract void generateRating();
}
