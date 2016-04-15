package com.ucsb.cs263.tunein.utils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonDateSerializer extends StdSerializer<Date> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JsonDateSerializer() {
	    super(Date.class, true);
	}

	@Override
	public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider)
	        throws IOException, JsonGenerationException {
	    SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy HH:mm:ss");
	    String format = formatter.format(value);
	    jgen.writeString(format);
	}

}