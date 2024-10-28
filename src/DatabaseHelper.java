import org.sql2o.Sql2o;

public class DatabaseHelper {
    private static Sql2o sql2o;

    static {
        try {
            sql2o = new Sql2o("jdbc:mysql://localhost:3306/toko_cat", "adri", "adri1234");
        } catch (Exception e) {
            System.err.println("Error connecting to the database:");
            e.printStackTrace();
        }
    }

    public static Sql2o getSql2o() {
        return sql2o;
    }
}
