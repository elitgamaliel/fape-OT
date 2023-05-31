package com.inretailpharma.digital.ordertracker.rest.core.interceptor;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class ResettableStreamHttpServletResponse extends HttpServletResponseWrapper {

        public String requestId;
        public String payloadFilePrefix;
        public String payloadTarget;

        public List<Byte> rawData = new ArrayList<Byte>();
        public HttpServletResponse response;
        private ResettableServletOutputStream servletStream;

        ResettableStreamHttpServletResponse(HttpServletResponse response) throws IOException {
                super(response);
                this.response = response;
                this.servletStream = new ResettableServletOutputStream(this);
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
                return servletStream;
        }
}
