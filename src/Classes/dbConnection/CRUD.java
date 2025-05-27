package Classes.dbConnection;

import java.util.List;

public interface CRUD<T> {

    public List<T> read();
    public void delete(int id);
    public int add(T t);


}
