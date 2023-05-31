package com.inretailpharma.digital.ordertracker.rest.core.interceptor;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResettableServletOutputStream extends ServletOutputStream {

    @Autowired
    LogInterceptor logInterceptor;

    public OutputStream outputStream;
    private ResettableStreamHttpServletResponse wrappedResponse;
    private ServletOutputStream servletOutputStream = new ServletOutputStream(){
        boolean isFinished = false;
        boolean isReady = true;
        WriteListener writeListener = null;

        @Override
        public void setWriteListener(WriteListener writeListener) {
            this.writeListener = writeListener;
        }

        public boolean isReady(){
            return isReady;
        }
        @Override
        public void write(int w) throws IOException{
            outputStream.write(w);
            
            wrappedResponse.rawData.add((byte)w);
        }
    };

    public ResettableServletOutputStream(ResettableStreamHttpServletResponse wrappedResponse) throws IOException {
        this.outputStream = wrappedResponse.response.getOutputStream();
        this.wrappedResponse = wrappedResponse;
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
        logInterceptor.writeResponse(wrappedResponse, "");
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        servletOutputStream.setWriteListener( writeListener );
    }
    @Override
    public boolean isReady(){
        return servletOutputStream.isReady();
    }

    @Override
    public void write(int w) throws IOException {
        servletOutputStream.write(w);
    }
}
