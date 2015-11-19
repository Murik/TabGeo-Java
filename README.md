# TabGeo-Java

 Библиотека для определения страны по ip адресу на основе http://tabgeo.com/ru/index/

import com.neovisionaries.i18n.CountryCode;
	TabGeo tabGeo = new TabGeo();
		CountryCode countryCode = tabGeo.country("8.8.8.8");
