package com.github.yangwk.more.demo.javase.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 将封装好的资源文件（如js,html）放在jar包内部，从url指向该servlet的文件名进行获取。
 * 比如，html文件在script标签引用url='${ctx}/resource/test.js'
 * 且，该servlet配置url-pattern为/resource/*
 * @see org.apache.activemq.web.AjaxServlet
 * @author yangwk
 *
 */
public class ResourceServlet extends HttpServlet {

    private static final long serialVersionUID = -3875280764356406114L;
    private Map<String, byte[]> jsCache = new HashMap<String, byte[]>();
    private long jsLastModified = 1000 * (System.currentTimeMillis() / 1000);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getPathInfo() != null && request.getPathInfo().endsWith(".js")) {
            doJavaScript(request, response);
        } else {
            super.doGet(request, response);
        }
    }

    protected void doJavaScript(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // Look for a local resource first.
        String js = request.getServletPath() + request.getPathInfo();
        URL url = getServletContext().getResource(js);
        if (url != null) {
            getServletContext().getNamedDispatcher("default").forward(request, response);
            return;
        }

        // Serve from the classpath resources
        String resource = "org/apache/activemq/web" + request.getPathInfo();
        synchronized (jsCache) {

            byte[] data = jsCache.get(resource);
            if (data == null) {
                try(InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource)) {
                    if (in != null) {
                        try(ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                            byte[] buf = new byte[4096];
                            int len = in.read(buf);
                            while (len >= 0) {
                                out.write(buf, 0, len);
                                len = in.read(buf);
                            }
                            data = out.toByteArray();
                            jsCache.put(resource, data);
                        }
                    }
                }
            }

            if (data != null) {

                long ifModified = request.getDateHeader("If-Modified-Since");

                if (ifModified == jsLastModified) {
                    response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
                } else {
                    response.setContentType("application/x-javascript");
                    response.setContentLength(data.length);
                    response.setDateHeader("Last-Modified", jsLastModified);
                    response.getOutputStream().write(data);
                }
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }
    
}
