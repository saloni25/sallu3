package com.capgemini.payroll.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.capgemini.payroll.beans.Associate;
import com.capgemini.payroll.beans.BankDetails;
import com.capgemini.payroll.beans.Salary;
import com.capgemini.payroll.daoservices.PayrollDAOServices;
import com.capgemini.payroll.daoservices.PayrollDAOServicesImpl;
import com.capgemini.payroll.exception.AssociateDetailsNotFoundException;
import com.capgemini.payroll.exception.PayrollServicesDownException;
import com.capgemini.payroll.services.PayrollServices;
import com.capgemini.payroll.services.PayrollServicesImpl;

public class PayrollServicesTestUsingMokito {

	private static PayrollServices payrollServices;
	private static PayrollDAOServices mockDaoServices;

	@BeforeClass
	public static void setUpPayrollServices() {
		mockDaoServices = Mockito.mock(PayrollDAOServices.class);
		payrollServices = new PayrollServicesImpl(mockDaoServices);
	}

	@Before
	public void setUpTestData() throws SQLException {
		
		Associate associate1 = new Associate(1000, 78000, "Satish", "Mahajan", "Training", "Manager", "DTDYF736",
				"satish.mahajan@capgemini.com", new Salary(35000, 1800, 1800),
				new BankDetails(12345, "HDFC", "HDFC0097"));
		
		Associate associate2 =new Associate(1001, 87372, "Ayush", "Mahajan", "Training", "Manager", "YCTCC727",
				"ayush.mahajan@capgemini.com", new Salary(87738, 1800, 1800),
				new BankDetails(12345, "HDFC", "HDFC0097"));
		ArrayList<Associate> associatesList = new ArrayList<>();
		associatesList.add(associate1);
		associatesList.add(associate2);
		Associate associate3 =new Associate( 65440, "Mayur", "Patil", "ADC", "Trainee", "CYYJUUF887",
				"mayur.patil@capgemini.com", new Salary(86222, 1800, 1800),
				new BankDetails(123738, "HDFC", "HDFC0097"));
		
		Mockito.when(mockDaoServices.getAssociate(1000)).thenReturn(associate1);
		Mockito.when(mockDaoServices.getAssociate(1001)).thenReturn(associate2);
		Mockito.when(mockDaoServices.getAssociate(1234)).thenReturn(null);
		Mockito.when(mockDaoServices.getAssociates()).thenReturn(associatesList);
		Mockito.when(mockDaoServices.insertAssociate(associate3)).thenReturn(1002);
		
		
	}

	@Test(expected = AssociateDetailsNotFoundException.class)
	public void testGetAssociateDataForInvalidAssociateId()
			throws PayrollServicesDownException, AssociateDetailsNotFoundException {
		payrollServices.getAssociateDetails(1234);
	}

	@Test
	public void testGetAssociateDataForValidAssociateId()
			throws PayrollServicesDownException, AssociateDetailsNotFoundException {
		Associate expectedAssociate = new Associate(1000, 78000, "Satish", "Mahajan", "Training", "Manager", "DTDYF736",
				"satish.mahajan@capgemini.com", new Salary(35000, 1800, 18000),
				new BankDetails(12345, "HDFC", "HDFC0097"));
		Associate actualAssociate = payrollServices.getAssociateDetails(1000);
		assertEquals(expectedAssociate, actualAssociate);
	}

	@Test
	public void testAcceptAssociateDetailsForValidData() throws PayrollServicesDownException {
		int expectedAssociateId = 1002;
		int actualAssociateId = payrollServices.acceptAssociateDetails("NIlesh", "Patil", "nilesh.patil@capgemini.com",
				"ADM", "Manager", "ohhoh73763", 78999, 30000, 1800, 1800, 645454, "ICICI", "ICICI8437");
		assertEquals(expectedAssociateId, actualAssociateId);
	}

	@Test(expected = AssociateDetailsNotFoundException.class)
	public void testCalculateNetSalaryForInvalidAssociateId()
			throws PayrollServicesDownException, AssociateDetailsNotFoundException {
		payrollServices.getAssociateDetails(63363);
	}

	@Test
	public void testCalculateNetSalaryForValidAssociateId() {
		fail();
	}

	@Test
	public void testGetAllAssociatesDetails() throws PayrollServicesDownException {
		List<Associate> expectedAssociateList = new ArrayList<>(PayrollDAOServicesImpl.associates.values());
		List<Associate> actualAssociateList = payrollServices.getAllAssociatesDetails();
		assertEquals(expectedAssociateList, actualAssociateList);
	}

	@After
	public void tearDownTestData() {
		
	}

	@AfterClass
	public static void tearDownPayrollServicesq() {
		mockDaoServices = null;
		payrollServices = null;
	}

}
