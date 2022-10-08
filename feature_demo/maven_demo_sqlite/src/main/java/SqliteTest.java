import org.sqlite.SQLiteConnection;
import org.sqlite.jdbc4.JDBC4Connection;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqliteTest {
    static String db_path = "database.db";
    static String create_sql = "CREATE TABLE \"basicdata\" (\n" +
            "\t\"id\"\tINTEGER NOT NULL UNIQUE,\n" +
            "\t\"username\"\tTEXT NOT NULL UNIQUE,\n" +
            "\t\"password\"\tTEXT NOT NULL,\n" +
            "\t\"data\"\tBLOB,\n" +
            "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
            ");";
    static Connection db_connection;

    static <T> int inser_map(String tb, T data) {
        Class cs = data.getClass();
        HashMap<String, Object> vp = new HashMap<String, Object>();
        for (Method i : cs.getDeclaredMethods()) {
            if (i.getName().toLowerCase().startsWith("get")) {
                try {
                    vp.put(i.getName().substring(3).toLowerCase(), i.invoke(data, null));
                } catch (Exception e) {

                }
            }
        }
//        "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY)\n" +
//                "VALUES (1, 'Paul', 32, 'California', 20000.00 );"
        String sql = "INSERT INTO " + tb + " (";
        for (String str : vp.keySet()) {
            sql += str.toUpperCase() + ",";
        }
        sql = sql.substring(0, sql.length() - 1);
        sql += ") VALUES(";
        for (String str : vp.keySet()) {
            sql += "?,";
        }
        sql = sql.substring(0, sql.length() - 1);
        sql+=");";
        try {
            PreparedStatement statement = db_connection.prepareStatement(sql);
            int cnt=1;
            for (String str : vp.keySet()) {
                statement.setObject(cnt++,vp.get(str));
            }
            statement.executeUpdate();
        }catch (Exception e) {
        }




        return 0;
    }

    static HashMap<String, Method> getMethods(ResultSetMetaData metaData, Class<?> cs) {
        try {
            int cnt = metaData.getColumnCount();
            HashMap<String, Method> mp = new HashMap<>();
            for (int i = 1; i <= cnt; i++) {
                String name = metaData.getColumnLabel(i);
                String csName = metaData.getColumnClassName(i);
                String h = String.valueOf(name.charAt(0));
                h = h.toUpperCase() + name.substring(1);
                try {
                    Method mt = cs.getDeclaredMethod("get" + h, null);
                    if (mt != null)
                        mp.put(name, mt);
                } catch (Exception e) {
                }
            }
            return mp;
        } catch (Exception e) {
            return null;
        }
    }

    static HashMap<String, Method> setMethod(ResultSetMetaData metaData, Class<?> cs) {
        try {
            int cnt = metaData.getColumnCount();
            HashMap<String, Method> setmp = new HashMap<>();
            for (int i = 1; i <= cnt; i++) {
                String name = metaData.getColumnLabel(i);
                String csName = metaData.getColumnClassName(i);
                String h = String.valueOf(name.charAt(0));
                h = h.toUpperCase() + name.substring(1);
                try {
                    Method setMt = cs.getDeclaredMethod("set" + h, Class.forName(csName));
                    if (setMt != null)
                        setmp.put(name, setMt);
                } catch (Exception e) {
                }
            }
            return setmp;
        } catch (Exception e) {
            return null;
        }
    }

    static <T> List<T> map_to_obj(Class<? extends T> cs, ResultSet set) {
        try {
            Map<String, Method> setmp = setMethod(set.getMetaData(), cs);
            ArrayList<T> ls = new ArrayList<>();
            while (set.next()) {
                T t = cs.getDeclaredConstructor(null).newInstance(null);
                for (String m : setmp.keySet()) {
                    Object obj = set.getObject(m);
                    setmp.get(m).invoke(t, obj);
                }
                ls.add(t);
            }
            return ls;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    static boolean makeTable(String sql) {
        try {

            int code = db_connection.prepareStatement(sql).executeUpdate();
            return code != 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    static ResultSet getData() {
        ResultSet set = null;

        try {
            PreparedStatement statement = db_connection.prepareStatement("select * from " +
                    "basicdata");
            set = statement.executeQuery();
            //statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return set;
    }

    static void init() throws Exception {
        db_connection = DriverManager.getConnection("jdbc:sqlite:" + db_path);
    }

    public static void main(String[] args) throws Exception {
        init();
        List<User> u = map_to_obj(User.class, getData());
        for(User i :u)
        {
            System.out.println(i);
        }

//        User user = new User();
//        user.setId(92);
//        user.setUsername("tim");
//        user.setPassword("123456");
//        user.setMoney(1000.2);
//        inser_map("basicdata",user);
    }
}

class User {
    private Integer id;
    private String username, password;
    private byte[] data = null;
    private Double money;

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = (byte[]) data;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}