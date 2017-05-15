import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.sql.*;
import java.util.ArrayList;

/**
 * Clase en la cual se manejan los servicios del modulo 11
 *
 */
@Path("/m11_servicios")
public class m11_servicios {
    //Atributo que se utiliza para transformar a formado JSON las consultas.
    Gson gson = new Gson();

    /**
     * Funcion que recibe el nombre del usuario, y con este extrae
     * la informacion de los alimentos que ha consumido el usuario.
     * @param username
     * @return
     */
    @GET
    @Path("/obtener_alimentos_personalizados")
    @Produces("application/json")
    public String ObtenerAlimento(@QueryParam("username") String username) {
        String query = "SELECT * FROM get_alimentos_person(username)";
        Food food = new Food();
        ArrayList<Food> foods = new ArrayList<>();
        JsonArray arregloJson = new JsonArray();
        try{
            Connection conn = conectarADb();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                int i = 0;
                food.setFoodName(rs.getString("nombre_comida"));
                food.setFoodWeight(rs.getString("peso_comida"));
                food.setFoodCalorie(rs.getString("calorias_comida"));
                foods.add(i, food);
                arregloJson.add(gson.toJson(food));
                i++;
            }
            return gson.toJson(arregloJson);
        }
        catch(Exception e) {
            return e.getMessage();
        }
    }

    /**
     * Conexion a la base de datos
     * @return
     */
    private Connection conectarADb()
    {
        Connection conn = null;
        try
        {
            //llamo al driver de Postgre (el primer import que muestro en el video)
            Class.forName("org.postgresql.Driver");
            //el string de conexion de la db el formato es el siguiente:
            //jdbc:postgresql://HOST//NOMBRE_DE_LA_DB
            String url = "jdbc:postgresql://localhost:5433/fitucabdb";
            //parametros de la conexion que basicamente es el usuario en mi caso es postgres y la clave es root
            // NO DEBEN DEJAR ESTO ASI POR DEFECTO
            conn = DriverManager.getConnection(url,"postgres", "gagb");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.exit(2);
        }
        return conn;
    }
}