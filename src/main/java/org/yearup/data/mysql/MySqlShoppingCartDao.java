package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    private ProductDao productDao;

    public MySqlShoppingCartDao(DataSource dataSource, ProductDao productDao) {
        super(dataSource);
        this.productDao = productDao;
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
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

            // loop through each row in results
            while (results.next()){
                int productId = results.getInt("product_id");
                int quantity = results.getInt("quantity");

                // getting product info from dao
                Product product = productDao.getById(productId);

                // make the cart
                ShoppingCartItem item = new ShoppingCartItem();
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





    public void addProduct(int userId, int productId)
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
