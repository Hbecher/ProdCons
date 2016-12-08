package jus.poc.prodcons.v1;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.common.Producteur;

public class ProducteurV1 extends Producteur
{
	public ProducteurV1(Observateur observateur, ProdConsV1 tampon) throws ControlException
	{
		super(observateur, tampon);
	}
}
