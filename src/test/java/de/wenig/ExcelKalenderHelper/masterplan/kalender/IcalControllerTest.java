package de.wenig.ExcelKalenderHelper.masterplan.kalender;

import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import de.wenig.ExcelKalenderHelper.masterplan.controller.IcalController;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.Masterplan;
import de.wenig.ExcelKalenderHelper.masterplan.eingabe.MasterplanFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MockServletContext.class)
public class IcalControllerTest {

	private MockMvc mvc;

	@Before
	public void setUp() throws Exception {
		IcalController ical = new IcalController();
		
		MasterplanFactory test = Mockito.mock(MasterplanFactory.class);
		Masterplan mplan = Mockito.mock(Masterplan.class);
		when(test.produceMasterplanFromExcel(anyObject())).thenReturn(mplan);
		when(mplan.getFilteredItemList(anyObject())).thenReturn(Collections.emptyList());
		
		ReflectionTestUtils.setField(ical, "path", "P:/R_RK/Masterplan/Masterplan_SEP51.xlsm");
		ReflectionTestUtils.setField(ical, "factory", test);
		
		
		mvc = MockMvcBuilders.standaloneSetup(ical).build();
	}

	@Test
	public void getHello() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/ical/masterplan.ics").accept(MediaType.APPLICATION_OCTET_STREAM))
				.andExpect(status().isOk())
				.andExpect(content().string(startsWith("BEGIN")));
	}
}
