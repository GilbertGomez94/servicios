import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

//Se define la raiz de todo el proyecto
@ApplicationPath("/")
public class Principal extends Application{
    //Se retorna las clases que contienen los WS de todo el proyecto
    @Override
    public Set<Class<?>> getClasses() {
        HashSet h = new HashSet<Class<?>>();
        //clases que contienen los WS
        h.add( m11_servicios.class );
        h.add(Food.class);
        return h;
    }
}