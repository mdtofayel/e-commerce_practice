package export;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

public class AbstractExporter {
	public void export(HttpServletResponse respons, String contextType, String extension) throws IOException {
		DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String timestamp = dateFormater.format(new Date());
		String fileName = "users_" + timestamp + extension;

		respons.setContentType(contextType 	);

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; fileName=" + fileName;
		respons.setHeader(headerKey, headerValue);
	}

}
