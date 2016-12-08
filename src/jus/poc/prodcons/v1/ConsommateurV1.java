package jus.poc.prodcons.v1;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.common.Consommateur;

public class ConsommateurV1 extends Consommateur
{
	public ConsommateurV1(Observateur observateur, ProdConsV1 tampon) throws ControlException
	{
		super(observateur, tampon);
	}
}
