package modelo;

// Esta clase representa una bebida del menú.
// Hereda de ProductoMenu.
public class Bebida extends ProductoMenu
{
	// indica si la bebida es alcoholica
	private boolean alcoholica;
	
	// indica si la bebida es calientr
	private boolean caliente;

	// Constructor
	public Bebida(String nombre, double precio, boolean alcoholica, boolean caliente)
	{
		super(nombre, precio);
		this.alcoholica = alcoholica;
		this.caliente = caliente;
	}

	// devuelve si es alcoholica
	public boolean isAlcoholica()
	{
		return alcoholica;
	}

	// devuelve si es caliente
	public boolean isCaliente()
	{
		return caliente;
	}

	// cambia si es alcoholica
	public void setAlcoholica(boolean alcoholica)
	{
		this.alcoholica = alcoholica;
	}

	// cambia si es caliente
	public void setCaliente(boolean caliente)
	{
		this.caliente = caliente;
	}

	@Override
	public String toString() {
	    String tipo = caliente ? "Caliente" : "Fría";
	    String alcohol = alcoholica ? " | Alcohólica" : "";
	    return getNombre() + " - $" + getPrecio() + " (" + tipo + alcohol + ")";
	}


}