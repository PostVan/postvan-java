package com.postvan.models;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.params.HttpParams;

import java.net.URI;

public class ApacheHttpUriRequest implements HttpUriRequest {
    @Override
    public String getMethod() {
        return null;
    }

    @Override
    public URI getURI() {
        return null;
    }

    @Override
    public void abort() throws UnsupportedOperationException {

    }

    @Override
    public boolean isAborted() {
        return false;
    }

    @Override
    public RequestLine getRequestLine() {
        return null;
    }

    @Override
    public ProtocolVersion getProtocolVersion() {
        return null;
    }

    @Override
    public boolean containsHeader(String s) {
        return false;
    }

    @Override
    public Header[] getHeaders(String s) {
        return new Header[0];
    }

    @Override
    public Header getFirstHeader(String s) {
        return null;
    }

    @Override
    public Header getLastHeader(String s) {
        return null;
    }

    @Override
    public Header[] getAllHeaders() {
        return new Header[0];
    }

    @Override
    public void addHeader(Header header) {

    }

    @Override
    public void addHeader(String s, String s1) {

    }

    @Override
    public void setHeader(Header header) {

    }

    @Override
    public void setHeader(String s, String s1) {

    }

    @Override
    public void setHeaders(Header[] headers) {

    }

    @Override
    public void removeHeader(Header header) {

    }

    @Override
    public void removeHeaders(String s) {

    }

    @Override
    public HeaderIterator headerIterator() {
        return null;
    }

    @Override
    public HeaderIterator headerIterator(String s) {
        return null;
    }

    @Override
    public HttpParams getParams() {
        return null;
    }

    @Override
    public void setParams(HttpParams httpParams) {

    }
}
