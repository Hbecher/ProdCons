package jus.poc.prodcons.v2;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.common.Consommateur;

public class ConsommateurV2 extends Consommateur
{
	public ConsommateurV2(Observateur observateur, ProdConsV2 tampon) throws ControlException
	{
		super(observateur, tampon);
	}
}
