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

    static <T> List<T> map_to_obj(Class<? extends T> cs, ResultSet set) {
        try {

            ResultSetMetaData metaData = set.getMetaData();
            int cnt = metaData.getColumnCount();
            HashMap<String, Method> getmp = new HashMap<>();
            HashMap<String, Method> setmp = new HashMap<>();

            for (int i = 1; i <= cnt; i++) {
                String name = metaData.getColumnLabel(i);
                String csName= metaData.getColumnClassName(i);
                String h = String.valueOf(name.charAt(0));
                h= h.toUpperCase() + name.substring(1);
                try {
                    Method mt = cs.getDeclaredMethod("get" + h, null);
                    if(mt!=null)
                        getmp.put(name,mt);
                }catch (Exception e) {

                }
                try {
                    Method setMt=cs.getDeclaredMethod("set"+h, Class.forName(csName));
                    if(setMt!=null)
                        setmp.put(name,setMt);
                }catch (Exception e) {
                }
            }
            ArrayList<T> ls=new ArrayList<>();
            while (set.next()) {
                T t = cs.getDeclaredConstructor(null).newInstance(null);
                for(String m :setmp.keySet())
                {
                   Object obj= set.getObject(m);
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
        List<User> u= map_to_obj(User.class, getData());
        System.out.println(u);
    }

    static void read_img() throws Exception {
        Connection db_connection = DriverManager.getConnection("jdbc:sqlite:" + db_path);
        PreparedStatement statement = db_connection.prepareStatement("select * from " +
                "students");
        ResultSet set = statement.executeQuery();
        int cnt = set.getMetaData().getColumnCount();
        while (set.next()) {
            String filename = "";
            for (int i = 1; i <= cnt - 1; ++i) {
                String t = set.getString(i);
                filename += t + "-";
                System.out.println(t);
            }
            filename += "pic.jpeg";
            byte[] data = set.getBytes(cnt);
            FileOutputStream stream = new FileOutputStream(filename);
            stream.write(data);
            stream.close();
            System.out.println("saveed");
        }
        int k = 0;
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
        this.data =(byte[]) data;
    }

}