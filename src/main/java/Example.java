package main.java;

import com.neovisionaries.i18n.CountryCode;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by murik on 17.11.15.
 */
public class Example {

	static Logger LOG = Logger.getLogger(Example.class);

	public static void main(String[] args) throws IOException {
		TabGeo tabGeo = new TabGeo();
		CountryCode countryCode = tabGeo.country("8.8.8.8");
		LOG.info(countryCode.getAlpha2());
		countryCode = tabGeo.country("94.243.141.214");
		LOG.info(countryCode.getAlpha2());
		countryCode = tabGeo.country("78.46.206.167");
		LOG.info(countryCode.getAlpha2());
	}
}
