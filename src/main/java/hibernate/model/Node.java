package hibernate.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;


@Entity
public class Node implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    private long id = System.nanoTime();

    @NotNull
    @Column(nullable = false)
    private String name;

    @Column
    private String label;

    @Override
    public int hashCode()
    {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj == this || (obj != null && obj instanceof Node && Objects.equals(name, ((Node) obj).getName()));
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getLabel() 
    {
		return label;
	}
    
    public void setLabel(String label) 
    {
		this.label = label;
	}
}
