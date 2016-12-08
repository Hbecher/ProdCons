package jus.poc.prodcons.v2;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.common.Producteur;

public class ProducteurV2 extends Producteur
{
	public ProducteurV2(Observateur observateur, ProdConsV2 tampon) throws ControlException
	{
		super(observateur, tampon);
	}
}
