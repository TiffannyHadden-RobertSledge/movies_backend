package data;
import com.mysql.cj.jdbc.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlMoviesDao implements MoviesDao {

    private Connection connection = null;

    public MySqlMoviesDao(Config config) {
        try {
            DriverManager.registerDriver(new Driver());

            this.connection = DriverManager.getConnection(config.getUrl(), config.getUrl(), config.getPassword());
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database!");
        }
    }

        @Override
        public List<Movie> all () throws SQLException {
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT * FROM movies");

        List<Movie> movies = new ArrayList<>();

        while(rs.next()){
            movies.add(new Movie(
                  rs.getInt("id"),
                  rs.getString("title"),
                  rs.getInt("release_date"),
                  rs.getString("director"),
                  rs.getString("cast"),
                  rs.getString("rating"),
                  rs.getString("poster"),
                  rs.getString("genre"),
                  rs.getString("plot")

            ));
        }
        return movies;
        }

        @Override
        public Movie findOne ( int id){
            return null;
        }

        @Override
        public void insert (Movie movie){

        }



        @Override
    public void insertAll(Movie[] movies) throws SQLException {

        // Build sql template
        StringBuilder sql = new StringBuilder("INSERT INTO movies (" +
                "id, title, poster, release_date, genre, rating, director, cast, plot) " +
                "VALUES ");


        // Add a interpolation template for each element in movies list
        sql.append("(?, ?, ?, ?, ?, ?, ?, ?, ?), ".repeat(movies.length));

        // Create a new String and take off the last comma and whitespace
        sql = new StringBuilder(sql.substring(0, sql.length() - 2));

        // Use the sql string to create a prepared statement
        PreparedStatement statement = connection.prepareStatement(sql.toString());

        // Add each movie to the prepared statement using the index of each sql param: '?'
        // This is done by using a counter
        // You could use a for loop to do this as well and use its incrementor
        int counter = 0;
        for (Movie movie : movies) {
            statement.setInt((counter * 9) + 1, movie.getId());
            statement.setString((counter * 9) + 2, movie.getTitle());
            statement.setInt((counter * 9) + 3, movie.getRelease_date());
            statement.setString((counter * 9) + 4, movie.getDirector());
            statement.setString((counter * 9) + 5, movie.getCast());
            statement.setString((counter * 9) + 5, movie.getRating());
            statement.setString((counter * 9) + 7, movie.getPoster());
            statement.setString((counter * 9) + 8, movie.getGenre());
            statement.setString((counter * 9) + 9, movie.getPlot());
            counter++;
        }
        statement.executeUpdate();
    }
    @Override
    public void update (Movie movie) throws SQLException {
        String sql = "UPDATE movies " +
               "SET title = '?', release_date = '?', director = '?', cast = '?', " +
                "poster = '?', genre = '?', plot = '?', rating = '?' " +
                "WHERE id = '?';";

        PreparedStatement statement = connection.prepareStatement(sql.toString());


        statement.setString(1, movie.getTitle());
        statement.setInt(2, movie.getRelease_date());
        statement.setString(3, movie.getCast());
        statement.setString(4, movie.getDirector());
        statement.setString(5, movie.getGenre());
        statement.setString(6, movie.getPlot());
        statement.setString(7, movie.getRating());
        statement.setInt(8, movie.getId());
        statement.setString(9, movie.getPoster());

                statement.executeUpdate();


    }

    @Override
    public void destroy (int id) throws SQLException {

        String sql = "DELETE FROM movies WHERE id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, id);

        statement.execute();

    }

}
