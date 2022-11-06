package export;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.AbstractExporter;
import com.shopme.common.entity.User;

public class UserCsvExporter extends AbstractExporter{
	
	public void export(List<User> listUsers, HttpServletResponse respons) throws IOException {
		 	 
			super.export(respons, "text/csv", ".csv","users_");
		 	 
		 	 ICsvBeanWriter csvWriter = new CsvBeanWriter(respons.getWriter(), CsvPreference.TAB_PREFERENCE);
		 	 
		 	 String[] csvHeader = {"User Id", "E-mail", "First Name", "LAst Name", "Roles", "Enabled"};
		 	 String [] fieldMapping = {"id", "email", "firstName", "lastName", "roles", "enabled"};
		 	 
		 	csvWriter.writeHeader(csvHeader);
		 	 for(User user: listUsers) {
		 		 csvWriter.write(user, fieldMapping);
		 	 }
		 	 
		 	 
		 	 csvWriter.close();
	}
}
