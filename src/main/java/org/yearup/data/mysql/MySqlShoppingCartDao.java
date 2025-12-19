package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    private ProductDao productDao;

    public MySqlShoppingCartDao(DataSource dataSource, ProductDao productDao) {
        super(dataSource);
        this.productDao = productDao;
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        // creates an empty cart
        ShoppingCart cart = new ShoppingCart();

        String query = """
                SELECT product_id, quantity
                FROM shopping_cart
                WHERE user_id = ?""";

        try(
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
            ){
            // sets userId parameter
            statement.setInt(1, userId);

            ResultSet results = statement.executeQuery();

            // loop through each row from the results
            while (results.next()){
                // reads the product_id + quantity column from current row
                int productId = results.getInt("product_id");
                int quantity = results.getInt("quantity");

                // uses ProductDao to fetch the full product details, it converts the product a productId
                Product product = productDao.getById(productId);

                // make the cart
                ShoppingCartItem item = new ShoppingCartItem(productId, quantity);
                // at
                item.setProduct(product);
                item.setQuantity(quantity);

                // Add item to cart
                cart.add(item);
            }
        } catch (Exception e){
            throw new RuntimeException("Didn't retrieve shopping cart", e);
        }
        return cart;
    }





    public void  addProduct(int userId, int productId)
    {
//        String sql = """
//            INSERT INTO shopping_cart (user_id, product_id, quantity)
//            VALUES (?, ?, 1)""";
//
//        try (Connection conn = getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql))
//        {
//            stmt.setInt(1, userId);
//            stmt.setInt(2, productId);
//
//            stmt.executeUpdate();
//        }
//        catch (Exception e)
//        {
//            throw new RuntimeException("Failed to add product to cart", e);
//        }

        String query = """
                INSERT INTO shopping_cart (user_id, product_id, quantity)
                VALUES (?, ? , 1)""";

        try(
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ){
            // 1st placeholder
            statement.setInt(1, userId);
            // 2nd placeholder
            statement.setInt(2, productId);

            // execute
            System.out.println( statement.executeUpdate());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }





    @Override
    public void updateQuantity(int userId, int productId, int quantity) {

    }





    @Override
    public void clear(int userId) {
        //query
        String query = """
                DELETE FROM shopping_cart
                WHERE user_id = ?""";
        // try connection
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                // ResultSet results = statement.executeQuery()
            ){
            // execute
            statement.setInt(1, userId);
            statement.executeUpdate();
        }
        //catch
        catch (Exception e) {
            throw new RuntimeException("Failed to clear cart", e);
        }
    }
}
