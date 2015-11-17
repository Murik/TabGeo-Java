package main.java;

import com.neovisionaries.i18n.CountryCode;

import javax.xml.bind.DatatypeConverter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by murik on 17.11.15.
 */
public class TabGeo {


	private Unpack tabgeo_bs(byte[][] data, int ip, boolean step) {
		int start = 0;
		int end = data.length - 1;
		Unpack unpack_prev = null;
		Unpack unpack = new Unpack();
		while (true) {
			int mid = (int) Math.floor((start + end) / 2);
			if(step) {
				byte[] bytes_f = new byte[6];
				System.arraycopy(data[mid], 0, bytes_f, 1, 5);
				unpack.setOffset(unpackByte(bytes_f,  ByteOrder.BIG_ENDIAN)[0]);
				unpack.setIp(bytes_f[4] & 0xFF);
				unpack.setCountry_ID(bytes_f[5] & 0xFF);
			}else {
				unpack.setIp(data[mid][0] & 0xFF);
				unpack.setCountry_ID(data[mid][1] & 0xFF);
			}

			if (ip == unpack.getIp()) return unpack;
			if (end - start < 0) return ip > unpack.getIp() ? unpack : unpack_prev;

			if (unpack.getIp() > ip) {
				end = mid - 1;
			} else {
				start = mid + 1;
			}
			unpack_prev = unpack;

		}

	}

	public CountryCode country(String ip) throws IOException {
		String[] ip_s= ip.split("\\.");
		int[] ip_array = new int[ip_s.length];
		for (int i = 0; i < ip_s.length; i++) {
			ip_array[i] = Integer.parseInt(ip_s[i]);
		}

		byte[] bytes = readBinaryFile("tabgeo_country_v4.dat");
		int ip_start = (ip_array[0] * 256 + ip_array[1]) * 4;
		byte[] bytes_f = new byte[5];
		System.arraycopy(bytes, ip_start, bytes_f, 1,4);

		Unpack d = new Unpack();

		d.setOffset(unpackByte(bytes_f,  ByteOrder.BIG_ENDIAN)[0]);
		d.setCountry_ID(bytes_f[4] & 0xFF);
		if(d.getOffset() == 16777215) return d.getCountryCode();

		ip_start = d.getOffset() * 5 + 262144;
		bytes_f=new byte[(d.getCountry_ID() + 1) * 5];
		System.arraycopy(bytes, ip_start, bytes_f, 0, (d.getCountry_ID() + 1) * 5);

		d = tabgeo_bs(dataSplit(bytes_f,d.getCountry_ID(),5), ip_array[2], true);
		if(d.getOffset() == 16777215) return CountryCode.getByCode(Iso.values()[d.getCountry_ID()].toString());


		if(ip_array[2] > d.getIp()) ip_array[3] = 255;

		ip_start = bytes.length-((d.getOffset() + 1 + d.getCountry_ID()) * 2);
		bytes_f=new byte[(d.getCountry_ID() + 1) * 2];
		System.arraycopy(bytes, ip_start, bytes_f, 0,(d.getCountry_ID() + 1) * 2);

		d = tabgeo_bs(dataSplit(bytes_f,d.getCountry_ID(),2), ip_array[3], false);
		return CountryCode.getByCode(Iso.values()[d.getCountry_ID()].toString());
	}


	private byte[] readBinaryFile(String aFileName) throws IOException {
		Path path = Paths.get(aFileName);
		if(!Files.exists(path)) throw new FileNotFoundException();
		return Files.readAllBytes(path);
	}

	public static int[] unpackByte ( byte[] bytes  , ByteOrder order) {
		ByteBuffer byteBuf = ByteBuffer.wrap( bytes );
		byteBuf.order(order);
		IntBuffer intBuf = byteBuf.asIntBuffer();
		int[] integers = new int[ intBuf.remaining() ];
		intBuf.get( integers );
		return integers;
	}

	private byte[][]dataSplit(byte[] bytes , int length, int arraySize){
		byte[][] data = new byte[length+1][];
		for (int i = 0; i <= length; i++) {
			data[i]= Arrays.copyOfRange(bytes, i*arraySize, i*arraySize+arraySize);
		}
		return data;
	}

}