import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.ws.rs.GET;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.sql.*;
import java.util.*;
import java.util.Date;

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
    public String ObtenerAlimento(@QueryParam("username") String username)
    {
        String query = "SELECT * FROM get_alimentos_person(?)";
        Food food = new Food();
        JsonArray arregloJson = new JsonArray();
        try{
            Connection conn = conectarADb();
            //Statement st = conn.createStatement();
            PreparedStatement stm = conn.prepareStatement(query);
            stm.setString(1, username);
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                food.setFoodName(rs.getString("nombre_comida"));
                food.setFoodWeight(rs.getString("peso_comida"));
                food.setFoodCalorie(rs.getString("calorias_comida"));
                arregloJson.add(gson.toJson(food));
            }
            return gson.toJson(arregloJson);
        }
        catch(Exception e) {
            return e.getMessage();
        }
    }

    /**
     * Funcion que recibe como parametros la fecha y el nombre del usuario
     * para hacer la consulta de las calorias consumidas por el usuario durante
     * esa fecha.
     * @param fecha
     * @param username
     * @return
     */
    @GET
    @Path("/obtener_calorias_fecha")
    @Produces("application/json")
    public String ObtenerCaloriasPorFecha(@QueryParam("fecha") Date fecha ,
                                          @QueryParam("username") String username)
    {
        String query = "SELECT * FROM get_calorias_fecha(fecha, username)";
        JsonArray arregloJson = new JsonArray();
        try{
            Connection conn = conectarADb();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            //La variable donde se almacena el resultado de la consulta.
            String calorie = "";
            while(rs.next()){
                calorie = (rs.getString("calorias"));
                arregloJson.add(gson.toJson(calorie));
            }
            return gson.toJson(arregloJson);
        }
        catch(Exception e) {
            return e.getMessage();
        }
    }

    /**
     * Metodo que recibe como parametros el momento (momento del dia en que se alimenta)
     * y el nombre del usuario para eliminar el alimento que ingirió en ese momento del
     * día.
     * @param momento
     * @param username
     */
    @DELETE
    @Path("/eliminar_alimento_dieta")
    @Produces("application/json")
    public void ObtenerCaloriasPorFecha(@QueryParam("momento") String momento ,
                                          @QueryParam("username") String username)
    {
        String query = "SELECT elimina_alimento_dieta(momento, username)";
        try{
            Connection conn = conectarADb();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
        }
        catch(Exception e) {
           e.getMessage();
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