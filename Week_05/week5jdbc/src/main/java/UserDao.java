import jdbc.HikaricpUtils;
import jdbc.JdbcUtils;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@Component("userDao")
public class UserDao {

    /**
     * 普通的插入
     *
     * @param user
     * @throws SQLException
     */
    public void insert(User user) throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = JdbcUtils.getConnection();
            String sql = String.format("insert into t_user (name,age) values ('%s',%s)",
                    user.getName(), user.getAge());
            statement = conn.prepareStatement(sql);
            Integer id = statement.executeUpdate();
        } catch (Exception ex) {
            String msg = ex.getMessage();
        } finally {
            try {
                JdbcUtils.close(conn, statement);
            } catch (Exception ex) {
                String msg = ex.getMessage();
            }
        }
    }

    /**
     * 带事务的插入
     *
     * @param user
     * @throws SQLException
     */
    public void insertWithTransaction(User user) throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = JdbcUtils.getConnection();
            conn.setAutoCommit(false);
            String sql = String.format("insert into t_user (name,age) values ('%s',%s)",
                    user.getName(), user.getAge());
            statement = conn.prepareStatement(sql);
            Integer id = statement.executeUpdate();
            int res = 1 / 0;
            conn.commit();
        } catch (Exception ex) {
            String msg = ex.getMessage();
            conn.rollback();
        } finally {
            try {
                JdbcUtils.close(conn, statement);
            } catch (Exception ex) {
                String msg = ex.getMessage();
            }
        }
    }

    /**
     * 使用连接池的插入
     *
     * @param user
     * @throws SQLException
     */
    public void insertWithPool(User user) throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            System.out.println("111111");
            conn = HikaricpUtils.getConnection();
            System.out.println("222222");
            String sql = String.format("insert into t_user (name,age) values ('%s',%s)",
                    user.getName(), user.getAge());
            statement = conn.prepareStatement(sql);
            Integer id = statement.executeUpdate();
        } catch (Exception ex) {
            String msg = ex.getMessage();

            Integer bb = 111;
        } finally {
            String aa = "123";
        }
    }

    /**
     * 带事务的插入
     *
     * @param user
     * @throws SQLException
     */
    public void batchInsert(User user) throws SQLException {
        Connection conn = null;
        Statement statement = null;
        try {
            conn = JdbcUtils.getConnection();
            String sql = String.format("insert into t_user (name,age) values ('%s',%s)",
                    user.getName(), user.getAge());
            statement = conn.createStatement();
            statement.addBatch(sql);
            statement.addBatch(sql);
            statement.executeBatch();
            statement.clearBatch();
        } catch (Exception ex) {
            String msg = ex.getMessage();
        } finally {
            try {
                JdbcUtils.close(conn, statement);
            } catch (Exception ex) {
                String msg = ex.getMessage();
            }
        }
    }

    public List<User> getAll() {
        Connection conn = null;
        PreparedStatement statement = null;
        List<User> list = new LinkedList<User>();
        try {
            conn = JdbcUtils.getConnection();
            String sql = String.format("select * from t_user");
            statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setAge(resultSet.getInt("age"));
                list.add(user);
            }

        } catch (Exception ex) {

        } finally {
            JdbcUtils.close(conn, statement);
        }
        return list;
    }

    public void delete(Integer id) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = JdbcUtils.getConnection();
            String sql = String.format("delete from t_user where id= %s",
                    id);
            statement = conn.prepareStatement(sql);
            int count = statement.executeUpdate();
        } catch (Exception ex) {

        } finally {
            JdbcUtils.close(conn, statement);
        }
    }

    public void update(User user) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = JdbcUtils.getConnection();
            String sql = String.format("update t_user set name='%s'," +
                    "age = %s where id= %s", user.getName(), user.getAge(), user.getId());
            statement = conn.prepareStatement(sql);
            int count = statement.executeUpdate();
        } catch (Exception ex) {

        } finally {
            JdbcUtils.close(conn, statement);
        }
    }
}
