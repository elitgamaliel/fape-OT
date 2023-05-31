package com.inretailpharma.digital.ordertracker.rest.core.interceptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class ResettableStreamHttpServletRequest extends HttpServletRequestWrapper {

        private byte[] rawData = {};
        private HttpServletRequest request;
        private ResettableServletInputStream servletStream;

        public String requestId;
        public String payloadFilePrefix;
        public String payloadTarget;

        ResettableStreamHttpServletRequest(HttpServletRequest request) throws IOException {
            super(request);
            this.request = request;
            this.servletStream = new ResettableServletInputStream();
        }

        private void initRawData() throws IOException {
            if ( rawData.length == 0 ) {
            	byte[] b = toByteArray(this.request.getInputStream());
                if ( b != null )
                	rawData = b;
            }
            servletStream.inputStream = new ByteArrayInputStream(rawData);
        }
        
        private byte[] toByteArray(InputStream is) throws IOException {
        	ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			int nRead;
			byte[] data = new byte[16384];

			while ((nRead = is.read(data, 0, data.length)) != -1) {
			  buffer.write(data, 0, nRead);
			}

			return buffer.toByteArray();
        }
        
        @Override
        public ServletInputStream getInputStream() throws IOException {
            initRawData();
            return servletStream;
        }
}
