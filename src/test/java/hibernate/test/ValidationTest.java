package hibernate.test;

import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import hibernate.model.Node;


/**
 * The Class ValidationTest.
 *
 * @author Michele Mariotti
 */
public class ValidationTest
{
    private static EntityManagerFactory emf;

    @BeforeClass
    public static void init()
    {
        emf = Persistence.createEntityManagerFactory("HHH-12003");
    }

    @AfterClass
    public static void finish()
    {
        if(emf != null)
        {
            emf.close();
        }
    }

    @Before
    public void setup()
    {
        accept(em -> em.createQuery("delete from Node x").executeUpdate());
    }

    @Test
    public void test1()
    {
        accept(em ->
        {
            Node n = new Node();
            em.persist(n);
            
            n.setName("node-1");
            n.setLabel("label-1");
        });
    }
    
    @Test
    public void test2()
    {
        accept(em ->
        {
            Node n = new Node();
            em.persist(n);
        });
    }

    @Test
    public void test3()
    {
    	accept(em ->
    	{
    		Node n = new Node();
    		n.setName("node-3");
    		
    		em.persist(n);
    		
    		n.setLabel("label-3");
    	});
    	
    	Node n = apply(em -> em.createQuery("select x from Node x", Node.class).getSingleResult());
    	
    	Assert.assertEquals("label-3", n.getLabel());
    }
    
    protected static <T> T apply(Function<EntityManager, T> mapper)
    {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try
        {
            tx.begin();

            T result = mapper.apply(em);

            tx.commit();

            return result;
        }
        catch(Exception e)
        {
            tx.rollback();
            throw e;
        }
        finally
        {
            em.close();
        }
    }

    protected static void accept(Consumer<EntityManager> consumer)
    {
        apply(em ->
        {
            consumer.accept(em);
            return null;
        });
    }
}
