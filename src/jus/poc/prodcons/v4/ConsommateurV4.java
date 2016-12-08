package jus.poc.prodcons.v4;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.v3.ConsommateurV3;

public class ConsommateurV4 extends ConsommateurV3
{
	public ConsommateurV4(Observateur observateur, ProdConsV4 tampon) throws ControlException
	{
		super(observateur, tampon);
	}
}
