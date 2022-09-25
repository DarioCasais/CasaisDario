package test;

import java.util.Calendar;
import java.util.Date;

import modelo.Cuenta;
import modelo.Debito;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DebitoTest2 {

	Cuenta cuenta, cuenta2;
	Debito tarjeta_d, tarjeta_d2;
	
	
	@BeforeEach
	public void setUp() throws Exception {
		cuenta = new Cuenta("20356298-33", "Garc�a Enrique");
		cuenta.ingresar(2.0);
		tarjeta_d = new Debito("123456", "Garc�a Enrique", new Date(
				2018, 03, 26));
		tarjeta_d.setCuenta(cuenta);
		
		cuenta2 = new Cuenta("20356298-42", "Arias Facundo");
		cuenta2.ingresar(1000.0);
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 1998);
		cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
		cal.set(Calendar.DAY_OF_MONTH, 15);
		Date date = cal.getTime();
		
		tarjeta_d2 = new Debito("123488", "Arias Facundo",date);
		tarjeta_d2.setCuenta(cuenta2);
	}

	@Test
	public void testNoPermitePagoConSaldoInsuficiente() {
		double saldoAnterior = cuenta.getSaldo();
		try {
			tarjeta_d.pagoEnEstablecimiento("establecimiento1", 500.0);
		} catch (Exception e) {
			//e.printStackTrace();
			assertTrue(saldoAnterior==cuenta.getSaldo(),"Fallo - Permitio pagar habiendo fondos insuficientes");
		}
	}
	
	@Test
	public void testRetirarMontoMenos100LanzaExcepcion(){
		double saldoAnterior = cuenta.getSaldo();
		try {
			tarjeta_d.retirar(-100);
		} catch (Exception e) {
			//e.printStackTrace();
			assertTrue(saldoAnterior==cuenta.getSaldo(),"Fallo - Permitio retirar importe negativo");
			
		}
	}
	
	@Test
	public void testNoPermiteRetirar3000ConFondosInsuficientes(){
		double saldoAnterior = cuenta.getSaldo();
		try {
			tarjeta_d.retirar(3000);
		} catch (Exception e) {
			//e.printStackTrace();
			assertTrue(saldoAnterior==cuenta.getSaldo(),"Fallo - Permitio retirar habiendo fondos insuficientes");
		}
	}

	@Test
	public void testIngresarMontoMenos200LanzaExcepcion(){
		double saldoAnterior = cuenta.getSaldo();
		try {
			tarjeta_d.ingresar(-200);
		} catch (Exception e) {
			//e.printStackTrace();
			assertTrue(cuenta.getSaldo()== saldoAnterior,"Fallo - Permitio ingresar importe negativo");
		}
	}
	
	@Test
	public void testNoPermitePagoConDebitoVencida(){
		try {
			cuenta2.ingresar(1000.0);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		double saldoAnterior = cuenta2.getSaldo();
		try {
			// entra al try y paga porque no esta implementado en el metodo pagoEnEstablecimiento el chequeo de fecha de caducidad
			tarjeta_d2.pagoEnEstablecimiento("establecimiento1", 500.0);
		} catch (Exception e) {
			//no entra al catch por eso el test pasa, ese es el problema de poner asserts en un catch, no detecta que existe Junit al no entrar.
			assertTrue(saldoAnterior==cuenta2.getSaldo(),"Fallo - Permitio pagar con la tarjeta de debito vencida");
		}
	}
	
	@Test
	public void testNoPermitePagoConDebitoVencida2() throws Exception{
		
		double saldoAnterior = cuenta2.getSaldo();
		//no esta implementado en el metodo pagoEnEstablecimiento el chequeo de fecha de caducidad por lo que paga igualmente con tarjeta vencida
		tarjeta_d2.pagoEnEstablecimiento("establecimiento1", 500.0);
		assertTrue(saldoAnterior==cuenta2.getSaldo(),"Fallo - Permitio pagar con la tarjeta de debito vencida");	
	}
	
}
