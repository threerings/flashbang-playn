//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.util;

import java.io.IOException;

import com.google.gwt.xml.client.*;

public class XmlReadException extends IOException
{
    public XmlReadException (String message)
    {
        this(message, null, null);
    }

    public XmlReadException (String message, Node badNode)
    {
        this(message, badNode, null);
    }

    public XmlReadException (String message, Throwable cause)
    {
        this(message, null, cause);
    }

    public XmlReadException (String message, Node badNode, Throwable cause)
    {
        super(null, cause);
        _baseMessage = message;
        _badNode = badNode;
    }

    public String getFilename ()
    {
        return _filename;
    }

    public void setFilename (String filename)
    {
        _filename = filename;
    }

    @Override
    public String getMessage ()
    {
        String message = _baseMessage;
        if (_filename != null) {
            message += " (filename: " + _filename + ")";
        }
        if (_badNode != null) {
            message += "\n" + nodeToString(_badNode);
        }
        return message;
    }

    protected static String nodeToString (Node node)
    {
        if (!(node instanceof Element)) {
            return node.toString();
        }

        StringBuilder val = new StringBuilder();
        val.append("<").append(node.getNodeName());
        NamedNodeMap attrs = node.getAttributes();
        for (int ii = 0; ii < attrs.getLength(); ++ii) {
            Node attr = attrs.item(ii);
            val.append(" ").append(attr.getNodeName()).append("=\"").append(attr.getNodeValue())
                .append("\"");
        }
        val.append(">");
        return val.toString();
    }

    protected String _baseMessage;
    protected Node _badNode;
    protected String _filename;
    protected Throwable _cause;
}
