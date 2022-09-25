package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import modelo.Credito;
import modelo.Cuenta;

public class CreditoTest {
	private Date fecha;
	private Credito credito;
	private Cuenta unaCuenta;
	
	@BeforeEach
	public void setUp() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateInString = "2020-09-15";
		dateInString = "2020-09-15";
		try {
			fecha = sdf.parse(dateInString);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		credito = new Credito("06-456213-33","Alfredo Hernandez",fecha,5000.00);
		unaCuenta=new Cuenta("06-456213-33","Alfredo Hernandez");
	}

	@Test
	public void testComprasConTarjetaCredito(){
	
		assertNotNull(fecha);
		
		//Se crea una cuenta con $7000 de saldo
		try {
			unaCuenta.ingresar(7000.00);
			//se asigna una cuenta a una tarjeta de credito con tope de $5000
			credito.setCuenta(unaCuenta);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			// Compras con tarjeta de credito no modifican estado de cuenta hasta que se liquide
			credito.pagoEnEstablecimiento("MusiMundo",3000.00);
			credito.pagoEnEstablecimiento("Fravega",1000.00);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("cuenta saldo = " + unaCuenta.getSaldo());
		System.out.println("credito disponible =  " + credito.getCreditoDisponible());
		//saldo credito.saldo es lo que se viene gastando hasta el momento sin liquidar
		System.out.println("credito.getSaldo() " + credito.getSaldo());
		
		//automatizando los tests
		assertTrue(unaCuenta.getSaldo()==7000.00);
		assertTrue(credito.getCreditoDisponible()==1000.00);
		assertTrue(credito.getSaldo()==4000.00);
		
	}
	// primer test
	@Test
	void testCompraValorNegativo () throws Exception {
		double saldoAnterior = credito.getCreditoDisponible();
		try {
			credito.pagoEnEstablecimiento("Pago cena", -2000);// el problema es que no tiene un validacion par apagos en negativo
			assertTrue(saldoAnterior == credito.getCreditoDisponible(), "No se aceptan pagos con monto negativo");
		} catch ( Exception e) {
			System.out.print("no debe fallar");
		}
	}
	// 2do test
	@Test
	void testTraerLiquidacion () {
		try {
			credito.pagoEnEstablecimiento("Carga sube", 1500);
			credito.pagoEnEstablecimiento("Hardware", 20000);
			credito.liquidar(9, 2022);// Metodo pago en establecimiento mal definido, no pide fecha
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//3er test - HICISTE UNO BIEN
	@Test
	void retiroMayor () throws Exception {
		double saldo = credito.getSaldo();
		try {
			credito.retirar(200000); // el metodo retirar funciona 
			assertTrue (saldo == credito.getSaldo(),"Fallo, retiro mas de lo permitido");
		} catch (Exception e){
			fail("monton mayor al habilitado");
		}
	}
	//4to test 
	@Test
	void ingresoNegativo () throws Exception {
		double ingreso = -1000.00;
		try {
			credito.ingresar(ingreso);//esta roto ingresar porque no teiene validaciojn negativa
			assertTrue (ingreso == credito.getSaldo(),"Fallo, ingreso menor a 0");
		}
		catch (Exception e) {
			fail("no se ingresan saldos negativos");
		}
	}

}
